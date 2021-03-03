package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private final static Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    // own methods
    @Override
    public void update(Resume resume) {
        LOG.info("update " + resume);
        setResume(getSearchKeyIfExist(resume.getUuid()), resume);
    }

    @Override
    public void save(Resume resume) {
        LOG.info("save " + resume);
        addResume(getSearchKeyIfNotExist(resume.getUuid()), resume);
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("get " + uuid);
        return getResume(getSearchKeyIfExist(uuid));
    }

    @Override
    public void delete(String uuid) {
        LOG.info("delete " + uuid);
        deleteResume(getSearchKeyIfExist(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> allResume = getAll();
        allResume.sort(RESUME_COMPARATOR);
        return allResume;
    }

    // child util methods

    protected abstract void deleteResume(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract void setResume(SK searchKey, Resume resume);

    protected abstract void addResume(SK searchKey, Resume resume);

    protected abstract Resume getResume(SK searchKey);

    protected abstract boolean isExist(SK searchKey);

    protected abstract List<Resume> getAll();

    // own util methods
    private SK getSearchKeyIfExist(String uuid) throws NotExistStorageException {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " is NOT exist!");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getSearchKeyIfNotExist(String uuid) throws ExistStorageException {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume " + uuid + " is currently exist!");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
