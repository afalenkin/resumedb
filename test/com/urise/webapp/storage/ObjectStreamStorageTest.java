package com.urise.webapp.storage;

import com.urise.webapp.storage.serializeStrategy.InputOutputStreamsStrategy;

public class ObjectStreamStorageTest extends AbstractStorageTest{
    public ObjectStreamStorageTest() {
        super(new FileStorage(STORAGE_DIR.getAbsolutePath(), new InputOutputStreamsStrategy()));
    }
}