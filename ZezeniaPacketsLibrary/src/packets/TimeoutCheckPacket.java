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
public class TimeoutCheckPacket extends Packet {
    
    public int random;
    
    public TimeoutCheckPacket() {
        id = PacketConstants.TimeoutCheckPacket;
    }
    
    public TimeoutCheckPacket(int random) {
        this();
        this.random = random;
    }

    @Override
    public TimeoutCheckPacket read(NetworkMessage m) throws IOException {
        random = m.readUshort();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(random);
    }
    
}
