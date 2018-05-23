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
public class ItemMovePacket extends Packet {

    public Loc from, to;
    public short count;
    
    public ItemMovePacket() {
        id = PacketConstants.ItemMovePacket;
    }

    public ItemMovePacket(Loc from, Loc to, short count) {
        this();
        this.from = from;
        this.to = to;
        this.count = count;
    }

    @Override
    public ItemMovePacket read(NetworkMessage m) throws IOException {
        from = new Loc().read(m);
        to = new Loc().read(m);
        count = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        from.write(m);
        to.write(m);
        m.writeByte(count);
    }
}
