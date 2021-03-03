package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SortedArrayStorage;
import com.urise.webapp.storage.Storage;

public class TestSortedArrayStorage {
    static final Storage SORTED_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        System.out.println("Testing SortedArrayStorage");
        Storage sortedStorage = new SortedArrayStorage();

        Resume res1 = new Resume("D", "Arnold");
        Resume res2 = new Resume("B", "Conor");
        Resume res3 = new Resume("A", "Conor");
        Resume res4 = new Resume("C", "Zack");

        //  System.out.println("Get Dummy = " + SORTED_STORAGE.get("Dummy"));

        SORTED_STORAGE.save(res1);
        printAll();
        SORTED_STORAGE.save(res2);
        printAll();
        SORTED_STORAGE.save(res3);
        printAll();
        SORTED_STORAGE.save(res4);
        printAll();

        System.out.println("Get C = " + SORTED_STORAGE.get("C"));

        SORTED_STORAGE.delete("B");
        printAll();

        Resume updated = new Resume("A");
        SORTED_STORAGE.update(updated);
        printAll();

        System.out.println(SORTED_STORAGE.getAllSorted());

        SORTED_STORAGE.clear();
        printAll();


    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : SORTED_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}
