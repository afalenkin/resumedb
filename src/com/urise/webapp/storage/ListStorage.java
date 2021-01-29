package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    private ArrayList<Resume> storage;

    public ListStorage() {
        this.storage =  new ArrayList<>();
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
    public void setResume(int index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    public void addResume(int index, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected Resume getResume(int index, String uuid) {
        return storage.get(index);
    }

    @Override
    public void deleteResume(int index, String uuid) {
        storage.remove(index);
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
