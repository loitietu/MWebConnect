package com.loistudio;

import org.java_websocket.WebSocket;
import org.json.JSONObject;
import org.json.JSONArray;

import com.loistudio.tools.EventEmitter;
import com.loistudio.file.FolderExample;
import com.loistudio.tools.Logger;

<<<<<<< HEAD
import java.util.*;
=======
import java.util.UUID;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
import java.util.concurrent.CountDownLatch;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;
import java.lang.Runtime;
import java.io.*;

public class Session {
<<<<<<< HEAD
    private final WebSocket client;
=======
    private WebSocket client;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Listen for events: " + event); }
=======
        if (this.log == "debug") { Logger.debug("Client " + this.getIp() + " Listen for events: " + event); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " To cancel listening for events: " + event); }
=======
        if (this.log == "debug") { Logger.debug("Client " + this.getIp() + " To cancel listening for events: " + event); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Send Command Request: " + command); }
=======
        if (this.log == "debug") { Logger.debug("Client " + this.getIp() + " Send Command Request: " + command); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
        Map<String, Object> json = new HashMap<>();
        Map<String, Object> header = this.buildHeader("commandRequest");
        String requestId = (String) header.get("requestId");
        json.put("header", header);
        Map<String, Object> body = new HashMap<>();  
        body.put("version", 1);
        body.put("commandLine", command);       
        json.put("body", body);  
<<<<<<< HEAD
        responsers.put(requestId, callback);
        String jsonStr = new JSONObject(json).toString();
        this.send(jsonStr);
        Formation.put(requestId, command);
=======
        this.responsers.put(requestId, callback);
        String jsonStr = new JSONObject(json).toString();
        this.send(jsonStr);
        this.Formation.put(requestId, command);
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
        return requestId;
    }
    
    public String sendCommandSync(String command) {
<<<<<<< HEAD
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Send Command Sync Request: " + command); }
=======
        if (this.log == "debug") { Logger.debug("Client " + this.getIp() + " Send Command Sync Request: " + command); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
            Logger.error(e.toString());
=======
            e.printStackTrace();
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            return null;
        }
    }
    
    public String sendCommand(String command) {
<<<<<<< HEAD
        return this.sendCommand(command, new CommandCallback() {
            @Override
            public void onResponse(String response) {};
        });
=======
        String id = this.sendCommand(command, new CommandCallback() {
            @Override
            public void onResponse(String response) {};
        });
        return id;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
    }
    
    @FunctionalInterface
    public interface CommandCallback {
        public void onResponse(String response);
    }
    
    public void runFunction(String path) {
        File file = new File(path);
        String name = file.getName().substring(0, file.getName().indexOf("."));
<<<<<<< HEAD
        if (Objects.equals(log, "debug")) { Logger.debug("Client " + this.getIp() + " Executive function: " + name); }
=======
        if (this.log == "debug") { Logger.debug("Client " + this.getIp() + " Executive function: " + name); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
        try {
            String function = FolderExample.readFile(path);
            int commandLine = 0;
            long startTime = System.currentTimeMillis();
<<<<<<< HEAD
            String[] lines = new String[0];
            if (function != null) {
                lines = function.split("\n");
            }
            for (String line : lines) {
                this.sendCommand(line);
=======
            String[] lines = function.split("\n");  
            for (int i = 0; i < lines.length; i++) {
                this.sendCommand(lines[i]);
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
                commandLine++;
                int elapsed = (int) (System.currentTimeMillis() - startTime);
                int totalMemory = (int) (Runtime.getRuntime().totalMemory() / 1024 / 1024);
                int freeMemory = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);
<<<<<<< HEAD
                int usedMemory = totalMemory - freeMemory;
                int memPercent = (int) ((double) usedMemory / totalMemory * 100);
                int progress = commandLine * 100 / lines.length;
                int speed = commandLine * 1000 / elapsed;
                int remaining = (lines.length - commandLine) * 1000 / speed;
                this.sendCommand("titleraw @s actionbar {\"rawtext\":[{\"text\":\"§b已执行命令: §r" + commandLine + " §a/§r " + lines.length + " (" + progress + "%)\\n§d平均执行速度: §r" + speed + "§a条/秒\\n§8预计剩余时间: §r" + remaining + "§a秒\\n§3内存使用率: §r" + usedMemory + "/" + totalMemory + " §3MB §r(" + memPercent + "%)§r\\n" + line + "\"}]}");
            }
            long totalTime = System.currentTimeMillis() - startTime;
            int elapsedSeconds = (int) Math.ceil((double) totalTime / 1000);
=======
                int usedMemory = totalMemory - freeMemory; 
                int memPercent = (int) ((double)usedMemory / totalMemory * 100);
                int progress = commandLine * 100 / lines.length;
                int speed = commandLine * 1000 / elapsed;
                int remaining = (lines.length - commandLine) * 1000 / speed; 
                this.sendCommand("titleraw @s actionbar {\"rawtext\":[{\"text\":\"§b已执行命令: §r" + commandLine + " §a/§r " + lines.length + " (" + progress + "%)\\n§d平均执行速度: §r" + speed + "§a条/秒\\n§8预计剩余时间: §r" + remaining + "§a秒\\n§3内存使用率: §r" + usedMemory + "/" + totalMemory + " §3MB §r(" + memPercent + "%)§r\\n" + lines[i] + "\"}]}");
            }
            long totalTime = System.currentTimeMillis() - startTime;
            int elapsedSeconds = (int) Math.ceil(totalTime / 1000);
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            if (elapsedSeconds == 0) { elapsedSeconds = 1; }
            int speeds = (int) commandLine / elapsedSeconds;
            this.tellraw("§r函数 " + name + " 已完成,共用时" + elapsedSeconds + "秒\n已执行" + commandLine +"个命令,平均速度" + speeds + "条/秒");
        } catch (Exception e) {
<<<<<<< HEAD
            Logger.error(e.toString());
=======
            e.printStackTrace();
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
        }
    }
    
    public String now() {
        LocalDateTime currentTime = LocalDateTime.now();
<<<<<<< HEAD
        return currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
=======
        String timestamp = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return timestamp;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (Objects.equals(log, "debug")) { Logger.debug("Execute the shell command: " + cmd); }
=======
        if (this.log == "debug") { Logger.debug("Execute the shell command: " + cmd); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while((line = br.readLine()) != null) {
                output.append(line).append("\n"); 
            } 
<<<<<<< HEAD
            return output.toString();
        } catch (Exception e) {
            Logger.error(e.toString());
=======
            String result = output.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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