/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Myzreal
 */
public class Loc extends Structure {

    public int x;
    public int y;
    public short z;

    public Loc() {}
    
    public Loc(int x, int y, short z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Loc read(NetworkMessage m) throws IOException {
        x = m.readUshort();
        y = m.readUshort();
        z = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(x);
        m.writeUshort(y);
        m.writeByte(z);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Loc other = (Loc) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        hash = 53 * hash + this.z;
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("%d/%d/%d", x, y, z);
    }
    
    @Override
    public Loc clone() {
        return new Loc(x, y, z);
    }
    
    public Loc add(int x, int y, short z) {
        return add(new Loc(x, y, z));
    }
    
    public Loc add(Loc l) {
        return new Loc(x + l.x, y + l.y, (short) (z + l.z));
    }
    
    /**
     * @return the direction of this location from l. Returns 0 if both are equal.
     */
    public short directionFrom(Loc l) {
        if(x > l.x) {
            if(y > l.y) {
                return Directions.SOUTH_EAST;
            }
            if(l.y > y) {
                return Directions.NORTH_EAST;
            }
            return Directions.EAST;
        }
        if(l.x > x) {
            if(y > l.y) {
                return Directions.SOUTH_WEST;
            }
            if(l.y > y) {
                return Directions.NORTH_WEST;
            }
            return Directions.WEST;
        }
        if(y > l.y) {
            if(x > l.x) {
                return Directions.SOUTH_EAST;
            }
            if(l.x > x) {
                return Directions.SOUTH_WEST;
            }
            return Directions.SOUTH;
        }
        if(l.y > y) {
            if(x > l.x) {
                return Directions.NORTH_EAST;
            }
            if(l.x > x) {
                return Directions.NORTH_WEST;
            }
            return Directions.NORTH;
        }
        return 0;
    }
    
    public boolean isVisibleFrom(Loc l) {
        int startz, endz;
        if (l.z > 7) {
            startz = l.z - 2;
            endz = l.z + 2;
        } else {
            startz = 0;
            endz = 7;
        }

        return z >= startz && z <= endz
                && x >= (l.x - 8) && x <= (l.x + 9)
                && y >= (l.y - 6) && y <= (l.y + 7);
    }
}
