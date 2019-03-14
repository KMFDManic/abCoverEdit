/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.autobleem.abcoveredit.ui;

import io.github.autobleem.abcoveredit.controller.CoverDBProcessor;
import io.github.autobleem.abcoveredit.domain.Game;
import io.github.autobleem.abcoveredit.domain.GameListEntry;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author artur.jakubowicz
 */
public class MainEditor extends javax.swing.JFrame {

    private String coverDBpath = null;
    private List<GameListEntry> games = null;
    private List<GameListEntry> allGames = null;
    
     private List<String> files = null;
    private List<String> allFiles = null;
   
    private Game visibleGame = null;
    private DefaultListModel gameListModel = new DefaultListModel();
    private DefaultListModel imageListModel = new DefaultListModel();
    

    private CoverDBProcessor cdbp;

    /**
     * Creates new form MainEditor
     */
    public MainEditor() {
        initComponents();
        searchGameEdit.setText("");
        updateGameData();
        gameList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int listIndex = gameList.getSelectedIndex();
                if (listIndex != -1) {
                    GameListEntry entry = games.get(listIndex);
                    visibleGame = cdbp.getGameData(entry.getId());
                    updateGameData();
                }
            }
        });
        
        jList2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int listIndex = jList2.getSelectedIndex();
                if (listIndex != -1) {
                    String fileName = files.get(listIndex);
                    String path = selFolderEdit.getText()+File.separator+fileName;
                    previewImage.loadImage(path);
                   
                } else
                {
                    previewImage.setImage(null);
                }
            }
        });
        
        jSlider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();
                    yearEdit.setText(Integer.toString(value));
                }
            }

        });
        jSlider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    int value = slider.getValue();
                    playersEdit.setText(Integer.toString(value));
                }
            }

        });
    }

    private void updateSerialInfo() {
        if (visibleGame == null) {
            serialsList.setModel(new DefaultListModel<>());
            serialsList.setSelectedIndex(-1);
        } else {
            DefaultListModel serialsModel = new DefaultListModel();
            for (String serial : visibleGame.getSerials()) {
                serialsModel.addElement(serial);
            }
            serialsList.setModel(serialsModel);
        }
    }

    public void updateGameData() {
        if (visibleGame == null) {
            titleEdit.setText("");
            publisherEdit.setText("");
            versionNumber.setText("");
            playersEdit.setText("");
            yearEdit.setText("");
            jSlider1.setValue(0);
            jSlider2.setValue(0);
            jSlider1.setEnabled(false);
            jSlider2.setEnabled(false);
            titleEdit.setEditable(false);
            publisherEdit.setEditable(false);
            gameCoverImage.setImage(null);

        } else {
            titleEdit.setEditable(true);
            publisherEdit.setEditable(true);
            jSlider1.setEnabled(true);
            jSlider2.setEnabled(true);
            titleEdit.setText(visibleGame.getTitle());
            publisherEdit.setText(visibleGame.getPublisher());
            yearEdit.setText(Integer.toString(visibleGame.getYear()));
            playersEdit.setText(Integer.toString(visibleGame.getPlayers()));
            gameCoverImage.setImage(visibleGame.getCover());
            jSlider1.setValue(visibleGame.getYear());
            jSlider2.setValue(visibleGame.getPlayers());
            versionNumber.setText(Integer.toString(visibleGame.getVersion()));

        }
        updateSerialInfo();
    }

    public void filterModel(DefaultListModel<String> model, String filter) {
        if (filter.trim().isEmpty()) {
            games.clear();
            model.clear();
            for (GameListEntry s : allGames) {
                games.add(s);
                model.addElement(s.getTitle());
            }
            return;
        }
        for (GameListEntry s : allGames) {
            if (!s.getTitle().toLowerCase().contains(filter.toLowerCase())) {
                if (model.contains(s.getTitle())) {
                    model.removeElement(s.getTitle());
                    games.remove(s);
                }
            } else {
                if (!model.contains(s.getTitle())) {
                    model.addElement(s.getTitle());
                    games.add(s);
                }
            }
        }
    }
    
    public void filterImageModel(DefaultListModel<String> model, String filter) {
        if (filter.trim().isEmpty()) {
            files.clear();
            model.clear();
            files.addAll(allFiles);
            for (String s : allFiles) {
              
                model.addElement(s);
            }
            return;
        }
        for (String s : allFiles) {
            if (!s.toLowerCase().contains(filter.toLowerCase())) {
                if (model.contains(s)) {
                    model.removeElement(s);
                    files.remove(s);
                }
            } else {
                if (!model.contains(s)) {
                    model.addElement(s);
                    files.add(s);
                }
            }
        }
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
        jLabel1 = new javax.swing.JLabel();
        searchGameEdit = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        gameList = new javax.swing.JList<>();
        searchGameClrBtn = new javax.swing.JButton();
        delGameBtn = new javax.swing.JButton();
        addGameBtn = new javax.swing.JButton();
        loadDBBtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        addSerialBtn = new javax.swing.JButton();
        deleteSerialBtn = new javax.swing.JButton();
        gameCoverImage = new io.github.autobleem.abcoveredit.ui.components.JImagePanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        titleEdit = new javax.swing.JTextField();
        publisherEdit = new javax.swing.JTextField();
        versionNumber = new javax.swing.JTextField();
        playersEdit = new javax.swing.JTextField();
        yearEdit = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        serialsList = new javax.swing.JList<>();
        saveBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        previewImage = new io.github.autobleem.abcoveredit.ui.components.JImagePanel();
        selFolderEdit = new javax.swing.JTextField();
        browsePngFolderBtn = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        searchImageEdit = new javax.swing.JTextField();
        clearImageBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Games List"));

        jLabel1.setText("Search:");

        searchGameEdit.setText("jTextField1");
        searchGameEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchGameEditKeyTyped(evt);
            }
        });

        gameList.setModel(gameListModel);
        jScrollPane1.setViewportView(gameList);

        searchGameClrBtn.setText("Clear");
        searchGameClrBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchGameClrBtnActionPerformed(evt);
            }
        });

        delGameBtn.setText("-");
        delGameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delGameBtnActionPerformed(evt);
            }
        });

        addGameBtn.setText("+");
        addGameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGameBtnActionPerformed(evt);
            }
        });

        loadDBBtn.setText("Load...");
        loadDBBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDBBtnActionPerformed(evt);
            }
        });

        jButton1.setText("CleanUp");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchGameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchGameClrBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadDBBtn)
                        .addGap(18, 18, 18)
                        .addComponent(addGameBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delGameBtn)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addGameBtn)
                        .addComponent(delGameBtn)
                        .addComponent(loadDBBtn)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(searchGameEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchGameClrBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Data"));

        addSerialBtn.setText("+");
        addSerialBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSerialBtnActionPerformed(evt);
            }
        });

        deleteSerialBtn.setText("-");
        deleteSerialBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSerialBtnActionPerformed(evt);
            }
        });

        gameCoverImage.setMinimumSize(new java.awt.Dimension(226, 226));
        gameCoverImage.setPreferredSize(new java.awt.Dimension(226, 226));

        javax.swing.GroupLayout gameCoverImageLayout = new javax.swing.GroupLayout(gameCoverImage);
        gameCoverImage.setLayout(gameCoverImageLayout);
        gameCoverImageLayout.setHorizontalGroup(
            gameCoverImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 226, Short.MAX_VALUE)
        );
        gameCoverImageLayout.setVerticalGroup(
            gameCoverImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 226, Short.MAX_VALUE)
        );

        jLabel2.setText("Title:");

        jLabel3.setText("Publisher:");

        jLabel4.setText("Year:");

        jLabel5.setText("Players:");

        jLabel6.setText("Ver:");

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setMaximum(2020);
        jSlider1.setMinimum(1990);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.setPaintTicks(true);

        jSlider2.setMajorTickSpacing(1);
        jSlider2.setMaximum(6);
        jSlider2.setMinimum(1);
        jSlider2.setPaintTicks(true);

        titleEdit.setText("jTextField3");

        publisherEdit.setText("jTextField3");

        versionNumber.setEditable(false);
        versionNumber.setText("jTextField3");

        playersEdit.setEditable(false);
        playersEdit.setText("jTextField3");

        yearEdit.setEditable(false);
        yearEdit.setText("jTextField3");

        serialsList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(serialsList);

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gameCoverImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(versionNumber)
                        .addGap(390, 390, 390))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleEdit)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jSlider1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                                            .addComponent(jSlider2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(yearEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                                            .addComponent(playersEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                                    .addComponent(publisherEdit)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(saveBtn)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(addSerialBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(deleteSerialBtn))
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(119, 119, 119))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(gameCoverImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 8, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(titleEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(publisherEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(yearEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(playersEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(versionNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane3))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addSerialBtn)
                            .addComponent(deleteSerialBtn)
                            .addComponent(saveBtn))))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Folder"));

        previewImage.setMinimumSize(new java.awt.Dimension(226, 226));

        javax.swing.GroupLayout previewImageLayout = new javax.swing.GroupLayout(previewImage);
        previewImage.setLayout(previewImageLayout);
        previewImageLayout.setHorizontalGroup(
            previewImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 226, Short.MAX_VALUE)
        );
        previewImageLayout.setVerticalGroup(
            previewImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        selFolderEdit.setEditable(false);

        browsePngFolderBtn.setText("Browse..");
        browsePngFolderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browsePngFolderBtnActionPerformed(evt);
            }
        });

        jButton5.setText("<< ");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel7.setText("Search:");

        searchImageEdit.setEditable(false);
        searchImageEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchImageEditKeyTyped(evt);
            }
        });

        clearImageBtn.setText("Clear");
        clearImageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearImageBtnActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jList2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(selFolderEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browsePngFolderBtn))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(previewImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchImageEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearImageBtn)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selFolderEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browsePngFolderBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(previewImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(searchImageEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearImageBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadDBBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDBBtnActionPerformed
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "SQLite Autobleem Cover Database", "db");
        chooser.setFileFilter(filter);
        int retVal = chooser.showOpenDialog(jPanel1);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            try {
                visibleGame = null;
                updateGameData();
                File f = chooser.getSelectedFile();
                cdbp = new CoverDBProcessor(f.getCanonicalPath());
                allGames = cdbp.getGamesList();
                games = new ArrayList<>();
                games.addAll(allGames);
                searchGameEdit.setText("");
                gameList.clearSelection();
                gameListModel.clear();
                this.setTitle("AutoBleem Cover Editor - " + f.getName());

                for (GameListEntry game : games) {
                    gameListModel.addElement(game.getTitle());
                }
                gameList.setModel(gameListModel);
            } catch (IOException ex) {
                Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_loadDBBtnActionPerformed

    private void searchGameEditKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchGameEditKeyTyped
        // TODO add your handling code here:
        if (allGames != null) {
            filterModel(gameListModel, searchGameEdit.getText());
        }
    }//GEN-LAST:event_searchGameEditKeyTyped

    private void searchGameClrBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchGameClrBtnActionPerformed
        // TODO add your handling code here:
        if (allGames != null) {
            searchGameEdit.setText("");
            filterModel(gameListModel, "");
        }
    }//GEN-LAST:event_searchGameClrBtnActionPerformed

    private void deleteSerialBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSerialBtnActionPerformed
        if (serialsList.getSelectedIndex() == -1) {
            return;
        }
        String serialToDelete = serialsList.getModel().getElementAt(serialsList.getSelectedIndex());
        int input = JOptionPane.showConfirmDialog(null, "Do you want to delete '" + serialToDelete + "' serial?");
        if (input == 0) {
            cdbp.removeSerial(visibleGame.getId(), serialToDelete);
            visibleGame = cdbp.getSerials(visibleGame);
            updateSerialInfo();
        }
    }//GEN-LAST:event_deleteSerialBtnActionPerformed

    private void addSerialBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSerialBtnActionPerformed
        // TODO add your handling code here:
        if (visibleGame == null) {
            return;
        }
        String s = (String) JOptionPane.showInputDialog(
                this,
                "Please enter serial",
                "");

        if (!s.trim().isEmpty()) {
            cdbp.addSerial(visibleGame.getId(), s.toUpperCase());
            visibleGame = cdbp.getSerials(visibleGame);
            updateSerialInfo();
        }
    }//GEN-LAST:event_addSerialBtnActionPerformed

    private void delGameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delGameBtnActionPerformed
        // TODO add your handling code here:
        if (gameList.getSelectedIndex() == -1) {
            return;
        }
        GameListEntry toDelete = games.get(gameList.getSelectedIndex());
        int input = JOptionPane.showConfirmDialog(null, "Do you want to delete '" + toDelete.getTitle() + "' game?");
        if (input == 0) {
            cdbp.removeGame(toDelete.getId());
            visibleGame = null;

            updateGameData();
            games.remove(toDelete);
            allGames.remove(toDelete);
            gameList.setSelectedIndex(-1);
            gameListModel.clear();
            for (GameListEntry game : games) {
                gameListModel.addElement(game.getTitle());
            }
            gameList.setModel(gameListModel);

            //updateSerialInfo();
        }
    }//GEN-LAST:event_delGameBtnActionPerformed

    private void addGameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGameBtnActionPerformed
        // TODO add your handling code here:
        String s = (String) JOptionPane.showInputDialog(
                this,
                "Please enter name",
                "");

        if (!s.trim().isEmpty()) {
            int id = cdbp.createGame(s);

            if (allGames != null) {
                allGames.clear();
                allGames = null;

            }

            allGames = cdbp.getGamesList();
            searchGameEdit.setText("");
            filterModel(gameListModel, "");
            for (int i = 0; i < games.size(); i++) {
                if (games.get(i).getId() == id) {
                    gameList.setSelectedIndex(i);
                    gameList.ensureIndexIsVisible(i);
                    visibleGame = cdbp.getGameData(id);
                    updateGameData();
                    break;
                }
            }

        }
    }//GEN-LAST:event_addGameBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // TODO add your handling code here:
        visibleGame.setTitle(titleEdit.getText());
        visibleGame.setPublisher(publisherEdit.getText());
        visibleGame.setYear(Integer.parseInt(yearEdit.getText()));
        visibleGame.setPlayers(Integer.parseInt(playersEdit.getText()));
        cdbp.updateGame(visibleGame);
        updateGameData();

        int lastSel = gameList.getSelectedIndex();
        gameListModel.setElementAt(visibleGame.getTitle(), lastSel);
        gameList.setModel(gameListModel);
        gameList.setSelectedIndex(lastSel);
        gameList.ensureIndexIsVisible(lastSel);

    }//GEN-LAST:event_saveBtnActionPerformed

    private void updateImageModel()
    {
        imageListModel.clear();
        for (String s:files)
        {
            imageListModel.addElement(s);
        }
        jList2.setModel(imageListModel);
    }
    private void createImageList()
    {
        String folder = selFolderEdit.getText();
        File dir = new File(folder); 
        FileFilter fileFilter = new WildcardFileFilter("*.PNG", IOCase.INSENSITIVE); // For taking both .JPG and .jpg files (useful in *nix env)
        File[] fileList = dir.listFiles(fileFilter); 
        if (fileList.length > 0) { 
            Arrays.sort(fileList, NameFileComparator.NAME_INSENSITIVE_COMPARATOR);
        } 
        allFiles = new ArrayList<>();
        files= new ArrayList<>();
        allFiles.clear();
        files.clear();
       
        for (File f:fileList)
        {
            if (!f.getName().startsWith("."))
            {
               allFiles.add(f.getName());
            }
            
        }
        files.addAll(allFiles);
        updateImageModel();
        
        
    }
    private void browsePngFolderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browsePngFolderBtnActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            selFolderEdit.setText(dir.getAbsolutePath());
            createImageList();
            searchImageEdit.setEditable(true);
            
        }
    }//GEN-LAST:event_browsePngFolderBtnActionPerformed

    private void clearImageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearImageBtnActionPerformed
        // TODO add your handling code here:
         if (allFiles != null) {
            searchImageEdit.setText("");
            filterImageModel(imageListModel, "");
        }
    }//GEN-LAST:event_clearImageBtnActionPerformed

    private void searchImageEditKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchImageEditKeyTyped
        // TODO add your handling code here:
        if (allFiles != null) {
            filterImageModel(imageListModel, searchImageEdit.getText());
        }
    }//GEN-LAST:event_searchImageEditKeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (visibleGame!=null)
        {
             int listIndex = jList2.getSelectedIndex();
                if (listIndex != -1) {
                    FileInputStream fs = null;
                 try {
                     String fileName = files.get(listIndex);
                     String path = selFolderEdit.getText()+File.separator+fileName;
                     fs = new FileInputStream(new File(path));
                   
                     ByteArrayOutputStream bos = new ByteArrayOutputStream();
                     IOUtils.copy(fs, bos);
                     bos.close();
                     visibleGame.setCoverData(bos.toByteArray());
                     visibleGame.setCover(ImageIO.read(new File(path)));
                     cdbp.updateGame(visibleGame);
                     gameCoverImage.loadImage(path);
                     updateGameData();
                     
                     
                     
                 } catch (FileNotFoundException ex) {
                     Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
                 } catch (IOException ex) {
                     Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
                 } finally {
                     try {
                         fs.close();
                     } catch (IOException ex) {
                         Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
                }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (allGames==null || !allGames.isEmpty())
        {
        cdbp.vacuum();
        JOptionPane.showMessageDialog(this, "DB size optimized");
        } else
        {
             JOptionPane.showMessageDialog(this, "Open DB first");
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addGameBtn;
    private javax.swing.JButton addSerialBtn;
    private javax.swing.JButton browsePngFolderBtn;
    private javax.swing.JButton clearImageBtn;
    private javax.swing.JButton delGameBtn;
    private javax.swing.JButton deleteSerialBtn;
    private io.github.autobleem.abcoveredit.ui.components.JImagePanel gameCoverImage;
    private javax.swing.JList<String> gameList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JButton loadDBBtn;
    private javax.swing.JTextField playersEdit;
    private io.github.autobleem.abcoveredit.ui.components.JImagePanel previewImage;
    private javax.swing.JTextField publisherEdit;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton searchGameClrBtn;
    private javax.swing.JTextField searchGameEdit;
    private javax.swing.JTextField searchImageEdit;
    private javax.swing.JTextField selFolderEdit;
    private javax.swing.JList<String> serialsList;
    private javax.swing.JTextField titleEdit;
    private javax.swing.JTextField versionNumber;
    private javax.swing.JTextField yearEdit;
    // End of variables declaration//GEN-END:variables
}
