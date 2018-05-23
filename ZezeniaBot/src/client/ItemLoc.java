/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import structures.Loc;

/**
 *
 * @author Moshe Revah
 */
public class ItemLoc {

    public static final int GROUND = 0;
    public static final int EQUIPMENT = 1;
    public static final int CONTAINER = 2;
    
    public int type;
    
    public Loc location;
    public int stackOrder;
    
    public short containerID;
    public short containerSlot;
    
    public short equipmentSlot;

    private ItemLoc() {
    }

    public static ItemLoc fromLocation(Loc l, int stackOrder) {
        ItemLoc iloc = new ItemLoc();
        iloc.type = ItemLoc.GROUND;
        iloc.location = l;
        iloc.stackOrder = stackOrder;
        return iloc;
    }

    public static ItemLoc fromLocation(Loc l) {
        return fromLocation(l, 0);
    }

    public static ItemLoc fromContainer(short id, short slot) {
        ItemLoc iloc = new ItemLoc();
        iloc.type = ItemLoc.CONTAINER;
        iloc.containerID = id;
        iloc.containerSlot = slot;
        return iloc;
    }

    public static ItemLoc fromEquipment(short slot) {
        ItemLoc iloc = new ItemLoc();
        iloc.type = ItemLoc.EQUIPMENT;
        iloc.equipmentSlot = slot;
        return iloc;
    }

    /**
     * @return the item in this location.
     */
    public Item get() {
        structures.Item item = null;
        try {
            switch (type) {
                case ItemLoc.GROUND:
                    item = Map.getInstance().at(location).items.get(stackOrder);
                    break;
                case ItemLoc.EQUIPMENT:
                    item = Client.getInstance().getEquipment().get(equipmentSlot);
                    break;
                case ItemLoc.CONTAINER:
                    item = Container.byID(containerID).items.get(containerSlot);
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return item == null ? null : new Item(this, item);
    }
    
    /**
     * @return location required by an ItemXXXPacket.
     */
    public Loc toLocation() {
        Loc l = new Loc();
        switch (type) {
            case ItemLoc.GROUND:
                l = location;
                break;
            case ItemLoc.CONTAINER:
                l.x = 0xFFFE;
                l.y = containerID;
                l.z = containerSlot;
                break;
            case ItemLoc.EQUIPMENT:
                l.x = 0xFFFF;
                l.z = equipmentSlot;
                break;
        }
        return l;
    }
}
