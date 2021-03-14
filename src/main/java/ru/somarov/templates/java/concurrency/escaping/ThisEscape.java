package ru.somarov.templates.java.concurrency.escaping;


import java.awt.*;
import java.util.EventListener;

public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(
                new EventListener() {
                    public void onEvent(Event e) {
                        doSomething(e);
                    }
                });
    }

    private void doSomething(Event e) {

    }
}


/*
*
* A final mechanism by which an object or its internal state can be published is to publish an inner class
instance, as shown in ThisEscape in Listing 3.7. When ThisEscape publishes the EventListener, it
implicitly publishes the enclosing ThisEscape instance as well, because inner class instances contain a
hidden reference to the enclosing instance.

*
*
* ThisEscape illustrates an important special case of escapewhen the this references escapes during
construction. When the inner EventListener instance is published, so is the enclosing ThisEscape
instance. But an object is in a predictable, consistent state only after its constructor returns, so publishing an
object from within its constructor can publish an incompletely constructed object. This is true even if the
publication is the last statement in the constructor. If the this reference escapes during construction, the
object is considered not properly constructed.[8]
A common mistake that can let the this reference escape during construction is to start a thread from a
constructor
*
* */
