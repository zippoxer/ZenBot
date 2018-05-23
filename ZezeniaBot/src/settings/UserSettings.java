/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Myzreal
 */
public class UserSettings {

    public static class Percentable {

        public int value;
        public boolean isPercent;

        public Percentable() {
        }

        public Percentable(int val, boolean b) {
            value = val;
            isPercent = b;
        }

        @Override
        public String toString() {
            if (isPercent) {
                return String.valueOf(value) + "%";
            } else {
                return String.valueOf(value);
            }
        }

        public static Percentable parse(String input) {
            Percentable p = new Percentable();
            String temp = "";
            if (input.contains("%")) {
                p.isPercent = true;
                temp = input.substring(0, input.length() - 1);
            } else {
                p.isPercent = false;
                temp = input;
            }
            p.value = Integer.parseInt(temp);

            return p;
        }

        public int toAbsolute(int max) {
            if (!isPercent) {
                return value;
            }
            return (int) (value / 100.0 * max);
        }
    }

    public class Healing {

        public String spellName;
        public Percentable spellHealth;
        public Percentable spellMana;
        public Percentable hpPotHealth;
        public int hpPotType;
        public Percentable mpPotMana;
        public int mpPotType;

        public Healing() {
            spellName = "";
            spellHealth = new Percentable(0, false);
            spellMana = new Percentable(0, false);
            hpPotHealth = new Percentable(0, false);
            hpPotType = 0;
            mpPotMana = new Percentable(0, false);
            mpPotType = 0;
        }
    }

    public static class Window {

        public String name;
        public Point location;
    }
    public Healing healing = new Healing();
    public List<Window> windows = new ArrayList<>();
    public boolean eatFood = false;
    public boolean showWaypoints = false;

    public static UserSettings load(String filename) throws FileNotFoundException, IOException {
        UserSettings settings = new UserSettings();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String section = null;
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("#")) {
                    section = line.substring(1);
                    
                    // sections with no body
                    switch (section) {
                        case "eat food":
                            settings.eatFood = true;
                            break;
                        case "show waypoints":
                            settings.showWaypoints = true;
                            break;
                    }
                    continue;
                }
                switch (section) {
                    case "healing":
                        String[] temp = line.split(",");
                        settings.healing.spellName = temp[0];
                        settings.healing.spellHealth = Percentable.parse(temp[1]);
                        settings.healing.spellMana = Percentable.parse(temp[2]);
                        settings.healing.hpPotHealth = Percentable.parse(temp[3]);
                        settings.healing.hpPotType = Integer.parseInt(temp[4]);
                        settings.healing.mpPotMana = Percentable.parse(temp[5]);
                        settings.healing.mpPotType = Integer.parseInt(temp[6]);
                        break;
                    case "windows":
                        Window wnd = new Window();
                        String[] nameLocation = line.split(" ");
                        wnd.name = nameLocation[0];
                        String[] xy = nameLocation[1].split("/");
                        wnd.location = new Point(new Integer(xy[0]), new Integer(xy[1]));
                        settings.windows.add(wnd);
                        break;
                }
            }
        }
        return settings;
    }

    public void save(String filename) throws IOException {
        try (PrintStream ps = new PrintStream(filename)) {
            ps.println("#healing");
            ps.print(healing.spellName + "," + healing.spellHealth + ","
                    + healing.spellMana + "," + healing.hpPotHealth + ","
                    + healing.hpPotType + "," + healing.mpPotMana + ","
                    + healing.mpPotType);

            if (windows.size() > 0) {
                ps.println();
                ps.println();
                ps.print("#windows");
                for (Window wnd : windows) {
                    ps.println();
                    ps.printf("%s %d/%d", wnd.name, wnd.location.x, wnd.location.y);
                }
            }

            if (eatFood) {
                ps.println();
                ps.println();
                ps.print("#eat food");
            }

            if (showWaypoints) {
                ps.println();
                ps.println();
                ps.print("#show waypoints");
            }
        }
    }
}
