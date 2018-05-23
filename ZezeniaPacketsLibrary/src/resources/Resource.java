/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import com.google.common.primitives.Ints;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Loads resource files and puts them into arrays. Resource files: pathblockers,
 * walkables, stackables. Each line is either a separate int value or a range of
 * values.
 *
 * @author Myzreal
 */
public class Resource {

    private static String dir = "resources/";
    private static String[] resourceFiles = {"walkables.txt", "pathblockers.txt",
        "stackables.txt"};
    private static String currentFile;
    private static int[] walkables;
    private static int[] pathblockers;
    private static int[] stackables;

    public static void loadResources() {
        for (String file : resourceFiles) {
            currentFile = file;
            InputStream in = ClassLoader.getSystemResourceAsStream(dir + file);
            parse(in);
        }
    }

    public static int[] getWalkables() {
        return walkables;
    }

    public static int[] getStackables() {
        return stackables;
    }

    public static int[] getPathblockers() {
        return pathblockers;
    }

    //=============== PRIVATE ===============
    private static void parse(InputStream input) {
        List<Integer> list = new ArrayList<>();
        Scanner scan = new Scanner(input);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (!line.startsWith("//")) { //ignore comments
                if (line.contains("-")) {
                    parseRange(line, list);
                } else {
                    parseValue(line, list);
                }
            }
        }
        if (currentFile.equals("walkables.txt")) {
            walkables = Ints.toArray(list);
        } else if (currentFile.equals("stackables.txt")) {
            stackables = Ints.toArray(list);
        } else if (currentFile.equals("pathblockers.txt")) {
            pathblockers = Ints.toArray(list);
        }
    }

    private static void parseRange(String line, List<Integer> list) {
        String[] range = line.split("-");
        int max = Integer.parseInt(range[1]);
        int min = Integer.parseInt(range[0]);

        for (int i = min; i <= max; i++) {
            list.add(i);
        }
    }

    private static void parseValue(String line, List<Integer> list) {
        list.add(Integer.parseInt(line));
    }
}
