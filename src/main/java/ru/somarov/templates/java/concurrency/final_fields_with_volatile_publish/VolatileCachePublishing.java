package ru.somarov.templates.java.concurrency.final_fields_with_volatile_publish;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;


import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;

@ThreadSafe
public class VolatileCachePublishing extends HttpServlet {
    private volatile ImmutableCashObject cache =
            new ImmutableCashObject(null, null);

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new ImmutableCashObject(i, factors);
        }
        encodeIntoResponse(resp, factors);
    }

    private BigInteger[] factor(BigInteger i) {
        return null;
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {

    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return null;
    }
}



