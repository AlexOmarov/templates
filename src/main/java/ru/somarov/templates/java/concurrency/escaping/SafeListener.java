package ru.somarov.templates.java.concurrency.escaping;

import java.awt.*;
import java.util.EventListener;

public class SafeListener {
    private final EventListener listener;
    private SafeListener() {
        listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

    private void doSomething(Event e) {

    }

    public static SafeListener newInstance(EventSource source) {
        // --- USING FACTORY METHOD TO PREVENT THIS REFERENCE FROM ESCAPING DURING CONSTRUCTION
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }
}
