package com.urise.webapp.storage;

import static org.junit.Assert.*;

public class ObjectStreamStorageTest extends AbstractStorageTest{
    protected ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }
}