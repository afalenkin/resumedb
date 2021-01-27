package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    private Storage storage;
    private final String uuid1 = "UUID_1";
    private final String uuid2 = "UUID_2";
    private final String uuid3 = "UUID_3";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(uuid1));
        storage.save(new Resume(uuid2));
        storage.save(new Resume(uuid3));
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertEquals(storage.size(), resumes.length);
        Assert.assertEquals(uuid1, resumes[0].getUuid());
        Assert.assertEquals(uuid2, resumes[1].getUuid());
        Assert.assertEquals(uuid3, resumes[2].getUuid());
    }

    @Test
    public void delete() {
        storage.delete(uuid2);
        Assert.assertEquals(2, storage.size());
        Assert.assertEquals(uuid1, storage.get(uuid1).getUuid());
        Assert.assertEquals(uuid3, storage.get(uuid3).getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void get() {
        Assert.assertEquals(uuid1, storage.get(uuid1).getUuid());
        Assert.assertEquals(uuid2, storage.get(uuid2).getUuid());
        Assert.assertEquals(uuid3, storage.get(uuid3).getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void update() {
        Resume resume = new Resume(uuid1);

        //check that the memory address is different
        assertNotSame(resume, storage.get(uuid1));

        storage.update(resume);

        //check that the address in memory matches
        assertSame(resume, storage.get(uuid1));
    }

    @Test
    public void save() {
        Resume resume = new Resume();
        storage.save(resume);
        Assert.assertEquals(4, storage.size());
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() {
        try {
            for (int i = 3; i < 10_000; i++) {
                storage.save(new Resume("U" +i));
            }
        } catch (StorageException e) {
            Assert.fail("Premature overflow");
        }
        storage.save(new Resume());
    }
}