/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import bot.Waypoint;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import structures.Loc;

/**
 *
 * @author Moshe Revah
 */
public class CavebotSettings {

    public static class LootItem {

        public int id;
        public String label;

        public LootItem() {
        }

        public LootItem(int id, String label) {
            this.id = id;
            this.label = label;
        }
    }

    public static class Alarm {

        public int type;
        public boolean sound, pause, exit;

        public Alarm() {
        }

        public Alarm(int type, boolean sound, boolean pause, boolean exit) {
            this.type = type;
            this.sound = sound;
            this.pause = pause;
            this.exit = exit;
        }
    }
    public List<Waypoint> waypoints = new ArrayList<>();
    public boolean lootExcept;
    public List<LootItem> loot = new ArrayList<>();
    public boolean targetAll;
    public List<String> targets = new ArrayList<>();
    public List<Alarm> alarms = new ArrayList<>();

    public static CavebotSettings load(String filename) throws FileNotFoundException, IOException {
        CavebotSettings settings = new CavebotSettings();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String section = null;
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (line.startsWith("#")) {
                    section = line.substring(1);
                    continue;
                }
                switch (section) {
                    case "waypoints":
                        Waypoint waypoint = new Waypoint();
                        waypoint.type = line.substring(0, 1);
                        String[] xyz = line.substring(2).split(" ");
                        int x = Integer.parseInt(xyz[0]);
                        int y = Integer.parseInt(xyz[1]);
                        short z = Short.parseShort(xyz[2]);
                        waypoint.location = new Loc(x, y, z);
                        settings.waypoints.add(waypoint);
                        break;
                    case "looting":
                        if (line.equals("except")) {
                            settings.lootExcept = true;
                        } else {
                            String[] idLabel = line.split(" ", 2);
                            int id = Integer.parseInt(idLabel[0]);
                            String label = "";
                            if (idLabel.length > 1) {
                                label = idLabel[1];
                            }
                            settings.loot.add(new LootItem(id, label));
                        }
                        break;
                    case "targeting":
                        if (line.equals("all")) {
                            settings.targetAll = true;
                        } else {
                            settings.targets.add(line);
                        }
                        break;
                    case "alarms":
                        Alarm alarm = new Alarm();
                        String[] typeFlags = line.split(" ");
                        alarm.type = Integer.parseInt(typeFlags[0]);
                        if (typeFlags.length > 1) {
                            for (char flag : typeFlags[1].toCharArray()) {
                                switch (flag) {
                                    case 'S':
                                        alarm.sound = true;
                                        break;
                                    case 'P':
                                        alarm.pause = true;
                                        break;
                                    case 'X':
                                        alarm.exit = true;
                                        break;
                                }
                            }
                        }
                        settings.alarms.add(alarm);
                }
            }
        }
        return settings;
    }

    public void save(String filename) throws IOException {
        try (PrintStream ps = new PrintStream(filename)) {
            if (waypoints.size() > 0) {
                ps.print("#waypoints");
                for (Waypoint waypoint : waypoints) {
                    ps.println();
                    ps.printf("%s %d %d %d", waypoint.type, waypoint.location.x, waypoint.location.y, waypoint.location.z);
                }
            }

            if (loot.size() > 0 || lootExcept) {
                ps.println();
                ps.println();
                ps.print("#looting");
                if(lootExcept) {
                    ps.println();
                    ps.print("except");
                }
                for (LootItem item : loot) {
                    ps.println();
                    ps.print(item.id);
                    if (!item.label.isEmpty()) {
                        ps.print(" " + item.label);
                    }
                }
            }

            if (targetAll || targets.size() > 0) {
                ps.println();
                ps.println();
                ps.print("#targeting");
                if (targetAll) {
                    ps.println();
                    ps.print("all");
                } else {
                    for (String target : targets) {
                        ps.println();
                        ps.print(target);
                    }
                }
            }

            if (alarms.size() > 0) {
                ps.println();
                ps.println();
                ps.print("#alarms");
                for (Alarm alarm : alarms) {
                    ps.println();
                    ps.print(alarm.type);
                    String flags = "";
                    if (alarm.sound) {
                        flags += 'S';
                    }
                    if (alarm.pause) {
                        flags += 'P';
                    }
                    if (alarm.exit) {
                        flags += 'X';
                    }
                    if (flags.length() > 0) {
                        ps.print(" " + flags);
                    }
                }
            }
        }
    }
}
