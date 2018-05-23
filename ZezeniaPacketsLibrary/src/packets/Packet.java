package packets;

import java.io.IOException;
import java.io.OutputStream;
import network.NetworkMessage;

public abstract class Packet {

    public int id;

    public abstract Packet read(NetworkMessage m) throws IOException;

    public abstract void write(NetworkMessage m) throws IOException;

    public boolean readWithID(NetworkMessage m) throws IOException {
        if (m.readByte() != id) {
            return false;
        }
        read(m);
        return true;
    }

    public void writeWithID(NetworkMessage m) throws IOException {
        m.writeByte((byte) id);
        write(m);
    }

    /**
     * A shortcut method.
     */
    public void send(OutputStream os) throws IOException {
        NetworkMessage m = new NetworkMessage();
        writeWithID(m);
        m.send(os);
    }
}
