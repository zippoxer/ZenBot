/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import network.NetworkMessage;
import structures.Loc;
import structures.MapDescription;
import structures.TileDescription;

/**
 *
 * @author Moshe Revah
 */
public class TileUpdatePacket extends Packet {
    
    public MapDescription.Tile tile;
    
    public TileUpdatePacket() {
        id = PacketConstants.TileUpdatePacket;
    }
    
    public TileUpdatePacket(MapDescription.Tile tile) {
        this();
        this.tile = tile;
    }

    @Override
    public TileUpdatePacket read(NetworkMessage m) throws IOException {
        Loc l = new Loc().read(m);
        TileDescription td = new TileDescription().read(m);
        tile = new MapDescription.Tile(l, td);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
