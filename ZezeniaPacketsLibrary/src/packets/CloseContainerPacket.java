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
public class CloseContainerPacket extends Packet {
    
    public short containerID;
    
    public CloseContainerPacket() {
        id = PacketConstants.CloseContainerPacket;
    }
    
    public CloseContainerPacket(short id) {
        this();
        this.containerID = id;
    }

    @Override
    public CloseContainerPacket read(NetworkMessage m) throws IOException {
        containerID = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte(containerID);
    }
    
}
