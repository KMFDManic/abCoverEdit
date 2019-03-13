/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.autobleem.abcoveredit.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author artur.jakubowicz
 */
public class CoverDBProcessor {
    private Connection db=null;
   
    private void openConnection(String dbFile)
    {
        
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
        }
    }
    
    
}
