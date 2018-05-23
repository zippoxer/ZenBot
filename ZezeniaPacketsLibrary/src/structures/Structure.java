/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.io.IOException;
import network.NetworkMessage;

/**
 *
 * @author Moshe Revah
 */
public abstract class Structure {

    public abstract Structure read(NetworkMessage m) throws IOException;

    public abstract void write(NetworkMessage m) throws IOException;
}
