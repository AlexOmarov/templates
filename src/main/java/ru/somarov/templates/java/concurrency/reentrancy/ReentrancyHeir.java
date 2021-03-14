package ru.somarov.templates.java.concurrency.reentrancy;

public class ReentrancyHeir extends Reentrancy {
        public synchronized void doSomething() {
            System.out.println(toString() + ": calling doSomething");
            super.doSomething(); // ---- this method is also synchronized by the same lock. So it looks like it is locked and our thread,
            //which is executing it will be waiting forever, but it is not true thanks to reentrancy. Main thought - lock
            //exists per thread, not per invocation!
        }


}
