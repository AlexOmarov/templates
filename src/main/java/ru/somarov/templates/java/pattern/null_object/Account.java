package ru.somarov.templates.java.pattern.null_object;

import java.lang.reflect.Proxy;

interface Log
{
    // max # of elements in the log
    int getRecordLimit();

    // number of elements already in the log
    int getRecordCount();

    // expected to increment record count
    void logInfo(String message);
}

class Account
{
    private Log log;

    public Account(Log log)
    {
        this.log = log;
    }

    public void someOperation() throws Exception
    {
        int c = log.getRecordCount();
        log.logInfo("Performing an operation");
        if (c+1 != log.getRecordCount())
            throw new Exception();
        if (log.getRecordCount() >= log.getRecordLimit())
            throw new Exception();
    }


    @SuppressWarnings("unchecked")
    private <T> T getProxy(Class<T> inst){

        return (T) Proxy.newProxyInstance(
                inst.getClassLoader(),
                new Class<?>[] { inst },
                ((proxy, method, args) -> {
                    if (method.getReturnType().equals(Void.TYPE))
                        return null;
                    else
                        return method.getReturnType().getConstructor().newInstance();
                })

        );
    }
}

class NullLog implements Log
{
    int recordLimit = 1;
    int recordCount = 0;
    @Override
    public int getRecordLimit() {
        return 0;
    }

    @Override
    public int getRecordCount() {
        return 0;
    }

    @Override
    public void logInfo(String message) {
        recordCount++;
        recordLimit++;
    }
    // todo
}
