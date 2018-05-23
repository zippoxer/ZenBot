/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package packets;

import java.io.IOException;
import network.NetworkMessage;
import packets.Packet;
import packets.PacketConstants;
import structures.Loc;

/**
 *
 * @author Moshe Revah
 */
public class LookAtPacket extends Packet {

    public Loc location;
    
    public LookAtPacket() {
        id = PacketConstants.LookAtPacket;
    }

    public LookAtPacket(Loc location) {
        this();
        this.location = location;
    }

    @Override
    public LookAtPacket read(NetworkMessage m) throws IOException {
        location = new Loc().read(m);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        location.write(m);
    }
}
