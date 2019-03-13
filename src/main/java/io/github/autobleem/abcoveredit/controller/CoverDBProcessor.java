/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.autobleem.abcoveredit.controller;

import io.github.autobleem.abcoveredit.domain.Game;
import io.github.autobleem.abcoveredit.domain.GameListEntry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author artur.jakubowicz
 */
public class CoverDBProcessor {
    private Connection db=null;
    private String dbFile=null;
    
    public CoverDBProcessor(String database) {
        dbFile = database;
    }
    
    
   
    private void openConnection(String dbFile)
    {
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
    private void closeConnection()
    {
        if (db!=null)
        {
            try {
                db.close();
            } catch (SQLException ex) {
                Logger.getLogger(CoverDBProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            db=null;
            dbFile=null;
        }
    }
    


    
    public Game getGameData(int id)
    {
        return new Game();
    }
    
    public List<GameListEntry> getGamesList()
    {
        try {      
            openConnection(dbFile);
            ArrayList<GameListEntry> entries=new ArrayList<>();
            String sql = "SELECT GAME.ID, GAME.TITLE FROM GAME";
            PreparedStatement ps = db.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next())
            {
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
