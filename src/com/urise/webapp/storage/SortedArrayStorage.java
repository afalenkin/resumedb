package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void localDelete(int index) {
        System.arraycopy(storage, (index + 1), storage, index, (resumeCount - index));
    }

    @Override
    protected void localSave(int position, Resume resume) {
        position = -position - 1;
        System.arraycopy(storage, position, storage, position + 1, resumeCount);
        storage[position] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        if (resumeCount < 1) return -1;
        Resume search = new Resume();
        search.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, resumeCount, search);
    }


}
