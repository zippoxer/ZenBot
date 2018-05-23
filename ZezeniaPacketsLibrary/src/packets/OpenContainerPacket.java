/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import network.NetworkMessage;
import structures.Item;

/**
 *
 * @author Myzreal
 */
public class OpenContainerPacket extends Packet {
    
    public short containerID;
    public String name;
    public short unanalyzed;
    public short volume;
    public List<Item> items = new ArrayList<>();
    
    public OpenContainerPacket() {
        id = PacketConstants.OpenContainerPacket;
    }
    
    public OpenContainerPacket(short cnr, String name, short una, short volume, List<Item> items) {
        this();
        this.containerID = cnr;
        this.name = name;
        this.unanalyzed = una;
        this.volume = volume;
        this.items = items;
    }

    @Override
    public OpenContainerPacket read(NetworkMessage m) throws IOException {
        containerID = m.readByte();
        name = m.readString();
        unanalyzed = m.readByte();
        volume = m.readByte();
        short n = m.readByte();
        for (int i = 0; i < n; i++) {
            items.add(new Item().read(m));
        }
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte(containerID);
        m.writeString(name);
        m.writeByte(unanalyzed);
        m.writeByte(volume);
        m.writeByte((short) items.size());
        for (Item i : items) {
            i.write(m);
        }
    }
    
}
