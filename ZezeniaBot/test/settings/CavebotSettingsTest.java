/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import bot.Waypoint;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import structures.Loc;

/**
 *
 * @author Moshe Revah
 */
public class CavebotSettingsTest {
    
    public CavebotSettingsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    public void testLoadWithLootAndNoTargets() throws Exception {
        System.out.println("load");
        String filename = getClass().getResource("testLoadWithLootAndNoTargets").toURI().getPath();
        CavebotSettings settings = CavebotSettings.load(filename);
        
        List<Waypoint> expectedWaypoints = new ArrayList<>();
        expectedWaypoints.add(new Waypoint("W", new Loc(100, 100, (short) 7)));
        expectedWaypoints.add(new Waypoint("L", new Loc(110, 100, (short) 7)));
        expectedWaypoints.add(new Waypoint("W", new Loc(120, 100, (short) 6)));
        assertEquals(expectedWaypoints, settings.waypoints);
        
        List<Integer> expectedLoot = new ArrayList<>();
        expectedLoot.add(395);
        assertEquals(expectedLoot, settings.loot);
        
        assertEquals(true, settings.targetAll);
        assertEquals(0, settings.targets.size());
        
        List<CavebotSettings.Alarm> expectedAlarms = new ArrayList<>();
        expectedAlarms.add(new CavebotSettings.Alarm(1, false, true, false));
        expectedAlarms.add(new CavebotSettings.Alarm(0, true, true, true));
        expectedAlarms.add(new CavebotSettings.Alarm(4, false, false, false));
        assertEquals(expectedAlarms, settings.alarms);
    }
    
    @Test
    public void testLoadWithTargetsAndNoLoot() throws Exception {
        String filename = getClass().getResource("testLoadWithTargetsAndNoLoot").toURI().getPath();
        CavebotSettings settings = CavebotSettings.load(filename);
        
        List<Waypoint> expectedWaypoints = new ArrayList<>();
        expectedWaypoints.add(new Waypoint("W", new Loc(100, 100, (short) 7)));
        expectedWaypoints.add(new Waypoint("L", new Loc(110, 100, (short) 7)));
        expectedWaypoints.add(new Waypoint("W", new Loc(120, 100, (short) 6)));
        assertEquals(expectedWaypoints, settings.waypoints);
        
        assertEquals(0, settings.loot.size());
        
        assertEquals(false, settings.targetAll);
        
        List<String> expectedTargets = new ArrayList<>();
        expectedTargets.add("Orknie");
        expectedTargets.add("Orc");
        assertEquals(expectedTargets, settings.targets);
    }

    /**
     * Test of save method, of class CavebotSettings.
     */
    @Test
    public void testSave() throws IOException {
        System.out.println("save");
        CavebotSettings settings = new CavebotSettings();
        settings.waypoints.add(new Waypoint("W", new Loc(100, 100, (short) 7)));
        settings.waypoints.add(new Waypoint("L", new Loc(110, 100, (short) 7)));
        settings.waypoints.add(new Waypoint("W", new Loc(120, 100, (short) 6)));
        settings.loot.add(new CavebotSettings.LootItem(395, "gold coin"));
        settings.targets.add("Orknie");
        settings.targets.add("Orc");
        settings.alarms.add(new CavebotSettings.Alarm(1, true, false, false));
        settings.alarms.add(new CavebotSettings.Alarm(3, false, true, false));
        settings.alarms.add(new CavebotSettings.Alarm(0, true, true, true));
        settings.save("testSave");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
