/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 * Since directions for different actions (like appearing and moving) seem to differ,
 * this class will hold constants for them.
 * @author Myzreal
 */
public class Directions {
    
    //These values are used in the CreatureWalkPacket 0x8 and in CreatureAppearPacket 0x6
    public static final short SOUTH = 0x1;
    public static final short WEST = 0x2;
    public static final short NORTH = 0x3;
    public static final short EAST = 0x4;
    
    public static final short SOUTH_WEST = 0x5;
    public static final short NORTH_WEST = 0x6;
    public static final short NORTH_EAST = 0x7;
    public static final short SOUTH_EAST = 0x8;
    
    /**
     * @return S, W, SW, etc...
     */
    public static String toString(short direction) {
        switch(direction) {
            case SOUTH:
                return "S";
            case WEST:
                return "W";
            case NORTH:
                return "N";
            case EAST:
                return "E";
            case SOUTH_WEST:
                return "SW";
            case NORTH_WEST:
                return "NW";
            case NORTH_EAST:
                return "NE";
            case SOUTH_EAST:
                return "SE";
        }
        return null;
    }
}
