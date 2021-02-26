package com.urise.webapp.util;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private int counter;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
                throw new IllegalStateException();
            }
        };
        thread0.start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState());
            }

            private void inc() {
                synchronized (this) {
//                    counter++;
                }
            }

        }).start();

        System.out.println(thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
            thread.start();
            threads.add(thread);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(mainConcurrency.counter);

        // deadlock
        System.out.println("\nDeadlock block: ");
        final MainConcurrency deadlock = new MainConcurrency();
        Thread first = new Thread(() -> {
            deadlock.firstMethod();
            deadlock.done();
        }, "First thread");

        Thread second = new Thread(() -> {
            deadlock.secondMethod();
            deadlock.done();
        }, "Second thread");

        first.start();
        second.start();
    }

    private synchronized void inc() {
        counter++;
    }

    // deadlock
    public synchronized int firstMethod() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " came in first method, result is + " + secondMethod());
        return 10000;

    }

    public synchronized int secondMethod() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " came in second method, result is " + firstMethod());
        return 20000;
    }

    public void done(){
        System.out.println("All threads have finished their work");
    }
}
