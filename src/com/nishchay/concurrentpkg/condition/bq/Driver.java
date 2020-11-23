package com.nishchay.concurrentpkg.condition.bq;

public class Driver {

    public static void main(String[] args) {

        final int BUFFER_SIZE = 5;
        BlockingQueue<Integer> queue = new BlockingQueue<>(BUFFER_SIZE);

        Thread prodThread = new Thread(() -> produce(queue), "Producer Thread");
        prodThread.start();

        Thread consThread = new Thread(() -> consume(queue), "Consumer Thread");
        consThread.start();
    }


    public static void produce(BlockingQueue<Integer> bq) {
        for (int i = 1; i <= 10; i++) {
            try {
                bq.put(i);
                System.out.println(Thread.currentThread().getName() + " produces: " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void consume(BlockingQueue<Integer> bq) {
        Integer data;
        for (int i = 1; i <= 10; i++) {
            try {
                data = bq.take();
                System.out.println(Thread.currentThread().getName() + " consumes: " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
