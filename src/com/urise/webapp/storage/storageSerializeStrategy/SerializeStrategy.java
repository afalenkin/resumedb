package com.urise.webapp.storage.storageSerializeStrategy;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializeStrategy {
    void serializeResume(Resume resume, OutputStream bos) throws IOException;

    Resume unserializeResume(InputStream bis) throws IOException;
}
