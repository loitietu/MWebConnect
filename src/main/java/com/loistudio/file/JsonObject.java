package com.loistudio.file;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class JsonObject {
    private String path;
    private JSONObject json;

    public JsonObject() {
        this.json = new JSONObject();
        this.path = "";
    }

    public void open(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(path);
                fileWriter.write("{}");
                fileWriter.close();
                this.path = path;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String json = new String(Files.readAllBytes(Paths.get(path)));
                this.json = new JSONObject(json);
                this.path = path;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void set(String key, Object value) {
        String path = this.path;
        this.json.put(key, value);
        String json = this.json.toString(4);
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(json);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get(String key) {
        try {
            return this.json.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(String key) {
        String path = this.path;
        this.json.remove(key);
        String json = this.json.toString(4);
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(json);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> toMap() {
        return this.json.toMap();
    }
}