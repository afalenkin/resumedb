package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.urise.webapp.TestData.*;

public abstract class AbstractStorageTest {
    protected static File STORAGE_DIR = Config.get().getStorageDir();
    protected Storage storage;


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
        List<Resume> expected = new ArrayList<>(List.of(RESUME_2, RESUME_3, RESUME_1));
        expected.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        Assert.assertEquals(storage.size(), resumes.size());
        Assert.assertEquals(expected, resumes);
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
        resume.addContact(ContactType.HOME_PHONE, "stackoverflow.com");
        storage.update(resume);

        Assert.assertEquals(resume, storage.get(UUID_1));
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