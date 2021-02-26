package com.urise.webapp;

public class MainDeadlock {

    public static void main(String[] args) {

        System.out.println("\n DEADLOCK:");

        new Thread(() -> {
            lockMethod(10, 20);
            done();
        }, "First thread").start();

        new Thread(() -> {
            lockMethod(20, 10);
            done();
        }, "Second thread").start();
    }

    // method with deadlock

    public static int lockMethod(Integer first, Integer second) {
        synchronized (first) {
            System.out.println(Thread.currentThread().getName() + " captured an object with a value " + first);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " tries to grab an object with a value " + second);
            synchronized (second) {
                System.out.println(Thread.currentThread().getName() + " captured an object with a value " + second);
                return first + second;
            }
        }
    }

    public static void done() {
        System.out.println("Thread have finished work");
    }
}
