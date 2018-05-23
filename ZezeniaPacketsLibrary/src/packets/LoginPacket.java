package packets;

import java.io.IOException;

import network.NetworkMessage;

public class LoginPacket extends Packet {

    public int version;
    public String name, password;
    public long gfxDate;

    public LoginPacket() {
        id = PacketConstants.LoginPacket;
    }

    public LoginPacket(short version, String name, String password, int gfxDate) {
        this();
        this.version = version;
        this.name = name;
        this.password = password;
        this.gfxDate = gfxDate;
    }

    public LoginPacket read(NetworkMessage m) throws IOException {
        version = m.readUshort();
        name = m.readString();
        password = m.readString();
        gfxDate = m.readUint();
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(version);
        m.writeString(name);
        m.writeString(password);
        m.writeUint(gfxDate);
    }
}