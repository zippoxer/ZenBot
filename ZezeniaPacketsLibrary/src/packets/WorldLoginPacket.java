package packets;

import java.io.IOException;

import network.NetworkMessage;

public class WorldLoginPacket extends Packet {

    public int version;
    public String name, password, charName;
    public long timestamp;

    public WorldLoginPacket() {
        id = PacketConstants.WorldLoginPacket;
    }

    public WorldLoginPacket(short version, String name, String password, String charName, int timeStamp) {
        this();
        this.version = version;
        this.name = name;
        this.password = password;
        this.charName = charName;
        this.timestamp = timeStamp;
    }

    public WorldLoginPacket read(NetworkMessage p) throws IOException {
        version = p.readUshort();
        name = p.readString();
        password = p.readString();
        charName = p.readString();
        timestamp = p.readInt();
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(version);
        m.writeString(name);
        m.writeString(password);
        m.writeString(charName);
        m.writeUint(timestamp);
    }
}
