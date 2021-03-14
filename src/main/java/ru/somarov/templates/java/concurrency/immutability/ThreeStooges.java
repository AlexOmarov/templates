package main.java.ru.somarov.javatest.concurrency.immutability;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

/*
An object is immutable if:
• Its state cannot be modifled after construction;
• All its flelds are final;
• and
• It is properly constructed (the this reference does not escape during construction).
*/

@Immutable
public final class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();
    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }
    public boolean isStooge(String name) {
        return stooges.contains(name);
    }
}
