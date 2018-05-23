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
public class Creature extends Structure {

    public short unanalyzed;
    public int id;
    public short[] unanalyzed2;
    public String name;
    public short health;
    public short direction;
    public short[] outfit;

    public Creature() {}
    
    public Creature(byte unanalyzed, int id, String name, byte health,
            byte direction, short[] outfit) {
        this.unanalyzed = unanalyzed;
        this.id = id;
        this.name = name;
        this.health = health;
        this.direction = direction;
        this.outfit = outfit;
    }

    @Override
    public Creature read(NetworkMessage m) throws IOException {
        unanalyzed = m.readByte();
        id = m.readUshort();
        unanalyzed2 = m.read(2);
        name = m.readString();
        health = m.readByte();
        direction = m.readByte();
        outfit = m.read(14);
        if(unanalyzed == 1) {
            m.readByte();
        }
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte(unanalyzed);
        m.writeUshort(id);
        m.write(unanalyzed2);
        m.writeString(name);
        m.writeByte(health);
        m.writeByte(direction);
        m.write(outfit);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Creature other = (Creature) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
        return hash;
    }
}
