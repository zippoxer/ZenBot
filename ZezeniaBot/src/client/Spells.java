/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Myzreal
 */
public class Spells {
    
    private static final java.util.Map<Integer, String> SPELLS = new HashMap<>();
    
    public Spells() {
        SPELLS.put(1418, "Weak Heal");
        SPELLS.put(2395, "Recuperation");
        SPELLS.put(2384, "Heaven's Aid");
    }
    
    public static String getName(int id) {
        return SPELLS.get(id);
    }
    
    public static int getId(String name) {
        for (Entry<Integer, String> e : SPELLS.entrySet()) {
            if (name.toLowerCase().equals(e.getValue().toLowerCase()))
                return e.getKey();
        }
        return -1;
    }
    
    public static java.util.Map<Integer, String> getSpells() {
        return SPELLS;
    }
    
    public static Collection<String> getSpellNames() {
        return SPELLS.values();
    }
    
    public static Set<Integer> getSpellIDs() {
        return SPELLS.keySet();
    }
    
}
