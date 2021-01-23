package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage{

    @Override
    public void save(Resume resume) {
        if (resumeCount == STORAGE_LIMIT) {
            System.out.println("Warning! Storage is crowded");
            return;
        }
        if (getIndex(resume.getUuid()) < 0) {
            storage[resumeCount] = resume;
            resumeCount++;
        } else {
            System.out.println("ERROR. Storage currently contains resume with uuid = " + resume.getUuid());
        }
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
