package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> storage;

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
    protected List<Resume> getAll() {
        return new ArrayList<>(storage);
    }

    //implement AbstractStorage methods

    @Override
    public void setResume(Integer index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    public void addResume(Integer index, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected Resume getResume(Integer index) {
        return storage.get(index);
    }

    @Override
    public void deleteResume(Integer index) {
        storage.remove(index.intValue());
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }
}
