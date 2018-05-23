/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import client.*;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import gui.CavebotFrame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import settings.CavebotSettings;
import structures.Loc;
import structures.MapDescription;

/**
 *
 * @author Myzreal
 */
public class Cavebot extends Thread {

    private static final Logger logger = Logger.getLogger(Cavebot.class.getName());
    private CavebotSettings settings;

    public Cavebot(CavebotSettings settings) {
        this.settings = settings;
    }

    @Override
    public void run() {
        logger.info("cavebot started");
        try {
            while (true) {
                killSurroundingTargets();
                int index = CavebotFrame.getInstance().waypointsList.getSelectedIndex();
                if (index == -1) {
                    index = 0;
                }
                for (int i = index; i < settings.waypoints.size(); i++) {
                    CavebotFrame.getInstance().selectWaypoint(i);

                    logger.log(Level.INFO, "doing waypoint {0}", i);
                    Waypoint waypoint = settings.waypoints.get(i);
                    Waypoint nextWaypoint = null;
                    try {
                        nextWaypoint = settings.waypoints.get(i + 1);
                    } catch (IndexOutOfBoundsException ex) {
                    }
                    switch (waypoint.type) {
                        case Waypoint.WALK:
                            walk(waypoint.location, (nextWaypoint != null ? nextWaypoint.location : null), false, true);
                            break;
                        case Waypoint.LURE:
                            walk(waypoint.location, null, false, false);
                            break;
                        case Waypoint.USE:
                            // TODO: expectedLocation = anywhere around waypoint.location
                            if (walk(waypoint.location, null, false, true)) {
                                int stackOrder = 0;
                                boolean expectFloorChange = false;
                                List<structures.Item> items = Map.getInstance().at(waypoint.location).items;
                                for (int j = 0; j < items.size(); j++) {
                                    structures.Item item = items.get(j);
                                    // give priority to a ladder or sewer gate
                                    if (item.id == 274 || item.id == 203) {
                                        stackOrder = j;
                                        expectFloorChange = true;
                                    }
                                }
                                ItemLoc iLoc = ItemLoc.fromLocation(waypoint.location, stackOrder);
                                iLoc.get().use();
                                if (expectFloorChange) {
                                    while (Map.getInstance().getPlayer().location.z == waypoint.location.z) {
                                        sleep(1);
                                    }
                                }
                            }
                            break;
                    }
                    logger.log(Level.INFO, "done waypoint {0}", i);
                    sleep(1);
                }
                CavebotFrame.getInstance().selectWaypoint(0);
            }
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof IOException) {
                ex.printStackTrace();
            }
            logger.info("cavebot terminated");
        }
    }

    private boolean walk(Loc destination, Loc nextDestination, boolean untilReachable, boolean killTargets) throws IOException, InterruptedException {
        long lastWalk = System.currentTimeMillis();
        long timeSpentKilling = 0; // time spent killing since last (successful) walk.
        while (true) {
            if (killTargets) {
                long start = System.currentTimeMillis();
                int killed = killSurroundingTargets();
                timeSpentKilling += System.currentTimeMillis() - start;

                /**
                 * TODO: test if the path from the current location to
                 * nextLocation is shorter than the path from expectedLocation
                 * to nextLocation, if it is, then walk to it.
                 */
                if (killed > 0 && nextDestination != null) {
                    short[] path1 = Map.getInstance().findPath(nextDestination, false, false);
                    if (path1 != null) {
                        short[] path2 = Map.getInstance().findPath(destination, nextDestination, false, false);
                        if (path2 != null) {
                            if (path1.length < path2.length) {
                                return false;
                            }
                        }
                    }
                }
            }
            if (Map.getInstance().getPlayer().location.z != destination.z) {
                return false;
            }
            if (!Client.getInstance().isWalking()) {
                if (untilReachable) {
                    if (Map.getInstance().isReachable(destination)) {
                        return true;
                    }
                } else if (Map.getInstance().getPlayer().location.equals(destination)) {
                    return true;
                }
                boolean pathFound;
                if (untilReachable) {
                    pathFound = Client.getInstance().reach(destination);
                } else {
                    pathFound = Client.getInstance().walk(destination);
                }
                if (pathFound) {
                    lastWalk = System.currentTimeMillis();
                    timeSpentKilling = 0;

                    // make sure that the trapped alarm is off.
                    CavebotFrame.getInstance().stopAlarm(Alarmer.TRAPPED);
                } else {
                    // no path found
                    if (System.currentTimeMillis() - lastWalk - timeSpentKilling > 30000) {
                        // no path found for over 30 seconds, discounting time spent on killing
                        // STUCK!
                        CavebotFrame.getInstance().startAlarm(Alarmer.TRAPPED);
                    }
                }
            }
            sleep(1);
        }
    }

    private int killSurroundingTargets() throws IOException, InterruptedException {
        int killed = 0;
        while (true) {
            Entity target = nextTarget();
            if (target == null) {
                break;
            }
            logger.info("target found");
            Client.getInstance().stopWalking();
            target.chase();
            long attackStart = System.currentTimeMillis();
            boolean skip = false;
            while (target.health > 0) {
                if (System.currentTimeMillis() - attackStart > 2000) {
                    boolean notAttacked = Client.getInstance().getCurrentlyAttacked() == null
                            || Client.getInstance().getCurrentlyAttacked().id != target.id;
                    boolean notFollowed = Client.getInstance().getCurrentlyFollowed() == null
                            || Client.getInstance().getCurrentlyFollowed().id != target.id;
                    if (notAttacked && notFollowed) {
                        if (System.currentTimeMillis() - attackStart > 4000) {
                            skip = true;
                            break;
                        }
                        target.chase();
                    } else if (notAttacked) {
                        target.attack();
                    } else if (notFollowed) {
                        target.follow();
                    }
                }
                if (Map.getInstance().findPath(target.location, true, true) == null) {
                    logger.info("leaving target because it became unreachable");
                    skip = true;
                    break;
                }
                sleep(50);
            }
            if (skip) {
                continue;
            }
            logger.info("target died");
            sleep(new Random().nextInt(630) + 181);
            if (settings.loot.size() > 0 || settings.lootExcept) {
                if (loot(target)) {
                    logger.info("looting done");
                } else {
                    logger.warning("looting failed");
                }
            }
            killed++;
        }
        return killed;
    }
    private Container targetContainer;

    private boolean loot(Entity target) throws IOException, InterruptedException {
        int openedContainers = Client.getInstance().getOpenContainers().size();
        if (openedContainers == 0) {
            return false;
        }
        if (targetContainer == null) {
            targetContainer = Client.getInstance().getOpenContainers().get(0);
            if (targetContainer == null) {
                logger.warning("no container found, where to loot to?");
                return false;
            }
        }
        int stack = 0;
        MapDescription.Tile tile = Map.getInstance().at(target.location);
        for (structures.Item item : tile.items) {
            if (Item.isDoor(item.id)) {
                // a door's stack order is always 0.
                stack = 1;
                break;
            }
        }
        if (Map.getInstance().at(target.location).items.size() < 2) {
            logger.warning("no corpse found");
            return false;
        }
        Container cont = null;
        for (int i = 0; i < 2; i++) {
            Item corpse = ItemLoc.fromLocation(target.location, stack).get();
            if (!Map.getInstance().isReachable(corpse.location.location)) {
                logger.info("walking to corpse...");
                walk(corpse.location.location, null, false, false);
            }
            logger.info("opening corpse...");
            cont = Container.open(corpse, new Random().nextInt(600) + 1700);
            if (cont != null) {
                break;
            }
        }
        if (cont == null) {
            logger.warning("could not open corpse");
            return false;
        }
        boolean succeeded = true;
        if (cont.items.size() > 0) {
            succeeded = lootContainer(cont, true);
        } else {
            logger.info("corpse is empty");
        }
        if (Client.getInstance().getOpenContainers().size() > new Random().nextInt(3) + 2) {
            logger.info("closing corpse...");
            cont.close();
        }
        sleep(new Random().nextInt(200) + 104);
        return succeeded;
    }

    private boolean lootContainer(Container cont, boolean eat) throws IOException, InterruptedException {
        if (!cont.isOpen()) {
            logger.warning("corpse closed while looting");
            return false;
        }
        if (cont.items.isEmpty()) {
            return true;
        }
        Item item = null;
        Item food = null;
        itemloop:
        for (Item i : cont.items) {
            boolean match = false;
            for (CavebotSettings.LootItem lootItem : settings.loot) {
                if (lootItem.id == i.id) {
                    match = true;
                    break;
                }
            }
            if (settings.lootExcept ^ match) {
                item = i;
                break;
            }
            if (food == null && Item.isFood(i.id)) {
                food = i;
            }
        }
        if (item != null) {
            if (targetContainer.emptySlots() == 0) {
                logger.info("target container is full; opening next container");
                if (!openNextContainer()) {
                    return false;
                }
                sleep(new Random().nextInt(1000) + 800);
            }
            logger.log(Level.INFO, "moving {0} items of id {1}...", new Object[]{item.count, item.id});
            int slot = 1;
            for (Item i : targetContainer.items) {
                if (i.id == item.id && i.count < 100) {
                    slot = i.location.containerSlot;
                    break;
                }
            }
            item.move(ItemLoc.fromContainer(targetContainer.id, (short) slot));
            long start = System.currentTimeMillis();
            int before = cont.items.size();
            while (before == cont.items.size()) {
                if (Client.getInstance().getLastCancelTime() > start
                        && Client.getInstance().getLastCancelMessage().toLowerCase().contains("nothing")) {
                    logger.warning("something went wrong with opening the corpse");
                    return false;
                }
                if (System.currentTimeMillis() - start > 2000) {
                    return lootContainer(cont, true);
                }
                sleep(10);
            }
            logger.info("moved");
            if (item.id == 395 && CavebotFrame.getInstance().calculator != null) {
                CavebotFrame.getInstance().calculator.addGold(item.count);
            }
            sleep(new Random().nextInt(160) + 95);
            return lootContainer(cont, true);
        }
        if (eat && food != null) {
            logger.log(Level.INFO, "found {0} food of id {1}, eating...", new Object[]{food.count, food.id});
            int timesToUse = new Random().nextInt(2) + food.count;
            long start = System.currentTimeMillis();
            int itemsBefore = cont.items.size();
            for (int i = 0; i < timesToUse; i++) {
                food.use();
                sleep(new Random().nextInt(200) + 64);
                if (Client.getInstance().getLastCancelTime() > start
                        && Client.getInstance().getLastCancelMessage().toLowerCase().contains("nothing")) {
                    logger.warning("something went wrong with opening the corpse");
                    return false;
                }
                if (Client.getInstance().getLastCancelTime() > start
                        && Client.getInstance().getLastCancelMessage().equals("You are full.")) {
                    logger.info("full!");
                    return lootContainer(cont, false);
                }
            }
            long start2 = System.currentTimeMillis();
            while (itemsBefore == cont.items.size()) {
                if (Client.getInstance().getLastCancelTime() > start
                        && Client.getInstance().getLastCancelMessage().toLowerCase().contains("nothing")) {
                    logger.warning("something went wrong with opening the corpse");
                    return false;
                }
                if (Client.getInstance().getLastCancelTime() > start
                        && Client.getInstance().getLastCancelMessage().equals("You are full.")) {
                    logger.info("full!");
                    return lootContainer(cont, false);
                }
                if (System.currentTimeMillis() - start2 > 2000) {
                    return lootContainer(cont, true);
                }
                sleep(10);
            }
            logger.info("ate");
            return lootContainer(cont, true);
        }
        return true;
    }

    private boolean openNextContainer() throws IOException, InterruptedException {
        while (targetContainer.emptySlots() == 0) {
            Item nextContainer = targetContainer.findItem(3141);
            if (nextContainer == null) {
                nextContainer = targetContainer.findItem(401);
                if (nextContainer == null) {
                    logger.warning("could not find next container");
                    return false;
                }
            }
            Container oldContainer = targetContainer;
            targetContainer = Container.open(nextContainer, 2000);
            if (targetContainer == null) {
                targetContainer = Container.open(nextContainer, 2000);
                if (targetContainer == null) {
                    targetContainer = oldContainer;
                    logger.warning("found next container but could not open it");
                    return false;
                }
            }
            if (oldContainer.id != 0) {
                oldContainer.close();
            }
        }
        return true;
    }

    private Entity nextTarget() {
        List<Target> targets = new ArrayList<>();
        for (Entity e : Map.getInstance().getEntities()) {
            if (!e.isPlayer()) {
                int index = -1;
                if (settings.targetAll) {
                    index = 0;
                } else {
                    for (int i = 0; i < settings.targets.size(); i++) {
                        String target = settings.targets.get(i);
                        if (e.name.equalsIgnoreCase(target)) {
                            index = i;
                            break;
                        }
                    }
                }
                if (index != -1) {
                    short[] path = Map.getInstance().findPath(e.location, true, true);
                    if (path != null) {
                        targets.add(new Target(index, path.length, e));
                    }
                }
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        Ordering<Target> targetOrdering = new Ordering<Target>() {

            @Override
            public int compare(Target left, Target right) {
                int index = Ints.compare(left.index, right.index);
                if (index != 0) {
                    return index;
                }
                return Ints.compare(left.distance, right.distance);
            }
        };
        return targetOrdering.sortedCopy(targets).get(0).entity;
    }

    private static class Target {

        public int index; // how far is it from the beginning of the targets list.
        public int distance;
        public Entity entity;

        public Target(int index, int distance, Entity entity) {
            this.index = index;
            this.distance = distance;
            this.entity = entity;
        }
    }
}
