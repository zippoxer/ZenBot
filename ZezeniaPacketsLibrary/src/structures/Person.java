/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Myzreal
 */
public class Person extends Structure {

    public String name;
    public Boolean online;
    public short mark;

    public Person() {}
    
    public Person(String name, Boolean online, short mark) {
        this.name = name;
        this.online = online;
        this.mark = mark;
    }

    @Override
    public Person read(NetworkMessage m) throws IOException {
        name = m.readString();
        online = m.readBool();
        mark = m.readByte();
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        m.writeString(name);
        m.writeBool(online);
        m.writeByte(mark);
    }
}
