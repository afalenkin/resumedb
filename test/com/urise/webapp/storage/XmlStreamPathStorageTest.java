package com.urise.webapp.storage;

import com.urise.webapp.storage.serializeStrategy.XmlStreamsStrategy;

public class XmlStreamPathStorageTest extends AbstractStorageTest{

    public XmlStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamsStrategy()));
    }
}