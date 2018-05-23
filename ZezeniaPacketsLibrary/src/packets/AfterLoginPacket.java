package packets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import structures.Item;
import structures.MapDescription;

import network.NetworkMessage;

public class AfterLoginPacket extends Packet {

    public MapDescription map;
    public List<Item> equipment = new ArrayList<>();
    public short[] unanalyzed = new short[3];
    
    public AfterLoginPacket() {
        id = PacketConstants.AfterLoginPacket;
    }
    
    public AfterLoginPacket(MapDescription map, List<Item> equipment, short[] unanalyzed) {
        this();
        this.map = map;
        this.equipment = equipment;
        this.unanalyzed = unanalyzed;
    }

    @Override
    public AfterLoginPacket read(NetworkMessage m) throws IOException {
        m.read(7);
        map = new MapDescription().read(m);
        
        int n = m.readByte();
        for(int i = 0; i < n; i++) {
            equipment.add(new Item().read(m));
        }
        unanalyzed = m.read(3);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
