package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapStorage extends AbstractMapStorage<String> {

    //implement AbstractStorage methods

    @Override
    public void setResume(String searchKey, Resume resume) {
        storage.put( searchKey, resume);
    }

    @Override
    public void addResume(String searchKey, Resume resume) {
        storage.put(searchKey, resume);
    }

    @Override
    protected Resume getResume(String uuid) {
        return storage.get(uuid);
    }

    @Override
    public void deleteResume(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;

    }

    @Override
    protected boolean isExist(String searchKey) {
        return storage.containsKey(searchKey);
    }
}
