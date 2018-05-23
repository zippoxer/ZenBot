/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import packets.CastSpellPacket;

/**
 *
 * @author Moshe Revah
 */
public class SpellCaster extends Thread {

    private class SpellPriorityComparator implements Comparator<Spell> {

        @Override
        public int compare(Spell o1, Spell o2) {
            return Integer.compare(o1.priority, o2.priority);
        }
    }

    public static class Spell {

        public final static int NONE = 0;
        public final static int QUEUED = 1;
        public final static int CASTED = 2;
        public final static int CANCELED = 3;
        public int priority, id;
        public int state;

        public Spell(int priority, int id) {
            this.priority = priority;
            this.id = id;
            state = NONE;
        }

        public void cast() {
            SpellCaster.getInstance().cast(this);
        }

        public void cancel() {
            state = CANCELED;
        }
    }
    public static final int HIGH = 2;
    public static final int NORMAL = 1;
    public static final int LOW = 0;
    private static SpellCaster instance;

    public SpellCaster() {
        instance = this;
    }

    public static SpellCaster getInstance() {
        return instance;
    }
    private PriorityBlockingQueue<Spell> queue = new PriorityBlockingQueue<>(11, new SpellPriorityComparator());

    @Override
    public void run() {
        try {
            long lastCast = 0;
            while (true) {
                // randomize delay between casts, but keep it above 2 seconds
                int delay = new Random().nextInt(300) + 2050;
                while (System.currentTimeMillis() - lastCast < delay) {
                    sleep(1);
                }
                Spell spell = queue.take();
                if (spell.state == spell.QUEUED) {
                    new CastSpellPacket(spell.id).send(Client.getInstance().getOs());
                    spell.state = Spell.CASTED;
                    lastCast = System.currentTimeMillis();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SpellCaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cast(int spellID, int priority) {
        cast(new Spell(priority, spellID));
    }
    
    /**
     * Casts the given spell safely, by not spamming and waiting for previous
     * spells to be casted.
     *
     * @param spellID
     * @param priority
     */
    public void cast(Spell spell) {
        spell.state = Spell.QUEUED;
        queue.offer(spell);
    }

    /**
     * Calls cast with normal priority.
     *
     * @see SpellCaster#cast(int, int)
     * @param id
     */
    public void cast(int id) {
        cast(id, NORMAL);
    }
}
