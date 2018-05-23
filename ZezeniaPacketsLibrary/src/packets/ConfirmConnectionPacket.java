/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Radek
 */
public class ConfirmConnectionPacket extends Packet {
    
    public int random;
    
    public ConfirmConnectionPacket() {
        id = PacketConstants.ConfirmConnectionPacket;
    }
    
    public ConfirmConnectionPacket(int random) {
        this();
        this.random = random;
    }

    @Override
    public ConfirmConnectionPacket read(NetworkMessage m) throws IOException {
        random = m.readUshort();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(random);
    }
    
}
