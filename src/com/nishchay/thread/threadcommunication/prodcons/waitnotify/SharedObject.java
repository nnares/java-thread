package com.nishchay.thread.threadcommunication.prodcons.waitnotify;

public class SharedObject {
    private int data;
    private boolean isProdTurn = true;

    public synchronized void put(int value) {
        // why did we use while(busy == false) instead of if(busy == false)
        // because of spurious wake ups
        while (!isProdTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        data = value;
        isProdTurn = false;
        notify();
    }

    public synchronized int get() {
        while (isProdTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        isProdTurn = true;
        notify();
        return data;
    }
}