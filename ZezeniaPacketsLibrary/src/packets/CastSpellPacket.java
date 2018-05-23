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
public class CastSpellPacket extends Packet {

    public long spellID;

    public CastSpellPacket() {
        id = PacketConstants.CastSpellPacket;
    }

    public CastSpellPacket(long spellID) {
        this();
        this.spellID = spellID;
    }

    @Override
    public CastSpellPacket read(NetworkMessage m) throws IOException {
        spellID = m.readUint();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeUint(spellID);
    }
}
