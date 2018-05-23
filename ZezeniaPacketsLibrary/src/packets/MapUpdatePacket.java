/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import network.NetworkMessage;
import structures.MapDescription;

/**
 *
 * @author Radek
 */
public class MapUpdatePacket extends Packet {

    public MapDescription map;
    
    public MapUpdatePacket() {
        id = PacketConstants.AfterWalkPacket;
    }

    public MapUpdatePacket(MapDescription map) {
        this();
        this.map = map;
    }

    @Override
    public MapUpdatePacket read(NetworkMessage m) throws IOException {
        map = new MapDescription().read(m);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
    }
}
