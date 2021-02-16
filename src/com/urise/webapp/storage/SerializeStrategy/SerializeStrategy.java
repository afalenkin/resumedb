package com.urise.webapp.storage.SerializeStrategy;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializeStrategy {
    void serialize(Resume resume, OutputStream bos) throws IOException;

    Resume deserialize(InputStream bis) throws IOException;
}
