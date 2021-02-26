package com.urise.webapp;

public class MainDeadlock {

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

    public static void main(String[] args) {

        System.out.println("\n DEADLOCK:");

        Integer firstInt = 10;
        Integer secondInt = 20;

        Thread first = new Thread(() -> {
            lockMethod(firstInt, secondInt);
            done();
        }, "First thread");

        Thread second = new Thread(() -> {
            lockMethod(secondInt, firstInt);
            done();
        }, "Second thread");

        first.start();
        second.start();
    }
}
