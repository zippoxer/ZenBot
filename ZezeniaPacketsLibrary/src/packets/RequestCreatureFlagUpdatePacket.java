/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Myzreal
 */
public class RequestCreatureFlagUpdatePacket extends Packet {
    
    public int attackedID, unknown, followedID, unknown2;
    
    public RequestCreatureFlagUpdatePacket() {
        id = PacketConstants.RequestCreatureFlagUpdatePacket;
    }
    
    public RequestCreatureFlagUpdatePacket(int aid, int u, int fid, int u2) {
        this();
        this.attackedID = aid;
        this.unknown = u;
        this.followedID = fid;
        this.unknown2 = u2;
    }

    @Override
    public RequestCreatureFlagUpdatePacket read(NetworkMessage m) throws IOException {
        attackedID = m.readUshort();
        unknown = m.readUshort();
        followedID = m.readUshort();
        unknown2 = m.readUshort();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUshort(attackedID);
        m.writeUshort(unknown);
        m.writeUshort(followedID);
        m.writeUshort(unknown2);
    }
    
}
