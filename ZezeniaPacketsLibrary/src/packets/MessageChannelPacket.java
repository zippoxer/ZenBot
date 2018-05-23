package packets;

import java.io.IOException;

import network.NetworkMessage;

public class MessageChannelPacket extends Packet {

    public String sender, channelName, message;
    public short type;

    public MessageChannelPacket() {
        id = PacketConstants.MessageChannelPacket;
    }

    public MessageChannelPacket(String sender, String channel, byte type, String message) {
        this();
        this.sender = sender;
        this.channelName = channel;
        this.type = type;
        this.message = message;
    }

    public MessageChannelPacket read(NetworkMessage m) throws IOException {
        sender = m.readString();
        channelName = m.readString();
        type = m.readByte();
        message = m.readString();
        return this;
    }

    public void write(NetworkMessage m) throws IOException {
        m.writeString(sender);
        m.writeString(channelName);
        m.writeByte(type);
        m.writeString(message);
    }
}
