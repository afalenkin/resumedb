package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
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
    protected List<Resume> getAll() {
        Resume[] allResume = new Resume[size];
        System.arraycopy(storage, 0, allResume, 0, size);
        return new ArrayList<>(Arrays.asList(allResume));
    }

    //implement AbstractStorage methods
    @Override
    public void setResume(Integer index, Resume resume) {
        storage[index] = resume;
    }

    @Override
    protected void addResume(Integer index, Resume resume) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Warning! Storage is crowded", resume.getUuid());
        }
        insertElement(index, resume);
        size++;
    }

    @Override
    public Resume getResume(Integer index) {
        return storage[index];
    }

    @Override
    protected void deleteResume(Integer index) {
        fillDeletedElement(index);
        storage[size--] = null;
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    protected abstract void insertElement(int position, Resume resume);

    protected abstract void fillDeletedElement(int index);
}
