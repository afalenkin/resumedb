package com.urise.webapp.storage;

import com.urise.webapp.storage.serializeStrategy.JsonStreamsStrategy;

public class JsonStreamPathStorageTest extends AbstractStorageTest{

    public JsonStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new JsonStreamsStrategy()));
    }
}