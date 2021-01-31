package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    private ArrayList<Resume> storage;

    public ListStorage() {
        this.storage = new ArrayList<>();
    }
    //own methods

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
        return storage.toArray(Resume[]::new);
    }

    //implement AbstractStorage methods

    @Override
    public void setResume(Object index, Resume resume) {
        storage.set((int) index, resume);
    }

    @Override
    public void addResume(Object index, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected Resume getResume(Object index) {
        return storage.get((int) index);
    }

    @Override
    public void deleteResume(Object index) {
        storage.remove((int) index);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Object index) {
        return (int) index >= 0;
    }
}
