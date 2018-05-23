/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on 2012-01-25, 14:55:21
 */
package gui;

import client.*;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import settings.AppSettings;
import settings.Constants;
import settings.UserSettings;
import structures.Creature;
import structures.Directions;
import structures.MapDescription;

/**
 *
 * @author Myzreal
 */
public class MainFrame extends javax.swing.JFrame {

    private class MinimapPainter extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    if (MainFrame.getInstance().getState() != JFrame.NORMAL) {
                        sleep(1);
                        continue;
                    }
                    minimapLock.lock();
                    BufferedImage bi = minimap.draw(Map.getInstance().getPlayer().location);
                    minimapPane.setImage(bi);
                    minimapPane.repaint();
                    minimapLock.unlock();

                    Point mousePos = minimapPane.getMousePosition();
                    if (mousePos != null) {
                        Point selectedLocation = translateFromMinimapPane(mousePos);
                        MapDescription.Tile selectedTile = Map.getInstance().at(selectedLocation.x, selectedLocation.y, Map.getInstance().getPlayer().location.z);
                        if (selectedTile != null) {
                            String s = "";
                            for (int i = 0; i < selectedTile.creatures.size(); i++) {
                                if (i != 0) {
                                    s += ", ";
                                }
                                Creature c = selectedTile.creatures.get(i);
                                s += c.name + " " + c.health + "%";
                            }
                            examinationLabel.setText(s);
                        }
                    }
                    sleep(50);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static MainFrame _instance;

