package com.urise.webapp;

public class MainLazySingleton {
    volatile private static MainLazySingleton INSTANCE;

    private MainLazySingleton() {
    }

    private static class MainLazySingletonHolder {
        private static final MainLazySingleton INSTANCE = new MainLazySingleton();
    }

    public static MainLazySingleton getInstance() {
        return MainLazySingletonHolder.INSTANCE;
    }
}
