package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;

public class MapStorage extends AbstractStorage {
    private HashMap<String, Resume> storage;

    public MapStorage() {
        this.storage = new HashMap<String, Resume>();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(Resume[]::new);
    }

    //implement AbstractStorage methods

    @Override
    public void setResume(int index, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    public void addResume(int index, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(int index, String uuid) {
        return storage.get(uuid);
    }

    @Override
    public void deleteResume(int index, String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected int getIndex(String uuid) {
        if (storage.containsKey(uuid)) return 1;
        return -1;
    }
}
