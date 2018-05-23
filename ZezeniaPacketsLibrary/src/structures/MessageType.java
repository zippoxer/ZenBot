package structures;

public class MessageType {

    /**
     * A message that was sent by creature (a player or an NPC).
     */
    public static final byte CREATURE = 1;
    /**
     * A response to an action the client took, indicating failure.
     */
    public static final byte CANCEL = 3;
    /**
     * An info message.
     */
    public static final byte INFO = 6;
}
