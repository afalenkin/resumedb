package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapStorage extends AbstractMapStorage {

    //implement AbstractStorage methods

    @Override
    public void setResume(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    public void addResume(Object searchKey, Resume resume) {
        storage.put((String) searchKey, resume);
    }

    @Override
    protected Resume getResume(Object uuid) {
        return storage.get((String) uuid);
    }

    @Override
    public void deleteResume(Object uuid) {
        storage.remove((String) uuid);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;

    }

    @Override
    protected boolean isExist(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }

}
