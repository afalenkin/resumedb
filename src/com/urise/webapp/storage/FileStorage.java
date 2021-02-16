package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SerializeStrategy.SerializeStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    protected File directory;
    private SerializeStrategy strategy;

    protected FileStorage(String dir, SerializeStrategy strategy) {
        this.directory = new File(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.strategy = strategy;
    }

    private File[] getNotNulFilesArray() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error", null);
        } else {
            return files;
        }
    }

    @Override
    protected void deleteResume(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void setResume(File file, Resume resume) {
        try {
            strategy.serialize(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", resume.getUuid(), e);
        }
    }

    @Override
    protected void addResume(File file, Resume resume) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + file.getAbsolutePath(), file.getName(), e);
        }
        setResume(file, resume);
    }

    @Override
    protected Resume getResume(File file) {
        try {
            return strategy.deserialize(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (File file : getNotNulFilesArray()) {
            list.add(getResume(file));
        }
        return list;
    }

    @Override
    public void clear() {
        for (File file : getNotNulFilesArray()) {
            deleteResume(file);
        }
    }

    @Override
    public int size() {
        return getNotNulFilesArray().length;
    }
}
