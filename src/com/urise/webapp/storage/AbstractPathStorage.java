package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    protected final Path directory;

    protected AbstractPathStorage(String directory) {
        this.directory = Paths.get(directory);
        Objects.requireNonNull(this.directory, "directory must not be null");
        if (!Files.isDirectory(this.directory) || !Files.isWritable(this.directory)) {
            throw new IllegalArgumentException(directory + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::deleteResume);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        return getAll().size();
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void setResume(Path path, Resume resume) {
        try {
            doWrite(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", resume.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void addResume(Path path, Resume resume) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create file ", path.toString(), e);
        }
        setResume(path, resume);
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.toString(), e);
        }
    }

    @Override
    protected void deleteResume(Path path) {
        try {
            if (Files.deleteIfExists(path)) {
                throw new StorageException("Path delete error", path.toString());
            }
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.toString());
        }
    }

    @Override
    protected List<Resume> getAll() {
        ArrayList<Resume> result = new ArrayList<>();
        try {
            for (Path path : Files.list(directory).collect(Collectors.toList())) {
                result.add(getResume(path));
            }
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
        return result;
    }

    protected abstract void doWrite(Resume resume, OutputStream bos) throws IOException;

    protected abstract Resume doRead(InputStream bis) throws IOException;
}