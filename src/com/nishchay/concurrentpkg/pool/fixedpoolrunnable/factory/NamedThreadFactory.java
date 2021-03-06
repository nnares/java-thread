package com.nishchay.concurrentpkg.pool.fixedpoolrunnable.factory;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

    private static int count = 0;
    private static String NAME = "My Thread - ";

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, NAME + ++count);
        return t;
    }

}