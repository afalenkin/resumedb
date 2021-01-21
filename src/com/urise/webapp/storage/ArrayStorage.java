package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int resumeCount = 0; // количество резюме в хранилище

    public void clear() {
        Arrays.fill(storage, 0, resumeCount, null);
        resumeCount = 0;
    }

    public void save(Resume r) {
        if (resumeCount == storage.length) {
            System.out.println("Warning! Storage is crowded");
            return;
        }
        if (contains(r.getUuid()) < 0) {
            storage[resumeCount] = r;
            resumeCount++;
        } else {
            System.out.println("ERROR. Storage currently contains this resume.");
        }
    }

    public Resume get(String uuid) {
        int index = contains(uuid);
        if (index < 0) {
            warning();
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = contains(uuid);
        if (index < 0) {
            warning();
            return;
        }
        System.arraycopy(storage, (index + 1), storage, index, (resumeCount - index));
        resumeCount--;
    }

    public Resume[] getAll() {
        Resume[] result = new Resume[resumeCount];
        System.arraycopy(storage, 0, result, 0, resumeCount);
        return result;
    }

    public int size() {
        return resumeCount;
    }

    public void update(Resume r) {
        int index = contains(r.getUuid());
        if (index < 0) {
            warning();
            return;
        }
        storage[index] = r;

    }

    private int contains(String uuid) {  // return index of element if contains, else return -1
        for (int i = 0; i < resumeCount; i++) {
            if (storage[i].getUuid().contains(uuid)) {
                return i;
            }
        }
        return -1;
    }

    private void warning() {
        System.out.println("ERROR. Storage does not contain resume with this uuid");
    }
}
