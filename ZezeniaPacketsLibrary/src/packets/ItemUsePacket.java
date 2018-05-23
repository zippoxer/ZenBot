/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import network.NetworkMessage;
import structures.Loc;

/**
 *
 * @author Myzreal
 */
public class ItemUsePacket extends Packet {

    public Loc location;
    public short requestedContainerID;
    public long itemID;
    
    public ItemUsePacket() {
        id = PacketConstants.ItemUsePacket;
    }
    
    public ItemUsePacket(Loc location, short requestedContainerID, int itemID) {
        this();
        this.location = location;
        this.requestedContainerID = requestedContainerID;
        this.itemID = itemID;
    }

    @Override
    public ItemUsePacket read(NetworkMessage m) throws IOException {
        location = new Loc().read(m);
        requestedContainerID = m.readByte();
        itemID = m.readUint();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        location.write(m);
        m.writeByte(requestedContainerID);
        m.writeUint(itemID);
    }
    
}
