package client.networking;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import packets.*;

import network.NetworkMessage;

/**
 * * A separate thread. * Listens to the server. * Recognizes packets and
 * interprets them.
 *
 * @author Myzreal
 */
public class PacketReceiver extends Thread {

    private static final Logger logger = Logger.getLogger(PacketReceiver.class.getName());
    private InputStream is;
    private PacketHandler handler;

    public PacketReceiver(InputStream is, PacketHandler handler) {
        this.is = is;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                NetworkMessage p = NetworkMessage.receive(is);
                try {
                    handle(p);
                } catch (Exception e) {
                    handler.onHandleException(e);
                }
                sleep(1);
            }
        } catch (IOException e) {
            handler.onReceiveException(e);
        } catch(InterruptedException e) {
        }
    }

    private void handle(NetworkMessage m) throws Exception {
        while (m.available() > 0) {
            int id = m.readByte();
            //logger.log(Level.INFO, "Received a packet of id {0}", id);
            switch (id) {
                case PacketConstants.ErrorPacket:
                    handler.handleErrorPacket(new ErrorPacket().read(m));
                    break;
                case PacketConstants.CharListPacket:
                    handler.handleCharListPacket(new CharListPacket().read(m));
                    break;
                case PacketConstants.AfterLoginPacket:
                    handler.handleAfterLoginPacket(new AfterLoginPacket().read(m));
                    break;
                case PacketConstants.MessageChannelPacket:
                    handler.handleMessageChannelPacket(new MessageChannelPacket().read(m));
                    break;
                case PacketConstants.OpenChannelPacket:
                    handler.handleOpenChannelPacket(new OpenChannelPacket().read(m));
                    break;
                case PacketConstants.LogoutPacket:
                    handler.handleLogoutPacket(new LogoutPacket().read(m));
                    break;
                case PacketConstants.StatusUpdatePacket:
                    handler.handleStatusUpdatePacket(new StatusUpdatePacket().read(m));
                    break;
                case PacketConstants.CreatureAppearPacket:
                    handler.handleCreatureAppearPacket(new CreatureAppearPacket().read(m));
                    break;
                case PacketConstants.CreatureDisappearPacket:
                    handler.handleCreatureDisappearPacket(new CreatureDisappearPacket().read(m));
                    break;
                case PacketConstants.CreatureWalkPacket:
                    handler.handleCreatureWalkPacket(new CreatureWalkPacket().read(m));
                    break;
                case PacketConstants.AfterWalkPacket:
                    handler.handleAfterWalkPacket(new AfterWalkPacket().read(m));
                    break;
                case PacketConstants.MapUpdatePacket:
                    handler.handleMapUpdatePacket(new MapUpdatePacket().read(m));
                    break;
                case PacketConstants.TileUpdatePacket:
                    handler.handleTileUpdatePacket(new TileUpdatePacket().read(m));
                    break;
                case PacketConstants.TimeoutCheckPacket:
                    handler.handleTimeoutCheckPacket(new TimeoutCheckPacket().read(m));
                    break;
                case PacketConstants.UpdateSignsPacket:
                    handler.handleUpdateSignsPacket(new UpdateSignsPacket().read(m));
                    break;
                case PacketConstants.CancelWalkPacket:
                    handler.handleCancelWalkPacket(new CancelWalkPacket().read(m));
                    break;
                case PacketConstants.CreatureFlagUpdatePacket:
                    handler.handleCreatureFlagUpdatePacket(new CreatureFlagUpdatePacket().read(m));
                    break;
                case PacketConstants.AnimatedTextPacket:
                    handler.handleAnimatedTextPacket(new AnimatedTextPacket().read(m));
                    break;
                case PacketConstants.EffectPacket:
                    handler.handleEffectPacket(new EffectPacket().read(m));
                    break;
                case PacketConstants.CancelPacket:
                    handler.handleCancelPacket(new CancelPacket().read(m));
                    break;
                case PacketConstants.OpenContainerPacket:
                    handler.handleOpenContainerPacket(new OpenContainerPacket().read(m));
                    break;
                case PacketConstants.CloseContainerPacket:
                    handler.handleCloseContainerPacket(new CloseContainerPacket().read(m));
                    break;
                case PacketConstants.CancelPathPacket:
                    handler.handleCancelPathPacket(new CancelPathPacket().read(m));
                    break;
                case PacketConstants.PeopleUpdatePacket:
                    handler.handlePeopleUpdatePacket(new PeopleUpdatePacket().read(m));
                    break;
                case 0xC:
                    m.read(39);
                    break;
                case 0x13:
                    m.read(10);
                    break;
                case 0x15:
                    m.read(10);
                    break;
                default:
                    // An unknown packet is read. All future packets in this message (if there are) are unreadable.
                    m.read(m.available());
                    throw new UnknownPacketException("Unknown packet of id " + id);
            }
        }
    }
}
