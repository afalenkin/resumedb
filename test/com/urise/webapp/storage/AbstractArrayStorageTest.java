package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import org.junit.Test;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() {
        testSaveOverFlow();
    }
}