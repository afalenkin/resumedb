package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected void localDelete(int index) {
        storage[index] = storage[resumeCount - 1];
        storage[resumeCount] = null;
    }


    @Override
    protected void localSave(int position, Resume resume) {
        storage[resumeCount] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        if (uuid.isEmpty()) return -1;
        for (int i = 0; i < resumeCount; i++) {
            if (storage[i].getUuid().contains(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
