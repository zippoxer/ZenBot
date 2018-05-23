/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package packets;

import java.io.IOException;
import structures.Creature;
import structures.Loc;
import network.NetworkMessage;

/**
 *
 * @author Myzreal
 */
public class CreatureAppearPacket extends Packet {
    
    public Loc location;
    public Creature creature;
    
    public CreatureAppearPacket() {
        id = PacketConstants.CreatureAppearPacket;
    }
    
    public CreatureAppearPacket(Loc location, Creature creature) {
        this();
        this.location = location;
        this.creature = creature;
    }

    @Override
    public CreatureAppearPacket read(NetworkMessage m) throws IOException {
        location = new Loc().read(m);
        creature = new Creature().read(m);
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        location.write(m);
        creature.write(m);
    }
    
}
