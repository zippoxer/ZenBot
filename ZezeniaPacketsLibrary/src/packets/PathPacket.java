/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package packets;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Moshe Revah
 */
public class PathPacket extends Packet {

    public short[] directions;
    
    public PathPacket() {
        id = PacketConstants.PathPacket;
    }

    public PathPacket(short[] directions) {
        this();
        this.directions = directions;
    }

    @Override
    public PathPacket read(NetworkMessage m) throws IOException {
        short n = m.readByte();
        directions = m.read(n);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte((short) directions.length);
        m.write(directions);
    }
}
