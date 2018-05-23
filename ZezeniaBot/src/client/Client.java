package client;

import client.networking.PacketHandler;
import client.networking.PacketReceiver;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.NetworkMessage;
import packets.*;
import settings.Constants;
import structures.*;

public class Client implements PacketHandler {

    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private static Client _instance;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private PacketReceiver receiver;
    private ClientEventListener listener;
    private List<Channel> openChannels = new CopyOnWriteArrayList<>();
    private Status playerStatus = null;
    private structures.Character character;
    private List<Item> equipment = new ArrayList<>();
    private List<Container> openContainers = new CopyOnWriteArrayList<>();
    private List<Person> people = new ArrayList();
    /**
     * A direction when a walk is in process, otherwise it is -1.
     */
    private int walkDirection = -1;
    private Entity currentlyAttacked;
    private Entity currentlyFollowed;
    private int remainingSteps = 0;
    
    private String lastCancelMessage;
    private long lastCancelTime;

    public Client(ClientEventListener listener) {
        _instance = this;
        this.listener = listener;

        // Initialize utilities.
        new Map();
        new SpellCaster().start();

        // Main channel is is not told to be opened by the server, although it's request by the client.
        // That's because the server expects the client to know that the request is obviously accepted.
        Channel ch = new Channel(ChannelType.PUBLIC, "Main");
        openChannels.add(ch);
        listener.onChannelOpen(ch);
    }

    public static Client getInstance() {
        return _instance;
    }

    public static structures.Character[] getCharList(String name,
            String password) throws Exception {
        Socket socket = new Socket(Constants.HOST_ADDRESS, Constants.LOGIN_PORT);
        new LoginPacket(Constants.ZEZENIA_VERSION, name, password, 1324924646).send(socket.getOutputStream());
        NetworkMessage m = NetworkMessage.receive(socket.getInputStream());
        socket.close();
        int id = m.readByte();
        if (id == PacketConstants.ErrorPacket) {
            ErrorPacket ep = new ErrorPacket();
            ep.read(m);
            throw new ServerErrorException(ep.message);
        }
        if (id != PacketConstants.CharListPacket) {
            throw new Exception("Unexpected packet of id " + id
                    + " received after sending a LoginPacket.");
        }
        CharListPacket clp = new CharListPacket();
        clp.read(m);
        return clp.chars;
    }
    private boolean expectingLoginResponse;

    /**
     * It is critical to call login() before starting to mess with anything else
     * in a Client instance. This will initialize a connection to the world
     * server and perform a world login with the given character.
     */
    public void login(String name, String password, structures.Character c)
            throws Exception {
        socket = new Socket(c.worldIP, Constants.WORLD_PORT);
        os = socket.getOutputStream();
        is = socket.getInputStream();

        receiver = new PacketReceiver(is, this);
        receiver.start();

        new WorldLoginPacket((short) 451, name, password, c.name,
                safeLongToInt((new Date().getTime() / 1000L))).send(os);

        character = c;

        expectingLoginResponse = true;
    }

    /**
     * Request to logout from the server. Should not be called if skull signed.
     */
    public void logout() throws Exception {
        new AttemptLogoutPacket().send(os);
    }

