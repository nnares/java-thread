package com.nishchay.concurrentpkg.condition.bq;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Designing a BlockingQueue using Lock & Condition
 * Designed it for the generic data type
 * Underlying data structure is LinkList
 * */
public class BlockingQueue<T> {

    private Queue<T> queue = new LinkedList<>();
    private int capacity;

    private final Lock lock;
    private final Condition addCondition;
    private final Condition removeCondition;

    public BlockingQueue(int size) {
        this.capacity = size;
        lock = new ReentrantLock();
        addCondition = lock.newCondition();
        removeCondition = lock.newCondition();
    }

    public boolean isFull() {
        return queue.size() == capacity;
    }

    public boolean isEmpty() {
        return queue.size() == 0;
    }

    public void put(T element) throws InterruptedException {
        lock.lock();
        try {
            while (isFull()) {
                addCondition.await();
            }
            // Append specified element to the end of list.
            queue.add(element);
            removeCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (isEmpty()) {
                removeCondition.await();
            }
            // Retrieves and removes the head of this list. Removes the first occurrence if many
            T item = queue.remove();
            addCondition.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}