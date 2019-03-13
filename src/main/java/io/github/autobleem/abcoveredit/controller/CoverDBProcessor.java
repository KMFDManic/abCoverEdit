/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.autobleem.abcoveredit.controller;

import io.github.autobleem.abcoveredit.domain.Game;
import io.github.autobleem.abcoveredit.domain.GameListEntry;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author artur.jakubowicz
 */
public class CoverDBProcessor {

    private Connection db = null;
    private String dbFile = null;

    public CoverDBProcessor(String database) {
        dbFile = database;
    }

    private void openConnection(String dbFile) {
        try {
            Class.forName("org.sqlite.JDBC"); // sqlite driver load
            String connectionStr = "jdbc:sqlite:" + dbFile;
            db = DriverManager.getConnection(connectionStr);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void closeConnection() {
        if (db != null) {
            try {
                db.close();
            } catch (SQLException ex) {
                Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            db = null;
       
        }
    }

    public Game getGameData(int id) {
           Game result = new Game();
        try {
           openConnection(dbFile);
            String sql = "SELECT ID, TITLE, PUBLISHER, RELEASE_YEAR, PLAYERS, COVER FROM GAME WHERE ID=?";
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.setTitle(rs.getString("TITLE"));
                result.setPublisher(rs.getString("PUBLISHER"));
                result.setYear(rs.getInt("YEAR"));
                result.setPlayers(rs.getInt("PLAYERS"));

                InputStream input = rs.getBinaryStream("COVER");
                if (input != null) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    IOUtils.copy(input, os);
                    input.close();
                    os.close();
                    result.setCoverData(os.toByteArray());
                    ByteArrayInputStream bis = new ByteArrayInputStream(result.getCoverData());
                    BufferedImage img = ImageIO.read(bis);
                    bis.close();
                    result.setCover(img);
                } else
                {
                    
                }
               
                closeConnection();
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    public List<GameListEntry> getGamesList() {
        try {
            openConnection(dbFile);
            ArrayList<GameListEntry> entries = new ArrayList<>();
            String sql = "SELECT GAME.ID, GAME.TITLE FROM GAME";
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entries.add(new GameListEntry(rs.getInt("ID"), rs.getString("TITLE")));
            }
            rs.close();
            ps.close();
            closeConnection();
            return entries;
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }

}
