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
public class WalkNorthPacket extends Packet {

    public WalkNorthPacket() {
        id = PacketConstants.WalkNorthPacket;
    }

    @Override
    public WalkNorthPacket read(NetworkMessage m) throws IOException {
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
    }
}
