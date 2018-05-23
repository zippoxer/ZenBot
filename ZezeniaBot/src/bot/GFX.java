/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import com.google.common.io.LittleEndianDataInputStream;
import gui.LoginFrame;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import settings.Constants;
import settings.UserSettings;

/**
 *
 * @author Moshe Revah
 */
public class GFX {
    
    public static void load(Logger logger) {
        File outputDir = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/sprites");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        if (outputDir.listFiles().length < 4942) {
            File gfx = GFX.locate();
            logger.log(Level.INFO, "gfx is found in {0}", gfx);
            logger.log(Level.INFO, "extracting to {0}...", outputDir);
            try {
                GFX.extract(gfx, outputDir);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
            }
            logger.info("done");
        } else {
            logger.info("Sprites are already extracted, skipping.");
        }
    }

    public static File locate() {
        File[] roots = File.listRoots();
        for (File root : roots) {
            File gfx = new File(root.getAbsolutePath(), "Program Files\\Zezenia Online\\zezenia.gfx");
            if (gfx.exists()) {
                return gfx;
            }
        }
        if (LoginFrame.getInstance().fileChooser.showOpenDialog(LoginFrame.getInstance()) == JFileChooser.APPROVE_OPTION) {
            File file = LoginFrame.getInstance().fileChooser.getSelectedFile();
            return file;
        }
        
        LoginFrame.getInstance().okayDialog.setVisible(true);
        LoginFrame.getInstance().setVisible(false);
        
        return null;
    }

    /**
     * Looks for the last extracted sprite in outputDir and begins an extraction from it.
     */
    public static void extract(File input, File outputDir)  throws FileNotFoundException, IOException {
        int start = 0;
        File[] files = outputDir.listFiles();
        for(File file : files) {
            int n = Integer.parseInt(file.getName().substring(0, file.getName().lastIndexOf('.')));
            if(n > start) {
                start = n;
            }
        }
        if(start > 0) {
            // Re-extract last sprite since it might not be properly extracted due to a termination.
            start--;
        }
        extract(input, outputDir, start);
    }
    
    public static void extract(File input, File outputDir, int start) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(input);
        LittleEndianDataInputStream dis = new LittleEndianDataInputStream(fis);
        int version = dis.readInt();
        int nsprites = dis.readInt();
        for (int i = 1; i <= nsprites; i++) {
            int width = dis.readUnsignedShort();
            int height = dis.readUnsignedShort();
            if (i < start) {
                byte[] b = new byte[width * height * 4];
                dis.read(b);
                continue;
            }
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int y = height - 1; y >= 0; y--) {
                for (int x = 0; x < width; x++) {
                    int b = dis.readUnsignedByte();
                    int g = dis.readUnsignedByte();
                    int r = dis.readUnsignedByte();
                    int a = dis.readUnsignedByte();
                    Color c = new Color(r, g, b, a);
                    bi.setRGB(x, y, c.getRGB());
                }
            }
            ImageIO.write(bi, "png", new File(outputDir, i + ".png"));
        }
    }
}
