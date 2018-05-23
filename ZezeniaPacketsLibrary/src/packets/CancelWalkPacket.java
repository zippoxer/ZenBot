/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Moshe Revah
 */
public class CancelWalkPacket extends Packet {

    public String message;
    public short unanalyzed;
    
    public CancelWalkPacket() {
        id = PacketConstants.CancelWalkPacket;
    }
    
    public CancelWalkPacket(String message, short unanalyzed) {
        this.message = message;
        this.unanalyzed = unanalyzed;
    }
    
    @Override
    public CancelWalkPacket read(NetworkMessage m) throws IOException {
        message = m.readString();
        unanalyzed = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeString(message);
        m.writeByte(unanalyzed);
    }
    
}
