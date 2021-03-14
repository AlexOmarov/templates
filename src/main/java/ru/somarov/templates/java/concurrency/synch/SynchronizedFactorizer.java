package ru.somarov.templates.java.concurrency.synch;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.math.BigInteger;

@ThreadSafe
public class SynchronizedFactorizer extends HttpServlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    public synchronized void service(ServletRequest req, //---- Actually this is right, but performance-wise it is awful
                                     // should use synchronized down the code
                                     ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber))
            encodeIntoResponse(resp, lastFactors);
        else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }

    private BigInteger[] factor(BigInteger i) {
        return new BigInteger[0];
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] lastFactors) {

    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return null;
    }
}

