package com.loistudio.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventEmitter {

    private Map<String, List<Listener>> listeners = new HashMap<>();

    public interface Listener {
        public void execute(Object... args);
    }
    
    public void on(String event, Listener listener) {
        List<Listener> userListeners = listeners.get(event);
        if (userListeners == null) {
            userListeners = new ArrayList<>();
            listeners.put(event, userListeners);
        }
        userListeners.add(listener);
    }

    public void emit(String event, Object... args) {
        List<Listener> userListeners = listeners.get(event);
        if (userListeners != null) {
            for (Listener listener : userListeners) {
                listener.execute(args);
            }
        }
    }
}