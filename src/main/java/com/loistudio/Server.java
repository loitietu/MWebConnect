// ___       ________  ___         
// |\  \     |\   __  \|\  \        
// \ \  \    \ \  \|\  \ \  \       
//  \ \  \    \ \  \\\  \ \  \      
//   \ \  \____\ \  \\\  \ \  \     
//    \ \_______\ \_______\ \__\    
//     \|_______| \ |_______|\ |__|    
//-------------------MWebConnect-------------------        

package com.loistudio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.lang.management.*;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import org.json.JSONObject;

import com.loistudio.tools.Logger;
import com.loistudio.tools.EventEmitter;

public abstract class Server {
    private WebSocketServer server;
    private String host = "127.0.0.1";
    private int port = 8080;
    private Map<String, Session> clientList = new HashMap<>();
    
    public abstract void connect(Session client);
    public abstract void close(Session client);
    public abstract void message(Session client, String msg);
    public abstract void create();
    
    public void newServer(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;
        server = new WebSocketServer(new InetSocketAddress(host, port)) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                Initialization.init(new Session(conn));
                connect(new Session(conn));
                clientList.put(getIp(conn), new Session(conn));
                Logger.info(getIp(conn) + " Client connection");
            }
    
            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                Initialization.uninit(new Session(conn));
                close(new Session(conn));
                Logger.info(getIp(conn) + " The client was disconnected.");
                removeSession(getIp(conn));
            }
    
            @Override
            public void onMessage(WebSocket conn, String message) {
                message(new Session(conn), message);
                Session.event.emit("onJSON", message);
            }
    
            @Override
            public void onError(WebSocket conn, Exception ex) {
                ex.printStackTrace();
            }
    
            @Override
            public void onStart() {
                Logger.info("Server has been created.");
                create();
            }
        };
    }
    
    public void run() {
        Logger.info("WebSocketServer started on " + host + ":" + port);
        server.start();
    }

    public void close() throws Exception {
        server.stop();
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public Session getSession(String ip) {
        return clientList.get(ip);
    }
    
    public void removeSession(String ip) {
        clientList.remove(ip);
    }
    
    public String getIp(WebSocket conn) {
        return conn.getRemoteSocketAddress().getAddress().getHostAddress();
    }
    
    public String status() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        JSONObject json = new JSONObject();
        JSONObject java = new JSONObject();
        JSONObject jvm = new JSONObject();
        JSONObject cpu = new JSONObject();
        JSONObject memory = new JSONObject();
        json.put("name", System.getProperty("os.name"));
        json.put("version", System.getProperty("os.version"));
        java.put("compiler", System.getProperty("java.compiler"));
        java.put("version", System.getProperty("java.version"));
        jvm.put("version", System.getProperty("java.vm.version"));
        java.put("jvm", jvm);
        cpu.put("core", Runtime.getRuntime().availableProcessors());
        cpu.put("arch", System.getProperty("os.arch"));
        cpu.put("load", osBean.getSystemLoadAverage());
        memory.put("total", Runtime.getRuntime().totalMemory());
        memory.put("free", Runtime.getRuntime().freeMemory());
        memory.put("max", Runtime.getRuntime().maxMemory());
        json.put("memory", memory);
        json.put("java", java);
        json.put("cpu", cpu);
        return json.toString(4);
    }
}