package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public abstract class AbstractStorageTest {

    protected Storage storage;
    private static final String uuid1 = "UUID_1";
    private static final Resume RESUME_1 = new Resume(uuid1);

    private static final String uuid2 = "UUID_2";
    private static final Resume RESUME_2 = new Resume(uuid2);

    private static final String uuid3 = "UUID_3";
    private static final Resume RESUME_3 = new Resume(uuid3);

    private static final String uuid4 = "UUID_4";
    private static final Resume RESUME_4 = new Resume(uuid4);

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertEquals(storage.size(), resumes.length);
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        int expSize = storage.size() - 1;
        storage.delete(uuid1);
        assertSize(expSize);
        storage.get(uuid1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void update() {
        Resume resume = new Resume(uuid1);
        storage.update(resume);

        //check that the address in memory matches
        assertSame(resume, storage.get(uuid1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume());
    }

    @Test
    public void save() {
        int expSize = storage.size() + 1;
        storage.save(RESUME_4);
        assertSize(expSize);
        assertGet(RESUME_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_1);
    }

    private void assertSize(int expected) {
        Assert.assertEquals(expected, storage.size());
    }

    private void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }

}