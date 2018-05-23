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
public class RequestCloseContainerPacket extends Packet {
    
    public short containerId;
    
    public RequestCloseContainerPacket() {
        this.id = PacketConstants.RequestCloseContainerPacket;
    }
    
    public RequestCloseContainerPacket(short id) {
        this();
        this.containerId = id;
    }

    @Override
    public RequestCloseContainerPacket read(NetworkMessage m) throws IOException {
        this.containerId = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte(containerId);
    }
    
}
