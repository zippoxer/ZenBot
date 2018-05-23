/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import java.io.IOException;
import network.NetworkMessage;
import resources.Resource;

/**
 *
 * @author Myzreal
 */
public class Item extends Structure {

    public static boolean isWalkable(int id) {
        for(int n : Resource.getWalkables()) {
            if(n == id) {
                return true;
            }
        }
        for(int n : Resource.getPathblockers()) {
            if(n == id) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isStackable(int id) {
        for(int n : Resource.getStackables()) {
            if(n == id) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPathblocker(int id) {
        return isWalkablePathblocker(id) || !isWalkable(id);
    }
    
    public static boolean isWalkablePathblocker(int id) {
        for(int n : Resource.getPathblockers()) {
            if(n == id) {
                return true;
            }
        }
        return false;
    }
    
    public int id;

    public short count;

    public Item() {
    }

    public Item(int id, short count) {
        this.id = id;
        this.count = count;
    }

    @Override
    public Item read(NetworkMessage m) throws IOException {
        id = m.readUshort();
        count = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(id);
        m.writeByte(count);
    }
}
