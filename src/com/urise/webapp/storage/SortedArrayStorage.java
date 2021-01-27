package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void fillDeletedElement(int index) {
        System.arraycopy(storage, (index + 1), storage, index, (size - index));
    }

    @Override
    protected void insertElement(int position, Resume resume) {
        position = -position - 1;
        System.arraycopy(storage, position, storage, position + 1, size - position);
        storage[position] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        if (size < 1 || uuid.isEmpty()) return -1;
        Resume search = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, search);
    }


}
