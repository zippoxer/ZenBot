package packets;

import java.io.IOException;

import structures.Character;
import network.NetworkMessage;

public class CharListPacket extends Packet {

    public Character[] chars;

    public CharListPacket() {
        id = PacketConstants.CharListPacket;
    }

    public CharListPacket(Character[] chars) {
        this();
        this.chars = chars;
    }

    public CharListPacket read(NetworkMessage m) throws IOException {
        short n = m.readByte(); // how many characters to read
        chars = new Character[n];
        for (int i = 0; i < n; i++) {
            chars[i] = new Character().read(m);
        }
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
    }
}
