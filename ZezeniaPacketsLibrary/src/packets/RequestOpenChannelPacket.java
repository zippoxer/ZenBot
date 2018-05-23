package packets;

import java.io.IOException;

import network.NetworkMessage;

public class RequestOpenChannelPacket extends Packet {

    public short type;
    public String name;

    public RequestOpenChannelPacket() {
        id = PacketConstants.RequestOpenChannelPacket;
    }

    public RequestOpenChannelPacket(short type, String name) {
        this();
        this.type = type;
        this.name = name;
    }

    public RequestOpenChannelPacket read(NetworkMessage p) throws IOException {
        type = p.readByte();
        name = p.readString();
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
        m.writeByte(type);
        m.writeString(name);
    }
}
