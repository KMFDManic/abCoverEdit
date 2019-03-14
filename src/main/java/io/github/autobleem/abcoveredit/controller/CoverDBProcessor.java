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
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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

    private void addVersionColumn() {

        try {
            String sql = "ALTER TABLE GAME ADD COLUMN VERSION INT NOT NULL DEFAULT 1";
            PreparedStatement ps = db.prepareStatement(sql);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Game getGameData(int id) {
        Game result = new Game();
        try {
            openConnection(dbFile);
            String sql = "SELECT ID, TITLE, PUBLISHER, RELEASE, PLAYERS, COVER, VERSION FROM GAME WHERE ID=?";
            String sql2 = "SELECT SERIAL FROM SERIALS WHERE GAME=?";
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result.setId(rs.getInt("ID"));
                result.setTitle(rs.getString("TITLE"));
                result.setPublisher(rs.getString("PUBLISHER"));
                result.setYear(rs.getInt("RELEASE"));
                result.setPlayers(rs.getInt("PLAYERS"));
                result.setVersion(rs.getInt("VERSION"));

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
                } else {
                    BufferedImage img = new BufferedImage(226, 226, BufferedImage.TYPE_INT_ARGB);
                    result.setCover(img);
                }

            }
            rs.close();
            ps.close();

            ps = db.prepareStatement(sql2);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            result.setSerials(new ArrayList<>());
            while (rs.next()) {
                result.getSerials().add(rs.getString("SERIAL"));
            }
            rs.close();
            ps.close();

            closeConnection();
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public void addSerial(int id, String serial) {
        try {
            serial = serial.trim();
            openConnection(dbFile);
            String sql = "INSERT INTO SERIALS (SERIAL,GAME) VALUES (?,?)";
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, serial);
            ps.setInt(2, id);
            ps.execute();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Game getSerials(Game game) {
        try {
            openConnection(dbFile);
            String sql = "SELECT SERIAL FROM SERIALS WHERE GAME=?";
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, game.getId());
            ResultSet rs = ps.executeQuery();
            game.setSerials(new ArrayList<>());
            while (rs.next()) {
                game.getSerials().add(rs.getString("SERIAL"));
            }
            rs.close();
            ps.close();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return game;
    }

    public void removeGame(int id) {
        try {
            openConnection(dbFile);
            String sql = "DELETE FROM SERIALS WHERE GAME=?";
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            sql = "DELETE FROM GAME WHERE ID=?";
            ps = db.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            ps.close();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void vacuum() {
        try {
            openConnection(dbFile);
            Statement stmt = db.createStatement();
            stmt.executeUpdate("VACUUM");
            closeConnection();

        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateGame(Game game) {
        String sql = "UPDATE GAME SET TITLE=?, PUBLISHER=?, RELEASE=?, PLAYERS=?, COVER=?, VERSION=VERSION+1 "
                + " WHERE ID = ?";

        try {
            openConnection(dbFile);

            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getPublisher());
            ps.setInt(3, game.getYear());
            ps.setInt(4, game.getPlayers());
            ps.setBytes(5, game.getCoverData());
            ps.setInt(6, game.getId());

            ps.execute();
            ps.close();

            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int createGame(String name) {
        name = name.trim();
        try {
            openConnection(dbFile);
            String sql = "INSERT INTO GAME (TITLE, PUBLISHER, RELEASE, PLAYERS, COVER, VERSION) "
                    + "VALUES (?, 'Other',1990, 1, null, 1);";

            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, name);
            ps.execute();
            ps.close();
            int newId = -1;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
            }
            rs.close();
            ps.close();
            closeConnection();
            return newId;
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    public void removeSerial(int id, String serial) {
        try {
            openConnection(dbFile);
            String sql = "DELETE FROM SERIALS WHERE SERIAL=? AND GAME=?";
            PreparedStatement ps = db.prepareStatement(sql);
            ps.setString(1, serial);
            ps.setInt(2, id);
            ps.execute();
            ps.close();
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<GameListEntry> getGamesList() {
        try {
            openConnection(dbFile);
            addVersionColumn();
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
            Collections.sort(entries, new Comparator<GameListEntry>() {
                @Override
                public int compare(GameListEntry o1, GameListEntry o2) {
                    return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
                }

            });
            return entries;
        } catch (SQLException ex) {
            Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList<>();
    }

}