    // TEMPORARY
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(l
                    + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    public void sendMessage(String channel, String message) throws IOException {
        // Safety - a regular user cannot send a message to a channel that is
        // not open in the client.
        if (!Channel.byName(channel).isOpen()) {
            return;
        }

        new RequestMessageChannelPacket(channel, message).send(os);
    }

    /**
     * If the target tile is walkable, walk() asks the server to walk to the
     * given direction.
     *
     * @param direction any value from structures.Directions
     * @return whether the target tile is walkable.
     */
    public boolean walk(short direction) throws Exception {
        if (walkDirection != -1) {
            return false;
        }
        Entity clone = Map.getInstance().getPlayer().clone();
        clone.move(direction);
        MapDescription.Tile tile = Map.getInstance().at(clone.location);
        if (!tile.isWalkable()) {
            return false;
        }
        switch (direction) {
            case Directions.SOUTH:
                new WalkSouthPacket().send(os);
                break;
            case Directions.WEST:
                new WalkWestPacket().send(os);
                break;
            case Directions.NORTH:
                new WalkNorthPacket().send(os);
                break;
            case Directions.EAST:
                new WalkEastPacket().send(os);
                break;
            default:
                throw new Exception("direction is not yet supported.");
        }
        walkDirection = direction;
        return true;
    }

    /**
     * @return whether a path is found.
     */
    public boolean walk(Loc to) throws IOException {
        short[] path = Map.getInstance().findPath(to, false, false);
        if (path == null) {
            return false;
        }
        walk(path);
        return true;
    }

    public boolean reach(Loc to) throws IOException {
        short[] path = Map.getInstance().findPath(to, false, true);
        if (path == null) {
            return false;
        }
        walk(path);
        return true;
    }

    /**
     * @return whether a path is found.
     */
    public boolean walk(int x, int y, short z) throws IOException {
        return walk(new Loc(x, y, z));
    }

    public void walk(short[] path) throws IOException {
        stopWalking();
        new RequestCreatureFlagUpdatePacket(1, 0, 0, 0).send(os);
        new PathPacket(path).send(os);
        remainingSteps = path.length;
    }

    public void stopWalking() throws IOException {
        remainingSteps = 0;
        new PathPacket(new short[0]).send(os);
    }

    public boolean isWalking() {
        return remainingSteps > 0;
    }

    /**
     * Casts the given spells immediately.
     *
     * @param spellID
     * @throws IOException
     */
    public void cast(int spellID) throws IOException {
        new CastSpellPacket(spellID).send(os);
    }

    @Override
    public void onReceiveException(Exception e) {
        e.printStackTrace();
        //receiver.cancel();
    }

    @Override
    public void onHandleException(Exception e) {
        logger.log(Level.WARNING, "Error while handling a packet received from the server.", e);
    }

    @Override
    public void handleErrorPacket(ErrorPacket p) {
        if (expectingLoginResponse) {
            listener.onLoginResponse(p.message);
            expectingLoginResponse = false;
        }
    }

    @Override
    public void handleCharListPacket(CharListPacket p) {
    }

    @Override
    public void handleAfterLoginPacket(AfterLoginPacket p) throws Exception {
        if (!expectingLoginResponse) {
            throw new Exception("Unexpected behaviour from the server: an AfterLoginPacket was received before sending a WorldLoginpacket or received more than once.");
        }
        Map.getInstance().update(p.map);
        for (short i = 0; i < p.equipment.size(); i++) {
            Item item = new Item(ItemLoc.fromEquipment(i), p.equipment.get(i));
            equipment.add(item);
        }
        new Channel(ChannelType.PUBLIC, "Main").open();
        listener.onLoginResponse(null);
        expectingLoginResponse = false;
    }

    @Override
    public void handleMessageChannelPacket(MessageChannelPacket p) {
        //This handle a case when we receive a priv - a channel should be
        //opened without sending an ROChPacket.
        //Check for channel
        Channel ch = Channel.byName(p.channelName);
        if (ch == null) { //Open it and add to the list if it doesn't exist
            //but not by using .open() as we don't want to send a packet
            ch = new Channel(ChannelType.PRIVATE, p.channelName); //it's always priv
            openChannels.add(ch);
            listener.onChannelOpen(ch);
        }

        listener.onMessage(ch, p.sender, p.message);
    }

    @Override
    public void handleOpenChannelPacket(OpenChannelPacket p) {
        //The channel was actually created already but I have to
        //recreate it (without calling .open() this time).
        Channel ch = new Channel(p.type, p.name);
        if (!ch.isOpen()) {
            openChannels.add(ch);
            listener.onChannelOpen(ch);
        }
    }

    @Override
    public void handleLogoutPacket(LogoutPacket p) {
        listener.onLogout();
    }

    @Override
    public void handleStatusUpdatePacket(StatusUpdatePacket p) throws Exception {
        Status oldStatus = playerStatus;
        playerStatus = p.status;
        listener.onStatusUpdate(oldStatus, p.status);
    }

    @Override
    public void handleCreatureAppearPacket(CreatureAppearPacket p) throws Exception {
        Entity e = new Entity(p.location, p.creature);
        Map.getInstance().addOrUpdateEntity(e);
        listener.onEntityAppear(e);
    }

    @Override
    public void handleCreatureDisappearPacket(CreatureDisappearPacket p) throws Exception {
        Entity e = Map.getInstance().getEntityByID(p.creatureID);
        Map.getInstance().removeEntity(p.creatureID);
        listener.onEntityDisappear(e);
    }

    @Override
    public void handleCreatureWalkPacket(CreatureWalkPacket p) throws Exception {
        if (p.creatureID == Map.getInstance().getPlayer().id) {
            if (isWalking()) {
                remainingSteps--;
            }
        }
        Map.getInstance().moveEntity(p.creatureID, p.direction);
        listener.onEntityWalk(Map.getInstance().getEntityByID(p.creatureID), p.direction);
    }

    @Override
    public void handleAfterWalkPacket(AfterWalkPacket p) throws Exception {
        Map.getInstance().moveEntity(Map.getInstance().getPlayer().id, (short) walkDirection);
        walkDirection = -1;

        short floorBefore = Map.getInstance().getPlayer().location.z;
        Map.getInstance().update(p.map);
        short floorAfter = Map.getInstance().getPlayer().location.z;
        if (floorBefore != floorAfter) {
            if (isWalking()) {
                remainingSteps = 0;
            }
        }
    }

    @Override
    public void handleMapUpdatePacket(MapUpdatePacket p) throws Exception {
        short floorBefore = Map.getInstance().getPlayer().location.z;
        Map.getInstance().update(p.map);
        short floorAfter = Map.getInstance().getPlayer().location.z;
        if (floorBefore != floorAfter) {
            if (isWalking()) {
                remainingSteps = 0;
            }
        }
    }

    @Override
    public void handleTileUpdatePacket(TileUpdatePacket p) throws Exception {
        Map.getInstance().updateTile(p.tile);
    }

    @Override
    public void handleTimeoutCheckPacket(TimeoutCheckPacket p) throws Exception {
        //Upon receiving a timeout check, we need to instantly react with a confirmconnection.
        new ConfirmConnectionPacket(p.random).send(os);
    }

    @Override
    public void handleUpdateSignsPacket(UpdateSignsPacket p) throws Exception {
    }

    @Override
    public void handleCancelWalkPacket(CancelWalkPacket p) throws Exception {
        // TODO: something with p.message
        walkDirection = -1;
    }

    @Override
    public void handleCreatureFlagUpdatePacket(CreatureFlagUpdatePacket p) throws Exception {
        currentlyAttacked = Map.getInstance().getEntityByID(p.attackedID);
        currentlyFollowed = Map.getInstance().getEntityByID(p.followedID);
        listener.onCreatureFlagUpdate(p.attackedID, p.followedID);
    }

    @Override
    public void handleAnimatedTextPacket(AnimatedTextPacket p) throws Exception {
    }

    @Override
    public void handleEffectPacket(EffectPacket p) throws Exception {
    }

    @Override
    public void handleCancelPacket(CancelPacket p) throws Exception {
        lastCancelMessage = p.message;
        lastCancelTime = System.currentTimeMillis();
        listener.onCancel(p.message);
    }

    @Override
    public void handleOpenContainerPacket(OpenContainerPacket p) throws Exception {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < p.items.size(); i++) {
            ItemLoc l = ItemLoc.fromContainer(p.containerID, (short) (i + 1));
            items.add(new Item(l, p.items.get(i)));
        }
        Container cont = new Container(p.containerID, p.name, p.volume, items);

        Container inList = Container.byID(cont.id);
        if (inList != null) {
            inList.name = cont.name;
            inList.volume = cont.volume;
            inList.items = cont.items;
            listener.onContainerUpdate(inList);
        } else {
            openContainers.add(cont);
            listener.onContainerOpen(cont);
        }
    }

