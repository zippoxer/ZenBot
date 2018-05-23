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
public class AfterWalkPacket extends Packet {

    public MapDescription map;
    
    public AfterWalkPacket() {
        id = PacketConstants.AfterWalkPacket;
    }

    public AfterWalkPacket(MapDescription map) {
        this();
        this.map = map;
    }

    @Override
    public AfterWalkPacket read(NetworkMessage m) throws IOException {
        map = new MapDescription().read(m);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
    }
}
