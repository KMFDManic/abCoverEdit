/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.autobleem.abcoveredit.domain;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author artur.jakubowicz
 */
public class Game {
    private int id;
    private String title;
    private String publisher;
    private int year;
    private int players;
    private List<String> serials;
    private byte [] coverData;
    private BufferedImage cover;
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    

    public BufferedImage getCover() {
        return cover;
    }

    public void setCover(BufferedImage cover) {
        this.cover = cover;
    }
    
    
    public byte[] getCoverData() {
        return coverData;
    }

    public void setCoverData(byte[] coverData) {
        this.coverData = coverData;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the players
     */
    public int getPlayers() {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(int players) {
        this.players = players;
    }

    /**
     * @return the serials
     */
    public List<String> getSerials() {
        return serials;
    }

    /**
     * @param serials the serials to set
     */
    public void setSerials(List<String> serials) {
        this.serials = serials;
    }
}
