package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    // количество резюме в хранилище
    protected int size = 0;

    // own methods
    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Resume[] getAll() {
        Resume[] result = new Resume[size];
        System.arraycopy(storage, 0, result, 0, size);
        return result;
    }

    //implement AbstractStorage methods

    public void setResume(int index, Resume resume) {
        storage[index] = resume;
    }

    @Override
    protected void addResume(int index, Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Warning! Storage is crowded", resume.getUuid());
        }
        insertElement(index, resume);
        size++;
    }

    @Override
    public Resume getResume(int index, String uuid) {
        return storage[index];
    }

    @Override
    protected void deleteResume(int index, String uuid) {
        fillDeletedElement(index);
        storage[size--] = null;
    }

    protected abstract void insertElement(int position, Resume resume);

    protected abstract void fillDeletedElement(int index);

    protected abstract int getIndex(String uuid);

}
