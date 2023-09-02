package com.loistudio;

import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

public class Player {
    private String color;
    private int dimension;
    private int id;
    private String name;
    private Map<String, Object> position = new HashMap<>();
    private Session client;

    Player(JSONObject json, Session client) {
        this.color = json.getString("color");
        this.dimension = json.getInt("dimension");
        this.id = json.getInt("id");
        this.name = json.getString("name");
        this.position = this.getNestedFieldsMap(json, "position");
        this.client = client;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public int getDimension() {
        return this.dimension;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Map<String, Object> getPosition() {
        return this.position;
    }
    
    public void exec(String command) {
        this.client.sendCommand("execute " + this.name + " ~ ~ ~ " + command);
    }
    
    public void sendMessage(String txt) {
        this.exec("tellraw @s {\"rawtext\":[{\"text\":\"" + txt + "\"}]}");
    }
    
    private Map<String, Object> getNestedFieldsMap(JSONObject jsonObject, String... fields) {
       Map<String, Object> map = new HashMap<>();
       for (String field : fields) {
           Object value = jsonObject.get(field);
           if (value instanceof JSONObject) {
               map.putAll(getNestedFieldsMap((JSONObject) value, field));
           }
       }
       return map;    
    }
}