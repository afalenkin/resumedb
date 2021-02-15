package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;

public class ObjectStreamPathStorage extends AbstractPathStorage {

    protected ObjectStreamPathStorage(String directory) {
        super(directory);
    }

    @Override
    protected void doWrite(Resume resume, OutputStream bos) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(resume);
        }
    }

    @Override
    protected Resume doRead(InputStream bis) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
