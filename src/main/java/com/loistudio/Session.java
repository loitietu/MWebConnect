package com.loistudio;

import org.java_websocket.WebSocket;
import org.json.JSONObject;
import org.json.JSONArray;

import com.loistudio.tools.EventEmitter;
import com.loistudio.tools.Logger;
import com.loistudio.file.FolderExample;

import java.util.*;
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
    private final WebSocket client;
    public static Map<String,CommandCallback> responsers = new HashMap<>();
    Map<String, Set<CommandCallback>> eventListeners = new HashMap<>();
    public static Map<String,String> Formation = new HashMap<>();
    public static EventEmitter event = new EventEmitter();
    public static String log = "release";
    
    Session(WebSocket client) {
        this.client = client;
    }
    
    public String getIp() {
        return client.getRemoteSocketAddress().getAddress().getHostAddress();
    }
    
    public void send(String msg) {
        this.client.send(msg);
    }
    
    public void close() {
        this.client.close();
    }
    
    public void subscribe(String event, CommandCallback callback) {
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Listen for events: " + event); }
        callback.onResponse(event);
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
    
    public void subscribe(String event) {
        this.subscribe(event, new CommandCallback() {
            @Override
            public void onResponse(String response) {};
        });
    }
    
    public void unsubscribe(String event, CommandCallback callback) {
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " To cancel listening for events: " + event); }
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
        callback.onResponse(event);
    }
    
    public void unsubscribe(String event) {
        this.unsubscribe(event, new CommandCallback() {
            @Override
            public void onResponse(String response) {};
        });
    }

    public String sendCommand(String command, CommandCallback callback) {
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Send Command Request: " + command); }
        Map<String, Object> json = new HashMap<>();
        Map<String, Object> header = this.buildHeader("commandRequest");
        String requestId = (String) header.get("requestId");
        json.put("header", header);
        Map<String, Object> body = new HashMap<>();  
        body.put("version", 1);
        body.put("commandLine", command);       
        json.put("body", body);
        responsers.put(requestId, callback);
        String jsonStr = new JSONObject(json).toString();
        this.send(jsonStr);
        Formation.put(requestId, command);
        return requestId;
    }
    
    public String sendCommandSync(String command) {
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Send Command Sync Request: " + command); }
        final String[] responser = new String[1];
        final CountDownLatch latch = new CountDownLatch(1);
        this.sendCommand(command, new CommandCallback() {
            @Override
            public void onResponse(String response) {
                responser[0] = response;
                latch.countDown();
            }
        });
        try {
            latch.await();
            return responser[0];
        } catch (InterruptedException e) {
            Logger.error(e.toString());
            return null;
        }
    }
    
    public String sendCommand(String command) {
        return this.sendCommand(command, new CommandCallback() {
            @Override
            public void onResponse(String response) {};
        });
    }
    
    @FunctionalInterface
    public interface CommandCallback {
        public void onResponse(String response);
    }
    
    public void runFunction(String path) {
        File file = new File(path);
        String name = file.getName().substring(0, file.getName().indexOf("."));
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Executive function: " + name); }
        try {
            String function = FolderExample.readFile(path);
            int commandLine = 0;
            long startTime = System.currentTimeMillis();
            String[] lines = new String[0];
            if (function != null) {
                lines = function.split("\n");
            }
            for (String line : lines) {
                this.sendCommand(line);
                commandLine++;
                int elapsed = (int) (System.currentTimeMillis() - startTime);
                int totalMemory = (int) (Runtime.getRuntime().totalMemory() / 1024 / 1024);
                int freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);
                int usedMemory = totalMemory - freeMemory;
                int memPercent = (int) ((double) usedMemory / totalMemory * 100);
                int progress = commandLine * 100 / lines.length;
                int speed = commandLine * 1000 / elapsed;
                int remaining = (lines.length - commandLine) * 1000 / speed;
                this.sendCommand("titleraw @s actionbar {\"rawtext\":[{\"text\":\"§b已执行命令: §r" + commandLine + " §a/§r " + lines.length + " (" + progress + "%)\\n§d平均执行速度: §r" + speed + "§a条/秒\\n§8预计剩余时间: §r" + remaining + "§a秒\\n§3内存使用率: §r" + usedMemory + "/" + totalMemory + " §3MB §r(" + memPercent + "%)§r\\n" + line + "\"}]}");
            }
            long totalTime = System.currentTimeMillis() - startTime;
            int elapsedSeconds = (int) Math.ceil((double) totalTime / 1000);
            if (elapsedSeconds == 0) { elapsedSeconds = 1; }
            int speeds = (int) commandLine / elapsedSeconds;
            this.tellraw("§r函数 " + name + " 已完成,共用时" + elapsedSeconds + "秒\n已执行" + commandLine +"个命令,平均速度" + speeds + "条/秒");
        } catch (Exception e) {
            Logger.error(e.toString());
        }
    }
    
    public String now() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    public void tellraw(String text, String color) {
        String command = "tellraw @s " + new JSONObject().put("rawtext", new JSONArray().put(new JSONObject().put("text", color + "[" + this.now() + "]" + text))).toString();
        this.sendCommand(command);
    }
    
    public void tellraw(String text) {
        String command = "tellraw @s " + new JSONObject().put("rawtext", new JSONArray().put(new JSONObject().put("text", "§e" + "[" + this.now() + "]" + text))).toString();
        this.sendCommand(command);
    }
    
    public String shell(String cmd) {
        if (Objects.equals(log, "debug")) { Logger.debug("Execute the shell command: " + cmd); }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while((line = br.readLine()) != null) {
                output.append(line).append("\n"); 
            }
            return output.toString();
        } catch (Exception e) {
            Logger.error(e.toString());
            return null;
        }
    }
    
    public void sendText(String txt) {
        this.sendCommand("say " + txt);
    }
    
    public void write(String txt) {
        this.sendCommand("title @s actionbar " + txt);
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