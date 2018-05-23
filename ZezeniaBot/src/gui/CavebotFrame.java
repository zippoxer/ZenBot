/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import bot.Alarmer;
import bot.Cavebot;
import bot.StatisticsCalculator;
import bot.Waypoint;
import client.Client;
import client.Entity;
import client.Map;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import settings.AppSettings;
import settings.CavebotSettings;
import settings.Constants;
import structures.Loc;

/**
 *
 * @author Myzreal
 */
public class CavebotFrame extends javax.swing.JDialog {

    private static CavebotFrame _instance;
    private Cavebot cavebot;
    private DefaultListModel waypointsModel = new DefaultListModel();
    private DefaultListModel targetsModel = new DefaultListModel();
    private DefaultListModel lootingModel = new DefaultListModel();
    private DefaultListModel settingsModel = new DefaultListModel();
    private CavebotSettings settings;
    public StatisticsCalculator calculator = null;
    private Alarmer alarmer;

    /**
     * Creates new form CavebotFrame
     */
    public CavebotFrame() {
        super(MainFrame.getInstance());
        
        _instance = this;
        
        initComponents();
        setLocationRelativeTo(null);
        init();
    }

    public static CavebotFrame getInstance() {
        return _instance;
    }

    private void init() {
        //Init settings instance.
        settings = new CavebotSettings();
        settings.targetAll = targetallCheck.isSelected();

        refreshSettingsList();

        //Put alarms in array.
        for (int i = 0; i < 5; i++) {
            settings.alarms.add(new CavebotSettings.Alarm(i, false, false, false));
        }
        
        // initialize the alarmer.
        alarmer = new Alarmer();

        // Hide calculator labels until cavebot starts.
        setCalculatorVisiblity(false);

        // An alarmer for player on screen.
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        boolean found = false;
                        for (Entity e : Map.getInstance().getEntities()) {
                            if (e.isPlayer() && e.id != Map.getInstance().getPlayer().id) {
                                found = true;
                                break;
                            }
                        }
                        if(found) {
                            CavebotFrame.getInstance().startAlarm(Alarmer.PLAYER_ONSCREEN);
                        } else {
                            CavebotFrame.getInstance().stopAlarm(Alarmer.PLAYER_ONSCREEN);
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                }
            }
        }).start();
        
        volumeSlider.setValue(AppSettings.getInstance().alarmVolume);
    }

    public CavebotSettings getCurrentSettings() {
        return settings;
    }

    private void refreshSettingsList() {
        settingsModel.clear();
        //Prepate settings list model.
        File file = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/cavebot");
        if (!file.exists()) {
            file.mkdirs();
        }
        for (String f : file.list()) {
            settingsModel.addElement(f);
        }
    }

    public void setCalculatorVisiblity(boolean visible) {
        Component[] components = new Component[]{
            jLabel10, jLabel11, jLabel12,
            xpperhourLabel, goldperhourLabel, timerunningLabel
        };
        for (Component c : components) {
            c.setVisible(visible);
        }
    }

    public void startAlarm(int type) {
        switch (type) {
            case Alarmer.PLAYER_ONSCREEN:
                playerOnScreenLabel.setForeground(Color.RED);
                if (pAppearedSoundChb.isSelected()) {
                    alarmer.play(Alarmer.PLAYER_ONSCREEN);
                }
                if (pAppearedPauseChb.isSelected() && cavebot != null) {
                    stop();
                }
                if (pAppearedExitChb.isSelected()) {
                    if (cavebot != null) {
                        stop();
                    }
                    try {
                        Client.getInstance().close();
                    } catch (IOException e) {
                    }
                }
                break;
            case Alarmer.PLAYER_ATTACKING:
                playerAttackingLabel.setForeground(Color.RED);
                if (playerAtkSoundChb.isSelected()) {
                    alarmer.play(Alarmer.PLAYER_ATTACKING);
                }
                if (playerAtkPauseChb.isSelected() && cavebot != null) {
                    stop();
                }
                if (playerAtkExitChb.isSelected()) {
                    if (cavebot != null) {
                        stop();
                    }
                    try {
                        Client.getInstance().close();
                    } catch (IOException e) {
                    }
                }
                break;
            case Alarmer.MAIN_CHAT:
                mainChatMsgLabel.setForeground(Color.RED);
                if (mainchatSoundChb.isSelected()) {
                    alarmer.play(Alarmer.MAIN_CHAT);
                }
                if (mainchatPauseChb.isSelected() && cavebot != null) {
                    stop();
                }
                if (mainchatExitChb.isSelected()) {
                    if (cavebot != null) {
                        stop();
                    }
                    try {
                        Client.getInstance().close();
                    } catch (IOException e) {
                    }
                }
                break;
            case Alarmer.TRAPPED:
                trappedLabel.setForeground(Color.RED);
                if (trappedSoundChb.isSelected()) {
                    alarmer.play(Alarmer.TRAPPED);
                }
                if (trappedPauseChb.isSelected() && cavebot != null) {
                    stop();
                }
                if (trappedExitChb.isSelected()) {
                    if (cavebot != null) {
                        stop();
                    }
                    try {
                        Client.getInstance().close();
                    } catch (IOException e) {
                    }
                }
                break;
            case Alarmer.PRIV_CHAT:
                privChatMsgLabel.setForeground(Color.RED);
                if (privchatSoundChb.isSelected()) {
                    alarmer.play(Alarmer.PRIV_CHAT);
                }
                if (privchatPauseChb.isSelected() && cavebot != null) {
                    stop();
                }
                if (privchatExitChb.isSelected()) {
                    if (cavebot != null) {
                        stop();
                    }
                    try {
                        Client.getInstance().close();
                    } catch (IOException e) {
                    }
                }
                break;
        }
    }

    public void stopAlarm(int type) {
        alarmer.stop(type);
        switch (type) {
            case Alarmer.PLAYER_ONSCREEN:
                playerOnScreenLabel.setForeground(Color.BLACK);
                break;
            case Alarmer.PLAYER_ATTACKING:
                playerAttackingLabel.setForeground(Color.BLACK);
                break;
            case Alarmer.MAIN_CHAT:
                mainChatMsgLabel.setForeground(Color.BLACK);
                break;
            case Alarmer.TRAPPED:
                trappedLabel.setForeground(Color.BLACK);
                break;
            case Alarmer.PRIV_CHAT:
                privChatMsgLabel.setForeground(Color.BLACK);
                break;
        }
    }

    private Loc getEmplacementLoc() {
        String dir = emplacementCombo.getSelectedItem().toString();
        Loc pLoc = Map.getInstance().getPlayer().location;
        switch (dir) {
            case "Center":
                return pLoc;
            case "North":
                return new Loc(pLoc.x, pLoc.y - 1, pLoc.z);
            case "East":
                return new Loc(pLoc.x + 1, pLoc.y, pLoc.z);
            case "South":
                return new Loc(pLoc.x, pLoc.y + 1, pLoc.z);
            case "West":
                return new Loc(pLoc.x - 1, pLoc.y, pLoc.z);
            case "North-West":
                return new Loc(pLoc.x - 1, pLoc.y - 1, pLoc.z);
            case "North-East":
                return new Loc(pLoc.x + 1, pLoc.y - 1, pLoc.z);
            case "South-East":
                return new Loc(pLoc.x + 1, pLoc.y + 1, pLoc.z);
            case "South-West":
                return new Loc(pLoc.x - 1, pLoc.y + 1, pLoc.z);
            default:
                return pLoc;
        }
    }

    private String waypointToString(int index, Waypoint waypoint) {
        return String.format("%s %03d: %d %d %d", waypoint.type, index, waypoint.location.x, waypoint.location.y, waypoint.location.z);
    }

    public void selectWaypoint(int index) {
        waypointsList.setSelectedIndex(index);
        //waypointsList.ensureIndexIsVisible(index);
    }

    private void addWaypoint(Waypoint waypoint) {
        int index = waypointsList.getSelectedIndex() + 1;
        waypointsModel.add(index, waypointToString(index, waypoint));
        settings.waypoints.add(index, waypoint);
        refreshWaypointsList();
        selectWaypoint(index);
    }

    private void removeWaypoint(int index) {
        settings.waypoints.remove(index);
        waypointsModel.removeElementAt(index);
        refreshWaypointsList();

        if (index == waypointsModel.size()) {
            index--;
        }
        selectWaypoint(index);
    }

    private void clearAllWaypoints() {
        settings.waypoints.clear();
        waypointsModel.clear();
    }

    private void addToTargetList(String name) {
        if (!targetsModel.contains(name)) {
            targetsModel.addElement(name.toLowerCase());
            settings.targets.add(name.toLowerCase());
        }
    }

    private void addToLootingList(int id, String name) {
        lootingInputID.setText("<ID>");
        lootingInputLabel.setText("<label, ex: gold>");

        String entry = lootEntryToString(id, name);
        if (!lootingModel.contains(entry)) {
            lootingModel.addElement(entry);
            settings.loot.add(new CavebotSettings.LootItem(id, name));
        }
    }

    private void removeFromLootingList(String entry) {
        if (lootingModel.contains(entry)) {
            settings.loot.remove(lootingModel.indexOf(entry));
            lootingModel.removeElement(entry);
        }
    }

    private String lootEntryToString(int id, String name) {
        return id + (name.isEmpty() ? "" : " " + name);
    }

    private void handleNumberFormatException() {
        lootingList.requestFocus();
    }

    private void executeSettings() {
        refreshWaypointsList();

        targetsModel.clear();
        for (String s : settings.targets) {
            targetsModel.addElement(s.toLowerCase());
        }

        lootingModel.clear();
        for (CavebotSettings.LootItem item : settings.loot) {
            lootingModel.addElement(lootEntryToString(item.id, item.label));
        }

        targetallCheck.setSelected(settings.targetAll);
        handleTargetAllStateChange(null);
        
        lootAllBox.setSelected(settings.lootExcept);

        Component[][] ar = {{pAppearedSoundChb, pAppearedPauseChb, pAppearedExitChb},
            {trappedSoundChb, trappedPauseChb, trappedExitChb},
            {mainchatSoundChb, mainchatPauseChb, mainchatExitChb},
            {privchatSoundChb, privchatPauseChb, privchatExitChb},
            {playerAtkSoundChb, playerAtkPauseChb, playerAtkExitChb}};

        for (int i = 0; i < settings.alarms.size(); i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (j == 0) {
                    ((javax.swing.JCheckBox) ar[i][j]).setSelected(settings.alarms.get(i).sound);
                } else if (j == 1) {
                    ((javax.swing.JCheckBox) ar[i][j]).setSelected(settings.alarms.get(i).pause);
                } else if (j == 2) {
                    ((javax.swing.JCheckBox) ar[i][j]).setSelected(settings.alarms.get(i).exit);
                }
            }
        }
    }

    private void refreshWaypointsList() {
        int previouslySelected = waypointsList.getSelectedIndex();
        waypointsModel.clear();
        for (int i = 0; i < settings.waypoints.size(); i++) {
            waypointsModel.addElement(waypointToString(i, settings.waypoints.get(i)));
        }
        if (previouslySelected == -1) {
            selectWaypoint(0);
        } else {
            selectWaypoint(previouslySelected);
        }
    }

    public void start() {
        cavebot = new Cavebot(settings);
        cavebot.start();
        startButton.setText("Stop");

        // Start calculator.
        setCalculatorVisiblity(true);
        calculator = new StatisticsCalculator();
        calculator.start();
    }

    public void stop() {
        cavebot.interrupt();
        cavebot = null;
        startButton.setText("Start");

        // Stop calculator.
        setCalculatorVisiblity(false);
        calculator.interrupt();
        calculator = null;
    }
    
    public int getAlarmVolume() {
        return volumeSlider.getValue();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        waypointsList = new javax.swing.JList();
        waypointsWalk = new javax.swing.JButton();
        waypointsDel = new javax.swing.JButton();
        waypointsUse = new javax.swing.JButton();
        waypointsClear = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        emplacementCombo = new javax.swing.JComboBox();
        waypointsAction = new javax.swing.JButton();
        waypointsLure = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        settingsField = new javax.swing.JTextField();
        settingsSave = new javax.swing.JButton();
        settingsLoad = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        settingsList = new javax.swing.JList();
        startButton = new javax.swing.JButton();
        targetPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        targetList = new javax.swing.JList();
        targetInput = new javax.swing.JTextField();
        targetAddButton = new javax.swing.JButton();
        targetDelButton = new javax.swing.JButton();
        targetClearButton = new javax.swing.JButton();
        targetallCheck = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lootingList = new javax.swing.JList();
        lootingInputID = new javax.swing.JTextField();
        lootingAdd = new javax.swing.JButton();
        lootingDel = new javax.swing.JButton();
        lootingClear = new javax.swing.JButton();
        lootingInputLabel = new javax.swing.JTextField();
        lootAllBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        playerOnScreenLabel = new javax.swing.JLabel();
        mainChatMsgLabel = new javax.swing.JLabel();
        privChatMsgLabel = new javax.swing.JLabel();
        playerAttackingLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        pAppearedSoundChb = new javax.swing.JCheckBox();
        pAppearedPauseChb = new javax.swing.JCheckBox();
        pAppearedExitChb = new javax.swing.JCheckBox();
        mainchatSoundChb = new javax.swing.JCheckBox();
        mainchatPauseChb = new javax.swing.JCheckBox();
        mainchatExitChb = new javax.swing.JCheckBox();
        privchatSoundChb = new javax.swing.JCheckBox();
        privchatPauseChb = new javax.swing.JCheckBox();
        privchatExitChb = new javax.swing.JCheckBox();
        playerAtkSoundChb = new javax.swing.JCheckBox();
        playerAtkPauseChb = new javax.swing.JCheckBox();
        playerAtkExitChb = new javax.swing.JCheckBox();
        volumeSlider = new javax.swing.JSlider();
        jLabel13 = new javax.swing.JLabel();
        trappedLabel = new javax.swing.JLabel();
        trappedSoundChb = new javax.swing.JCheckBox();
        trappedPauseChb = new javax.swing.JCheckBox();
        trappedExitChb = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        xpperhourLabel = new javax.swing.JLabel();
        goldperhourLabel = new javax.swing.JLabel();
        timerunningLabel = new javax.swing.JLabel();

        setTitle("Cavebot");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Waypoints"));

        waypointsList.setFont(new java.awt.Font("Lucida Console", 0, 11)); // NOI18N
        waypointsList.setModel(waypointsModel);
        waypointsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(waypointsList);

        waypointsWalk.setText("Walk");
        waypointsWalk.setMargin(new java.awt.Insets(2, 10, 2, 10));
        waypointsWalk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleWalkPressed(evt);
            }
        });

        waypointsDel.setText("Del");
        waypointsDel.setMargin(new java.awt.Insets(2, 10, 2, 10));
        waypointsDel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleDeleteWaypoint(evt);
            }
        });

        waypointsUse.setText("Use");
        waypointsUse.setMargin(new java.awt.Insets(2, 10, 2, 10));
        waypointsUse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleUsePressed(evt);
            }
        });

        waypointsClear.setText("Clear");
        waypointsClear.setMargin(new java.awt.Insets(2, 10, 2, 10));
        waypointsClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleClearPressed(evt);
            }
        });

        jLabel1.setText("Emplacement");

        emplacementCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Center", "North", "East", "South", "West", "North-West", "North-East", "South-East", "South-West" }));
        emplacementCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emplacementComboActionPerformed(evt);
            }
        });

        waypointsAction.setText("Action");
        waypointsAction.setEnabled(false);
        waypointsAction.setMargin(new java.awt.Insets(2, 10, 2, 10));

        waypointsLure.setText("Lure");
        waypointsLure.setMargin(new java.awt.Insets(2, 10, 2, 10));
        waypointsLure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                waypointsLureMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(waypointsDel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(waypointsClear)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(emplacementCombo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(waypointsUse, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(waypointsWalk))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(waypointsLure, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(waypointsAction, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(12, 12, 12))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emplacementCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(waypointsWalk)
                            .addComponent(waypointsLure))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(waypointsAction)
                            .addComponent(waypointsUse))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(waypointsDel)
                    .addComponent(waypointsClear))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        settingsField.setText("<file name>");
        settingsField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleSettingsInputFieldPressed(evt);
            }
        });
        settingsField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                handleSettingsInputFieldFocusLost(evt);
            }
        });

        settingsSave.setText("Save");
        settingsSave.setMargin(new java.awt.Insets(2, 10, 2, 10));
        settingsSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleSettingsSave(evt);
            }
        });

        settingsLoad.setText("Load");
        settingsLoad.setMargin(new java.awt.Insets(2, 10, 2, 10));
        settingsLoad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleSettingsLoad(evt);
            }
        });

        settingsList.setModel(settingsModel);
        settingsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        settingsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                settingsListValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(settingsList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(settingsSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(settingsLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(settingsField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsLoad)
                    .addComponent(settingsSave))
                .addContainerGap())
        );

        startButton.setText("Start");
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });

        targetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Targeting"));

        targetList.setModel(targetsModel);
        targetList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(targetList);

        targetInput.setText("<mob name, ex: swarm>");
        targetInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleTargetInputActionPerf(evt);
            }
        });
        targetInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                targetInputFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                targetInputFocusLost(evt);
            }
        });

        targetAddButton.setText("Add");
        targetAddButton.setMargin(new java.awt.Insets(2, 10, 2, 10));
        targetAddButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleTargetAddPressed(evt);
            }
        });

        targetDelButton.setText("Del");
        targetDelButton.setMargin(new java.awt.Insets(2, 10, 2, 10));
        targetDelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleTargetDelPressed(evt);
            }
        });

        targetClearButton.setText("Clear");
        targetClearButton.setMargin(new java.awt.Insets(2, 10, 2, 10));
        targetClearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleTargetClearPressed(evt);
            }
        });

        targetallCheck.setText("Target all");
        targetallCheck.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                handleTargetAllStateChange(evt);
            }
        });

        javax.swing.GroupLayout targetPanelLayout = new javax.swing.GroupLayout(targetPanel);
        targetPanel.setLayout(targetPanelLayout);
        targetPanelLayout.setHorizontalGroup(
            targetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(targetPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(targetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(targetPanelLayout.createSequentialGroup()
                        .addComponent(targetallCheck)
                        .addContainerGap())
                    .addGroup(targetPanelLayout.createSequentialGroup()
                        .addGroup(targetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(targetInput, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(targetPanelLayout.createSequentialGroup()
                                .addComponent(targetAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(targetDelButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(targetClearButton))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(14, 14, 14))))
        );
        targetPanelLayout.setVerticalGroup(
            targetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(targetPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(targetallCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(targetInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(targetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(targetAddButton)
                    .addComponent(targetDelButton)
                    .addComponent(targetClearButton))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Looting"));

        lootingList.setModel(lootingModel);
        lootingList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(lootingList);

        lootingInputID.setText("<ID>");
        lootingInputID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                handleLootingIDFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                handleLootingIDFocusLost(evt);
            }
        });

        lootingAdd.setText("Add");
        lootingAdd.setMargin(new java.awt.Insets(2, 10, 2, 10));
        lootingAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleLootingAddClicked(evt);
            }
        });

        lootingDel.setText("Del");
        lootingDel.setMargin(new java.awt.Insets(2, 10, 2, 10));
        lootingDel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleLootingDelPressed(evt);
            }
        });

        lootingClear.setText("Clear");
        lootingClear.setMargin(new java.awt.Insets(2, 10, 2, 10));
        lootingClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleLootingClearPressed(evt);
            }
        });

        lootingInputLabel.setText("<label, ex: gold>");
        lootingInputLabel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                handleLootingLabelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                handleLootingLabelFocusLost(evt);
            }
        });
        lootingInputLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                handleLootingLabelKeyReleased(evt);
            }
        });

        lootAllBox.setText("Loot all except listed below");
        lootAllBox.setToolTipText("");
        lootAllBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lootAllStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lootAllBox)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lootingAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lootingDel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lootingClear, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lootingInputID, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lootingInputLabel)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lootAllBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lootingInputID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lootingInputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lootingAdd)
                    .addComponent(lootingDel)
                    .addComponent(lootingClear))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Alarms"));

        playerOnScreenLabel.setText("Player on screen");

        mainChatMsgLabel.setText("Main chat msg");

        privChatMsgLabel.setText("Priv chat msg");

        playerAttackingLabel.setText("Player attacking");

        jLabel7.setText("S");
        jLabel7.setToolTipText("Sound");

        jLabel8.setText("P");
        jLabel8.setToolTipText("Pause");

        jLabel9.setText("X");
        jLabel9.setToolTipText("Exit");

        pAppearedSoundChb.setToolTipText("Sound alert");
        pAppearedSoundChb.setActionCommand("0");
        pAppearedSoundChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateSoundChanged(evt);
            }
        });

        pAppearedPauseChb.setToolTipText("Pause bot");
        pAppearedPauseChb.setActionCommand("0");
        pAppearedPauseChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStatePauseChanged(evt);
            }
        });

        pAppearedExitChb.setToolTipText("Exit");
        pAppearedExitChb.setActionCommand("0");
        pAppearedExitChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateExitChanged(evt);
            }
        });

        mainchatSoundChb.setToolTipText("Sound alert");
        mainchatSoundChb.setActionCommand("2");
        mainchatSoundChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateSoundChanged(evt);
            }
        });

        mainchatPauseChb.setToolTipText("Pause bot");
        mainchatPauseChb.setActionCommand("2");
        mainchatPauseChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStatePauseChanged(evt);
            }
        });

        mainchatExitChb.setToolTipText("Exit");
        mainchatExitChb.setActionCommand("2");
        mainchatExitChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateExitChanged(evt);
            }
        });

        privchatSoundChb.setToolTipText("Sound alert");
        privchatSoundChb.setActionCommand("3");
        privchatSoundChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateSoundChanged(evt);
            }
        });

        privchatPauseChb.setToolTipText("Pause bot");
        privchatPauseChb.setActionCommand("3");
        privchatPauseChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStatePauseChanged(evt);
            }
        });

        privchatExitChb.setToolTipText("Exit");
        privchatExitChb.setActionCommand("3");
        privchatExitChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateExitChanged(evt);
            }
        });

        playerAtkSoundChb.setToolTipText("Sound alert");
        playerAtkSoundChb.setActionCommand("4");
        playerAtkSoundChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateSoundChanged(evt);
            }
        });

        playerAtkPauseChb.setToolTipText("Pause bot");
        playerAtkPauseChb.setActionCommand("4");
        playerAtkPauseChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStatePauseChanged(evt);
            }
        });

        playerAtkExitChb.setToolTipText("Exit");
        playerAtkExitChb.setActionCommand("4");
        playerAtkExitChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateExitChanged(evt);
            }
        });

        volumeSlider.setValue(75);
        volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeSliderStateChanged(evt);
            }
        });

        jLabel13.setText("Volume");

        trappedLabel.setText("Trapped");

        trappedSoundChb.setToolTipText("Sound alert");
        trappedSoundChb.setActionCommand("1");
        trappedSoundChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateSoundChanged(evt);
            }
        });

        trappedPauseChb.setToolTipText("Pause bot");
        trappedPauseChb.setActionCommand("1");
        trappedPauseChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStatePauseChanged(evt);
            }
        });

        trappedExitChb.setToolTipText("Exit");
        trappedExitChb.setActionCommand("1");
        trappedExitChb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleItemStateExitChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(playerAttackingLabel)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(playerAtkSoundChb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerAtkPauseChb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerAtkExitChb)
                        .addGap(2, 2, 2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(trappedLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(trappedSoundChb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(trappedPauseChb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(trappedExitChb))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(mainChatMsgLabel)
                                            .addComponent(privChatMsgLabel))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(playerOnScreenLabel)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(privchatSoundChb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(privchatPauseChb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(privchatExitChb))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(mainchatSoundChb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mainchatPauseChb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mainchatExitChb))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(pAppearedSoundChb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pAppearedPauseChb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pAppearedExitChb)))))
                        .addGap(2, 2, 2))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(pAppearedExitChb)
                        .addComponent(pAppearedPauseChb)
                        .addComponent(pAppearedSoundChb))
                    .addComponent(playerOnScreenLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(playerAtkSoundChb)
                            .addComponent(playerAttackingLabel, javax.swing.GroupLayout.Alignment.LEADING))
                        .addComponent(playerAtkPauseChb, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(playerAtkExitChb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mainchatExitChb, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(mainchatPauseChb, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(mainchatSoundChb, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(mainChatMsgLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(privchatExitChb)
                    .addComponent(privchatPauseChb)
                    .addComponent(privchatSoundChb)
                    .addComponent(privChatMsgLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(trappedLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(trappedExitChb, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(trappedPauseChb, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(trappedSoundChb, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addContainerGap())
        );

        jLabel10.setText("Exp/hour:");

        jLabel11.setText("Gold/hour:");

        jLabel12.setText("Time running:");

        xpperhourLabel.setText("0");

        goldperhourLabel.setText("0");

        timerunningLabel.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xpperhourLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(goldperhourLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timerunningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(targetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(targetPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(xpperhourLabel)
                    .addComponent(goldperhourLabel)
                    .addComponent(timerunningLabel)
                    .addComponent(startButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void emplacementComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emplacementComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emplacementComboActionPerformed

    private void handleWalkPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleWalkPressed
        Waypoint wyp = new Waypoint(Waypoint.WALK, getEmplacementLoc());
        addWaypoint(wyp);
    }//GEN-LAST:event_handleWalkPressed

    private void handleUsePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleUsePressed
        Waypoint wyp = new Waypoint(Waypoint.USE, getEmplacementLoc());
        addWaypoint(wyp);
    }//GEN-LAST:event_handleUsePressed

    private void handleDeleteWaypoint(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleDeleteWaypoint
        removeWaypoint(waypointsList.getSelectedIndex());
    }//GEN-LAST:event_handleDeleteWaypoint

    private void handleClearPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleClearPressed
        clearAllWaypoints();
    }//GEN-LAST:event_handleClearPressed

    private void handleSettingsInputFieldPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleSettingsInputFieldPressed
        if (settingsField.getText().equals("<file name>")) {
            settingsField.setText("");
        }
    }//GEN-LAST:event_handleSettingsInputFieldPressed

    private void handleSettingsInputFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_handleSettingsInputFieldFocusLost
        if (settingsField.getText().equals("")) {
            settingsField.setText("<file name>");
        }
    }//GEN-LAST:event_handleSettingsInputFieldFocusLost

    private void handleTargetAddPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleTargetAddPressed
        if (!targetInput.isEnabled()) {
            return;
        }
        if (targetInput.getText().length() == 0) {
            return;
        }
        addToTargetList(targetInput.getText());
        targetInput.setText("");
        targetInput.requestFocus();
    }//GEN-LAST:event_handleTargetAddPressed

    private void handleTargetDelPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleTargetDelPressed
        if (!targetInput.isEnabled()) {
            return;
        }
        String selected = (String) targetList.getSelectedValue();
        if (targetsModel.contains(selected)) {
            targetsModel.removeElement(selected);
            settings.targets.remove(selected.toLowerCase());
        }
    }//GEN-LAST:event_handleTargetDelPressed

    private void handleTargetClearPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleTargetClearPressed
        if (!targetInput.isEnabled()) {
            return;
        }
        targetsModel.clear();
        settings.targets.clear();
    }//GEN-LAST:event_handleTargetClearPressed

    private void targetInputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_targetInputFocusLost
        if (targetInput.getText().equals("")) {
            targetInput.setText("<mob name, ex: swarm>");
        }
    }//GEN-LAST:event_targetInputFocusLost

    private void targetInputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_targetInputFocusGained
        if (!targetInput.isEnabled()) {
            return;
        }
        targetInput.setText("");
    }//GEN-LAST:event_targetInputFocusGained

    private void handleLootingIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_handleLootingIDFocusLost
        if (lootingInputID.getText().length() == 0) {
            lootingInputID.setText("<ID>");
        }
    }//GEN-LAST:event_handleLootingIDFocusLost

    private void handleLootingLabelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_handleLootingLabelFocusLost
        if (lootingInputLabel.getText().length() == 0) {
            lootingInputLabel.setText("<label, ex: gold>");
        }
    }//GEN-LAST:event_handleLootingLabelFocusLost

    private void handleLootingLabelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_handleLootingLabelFocusGained
        if (lootingInputLabel.getText().equals("<label, ex: gold>")) {
            lootingInputLabel.setText("");
        }
    }//GEN-LAST:event_handleLootingLabelFocusGained

    private void handleLootingIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_handleLootingIDFocusGained
        if (lootingInputID.getText().equals("<ID>")) {
            lootingInputID.setText("");
        }
    }//GEN-LAST:event_handleLootingIDFocusGained

    private void handleLootingLabelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_handleLootingLabelKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (lootingInputID.getText().length() > 0) {
                int id = 0;
                try {
                    id = Integer.parseInt(lootingInputID.getText());
                } catch (NumberFormatException e) {
                    handleNumberFormatException();
                }
                if (id != 0) {
                    addToLootingList(id, lootingInputLabel.getText());
                }
            }
        }
    }//GEN-LAST:event_handleLootingLabelKeyReleased

    private void handleLootingAddClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleLootingAddClicked
        if (lootingInputID.getText().length() > 0) {
            int id = 0;
            try {
                id = Integer.parseInt(lootingInputID.getText());
            } catch (NumberFormatException e) {
                handleNumberFormatException();
            }
            if (id != 0) {
                addToLootingList(id, lootingInputLabel.getText());
            }
        }
    }//GEN-LAST:event_handleLootingAddClicked

    private void handleLootingDelPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleLootingDelPressed
        removeFromLootingList((String) lootingList.getSelectedValue());
    }//GEN-LAST:event_handleLootingDelPressed

    private void handleLootingClearPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleLootingClearPressed
        lootingModel.clear();
        settings.loot.clear();
    }//GEN-LAST:event_handleLootingClearPressed

    private void handleSettingsSave(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleSettingsSave
        if (settingsField.getText().length() > 0 && !settingsField.getText().equals("<file name>")) {
            String filename = settingsField.getText();
            File file = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/cavebot/" + filename);
            file.mkdirs();
            if (file.exists()) {
                file.delete();
            }
            try {
                settings.save(file.getCanonicalPath());
            } catch (IOException e) {
            }
            refreshSettingsList();
            settingsList.setSelectedValue(filename, true);
        }
    }//GEN-LAST:event_handleSettingsSave

    private void handleSettingsLoad(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleSettingsLoad
        if (settingsField.getText().length() > 0 && !settingsField.getText().equals("<file name>")) {
            String filename = settingsField.getText();
            File file = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/cavebot/" + filename);
            if (file.exists()) {
                try {
                    settings = CavebotSettings.load(file.getCanonicalPath());
                } catch (IOException e) {
                }
                executeSettings();
            } else {
                settingsField.setText("<file name>");
            }
        }
    }//GEN-LAST:event_handleSettingsLoad

    private void handleTargetAllStateChange(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_handleTargetAllStateChange
        Component[] components = new Component[]{targetAddButton, targetClearButton, targetDelButton, targetInput, targetList};
        for (Component c : components) {
            c.setEnabled(!targetallCheck.isSelected());
        }
        settings.targetAll = targetallCheck.isSelected();
    }//GEN-LAST:event_handleTargetAllStateChange

    private void handleTargetInputActionPerf(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleTargetInputActionPerf
        if (!targetInput.isEnabled()) {
            return;
        }
        if (targetInput.getText().length() == 0) {
            return;
        }
        addToTargetList(targetInput.getText());
        targetInput.setText("");
        targetInput.requestFocus();
    }//GEN-LAST:event_handleTargetInputActionPerf

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        if (cavebot == null) {
            start();
        } else {
            stop();
        }
    }//GEN-LAST:event_startButtonMouseClicked

    private void handleItemStateSoundChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_handleItemStateSoundChanged
        int index = Integer.parseInt(((javax.swing.JCheckBox) evt.getSource()).getActionCommand());
        stopAlarm(index);
        settings.alarms.get(index).sound = (evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_handleItemStateSoundChanged

    private void handleItemStatePauseChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_handleItemStatePauseChanged
        int index = Integer.parseInt(((javax.swing.JCheckBox) evt.getSource()).getActionCommand());
        settings.alarms.get(index).pause = (evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_handleItemStatePauseChanged

    private void handleItemStateExitChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_handleItemStateExitChanged
        int index = Integer.parseInt(((javax.swing.JCheckBox) evt.getSource()).getActionCommand());
        settings.alarms.get(index).exit = (evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_handleItemStateExitChanged

    private void settingsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_settingsListValueChanged
        String selected = (String) settingsList.getSelectedValue();
        if (selected != null) {
            settingsField.setText(selected);
        }
    }//GEN-LAST:event_settingsListValueChanged

    private void volumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeSliderStateChanged
        alarmer.setVolume(volumeSlider.getValue());
    }//GEN-LAST:event_volumeSliderStateChanged

    private void waypointsLureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_waypointsLureMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_waypointsLureMouseClicked

    private void lootAllStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lootAllStateChanged
        settings.lootExcept = (evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_lootAllStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox emplacementCombo;
    public javax.swing.JLabel goldperhourLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JCheckBox lootAllBox;
    private javax.swing.JButton lootingAdd;
    private javax.swing.JButton lootingClear;
    private javax.swing.JButton lootingDel;
    private javax.swing.JTextField lootingInputID;
    private javax.swing.JTextField lootingInputLabel;
    private javax.swing.JList lootingList;
    private javax.swing.JLabel mainChatMsgLabel;
    public javax.swing.JCheckBox mainchatExitChb;
    public javax.swing.JCheckBox mainchatPauseChb;
    public javax.swing.JCheckBox mainchatSoundChb;
    public javax.swing.JCheckBox pAppearedExitChb;
    public javax.swing.JCheckBox pAppearedPauseChb;
    public javax.swing.JCheckBox pAppearedSoundChb;
    public javax.swing.JCheckBox playerAtkExitChb;
    public javax.swing.JCheckBox playerAtkPauseChb;
    public javax.swing.JCheckBox playerAtkSoundChb;
    private javax.swing.JLabel playerAttackingLabel;
    private javax.swing.JLabel playerOnScreenLabel;
    private javax.swing.JLabel privChatMsgLabel;
    public javax.swing.JCheckBox privchatExitChb;
    public javax.swing.JCheckBox privchatPauseChb;
    public javax.swing.JCheckBox privchatSoundChb;
    private javax.swing.JTextField settingsField;
    private javax.swing.JList settingsList;
    private javax.swing.JButton settingsLoad;
    private javax.swing.JButton settingsSave;
    private javax.swing.JButton startButton;
    private javax.swing.JButton targetAddButton;
    private javax.swing.JButton targetClearButton;
    private javax.swing.JButton targetDelButton;
    private javax.swing.JTextField targetInput;
    private javax.swing.JList targetList;
    private javax.swing.JPanel targetPanel;
    private javax.swing.JCheckBox targetallCheck;
    public javax.swing.JLabel timerunningLabel;
    public javax.swing.JCheckBox trappedExitChb;
    private javax.swing.JLabel trappedLabel;
    public javax.swing.JCheckBox trappedPauseChb;
    public javax.swing.JCheckBox trappedSoundChb;
    private javax.swing.JSlider volumeSlider;
    private javax.swing.JButton waypointsAction;
    private javax.swing.JButton waypointsClear;
    private javax.swing.JButton waypointsDel;
    public javax.swing.JList waypointsList;
    private javax.swing.JButton waypointsLure;
    private javax.swing.JButton waypointsUse;
    private javax.swing.JButton waypointsWalk;
    public javax.swing.JLabel xpperhourLabel;
    // End of variables declaration//GEN-END:variables
}
