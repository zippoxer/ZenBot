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
public class CreatureDisappearPacket extends Packet {
    
    public int creatureID;
    
    public CreatureDisappearPacket() {
        id = PacketConstants.CreatureDisappearPacket;
    }
    
    public CreatureDisappearPacket(int id) {
        this.creatureID = id;
    }

    @Override
    public CreatureDisappearPacket read(NetworkMessage m) throws IOException {
        creatureID = m.readUshort();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeShort((short) creatureID);
    }
    
}
