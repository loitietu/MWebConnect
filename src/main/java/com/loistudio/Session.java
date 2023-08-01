package com.loistudio;

import org.java_websocket.WebSocket;
import org.json.JSONObject;
import org.json.JSONArray;

import com.loistudio.tools.EventEmitter;

import java.util.UUID;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;
import java.lang.Runtime;
import java.io.*;

public class Session {
    private WebSocket client;
    Map<String,CommandCallback> responsers = new HashMap<>();
    Map<String, Set<CommandCallback>> eventListeners = new HashMap<>();
    static Map<String,String> Formation = new HashMap<>();
    
    public static EventEmitter event = new EventEmitter();
    
    Session(WebSocket client) {
        this.client = client;
    }
    
    public void send(String msg) {
        this.client.send(msg);
    }
    
    public void close() {
        this.client.close();
    }
    
    public void subscribe(String event, CommandCallback callback) {
        Set<CommandCallback> listeners = this.eventListeners.get(event);
        if (listeners == null) {
            Map<String, Object> json = new HashMap<>();
            Map<String, Object> body = new HashMap<>();
            listeners = new HashSet<>();
            this.eventListeners.put(event, listeners);
            json.put("header", this.buildHeader("subscribe"));
            body.put("eventName", event);
            json.put("body", body);
            String jsonStr = new JSONObject(json).toString();
            this.send(jsonStr);
        }
        listeners.add(callback);
    }
    
    public void unsubscribe(String event, CommandCallback callback) {
        Set<CommandCallback> listeners = this.eventListeners.get(event);     
        if (listeners == null) return;
        listeners.remove(callback);
        if (listeners.isEmpty()) {
            this.eventListeners.remove(event);
            Map<String, Object> json = new HashMap<>();
            Map<String, Object> body = new HashMap<>();
            json.put("header", this.buildHeader("unsubscribe"));
            body.put("eventName", event);
            json.put("body", body);
            String jsonStr = new JSONObject(json).toString();
            this.send(jsonStr);
        }
    }

    public String sendCommand(String command, CommandCallback callback) {
        Map<String, Object> json = new HashMap<>();
        Map<String, Object> header = this.buildHeader("commandRequest");
        String requestId = (String) header.get("requestId");
        json.put("header", header);
        Map<String, Object> body = new HashMap<>();  
        body.put("version", 1);
        body.put("commandLine", command);       
        json.put("body", body);  
        this.responsers.put(requestId, callback);
        String jsonStr = new JSONObject(json).toString();  
        this.send(jsonStr);
        this.Formation.put(requestId, command);
        return requestId;
    }
    
    public interface CommandCallback {
        public void onResponse(String response);
    }
    
    public Response sendCommandSync(String command) throws Exception {
        Map<String, Object> json = new HashMap<>();
        Map<String, Object> header = this.buildHeader("commandRequest");
        String requestId = (String) header.get("requestId");
        json.put("header", header);
        Map<String, Object> body = new HashMap<>();  
        body.put("version", 1);
        body.put("commandLine", command);       
        json.put("body", body); 
        Response response = new Response();
        CountDownLatch latch = new CountDownLatch(1);
        this.responsers.put(requestId, response);  
        String jsonStr = new JSONObject(json).toString();  
        this.send(jsonStr);
        latch.await(); 
        this.Formation.remove(requestId);
        return response;
    }
    
    class Response implements CommandCallback {
        String data;
        @Override
        public void onResponse(String response) {
            this.data = response;
        }
        synchronized void setResult(String data) {  
            this.data = data;    
        }
    }
    
    public String now() {
        LocalDateTime currentTime = LocalDateTime.now();
        String timestamp = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "[" + timestamp + "]";
    }
    
    public void tellraw(String text, String color) {
        String command = "tellraw @s " + new JSONObject().put("rawtext", new JSONArray().put(new JSONObject().put("text", color + this.now() + text))).toString();
        this.sendCommand(command, null);
    }
    
    public void tellraw(String text) {
        String command = "tellraw @s " + new JSONObject().put("rawtext", new JSONArray().put(new JSONObject().put("text", "§e" + this.now() + text))).toString();
        this.sendCommand(command, null);
    }
    
    public String shell(String cmd) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while((line = br.readLine()) != null) {
                output.append(line).append("\n"); 
            } 
            String result = output.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void sendText(String txt) {
        this.sendCommand("say " + txt, null);
    }
    
    public void write(String txt) {
        this.sendCommand("title @s actionbar " + txt, null);
    }
        
    Map<String, Object> buildHeader(String purpose) {
        Map<String, Object> header = new HashMap<>();
        header.put("version", 1);
        header.put("requestId", UUID.randomUUID().toString()); 
        header.put("messagePurpose", purpose);
        header.put("messageType", "commandRequest");
        return header;  
    }
}