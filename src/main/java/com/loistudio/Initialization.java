package com.loistudio;

import org.json.JSONObject;

import com.loistudio.tools.EventEmitter;
import com.loistudio.tools.Logger;

<<<<<<< HEAD
import java.util.Objects;
=======
import java.io.*;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3

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
                if (header.has("eventName") && body.has("type") && header.getString("messagePurpose") == "event") {
                    if ("PlayerMessage".equals(header.getString("eventName")) && "chat".equals(body.getString("type"))) {
<<<<<<< HEAD
                        if (Objects.equals(Session.log, "debug")) { Logger.debug("PlayerMessage event is executed: " + args[0]); }
                        Session.event.emit("onMessageJSON", (String) args[0]);
                        Session.event.emit("onMessage", body.getString("sender"), body.getString("message"));
                    } else if ("BlockPlaced".equals(header.getString("eventName"))) {
                        if (Objects.equals(Session.log, "debug")) { Logger.debug("BlockPlaced event is executed: " + args[0]); }
                        Session.event.emit("onBlockPlacedJSON", (String) args[0]);
                        Session.event.emit("onBlockPlaced", new Player(body.getJSONObject("player"), client), new Block(body.getJSONObject("block")));
                    } else if ("BlockBroken".equals(header.getString("eventName"))) {
                        if (Objects.equals(Session.log, "debug")) { Logger.debug("BlockBroken event is executed: " + args[0]); }
=======
                        if (Session.log == "debug") { Logger.debug("PlayerMessage event is executed: " + args[0]); }
                        Session.event.emit("onMessageJSON", (String) args[0]);
                        Session.event.emit("onMessage", body.getString("sender"), body.getString("message"));
                    } else if ("BlockPlaced".equals(header.getString("eventName"))) {
                        if (Session.log == "debug") { Logger.debug("BlockPlaced event is executed: " + args[0]); }
                        Session.event.emit("onBlockPlacedJSON", (String) args[0]);
                        Session.event.emit("onBlockPlaced", new Player(body.getJSONObject("player"), client), new Block(body.getJSONObject("block")));
                    } else if ("BlockBroken".equals(header.getString("eventName"))) {
                        if (Session.log == "debug") { Logger.debug("BlockBroken event is executed: " + args[0]); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
                        Session.event.emit("onBlockBrokenJSON", (String) args[0]);
                        Session.event.emit("onBlockBroken", new Player(body.getJSONObject("player"), client), new Block(body.getJSONObject("block")));
                    }
                } else if ("commandResponse".equals(header.getString("messagePurpose"))) {
<<<<<<< HEAD
                    if (Objects.equals(Session.log, "debug")) { Logger.debug("Command callback event trigger: " + args[0]); }
=======
                    if (Session.log == "debug") { Logger.debug("Command callback event trigger: " + args[0]); }
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
                    Session.Formation.remove(header.getString("requestId"));
                    Session.CommandCallback callback = Session.responsers.get(header.getString("requestId"));
                    callback.onResponse((String) args[0]);
                    Session.responsers.remove(header.getString("requestId"));
                    Session.event.emit("onCommandResponseJSON", (String) args[0]);
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