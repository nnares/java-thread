package com.nishchay.thread.basic.interrupt;

import com.nishchay.Utils;

import java.util.concurrent.*;

/*
* ==================== how to stop a thread ======================
*
* There is no way, we can stop a thread. No method as such, stop()/cancel()
* Co-operative mechanism, we can’t force a thread to suspend
* Interrupts are co-operative mechanism for indicating stop signal to a thread
*
*
* https://www.youtube.com/watch?v=-7ZB-jpaPPo&ab_channel=DefogTech
* https://www.baeldung.com/java-stop-execution-after-certain-time
* */
public class InterruptAThread {

    public static void main(String[] args) {

        interruptNonBlockingThreadRunnable();
        interruptNonBlockingThreadCallable();

        interruptSleepingThread();
        interruptWaitingThread();

    }


    /*
    * Here in this approach , problem is that
    * You caller thread will never know that , how your worker thread is being finished or returned
    *  - it's been completed successfully
    *  - or it's been interrupted
    * So we need to single back the main thread for the interruption. That signal is nothing but the - InterruptedException
    * Since InterruptedException is checked one, so we can't do this with Runnable, we have to move to Callable interface.
    *
    * */
    private static void interruptNonBlockingThreadRunnable() {

        Thread t = new Thread(() -> {

            for (int i = 1; i <= 500; i++) {
                System.out.println("Counting - " + i);
                // polling for interrupt
                if (Thread.currentThread().isInterrupted()) {
                    // resetting the status
                    Thread.interrupted();
                    System.out.println("Interrupted status- " + Thread.currentThread().isInterrupted());
                    return;
                }
            }

        });

        t.start();

        Utils.sleep0(1);

        t.interrupt();

    }
    /*
     * o/p =>
     *	Counting - 1,2,3,4...65
     *	Interrupted status- false
     *
     * */
    private static void interruptNonBlockingThreadCallable() {

        // FutureTask is a concrete class that implements both Runnable and Future
        FutureTask<Void> futureTask = new FutureTask<>(() -> {
            for (int i = 1; i <= 500; i++) {
                System.out.println("Counting - " + i);

                // polling for interrupt
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                    //can throw any type of Exception
                }
            }
            return null;
        });

        Thread t = new Thread(futureTask);
        t.start();

        Utils.sleep0(1);

        t.interrupt();

        try {
            futureTask.get();
            System.out.println("thread completed");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("thread is been interrupted");
            throw new RuntimeException("Exception trace - " + e);
        }
    }

    private static void interruptSleepingThread() {

        Thread t = new Thread(() -> {
            System.out.println("started");
            try {
                Thread.sleep(3 * 1000);
                System.out.println("sleeping");
            } catch (InterruptedException e) { // java.lang.InterruptedException: sleep interrupted
                System.out.println("thread is been interrupted");
                throw new RuntimeException("Exception trace - " + e);
            }
            System.out.println("finished");
        });

        t.start();
        t.interrupt();

    }

    private static void interruptWaitingThread() {

        Object lock = new Object();

        Thread t = new Thread(() -> {
            System.out.println("started");
            try {
                synchronized (lock) {
                    lock.wait();
                }
                System.out.println("waiting");
            } catch (InterruptedException e) { // java.lang.InterruptedException
                System.out.println("thread is been interrupted");
                throw new RuntimeException("Exception trace - " + e);
            }
            System.out.println("finished");
        });

        t.start();
        t.interrupt();

    }

}

/*
O/P =>

Counting - 1,2...50+..73
Counting - 72
thread is been interrupted
Exception in thread "main" java.lang.RuntimeException: Thread interrupted...java.util.concurrent.ExecutionException: java.lang.InterruptedException
	at com.nishchay.thread.basic.interrupt.InterruptAThread.interruptNonBlockingThreadCallable(InterruptAThread.java:88)
	at com.nishchay.thread.basic.interrupt.InterruptAThread.interruptNonBlockingThreadEx(InterruptAThread.java:27)
	at com.nishchay.thread.basic.interrupt.InterruptAThread.main(InterruptAThread.java:19)

*/
