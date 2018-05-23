package structures;

import java.io.IOException;
import network.NetworkMessage;

public class Character extends Structure {

    public String name;
    public String worldName, worldIP;
    public short[] outfit;
    public String vocation;
    public short level;

    public Character() {}
    
    public Character(String name, String worldName, String worldIP, short[] outfit, String vocation, short level) {
        this.name = name;
        this.worldName = worldName;
        this.worldIP = worldIP;
        this.outfit = outfit;
        this.vocation = vocation;
        this.level = level;
    }

    @Override
    public Character read(NetworkMessage m) throws IOException {
        name = m.readString();
        worldName = m.readString();
        worldIP = m.readString();
        outfit = m.read(14);
        vocation = m.readString();
        level = m.readShort();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeString(name);
        m.writeString(worldName);
        m.writeString(worldIP);
        m.write(outfit);
        m.writeString(vocation);
        m.writeShort(level);
    }
}
