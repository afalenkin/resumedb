package com.urise.webapp.storage;

import com.urise.webapp.storage.storageSerializeStrategy.FileStrategy;

public class ObjectStreamStorageTest extends AbstractStorageTest{
    public ObjectStreamStorageTest() {
        super(new FileStorage(STORAGE_DIR, new FileStrategy()));
    }
}