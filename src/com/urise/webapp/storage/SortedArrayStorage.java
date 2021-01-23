package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        if (resumeCount == STORAGE_LIMIT) {
            System.out.println("Warning! Storage is crowded");
            return;
        }
        if (resumeCount == 0) {
            storage[0] = resume;
            resumeCount++;
            return;
        }
        int position = getIndex(resume.getUuid());
        if (position > 0) {
            System.out.println("ERROR. Storage currently contains resume with uuid = " + resume.getUuid());
        } else {
            position = -position - 1;
            System.arraycopy(storage, position, storage, position + 1, resumeCount);
            storage[position] = resume;
            resumeCount++;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        if (resumeCount < 1) return -1;
        Resume search = new Resume();
        search.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, resumeCount, search);
    }
}
