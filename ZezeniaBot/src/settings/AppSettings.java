/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Moshe Revah
 */
public class AppSettings {
    
    private static AppSettings instance;
    
    public int alarmVolume = 75;
    public int minimapZoomLevel = 3;
    
    public AppSettings() {
        instance = this;
    }
    
    public void load(String filename) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(filename));
        alarmVolume = Integer.parseInt(props.getProperty("alarm-volume"));
        minimapZoomLevel = Integer.parseInt(props.getProperty("minimap-zoom"));
    }
    
    public void save(String filename) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        props.setProperty("alarm-volume", String.valueOf(alarmVolume));
        props.setProperty("minimap-zoom", String.valueOf(minimapZoomLevel));
        props.store(new FileOutputStream(filename), "");
    }
    
    public static AppSettings getInstance() {
        return instance;
    }
}
