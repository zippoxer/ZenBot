package packets;

import java.io.IOException;

import network.NetworkMessage;

public class ErrorPacket extends Packet {

    public String message;

    public ErrorPacket() {
        id = PacketConstants.ErrorPacket;
    }

    public ErrorPacket(String message) {
        this();
        this.message = message;
    }

    public ErrorPacket read(NetworkMessage m) throws IOException {
        message = m.readString();
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
        m.writeString(message);
    }
}
