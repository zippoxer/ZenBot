/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import structures.Status;
import network.NetworkMessage;

/**
 *
 * @author Moshe Revah
 */
public class StatusUpdatePacket extends Packet {

    public Status status;
    public short[] unanalyzed;

    public StatusUpdatePacket() {
        id = PacketConstants.StatusUpdatePacket;
    }

    public StatusUpdatePacket(Status status, short[] unanalyzed) {
        this();
        this.status = status;
        this.unanalyzed = unanalyzed;
    }

    @Override
    public StatusUpdatePacket read(NetworkMessage m) throws IOException {
        status = new Status().read(m);
        unanalyzed = m.read(8);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        status.write(m);
        m.write(unanalyzed);
    }
}
