package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10_000];
    private int resumeCount = 0; // количество резюме в хранилище

    public void clear() {
        Arrays.fill(storage, 0, resumeCount, null);
        resumeCount = 0;
    }

    public void save(Resume resume) {
        if (resumeCount == storage.length) {
            System.out.println("Warning! Storage is crowded");
            return;
        }
        if (contains(resume.getUuid()) < 0) {
            storage[resumeCount] = resume;
            resumeCount++;
        } else {
            System.out.println("ERROR. Storage currently contains resume with uuid = " + resume.getUuid());
        }
    }

    public Resume get(String uuid) {
        int index = contains(uuid);
        if (index < 0) {
            System.out.println("ERROR. Storage does not contain resume with uuid = " + uuid);
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = contains(uuid);
        if (index < 0) {
            System.out.println("ERROR. Storage does not contain resume with uuid = " + uuid);
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

    public void update(Resume resume) {
        int index = contains(resume.getUuid());
        if (index < 0) {
            System.out.println("ERROR. Storage does not contain resume with uuid = " + resume.getUuid());
            return;
        }
        storage[index] = resume;

    }

    // return index of element if contains, else return -1
    private int contains(String uuid) {
        if (uuid.isEmpty()) return -1;
        for (int i = 0; i < resumeCount; i++) {
            if (storage[i].getUuid().contains(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
