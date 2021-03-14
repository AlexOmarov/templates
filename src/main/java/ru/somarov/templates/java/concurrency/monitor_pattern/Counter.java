package ru.somarov.templates.java.concurrency.monitor_pattern;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Counter {
    @GuardedBy("this") private long value = 0;
    public synchronized long getValue() { //--- Здесь используется монитор объекта, у
                                          //которого мы вызываем метод
        return value;
    }
    public synchronized long increment() {
        if (value == Long.MAX_VALUE)
            throw new IllegalStateException("counter overflow");
        return ++value;
    }
}

