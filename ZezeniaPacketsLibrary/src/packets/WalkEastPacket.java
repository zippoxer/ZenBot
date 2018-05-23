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
public class WalkEastPacket extends Packet {

    public WalkEastPacket() {
        id = PacketConstants.WalkEastPacket;
    }

    @Override
    public WalkEastPacket read(NetworkMessage m) throws IOException {
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
    }
}
