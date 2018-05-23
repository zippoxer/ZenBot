/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package packets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import network.NetworkMessage;
import structures.Person;

/**
 *
 * @author Moshe Revah
 */
public class PeopleUpdatePacket extends Packet {

    public List<Person> people = new ArrayList<>();
    public short unanalayzed = 1;
    
    public PeopleUpdatePacket() {
        id = PacketConstants.PeopleUpdatePacket;
    }
    
    public PeopleUpdatePacket(List<Person> people, short unanalayzed) {
        this();
        this.people = people;
        this.unanalayzed = unanalayzed;
    }

    @Override
    public PeopleUpdatePacket read(NetworkMessage m) throws IOException {
        int n = m.readByte();
        unanalayzed = m.readByte();
        for(int i = 0; i < n; i++) {
            people.add(new Person().read(m));
        }
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeByte((short) people.size());
        m.writeByte(unanalayzed);
        for(Person p : people) {
            p.write(m);
        }
    }
}
