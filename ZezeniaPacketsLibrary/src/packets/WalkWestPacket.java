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
public class WalkWestPacket extends Packet {

    public WalkWestPacket() {
        id = PacketConstants.WalkWestPacket;
    }

    @Override
    public WalkWestPacket read(NetworkMessage m) throws IOException {
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
    }
}
