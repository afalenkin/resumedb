package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    // количество резюме в хранилище
    protected int resumeCount = 0;

    public int size() {
        return resumeCount;
    }

    public void clear() {
        Arrays.fill(storage, 0, resumeCount, null);
        resumeCount = 0;
    }

    public Resume[] getAll() {
        Resume[] result = new Resume[resumeCount];
        System.arraycopy(storage, 0, result, 0, resumeCount);
        return result;
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("ERROR. Storage does not contain resume with uuid = " + uuid);
            return;
        }
        localDelete(index);
        storage[resumeCount] = null;
        resumeCount--;
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

    public void save(Resume resume) {
        if (resumeCount == STORAGE_LIMIT) {
            System.out.println("Warning! Storage is crowded");
            return;
        }
        int position = getIndex(resume.getUuid());
        if (position > 0) {
            System.out.println("ERROR. Storage currently contains resume with uuid = " + resume.getUuid());
        } else {
            localSave(position, resume);
            resumeCount++;
        }
    }

    protected abstract void localSave(int position, Resume resume);

    protected abstract void localDelete(int index);

    protected abstract int getIndex(String uuid);

}
