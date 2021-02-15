package com.urise.webapp.storage;

import com.urise.webapp.storage.storageSerializeStrategy.PathStrategy;

import static org.junit.Assert.*;

public class ObjectStreamPathStorageTest extends AbstractStorageTest{

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new PathStrategy()));
    }
}