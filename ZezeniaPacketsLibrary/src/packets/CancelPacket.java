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
public class CancelPacket extends Packet {
    
    public String message;
    
    public CancelPacket() {
        id = PacketConstants.CancelPacket;
    }
    
    public CancelPacket(String message) {
        this();
        this.message = message;
    }

    @Override
    public CancelPacket read(NetworkMessage m) throws IOException {
        message = m.readString();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeString(message);
    }
    
}