    public static MainFrame getInstance() {
        return _instance;
    }
    private Minimap minimap;
    private Lock minimapLock = new ReentrantLock();
    public UserSettings settings;
    public DefaultComboBoxModel settingsModel = new DefaultComboBoxModel();
    private FoodEater foodEater;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        _instance = this;
        initComponents();
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }

    private void init() {
        // Adjust the slider and start drawing the minimap in the default zoom level.
        minimap = new Minimap();
        minimapSetZoomLevel(AppSettings.getInstance().minimapZoomLevel, true);
        zoomSlider.setValue(AppSettings.getInstance().minimapZoomLevel);
        new MinimapPainter().start();

        settings = new UserSettings();

        refreshSettingsList();

        // Init subframes
        new HealingFrame();
        new CavebotFrame();

        // A little magic - loads settings with the same name as the player, if exists.
        String charName = Client.getInstance().getCharacter().name;
        settingsModel.setSelectedItem(charName);
        if(!settingsModel.getSelectedItem().toString().equals(charName)) {
            settingsModel.insertElementAt(charName, 0);
            handleSave(null);
        }

        setTitle(Client.getInstance().getCharacter().name + " - ZenBot");
    }

    private void refreshSettingsList() {
        Object previouslySelected = settingsComboBox.getSelectedItem();
        settingsModel.removeAllElements();
        settingsModel.addElement("<New settings>");
        
        //Prepate settings list model.
        File file = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/user");
        if (!file.exists()) {
            file.mkdirs();
        }
        String[] files = file.list();
        for (String f : files) {
            settingsModel.insertElementAt(f, 0);
        }
        settingsComboBox.setSelectedItem(previouslySelected);
    }

    private void minimapSetZoomLevel(final int n, boolean block) {
        Runnable func = new Runnable() {

            @Override
            public void run() {
                int width = 9 * n;
                int height = 7 * n;
                if (n % 2 != 0) {
                    width++;
                    height++;
                }
                int xs = -width / 2 + 1;
                int ys = -height / 2 + 1;

                minimapLock.lock();
                minimap.configure(xs, ys, width, height);
                minimapLock.unlock();
            }
        };
        if (block) {
            func.run();
        } else {
            new Thread(func).start();
        }
    }

    public Point translateFromMinimapPane(Point p) {
        int x = (int) (p.x / (minimapPane.getWidth() / (double) minimap.width)) + Map.getInstance().getPlayer().location.x + minimap.xs;
        int y = (int) (p.y / (minimapPane.getHeight() / (double) minimap.height)) + Map.getInstance().getPlayer().location.y + minimap.ys;
        return new Point(x, y);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        minimapPopupMenu = new javax.swing.JPopupMenu();
        chaseMenuItem = new javax.swing.JMenuItem();
        attackMenuItem = new javax.swing.JMenuItem();
        followMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        useMenuItem = new javax.swing.JMenuItem();
        lookMenuItem = new javax.swing.JMenuItem();
        newSettingsDialog = new javax.swing.JDialog();
        newSettingsTextField = new javax.swing.JTextField();
        newSettingsButton = new javax.swing.JButton();
        cavebotButton = new javax.swing.JButton();
        healingButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        showWaypointsBox = new javax.swing.JCheckBox();
        zoomSlider = new javax.swing.JSlider();
        examinationLabel = new javax.swing.JLabel();
        minimapPane = new gui.MinimapPanel();
        jPanel3 = new javax.swing.JPanel();
        eatfoodCheck = new javax.swing.JCheckBox();
        walkWithArrowsCheckBox = new javax.swing.JCheckBox();
        chatPanel1 = new gui.ChatPanel();
        inventoryPanel2 = new gui.InventoryPanel();
        playerStatusPanel1 = new gui.PlayerStatusPanel();
        settingsComboBox = new javax.swing.JComboBox();
        settingsSave = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        mainMenu = new javax.swing.JMenuBar();
        menuHeadBot = new javax.swing.JMenu();
        cavebotMenuItem = new javax.swing.JCheckBoxMenuItem();
        healingMenu = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuitemLogout = new javax.swing.JMenuItem();
        menuHeadOpen = new javax.swing.JMenu();
        checkBoxCreatures = new javax.swing.JCheckBoxMenuItem();

        minimapPopupMenu.setLightWeightPopupEnabled(false);

        chaseMenuItem.setText("Chase");
        chaseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chaseMenuItemActionPerformed(evt);
            }
        });
        minimapPopupMenu.add(chaseMenuItem);

        attackMenuItem.setText("Attack");
        attackMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attackMenuItemActionPerformed(evt);
            }
        });
        minimapPopupMenu.add(attackMenuItem);

        followMenuItem.setText("Follow");
        followMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                followMenuItemActionPerformed(evt);
            }
        });
        minimapPopupMenu.add(followMenuItem);
        minimapPopupMenu.add(jSeparator2);

        useMenuItem.setText("Use");
        useMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useMenuItemActionPerformed(evt);
            }
        });
        minimapPopupMenu.add(useMenuItem);

        lookMenuItem.setText("Look");
        lookMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lookMenuItemActionPerformed(evt);
            }
        });
        minimapPopupMenu.add(lookMenuItem);

        newSettingsDialog.setTitle("New settings");
        newSettingsDialog.setIconImage(null);
        newSettingsDialog.setMinimumSize(new java.awt.Dimension(217, 75));
        newSettingsDialog.setPreferredSize(new java.awt.Dimension(217, 75));
        newSettingsDialog.setResizable(false);

        newSettingsButton.setText("Save");
        newSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newSettingsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout newSettingsDialogLayout = new javax.swing.GroupLayout(newSettingsDialog.getContentPane());
        newSettingsDialog.getContentPane().setLayout(newSettingsDialogLayout);
        newSettingsDialogLayout.setHorizontalGroup(
            newSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newSettingsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newSettingsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newSettingsButton)
                .addContainerGap())
        );
        newSettingsDialogLayout.setVerticalGroup(
            newSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newSettingsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newSettingsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newSettingsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newSettingsButton))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Zezenia Bot");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                handleClosingWindow(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        cavebotButton.setText("Cavebot");
        cavebotButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cavebotButtonActionPerformed(evt);
            }
        });
        cavebotButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        healingButton.setText("Healing");
        healingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                healingButtonActionPerformed(evt);
            }
        });
        healingButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Minimap"));
        jPanel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        showWaypointsBox.setText("Show waypoints");
        showWaypointsBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleShowWaypointsStateChange(evt);
            }
        });
        showWaypointsBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        zoomSlider.setMajorTickSpacing(1);
        zoomSlider.setMaximum(6);
        zoomSlider.setMinimum(2);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setValue(3);
        zoomSlider.setFocusable(false);
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zoomSliderStateChanged(evt);
            }
        });
        zoomSlider.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        examinationLabel.setForeground(new java.awt.Color(51, 51, 51));

        minimapPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        minimapPane.setPreferredSize(new java.awt.Dimension(216, 168));
        minimapPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimapPaneMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                minimapPaneMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                minimapPaneMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout minimapPaneLayout = new javax.swing.GroupLayout(minimapPane);
        minimapPane.setLayout(minimapPaneLayout);
        minimapPaneLayout.setHorizontalGroup(
            minimapPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 214, Short.MAX_VALUE)
        );
        minimapPaneLayout.setVerticalGroup(
            minimapPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 166, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minimapPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(showWaypointsBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(zoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(examinationLabel))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showWaypointsBox, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(minimapPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(examinationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        eatfoodCheck.setText("Eat food");
        eatfoodCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                handleEatFoodStateChange(evt);
            }
        });
        eatfoodCheck.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        walkWithArrowsCheckBox.setText("Walk with arrows");
        walkWithArrowsCheckBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(walkWithArrowsCheckBox)
                    .addComponent(eatfoodCheck))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(walkWithArrowsCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(eatfoodCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsComboBox.setModel(settingsModel);
        settingsComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                settingsComboBoxItemStateChanged(evt);
            }
        });

        settingsSave.setText("Save");
        settingsSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleSave(evt);
            }
        });
        settingsSave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                handleKeyCombination(evt);
            }
        });

        jButton1.setText("Scripting");
        jButton1.setEnabled(false);

        menuHeadBot.setText("Bot");

        cavebotMenuItem.setText("Cavebot");
        cavebotMenuItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cavebotMenuItemItemStateChanged(evt);
            }
        });
        menuHeadBot.add(cavebotMenuItem);

        healingMenu.setText("Healing");
        healingMenu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                healingMenuItemStateChanged(evt);
            }
        });
        menuHeadBot.add(healingMenu);
        menuHeadBot.add(jSeparator1);

        menuitemLogout.setText("Logout");
        menuitemLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleLogoutPressed(evt);
            }
        });
        menuHeadBot.add(menuitemLogout);

        mainMenu.add(menuHeadBot);

        menuHeadOpen.setText("View");

        checkBoxCreatures.setText("Creatures");
        checkBoxCreatures.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                handleCreaturesPressed(evt);
            }
        });
        menuHeadOpen.add(checkBoxCreatures);

        mainMenu.add(menuHeadOpen);

        setJMenuBar(mainMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cavebotButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(healingButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(settingsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(settingsSave, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(chatPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inventoryPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(playerStatusPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cavebotButton)
                    .addComponent(healingButton)
                    .addComponent(settingsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingsSave)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chatPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inventoryPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerStatusPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void handleClosingWindow(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_handleClosingWindow
    }//GEN-LAST:event_handleClosingWindow

    private void handleKeyCombination(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_handleKeyCombination
        if (walkWithArrowsCheckBox.isSelected()) {
            try {
                if (evt.getKeyCode() == evt.VK_UP) {
                    Client.getInstance().walk(Directions.NORTH);
                } else if (evt.getKeyCode() == evt.VK_DOWN) {
                    Client.getInstance().walk(Directions.SOUTH);
                } else if (evt.getKeyCode() == evt.VK_LEFT) {
                    Client.getInstance().walk(Directions.WEST);
                } else if (evt.getKeyCode() == evt.VK_RIGHT) {
                    Client.getInstance().walk(Directions.EAST);
                }
            } catch (Exception e) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if (evt.getKeyChar() >= '1' && evt.getKeyChar() <= '5') {
            int zoomLevel = evt.getKeyChar() - '1' + 2;
            minimapSetZoomLevel(zoomLevel, true);
            zoomSlider.setValue(zoomLevel);
        }
    }//GEN-LAST:event_handleKeyCombination
    private void handleLogoutPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleLogoutPressed
        try {
            Client.getInstance().logout();


        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_handleLogoutPressed

    private void handleCreaturesPressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleCreaturesPressed
        if (CreaturesFrame.getInstance() == null) {
            new CreaturesFrame();
            //checkBoxCreatures.setSelected(true);
        } else if (CreaturesFrame.getInstance() != null) {
            CreaturesFrame.getInstance().dispose();
            //checkBoxCreatures.setSelected(false);
        }
    }//GEN-LAST:event_handleCreaturesPressed
    private static Class[] frames = new Class[]{
        MainFrame.class, CavebotFrame.class, CreaturesFrame.class,
        HealingFrame.class
    };

    public void packSettings() {
        for (Class frame : frames) {
            try {
                Method getInstance = frame.getMethod("getInstance");
                Window frameInstance = (Window) getInstance.invoke(frame);
                if (frameInstance != null && frameInstance.isVisible()) {
                    UserSettings.Window wnd = new UserSettings.Window();
                    String name = frame.getSimpleName();
                    wnd.name = name.substring(0, name.length() - 5);
                    wnd.location = frameInstance.getLocation();
                    UserSettings.Window inList = null;
                    for (UserSettings.Window w : settings.windows) {
                        if (w.name.equals(wnd.name)) {
                            inList = w;
                            break;
                        }
                    }
                    if (inList != null) {
                        inList.location = wnd.location;
                    } else {
                        settings.windows.add(wnd);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(UserSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // MainFrame
        settings.eatFood = eatfoodCheck.isSelected();
        settings.showWaypoints = showWaypointsBox.isSelected();

        // Other frames that use UserSettings
        HealingFrame.getInstance().packSettings();
    }

    public void unpackSettings() {
        for (UserSettings.Window wnd : settings.windows) {
            for (Class frame : frames) {
                String name = frame.getSimpleName();
                if (wnd.name.equals(name.substring(0, name.length() - 5))) {
                    try {
                        Method getInstance = frame.getMethod("getInstance");
                        Window wndInstance = (Window) getInstance.invoke(frame);
                        if (wndInstance == null) {
                            wndInstance = (Window) frame.newInstance();
                        }
                        if (!wndInstance.getLocation().equals(wnd.location)) {
                            wndInstance.setLocation(wnd.location);
                        }
                        if (!wndInstance.isVisible()) {
                            wndInstance.setVisible(true);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }

        // MainFrame
        eatfoodCheck.setSelected(settings.eatFood);
        showWaypointsBox.setSelected(settings.showWaypoints);

        // Other frames that use UserSettings
        HealingFrame.getInstance().unpackSettings();
    }

    public int getMinimapZoomLevel() {
        return zoomSlider.getValue();
    }

    private void handleSave(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_handleSave
        // pack settings from all needed frames.
        packSettings();
        
        String filename = settingsComboBox.getSelectedItem().toString();
        File file = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/user/" + filename);
        file.mkdirs();
        if (file.exists()) {
            file.delete();
        }
        try {
            settings.save(file.getCanonicalPath());
        } catch (IOException e) {
        }
        refreshSettingsList();
    }//GEN-LAST:event_handleSave

    private void handleEatFoodStateChange(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_handleEatFoodStateChange
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (foodEater == null) {
                foodEater = new FoodEater();
                foodEater.start();
            }
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            if (foodEater != null) {
                foodEater.interrupt();
                foodEater = null;
            }
        }
    }//GEN-LAST:event_handleEatFoodStateChange

    private void cavebotButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cavebotButtonActionPerformed
        cavebotMenuItemItemStateChanged(null);
    }//GEN-LAST:event_cavebotButtonActionPerformed

    private void healingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_healingButtonActionPerformed
        healingMenuItemStateChanged(null);
    }//GEN-LAST:event_healingButtonActionPerformed

    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zoomSliderStateChanged
        AppSettings.getInstance().minimapZoomLevel = zoomSlider.getValue();
        minimapSetZoomLevel(zoomSlider.getValue(), false);
    }//GEN-LAST:event_zoomSliderStateChanged

    private void attackMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attackMenuItemActionPerformed
        try {
            selectedEntity.attack();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_attackMenuItemActionPerformed
    private MapDescription.Tile selectedTile = null;
    private Item selectedItem = null;
    private Entity selectedEntity = null;

    private void minimapPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimapPaneMouseClicked
        if (SwingUtilities.isLeftMouseButton(evt)) {
            Point location = translateFromMinimapPane(evt.getPoint());
            try {
                Client.getInstance().walk(location.x, location.y, Map.getInstance().getPlayer().location.z);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_minimapPaneMouseClicked

    private void followMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_followMenuItemActionPerformed
        try {
            selectedEntity.follow();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_followMenuItemActionPerformed

    private void useMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useMenuItemActionPerformed
        try {
            selectedItem.use();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_useMenuItemActionPerformed

    private void lookMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lookMenuItemActionPerformed
        try {
            selectedItem.look();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lookMenuItemActionPerformed

    private void chaseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chaseMenuItemActionPerformed
        try {
            selectedEntity.chase();
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_chaseMenuItemActionPerformed

    private void minimapPaneMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimapPaneMousePressed
        minimapPaneMousePressedOrReleased(evt);
    }//GEN-LAST:event_minimapPaneMousePressed

    private void minimapPaneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimapPaneMouseReleased
        minimapPaneMousePressedOrReleased(evt);
    }//GEN-LAST:event_minimapPaneMouseReleased

    private void handleShowWaypointsStateChange(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_handleShowWaypointsStateChange
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            settings.showWaypoints = true;
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            settings.showWaypoints = false;
        }
    }//GEN-LAST:event_handleShowWaypointsStateChange

    private void cavebotMenuItemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cavebotMenuItemItemStateChanged
        if (!CavebotFrame.getInstance().isVisible()) {
            CavebotFrame.getInstance().setVisible(true);
            //cavebotMenuItem.setSelected(true);
        } else {
            CavebotFrame.getInstance().setVisible(false);
            //cavebotMenuItem.setSelected(false);
        }
    }//GEN-LAST:event_cavebotMenuItemItemStateChanged

    private void healingMenuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_healingMenuItemStateChanged
        if (!HealingFrame.getInstance().isVisible()) {
            HealingFrame.getInstance().setVisible(true);
            //healingMenu.setSelected(true);
        } else {
            HealingFrame.getInstance().setVisible(false);
            //healingMenu.setSelected(false);
        }
    }//GEN-LAST:event_healingMenuItemStateChanged

    private void settingsComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_settingsComboBoxItemStateChanged
        if(settingsComboBox.getSelectedItem() == null) {
            return;
        }
        if(settingsModel.getSize() > 1 && settingsComboBox.getSelectedItem().toString().equals("<New settings>")) {
            newSettingsDialog.setLocation(settingsComboBox.getLocationOnScreen().x, settingsComboBox.getLocationOnScreen().y);
            newSettingsDialog.setVisible(true);
            newSettingsTextField.requestFocusInWindow();
        }
        String filename = settingsComboBox.getSelectedItem().toString();
        File f = new File(System.getenv("APPDATA"), Constants.APP_NAME + "/settings/user/" + filename);
        if (f.exists()) {
            try {
                settings = UserSettings.load(f.getCanonicalPath());
            } catch (IOException e) {
            }
            unpackSettings();
        }
    }//GEN-LAST:event_settingsComboBoxItemStateChanged

    private void newSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newSettingsButtonActionPerformed
        newSettingsDialog.setVisible(false);
        settingsModel.insertElementAt(newSettingsTextField.getText(), 0);
        newSettingsTextField.setText("");
        settingsComboBox.setSelectedIndex(0);
        handleSave(null);
    }//GEN-LAST:event_newSettingsButtonActionPerformed

    private void minimapPaneMousePressedOrReleased(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            Point p = translateFromMinimapPane(evt.getPoint());
            selectedTile = Map.getInstance().at(p.x, p.y, Map.getInstance().getPlayer().location.z);
            if (selectedTile != null) {
                selectedItem = ItemLoc.fromLocation(selectedTile.location).get();
                try {
                    selectedEntity = new Entity(selectedTile.location, selectedTile.creatures.get(0));
                    if (selectedEntity.id == Map.getInstance().getPlayer().id) {
                        throw new Exception();
                    }
                    chaseMenuItem.setVisible(true);
                    attackMenuItem.setVisible(true);
                    followMenuItem.setVisible(true);
                    jSeparator2.setVisible(true);
                } catch (Exception ex) {
                    chaseMenuItem.setVisible(false);
                    attackMenuItem.setVisible(false);
                    followMenuItem.setVisible(false);
                    jSeparator2.setVisible(false);
                }
                minimapPopupMenu.show(minimapPane, evt.getX(), evt.getY());
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem attackMenuItem;
    private javax.swing.JButton cavebotButton;
    public javax.swing.JCheckBoxMenuItem cavebotMenuItem;
    private javax.swing.JMenuItem chaseMenuItem;
    private gui.ChatPanel chatPanel1;
    public javax.swing.JCheckBoxMenuItem checkBoxCreatures;
    public javax.swing.JCheckBox eatfoodCheck;
    private javax.swing.JLabel examinationLabel;
    private javax.swing.JMenuItem followMenuItem;
    private javax.swing.JButton healingButton;
    public javax.swing.JCheckBoxMenuItem healingMenu;
    private gui.InventoryPanel inventoryPanel2;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem lookMenuItem;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JMenu menuHeadBot;
    private javax.swing.JMenu menuHeadOpen;
    private javax.swing.JMenuItem menuitemLogout;
    private gui.MinimapPanel minimapPane;
    private javax.swing.JPopupMenu minimapPopupMenu;
    private javax.swing.JButton newSettingsButton;
    private javax.swing.JDialog newSettingsDialog;
    private javax.swing.JTextField newSettingsTextField;
    private gui.PlayerStatusPanel playerStatusPanel1;
    private javax.swing.JComboBox settingsComboBox;
    private javax.swing.JButton settingsSave;
    public javax.swing.JCheckBox showWaypointsBox;
    private javax.swing.JMenuItem useMenuItem;
    private javax.swing.JCheckBox walkWithArrowsCheckBox;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables
}
