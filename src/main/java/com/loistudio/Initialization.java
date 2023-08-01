package com.loistudio;

import org.json.JSONObject;
import com.loistudio.tools.EventEmitter;

import java.io.*;

public class Initialization {
    public static void init(Session client) {
        client.subscribe("PlayerMessage", null);
        client.subscribe("BlockPlaced", null);
        client.subscribe("BlockBroken", null);
        Session.event.on("onJSON", new EventEmitter.Listener() {
            public void execute(Object... args) {
                JSONObject json = new JSONObject(args[0].toString());
                JSONObject body = json.getJSONObject("body");
                JSONObject header = json.getJSONObject("header");
                if (header.has("eventName") && body.has("type")) {
                    if ("PlayerMessage".equals(header.getString("eventName")) && "chat".equals(body.getString("type"))) {
                        Session.event.emit("onMessageJSON", args[0]);
                        Session.event.emit("onMessage", body.getString("sender"), body.getString("message"));
                    } else if ("BlockPlaced".equals(header.getString("eventName"))) {
                        Session.event.emit("onBlockPlacedJSON", args[0]);
                        Session.event.emit("onBlockPlaced", new Player(body.getJSONObject("player")), new Block(body.getJSONObject("block")));
                    } else if ("BlockBroken".equals(header.getString("eventName"))) {
                        Session.event.emit("onBlockBrokenJSON", args[0]);
                        Session.event.emit("onBlockBroken", new Player(body.getJSONObject("player")), new Block(body.getJSONObject("block")));
                    }
                }
            }
        });
    }
    
    public static void uninit(Session client) {
        client.unsubscribe("PlayerMessage", null);
        client.unsubscribe("BlockPlaced", null);
        client.unsubscribe("BlockBroken", null);
    }
}