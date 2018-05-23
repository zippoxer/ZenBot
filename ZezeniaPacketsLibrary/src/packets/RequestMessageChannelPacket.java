package packets;

import java.io.IOException;

import network.NetworkMessage;

public class RequestMessageChannelPacket extends Packet {

    public String channelName, message;

    public RequestMessageChannelPacket() {
        id = PacketConstants.RequestMessageChannelPacket;
    }

    public RequestMessageChannelPacket(String channelName, String message) {
        this();
        this.channelName = channelName;
        this.message = message;
    }

    public RequestMessageChannelPacket read(NetworkMessage p) throws IOException {
        channelName = p.readString();
        message = p.readString();
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
        m.writeString(channelName);
        m.writeString(message);
    }
}
