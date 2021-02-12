package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory myst not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not Directory");
        }

        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is System directory");
        }

        this.directory = directory;
    }

    @Override
    protected void deleteResume(File file) {
        file.delete();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void setResume(File file, Resume resume) {
        try {
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error ", file.getName(), e);
        }
    }

    @Override
    protected void addResume(File file, Resume resume) {
        try {
            file.createNewFile();
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error ", file.getName(), e);
        }
    }

    @Override
    protected Resume getResume(File file) {
        return doRead(file);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> getAll() {
        File[] resumeFiles = directory.listFiles();
        ArrayList<Resume> resumes = new ArrayList<>();
        for (File resumeFile : Objects.requireNonNull(resumeFiles)) {
            resumes.add(doRead(resumeFile));
        }
        return resumes;
    }

    @Override
    public void clear() {
        File[] fileStorage = this.directory.listFiles();
        for (File resumeFile : Objects.requireNonNull(fileStorage)
        ) {
            resumeFile.delete();
        }
    }

    @Override
    public int size() {
        return Objects.requireNonNull(this.directory.listFiles()).length;
    }

    protected abstract Resume doRead(File file);

    protected abstract void doWrite(Resume resume, File file) throws IOException;
}
