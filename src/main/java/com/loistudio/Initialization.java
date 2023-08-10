package com.loistudio;

import org.json.JSONObject;

import com.loistudio.tools.EventEmitter;
import com.loistudio.tools.Logger;

import java.io.*;

public class Initialization {
    public static void init(Session client) {
        client.subscribe("PlayerMessage");
        client.subscribe("BlockPlaced");
        client.subscribe("BlockBroken");
        Session.event.on("onJSON", new EventEmitter.Listener() {
            public void execute(Object... args) {
                JSONObject json = new JSONObject(args[0].toString());
                JSONObject body = json.getJSONObject("body");
                JSONObject header = json.getJSONObject("header");
                if (header.has("eventName") && body.has("type")) {
                    if ("PlayerMessage".equals(header.getString("eventName")) && "chat".equals(body.getString("type"))) {
                        if (Session.log == "debug") { Logger.debug("PlayerMessage event is executed: " + args[0]); }
                        Session.event.emit("onMessageJSON", args[0]);
                        Session.event.emit("onMessage", body.getString("sender"), body.getString("message"));
                    } else if ("BlockPlaced".equals(header.getString("eventName"))) {
                        if (Session.log == "debug") { Logger.debug("BlockPlaced event is executed: " + args[0]); }
                        Session.event.emit("onBlockPlacedJSON", args[0]);
                        Session.event.emit("onBlockPlaced", new Player(body.getJSONObject("player"), client), new Block(body.getJSONObject("block")));
                    } else if ("BlockBroken".equals(header.getString("eventName"))) {
                        if (Session.log == "debug") { Logger.debug("BlockBroken event is executed: " + args[0]); }
                        Session.event.emit("onBlockBrokenJSON", args[0]);
                        Session.event.emit("onBlockBroken", new Player(body.getJSONObject("player"), client), new Block(body.getJSONObject("block")));
                    }
                }
            }
        });
    }
    
    public static void uninit(Session client) {
        client.unsubscribe("PlayerMessage");
        client.unsubscribe("BlockPlaced");
        client.unsubscribe("BlockBroken");
    }
}