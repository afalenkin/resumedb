package com.urise.webapp.storage.SerializeStrategy;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;

public class InputOutputStreamsStrategy implements SerializeStrategy {

    @Override
    public void serialize(Resume resume, OutputStream bos) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(resume);
        }
    }

    @Override
    public Resume deserialize(InputStream bis) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
