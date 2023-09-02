package com.loistudio.game;

import com.loistudio.Session;

public class scoreboard {
    private Session client;

    scoreboard(Session client) {
        this.client = client;
    }
    
    public void add(String playerName, String name, int scores) {
        this.client.sendCommand("scoreboard players add " + playerName + " " + name + " " + scores);
    }
    
    public void remove(String playerName, String name, int scores) {
        this.client.sendCommand("scoreboard players remove " + playerName + " " + name + " " + scores);
    }
    
    public void set(String playerName, String name, int scores) {
        this.client.sendCommand("scoreboard players set " + playerName + " " + name + " " + scores);
    }
    
    public void reset(String playerName, String name) {
        this.client.sendCommand("scoreboard players reset " + playerName + " " + name);
    }
    
    public void random(String playerName, String name, int smallScores, int maxScores) {
        this.client.sendCommand("scoreboard players random " + playerName + " " + name + " " + smallScores + " " + maxScores);
    }
    
    public void create(String scores, String name) {
        this.client.sendCommand("scoreboard objectives add " + scores + " dummy " + name);
    }
    
    public void deleteCreate(String scores) {
        this.client.sendCommand("scoreboard objectives remove " + scores);
    }
}