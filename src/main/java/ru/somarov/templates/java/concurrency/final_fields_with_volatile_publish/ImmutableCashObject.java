package ru.somarov.templates.java.concurrency.final_fields_with_volatile_publish;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.math.BigInteger;
import java.util.Arrays;

@Immutable
class ImmutableCashObject {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public ImmutableCashObject(BigInteger i,
                               BigInteger[] factors) {
        lastNumber = i;
        lastFactors = Arrays.copyOf(factors, factors.length);
    }

    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i))
            return null;
        else
            return Arrays.copyOf(lastFactors, lastFactors.length);
    }
}

