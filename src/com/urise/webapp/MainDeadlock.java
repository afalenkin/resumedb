package com.urise.webapp;

public class MainDeadlock {
    public Integer firstInt = 10;
    public Integer secondInt = 20;

    // method with deadlock
    public int lockMethod(Integer first, Integer second) {
        synchronized (first) {
            System.out.println(Thread.currentThread().getName() + " захватил объект со значением " + first);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " пытается захватить объект со значением " + second);
            synchronized (second) {
                System.out.println(Thread.currentThread().getName() + " захватил объект со значением " + second);
                return first + second;
            }
        }
    }

    // same method avoiding deadlock
    public int nonLockMethod(Integer first, Integer second) {
        synchronized (first > second ? first : second) {
            System.out.println(Thread.currentThread().getName() + " захватил объект со значением " + first);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " пытается захватить объект со значением " + second);
            synchronized (second) {
                System.out.println(Thread.currentThread().getName() + " захватил объект со значением " + second);
                return first + second;
            }
        }
    }

    public void done() {
        System.out.println("All threads have finished their work");
    }

    public static void main(String[] args) throws InterruptedException {
        MainDeadlock nonDeadLock = new MainDeadlock();

        Thread firstWithoutDeadlock = new Thread(() -> {
            System.out.println(nonDeadLock.nonLockMethod(nonDeadLock.firstInt, nonDeadLock.secondInt));
            nonDeadLock.done();
        }, "First thread");

        Thread secondWithoutDeadlock = new Thread(() -> {
            System.out.println(nonDeadLock.nonLockMethod(nonDeadLock.secondInt, nonDeadLock.firstInt));
            nonDeadLock.done();
        }, "Second thread");

        firstWithoutDeadlock.start();
        secondWithoutDeadlock.start();

        Thread.sleep(1000);

        System.out.println("\n DEADLOCK:");

        MainDeadlock deadlock = new MainDeadlock();

        Thread first = new Thread(() -> {
            deadlock.lockMethod(deadlock.firstInt, deadlock.secondInt);
            deadlock.done();
        }, "First thread");

        Thread second = new Thread(() -> {
            deadlock.lockMethod(deadlock.secondInt, deadlock.firstInt);
            deadlock.done();
        }, "Second thread");

        first.start();
        second.start();
    }
}
