/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import client.Client;
import client.Container;
import client.SpellCaster;
import client.Spells;
import java.util.Random;
import settings.UserSettings;
import structures.Status;

/**
 * The healer should: * Constantly monitor player's HP & MP. * If they cross the
 * values specified by user - react. * Reaction should consist of: * casting a
 * spell if specified * searching for a potion in open containers and using if
 * available
 *
 * @author Myzreal
 */
public class Healer extends Thread {

    private UserSettings.Healing settings;
    public static int[] HP_POTIONS = {1932, 1935, 1938, 1941, 1944};
    public static int[] MP_POTIONS = {1933, 1936, 1939, 1942, 1945};

    public Healer(UserSettings.Healing settings) {
        this.settings = settings;
    }

    @Override
    public void run() {
        SpellCaster.Spell spell = new SpellCaster.Spell(SpellCaster.HIGH,
                Spells.getId(settings.spellName));
        try {
            long lastPot = 0;
            long delay = new Random().nextInt(600) + 700;
            while (true) {
                Status status = Client.getInstance().getPlayerStatus();
                if (status.hp <= settings.spellHealth.toAbsolute(status.maxHp)
                        && status.mp >= settings.spellMana.toAbsolute(status.maxMp)) {
                    // condition is met, making sure the spell is going to be casted.
                    if (spell.state != SpellCaster.Spell.QUEUED) {
                        spell.cast();
                    }
                } else {
                    // condition isn't met, making sure the spell isn't going to be casted.
                    spell.cancel();
                }

                boolean used = false;
                if (status.hp <= settings.hpPotHealth.toAbsolute(status.maxHp)
                        && System.currentTimeMillis() - lastPot > delay) {
                    for (Container container : Client.getInstance().getOpenContainers()) {
                        for (client.Item item : container.items) {
                            if (item.id == HP_POTIONS[settings.hpPotType]) {
                                item.use();
                                used = true;
                                break;
                            }
                        }
                    }
                }
                if (!used
                        && status.mp <= settings.mpPotMana.toAbsolute(status.maxMp)
                        && System.currentTimeMillis() - lastPot > delay) {
                    for (Container container : Client.getInstance().getOpenContainers()) {
                        for (client.Item item : container.items) {
                            if (item.id == MP_POTIONS[settings.mpPotType]) {
                                item.use();
                                used = true;
                                break;
                            }
                        }
                    }
                }
                if (used) {
                    lastPot = System.currentTimeMillis();
                    delay = new Random().nextInt(600) + 700;
                }
                sleep(1);
            }
        } catch (Exception ex) {
            spell.cancel();
        }
    }

    public static int visibleHpots(int type) {
        int n = 0;
        for (Container container : Client.getInstance().getOpenContainers()) {
            for (client.Item item : container.items) {
                if (item.id == HP_POTIONS[type]) {
                    n += item.count;
                }
            }
        }
        return n;
    }

    public static int visibleMpots(int type) {
        int n = 0;
        for (Container container : Client.getInstance().getOpenContainers()) {
            for (client.Item item : container.items) {
                if (item.id == MP_POTIONS[type]) {
                    n += item.count;
                }
            }
        }
        return n;
    }
}
