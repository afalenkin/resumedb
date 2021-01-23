package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int resumeCount = 0; // количество резюме в хранилище

    public int size() {
        return resumeCount;
    }

    public void clear() {
        Arrays.fill(storage, 0, resumeCount, null);
        resumeCount = 0;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
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

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR. Storage does not contain resume with uuid = " + uuid);
            return null;
        }
        return storage[index];
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            System.out.println("ERROR. Storage does not contain resume with uuid = " + resume.getUuid());
            return;
        }
        storage[index] = resume;

    }

    protected abstract int getIndex(String uuid);

}
