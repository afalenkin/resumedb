package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ResumeSearchKeyMapStorage extends AbstractMapStorage {

    //implement AbstractStorage methods

    @Override
    public void setResume(Object searchKey, Resume resume) {
        Resume resumeKey = (Resume) searchKey;
        storage.put(resumeKey.getUuid(), resume);
    }

    @Override
    public void addResume(Object searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    public void deleteResume(Object searchKey) {
        Resume resume = (Resume) searchKey;
        storage.remove(resume.getUuid());
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return searchKey != null;
    }

}
