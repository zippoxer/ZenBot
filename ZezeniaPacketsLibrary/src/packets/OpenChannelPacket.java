package packets;

import java.io.IOException;

import network.NetworkMessage;

public class OpenChannelPacket extends Packet {

    public short type;
    public String name;

    public OpenChannelPacket() {
        id = PacketConstants.OpenChannelPacket;
    }

    public OpenChannelPacket(byte type, String name) {
        this();
        this.type = type;
        this.name = name;
    }

    public OpenChannelPacket read(NetworkMessage p) throws IOException {
        type = p.readByte();
        name = p.readString();
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
        m.writeByte(type);
        m.writeString(name);
    }
}
