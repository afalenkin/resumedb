package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.*;

public abstract class AbstractStorage implements Storage {

    private final static Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    // own methods
    @Override
    public void update(Resume resume) {
        setResume(getSearchKeyIfExist(resume.getUuid()), resume);
    }

    @Override
    public void save(Resume resume) {
        addResume(getSearchKeyIfNotExist(resume.getUuid()), resume);
    }

    @Override
    public Resume get(String uuid) {
        return getResume(getSearchKeyIfExist(uuid));
    }

    @Override
    public void delete(String uuid) {
        deleteResume(getSearchKeyIfExist(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> allResume = new ArrayList<>(Arrays.asList(getAll()));
        allResume.sort(RESUME_COMPARATOR);
        return allResume;
    }

    // own util methods
    private Object getSearchKeyIfExist(String uuid) throws NotExistStorageException {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getSearchKeyIfNotExist(String uuid) throws ExistStorageException {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    // child util methods

    protected abstract void deleteResume(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void setResume(Object searchKey, Resume resume);

    protected abstract void addResume(Object searchKey, Resume resume);

    protected abstract Resume getResume(Object searchKey);

    protected abstract boolean isExist(Object searchKey);

    protected abstract Resume[] getAll();


}
