package com.urise.webapp.storage;

import java.io.File;

import static org.junit.Assert.*;

public class ObjectStreamStorageTest extends AbstractStorageTest{
    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(new File(STORAGE_DIR)));
    }
}