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
public class AnimatedTextPacket extends Packet {

    public int x, y;
    public String text;
    public short color;

    public AnimatedTextPacket() {
        id = PacketConstants.AnimatedTextPacket;
    }

    public AnimatedTextPacket(int x, int y, String text, short color) {
        this();
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
    }

    @Override
    public AnimatedTextPacket read(NetworkMessage m) throws IOException {
        x = m.readUshort();
        y = m.readUshort();
        text = m.readString();
        color = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(x);
        m.writeUshort(y);
        m.writeString(text);
        m.writeByte(color);
    }
}
