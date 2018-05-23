/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Myzreal
 */
public class CreatureWalkPacket extends Packet {
    
    public int creatureID;
    public short direction;
    
    public CreatureWalkPacket() {
        id = PacketConstants.CreatureWalkPacket;
    }
    
    public CreatureWalkPacket(int id, short direction) {
        this();
        this.creatureID = id;
        this.direction = direction;
    }

    @Override
    public CreatureWalkPacket read(NetworkMessage m) throws IOException {
        creatureID = m.readUshort();
        direction = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(creatureID);
        m.writeByte(direction);
    }
    
}
