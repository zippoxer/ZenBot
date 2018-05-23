package client;

/**
 * Occurs in Client.getCharList method when the server sends an ErrorPacket.
 *
 * @author Moshe Revah
 *
 */
public class ServerErrorException extends Exception {

    public ServerErrorException(String message) {
        super(message);
    }
}