    @Override
    public void handleCloseContainerPacket(CloseContainerPacket p) throws Exception {
        Container cont = Container.byID(p.containerID);
        openContainers.remove(cont);
        listener.onContainerClose(cont);
    }

    @Override
    public void handleCancelPathPacket(CancelPathPacket p) throws Exception {
        remainingSteps = 0;
    }
    
    @Override
    public void handlePeopleUpdatePacket(PeopleUpdatePacket p) throws Exception {
        people = p.people;
    }

    public void close() throws IOException {
        receiver.interrupt();
        socket.close();
    }

    // ===================== GETTERS & SETTERS =====================
    public List<Channel> getOpenChannels() {
        return openChannels;
    }

    public List<Container> getOpenContainers() {
        return openContainers;
    }

    public Status getPlayerStatus() {
        return playerStatus;
    }

    public structures.Character getCharacter() {
        return character;
    }

    public Entity getCurrentlyAttacked() {
        return currentlyAttacked;
    }

    public Entity getCurrentlyFollowed() {
        return currentlyFollowed;
    }

    public List<Item> getEquipment() {
        return equipment;
    }

    public OutputStream getOs() {
        return os;
    }

    public InputStream getIs() {
        return is;
    }

    public String getLastCancelMessage() {
        return lastCancelMessage;
    }

    public long getLastCancelTime() {
        return lastCancelTime;
    }
}
