/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import network.NetworkMessage;

/**
 *
 * @author Moshe Revah
 */
public class TileDescription extends Structure {

    public List<Item> items = new ArrayList<>();
    public List<Creature> creatures = new ArrayList<>();
    public int skip;

    @Override
    public TileDescription read(NetworkMessage m) throws IOException {
        while (true) {
            int v = m.readUshort();
            if (v >= 0xFF00) {
                skip = v & 0xFF;
                break;
            }
            if (v == 0xA) {
                Creature c = new Creature().read(m);
                creatures.add(c);
            } else {
                Item i = new Item(v, (short) 1);
                if (Item.isStackable(v)) {
                    i.count = m.readByte();
                }
                items.add(i);
            }
        }
        return this;
    }

    @Override
    public void write(NetworkMessage m) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
