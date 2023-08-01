package com.loistudio;

import org.json.JSONObject;

public class Block {
    private int aux;
    private String id;
    private String namespace;

    Block(JSONObject json) {
        this.aux = json.getInt("aux");
        this.id = json.getString("id");
        this.namespace = json.getString("namespace");
    }
    
    public int getAux() {
        return this.aux;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getNameSpace() {
        return this.namespace;
    }
}