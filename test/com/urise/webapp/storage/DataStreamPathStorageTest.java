package com.urise.webapp.storage;

import com.urise.webapp.storage.serializeStrategy.DataStreamStrategy;

public class DataStreamPathStorageTest extends AbstractStorageTest{

    public DataStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new DataStreamStrategy()));
    }
}