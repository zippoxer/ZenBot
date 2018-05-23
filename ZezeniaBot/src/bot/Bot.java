package bot;

import client.*;
import client.networking.PacketReceiver;
import gui.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import resources.Resource;
import settings.AppSettings;
import settings.Constants;
import structures.ChannelType;
import structures.Status;

/**
 * The main class for the Bot application.
 */
public class Bot implements ClientEventListener {

    private static final Logger logger = Logger.getLogger(PacketReceiver.class.getName());
    private static Bot _instance;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        new Bot();
    }

    public Bot() {
        _instance = this;

        Resource.loadResources();
        initSubclasses();
        init();

        // AUTO LOGIN, FOR TESTING!
        //autologin(...);
    }
    
    private void autologin(String name, String pass, String charName, String serverIP) {
        logger.info("automatically logging in...");
        try {
            Client.getInstance().login(name, pass, new structures.Character(charName, null, serverIP, null, null, (short) 0));
        } catch (Exception ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Bot getInstance() {
        return _instance;
    }

    private void init() {
        try {
            Formatter formatter = new Formatter() {

                @Override
                public String format(LogRecord record) {
                    DateFormat f = new SimpleDateFormat("HH:mm:ss.SS");
                    String s = String.format("%s %s://%s/%s >> %s\n", f.format(new Date()),
                            record.getLevel().getName(), record.getSourceClassName(),
                            record.getSourceMethodName(), formatMessage(record));
                    if (record.getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        record.getThrown().printStackTrace(new PrintWriter(sw));
                        s += sw.toString() + "\n";
                    }
                    return s;
                }
            };

            // apply formatter on standard output
            Logger.getLogger("").getHandlers()[0].setFormatter(formatter);

            // add handler to a log file and apply formatter on it
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH.mm ss.SS");
            File logFile = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/log/" + dateFormat.format(new Date()));
            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }
            Handler handler = new FileHandler(logFile.getAbsolutePath(), false);
            handler.setFormatter(formatter);
            Logger.getLogger("").addHandler(handler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }

        GFX.load(logger);
    }

    @Override
    public void onException(Exception e) {
    }

    @Override
    public void onLoginResponse(String error) {
        if (error != null) {
            logger.log(Level.WARNING, "could not login: {0}", error);
            return;
        }

        logger.info("logged in to the world!");
        new MainFrame();
        LoginFrame.getInstance().dispose();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(new Random().nextInt(1000) + 400);
                    Client.getInstance().getEquipment().get(Slots.CONTAINER).use();
                } catch (InterruptedException | IOException ex) {
                    Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    /**
     * Inits once all the clases which have the .getInstance() method.
     */
    private void initSubclasses() {
        new AppSettings();
        try  {
            AppSettings.getInstance().load(new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/application").getAbsolutePath());
        } catch(IOException ex) {
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                AppSettings.getInstance().alarmVolume = CavebotFrame.getInstance().getAlarmVolume();
                AppSettings.getInstance().minimapZoomLevel = MainFrame.getInstance().getMinimapZoomLevel();
                try {
                    AppSettings.getInstance().save(new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/application").getAbsolutePath());
                } catch (IOException ex) {
                }
                try {
                    Client.getInstance().logout();
                } catch (Exception ex) {
                    Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));
        
        new Client(this);
        new LoginFrame();
        new Spells();
    }

    @Override
    public void onMessage(Channel ch, String sender, String message) {
        if (ChatPanel.getInstance() != null) {
            ChatPanel.getInstance().addMessage(ch, sender, message);
        }

        if (ch.type == ChannelType.PUBLIC) {
            if (ch.name.equals("Main") && !sender.isEmpty()) {
                if (CavebotFrame.getInstance() != null) {
                    if (!sender.equals(Map.getInstance().getPlayer().name)) {
                        CavebotFrame.getInstance().startAlarm(Alarmer.MAIN_CHAT);
                    }
                }
            }
        } else {
            if (CavebotFrame.getInstance() != null) {
                CavebotFrame.getInstance().startAlarm(Alarmer.PRIV_CHAT);
            }
        }
    }

    @Override
    public void onChannelOpen(Channel ch) {
        if (ChatPanel.getInstance() != null) {
            ChatPanel.getInstance().addChannel(ch);
        }
    }

    @Override
    public void onLogout() {
        //System.exit(0);
        if (HealingFrame.getInstance() != null) {
            HealingFrame.getInstance().dispose();
        }
        if (CreaturesFrame.getInstance() != null) {
            CreaturesFrame.getInstance().dispose();
        }
        if (CavebotFrame.getInstance() != null) {
            CavebotFrame.getInstance().dispose();
        }
        new LoginFrame();
        if (MainFrame.getInstance() != null) {
            MainFrame.getInstance().dispose();
        }
    }

    @Override
    public void onStatusUpdate(Status oldStatus, Status newStatus) {
        if (PlayerStatusPanel.getInstance() != null) {
            PlayerStatusPanel.getInstance().update(newStatus);
        }
        if (CavebotFrame.getInstance().calculator != null) {
            CavebotFrame.getInstance().calculator.addExp((int) (newStatus.exp - oldStatus.exp));
        }
    }

    @Override
    public void onEntityAppear(Entity e) {
        if (CreaturesFrame.getInstance() != null) {
            CreaturesFrame.getInstance().update(e);
        }
    }

    @Override
    public void onEntityDisappear(Entity e) {
        if (CreaturesFrame.getInstance() != null) {
            CreaturesFrame.getInstance().remove(e);
        }
    }

    @Override
    public void onEntityWalk(Entity e, short direction) {
        if (CreaturesFrame.getInstance() != null) {
            CreaturesFrame.getInstance().update(e);
        }
    }

    @Override
    public void onCreatureFlagUpdate(int attackedID, int followedID) {
        if (CreaturesFrame.getInstance() != null) {
            if (attackedID != Map.getInstance().getPlayer().id) {
                Entity e = Map.getInstance().getEntityByID(attackedID);
                if (e != null) {
                    CreaturesFrame.getInstance().update(e);
                }
            }
            if (followedID != Map.getInstance().getPlayer().id) {
                Entity e = Map.getInstance().getEntityByID(followedID);
                if (e != null) {
                    CreaturesFrame.getInstance().update(e);
                }
            }
        }
    }

    @Override
    public void onCancel(String message) {
        ChatPanel.getInstance().addCancelMessage(message);
    }

    @Override
    public void onContainerOpen(Container container) {
        if (InventoryPanel.getInstance() != null) {
            InventoryPanel.getInstance().openContainer(container);
        }
    }

    @Override
    public void onContainerClose(Container container) {
        if (InventoryPanel.getInstance() != null) {
            InventoryPanel.getInstance().closeContainer(container);
        }
    }

    @Override
    public void onContainerUpdate(Container container) {
        if (InventoryPanel.getInstance() != null) {
            InventoryPanel.getInstance().updateContainer(container);
        }
    }
}
