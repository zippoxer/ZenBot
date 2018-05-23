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
public class CancelPathPacket extends Packet {

    public CancelPathPacket() {
        id = PacketConstants.CancelPathPacket;
    }

    @Override
    public CancelPathPacket read(NetworkMessage m) throws IOException {
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
    }
}
