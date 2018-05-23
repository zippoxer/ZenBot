/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import java.util.Objects;
import structures.Loc;

/**
 *
 * @author Radek
 */
public class Waypoint {

    public static final String WALK = "W";
    public static final String LURE = "L";
    public static final String USE = "U";
    public static final String ACTION = "A";
    public Loc location;
    public String type;

    public Waypoint() {}
    
    public Waypoint(String type,  Loc location) {
        this.type = type;
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Waypoint other = (Waypoint) obj;
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.location);
        hash = 53 * hash + Objects.hashCode(this.type);
        return hash;
    }
}
