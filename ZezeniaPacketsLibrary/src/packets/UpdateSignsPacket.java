/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package packets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import network.NetworkMessage;

/**
 *
 * @author Moshe Revah
 */
public class UpdateSignsPacket extends Packet {

    public List<Long> sprites = new ArrayList<>();
    
    public UpdateSignsPacket() {
        id = PacketConstants.UpdateSignsPacket;
    }

    @Override
    public UpdateSignsPacket read(NetworkMessage m) throws IOException {
        short n = m.readByte();
        for(int i = 0; i < n; i++) {
            sprites.add(m.readUint());
        }
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte((short) sprites.size());
        for(long sprite : sprites) {
            m.writeUint(sprite);
        }
    }
}
