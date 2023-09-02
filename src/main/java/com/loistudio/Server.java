<<<<<<< HEAD
// ___       ________  ___
// |\  \     |\   __  \|\  \
// \ \  \    \ \  \|\  \ \  \
//  \ \  \    \ \  \\\  \ \  \
//   \ \  \____\ \  \\\  \ \  \
//    \ \_______\ \_______\ \__\
//     \|_______| \ |_______|\ |__|
=======
// ___       ________  ___         
// |\  \     |\   __  \|\  \        
// \ \  \    \ \  \|\  \ \  \       
//  \ \  \    \ \  \\\  \ \  \      
//   \ \  \____\ \  \\\  \ \  \     
//    \ \_______\ \_______\ \__\    
//     \|_______| \ |_______|\ |__|    
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
//-------------------MWebConnect-------------------        

package com.loistudio;

<<<<<<< HEAD
import java.net.InetSocketAddress;
import java.util.Objects;
=======
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.lang.management.*;

import org.java_websocket.WebSocket;
<<<<<<< HEAD
=======
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import org.json.JSONObject;

import com.loistudio.tools.Logger;
<<<<<<< HEAD
=======
import com.loistudio.tools.EventEmitter;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3

public abstract class Server {
    private WebSocketServer server;
    private String host = "127.0.0.1";
    private int port = 8080;
<<<<<<< HEAD
    private final Map<String, Session> clientList = new HashMap<>();
=======
    private Map<String, Session> clientList = new HashMap<>();
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
    private String model = "Release";
    
    public static final int LOGGER_RELEASE = 0;
    public static final int LOGGER_DEBUG = 1;
    
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
                clientList.put(getIp(conn), new Session(conn));
                Logger.info(getIp(conn) + " Client connection");
                new Thread(() -> {
                    connect(new Session(conn));
                }).start();
            }
    
            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                Initialization.uninit(new Session(conn));
                Logger.info(getIp(conn) + " The client was disconnected.");
                removeSession(getIp(conn));
                new Thread(() -> {
                    close(new Session(conn));
                });
            }
    
            @Override
            public void onMessage(WebSocket conn, String message) {
<<<<<<< HEAD
                if (Objects.equals(Session.log, "debug")) { Logger.debug("Client " + getIp(conn) + " sends a message: " + message); }
=======
                if (Session.log == "debug") { Logger.debug("Client " + getIp(conn) + " sends a message: " + message); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
                Session.event.emit("onJSON", message);
                new Thread(() -> {
                    message(new Session(conn), message);
                });
            }
    
            @Override
            public void onError(WebSocket conn, Exception ex) {
<<<<<<< HEAD
                Logger.error(ex.toString());
=======
                ex.printStackTrace();
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            }
    
            @Override
            public void onStart() {
                server.setConnectionLostTimeout(3);
                Logger.info("Server has been created.");
                create();
            }
        };
        server.setTcpNoDelay(true);
    }
    
    public void run() {
<<<<<<< HEAD
        if (Objects.equals(this.model, "Debug")) { Session.log = "debug"; }
=======
        if (this.model == "Debug") { Session.log = "debug"; }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
        Logger.info("WebSocketServer mode: " + this.model);
        Logger.info("WebSocketServer started on " + host + ":" + port);
        server.start();
    }

    public void close() throws Exception {
        Logger.info("Server shutdown");
        server.stop();
    }
    
    public void setModel(int model) {
        if (model == 0) { this.model = "Release"; }
        else { this.model = "Debug"; }
    }
    
    public void sendBroadClientCommand(String command) {
<<<<<<< HEAD
        if (Objects.equals(Session.log, "debug")) { Logger.debug("Command sent to all connected clients: " + command); }
=======
        if (Session.log == "debug") { Logger.debug("Command sent to all connected clients: " + command); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
        Set<Map.Entry<String, Session>> entries = clientList.entrySet();
        for (Map.Entry<String, Session> entry : entries) {
            Session session = entry.getValue();
            session.sendCommand(command);
        }
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
        return json.toString();
    }
}