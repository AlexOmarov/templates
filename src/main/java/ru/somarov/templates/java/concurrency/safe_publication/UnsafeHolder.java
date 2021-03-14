package ru.somarov.templates.java.concurrency.safe_publication;

public class UnsafeHolder {


    private int n;

    public UnsafeHolder(int n) {
        this.n = n;
    }

    public void assertSanity() {
        if (n != n) // --- due to publishing without synch it can be true - one thread calls function and in the time of
            //reading n second time another thread writes to it.
            throw new AssertionError("This statement is false.");
    }
}


/*
* To publish an object safely, both the reference to the object and the object's state must be made
visible to other threads at the same time. A properly constructed object can be safely published
by:
• Initializing an object reference from a static initializer;
• Storing a reference to it into a volatile field or AtomicReference;
• Storing a reference to it into a final field of a properly constructed object; or
• Storing a reference to it into a field that is properly guarded by a lock.
*
*The publication requirements for an object depend on its mutability:
• Immutable objects can be published through any mechanism;
• Effectively immutable objects must be safely published;
Mutable objects must be safely published, and must be either threadsafe or guarded by a
lock.

*
*
*
*
* Whenever you acquire a reference to an object, you should know what you are allowed to do with it. Do you
need to acquire a lock before using it? Are you allowed to modify its state, or only to read it? Many
concurrency errors stem from failing to understand these "rules of engagement" for a shared object. When you
publish an object, you should document how the object can be accessed.
The most useful policies for using and sharing objects in a concurrent program are:
Thread-confined. A thread-confined object is owned exclusively by and confined
to one thread, and can be modifled by its owning thread.
Shared read-only. A shared read-only object can be accessed concurrently by
multiple threads without additional synchronization, but cannot be modified by
any thread. Shared read-only objects include immutable and effectively
immutable objects.
Shared thread-safe. A thread-safe object performs synchronization internally, so
multiple threads can freely access it through its public interface without further
synchronization.
Guarded. A guarded object can be accessed only with a specific lock held.
Guarded objects include those that are encapsulated within other thread-safe
objects and published objects that are known to be guarded by a specific lock
*
*
*
*
*
* */

