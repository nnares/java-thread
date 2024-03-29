package com.nishchay.concurrentpkg.lock.rwlock;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteList.java
 * This class demonstrates how to use ReadWriteLock to add concurrency
 * features to a non-threadsafe collection like - ArrayList
 */
public class ReadWriteList<E> {

    private final List<E> list;
    private final ReadWriteLock rwLock;

    public ReadWriteList() {
        this.list = new ArrayList<>();
        this.rwLock = new ReentrantReadWriteLock();
    }

    @SafeVarargs
    public ReadWriteList(E... initialElements) {
        this();
        list.addAll(Arrays.asList(initialElements));
    }
 
    public void add(E element) {
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
 
        try {
            list.add(element);
        } finally {
            writeLock.unlock();
        }
    }
 
    public E get(int index) {
        Lock readLock = rwLock.readLock();
        readLock.lock();
 
        try {
            return list.get(index);
        } finally {
            readLock.unlock();
        }
    }
 
    public int size() {
        Lock readLock = rwLock.readLock();
        readLock.lock();
 
        try {
            return list.size();
        } finally {
            readLock.unlock();
        }
    }
 
}
