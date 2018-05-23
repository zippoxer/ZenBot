/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

/**
 *
 * @author Radek
 */
import java.io.File;
import javax.swing.filechooser.*;
 
/* ImageFilter.java is used by FileChooserDemo2.java. */
public class GfxFilter extends FileFilter {
    
    public GfxFilter() {
        
    }
 
    //Accept .gfx
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension = f.getName();
        
        if (extension.equals("zezenia.gfx"))
            return true;
 
        return false;
    }
 
    //The description of this filter
    public String getDescription() {
        return "Zezenia.gfx file.";
    }
}
