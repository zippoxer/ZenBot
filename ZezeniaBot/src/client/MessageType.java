package client;

public class MessageType {

    /**
     * A message that was sent by creature (a player or an NPC).
     */
    public final byte CREATURE = 1;
    /**
     * A response to an action the client took, indicating failure.
     */
    public final byte CANCEL = 3;
    /**
     * An info message.
     */
    public final byte INFO = 6;
}
