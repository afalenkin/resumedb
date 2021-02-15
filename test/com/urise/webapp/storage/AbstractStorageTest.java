package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;

public abstract class AbstractStorageTest {
    protected static File STORAGE_DIR = new File("D:\\JavaOPS\\BaseJava\\basejava\\src\\com\\urise\\webapp\\resumes");
    protected Storage storage;
    private static final String UUID_1 = "UUID_1";
    private static final Resume RESUME_1 = ResumeTestData.newResume(UUID_1, "Zack");

    private static final String UUID_2 = "UUID_2";
    private static final Resume RESUME_2 = ResumeTestData.newResume(UUID_2, "Arnold");

    private static final String UUID_3 = "UUID_3";
    private static final Resume RESUME_3 = ResumeTestData.newResume(UUID_3, "Arnold");

    private static final Resume RESUME_4 = ResumeTestData.newResume("Jorge");

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
    public void getAllSorted() {
        List<Resume> resumes = storage.getAllSorted();
        Assert.assertEquals(storage.size(), resumes.size());
        Assert.assertEquals(new ArrayList<Resume>(List.of(RESUME_2, RESUME_3, RESUME_1)), resumes);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        int expSize = storage.size() - 1;
        storage.delete(UUID_1);
        assertSize(expSize);
        storage.get(UUID_1);
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
        Resume resume = new Resume(UUID_1, "Jacky");
        storage.update(resume);

        //check that the address in memory matches
        assertSame(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume("Test"));
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