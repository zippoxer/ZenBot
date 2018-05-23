package packets;

import java.io.IOException;
import network.NetworkMessage;
import packets.Packet;
import packets.PacketConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Moshe Revah
 */
public class EffectPacket extends Packet {

    public short unanalyzed;
    public int x, y, effectID;
    
    public EffectPacket() {
        id = PacketConstants.EffectPacket;
    }

    public EffectPacket(short unanalyzed, int x, int y, int effectID) {
        this();
        this.unanalyzed = unanalyzed;
        this.x = x;
        this.y = y;
        this.effectID = effectID;
    }

    @Override
    public EffectPacket read(NetworkMessage m) throws IOException {
        unanalyzed = m.readByte();
        x = m.readUshort();
        y = m.readUshort();
        effectID = m.readUshort();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte(unanalyzed);
        m.writeUshort(x);
        m.writeUshort(y);
        m.writeUshort(effectID);
    }
}
