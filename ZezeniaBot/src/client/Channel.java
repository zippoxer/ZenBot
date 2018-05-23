package client;

import java.io.IOException;
import java.util.Objects;
import packets.RequestCloseChannelPacket;
import packets.RequestOpenChannelPacket;

public class Channel {

    public short type;
    public String name;

    public Channel(short type, String name) {
        this.type = type;
        this.name = name;
    }

    public void open() throws IOException {
        new RequestOpenChannelPacket(type, name).send(Client.getInstance().getOs());
    }

    public void close() throws IOException {
        new RequestCloseChannelPacket(type, name).send(Client.getInstance().getOs());
    }

    // Performs a check whether the given channel is open or not.
    // Requires passing the array of channels as it may differ between bot and
    // server.
    public boolean isOpen() {
        return Client.getInstance().getOpenChannels().contains(this);
    }

    //Performs a search for a channel of a given name in the specified list
    //Returns a Channel object if found, null if not.
    public static Channel byName(String name) {
        for (Channel ch : Client.getInstance().getOpenChannels()) {
            if (ch.name.equals(name)) {
                return ch;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Channel other = (Channel) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.type;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
