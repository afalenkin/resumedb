package com.urise.webapp.storage.serializeStrategy;

import com.urise.webapp.model.Resume;
import com.urise.webapp.util.JsonParser;

import java.io.*;

public class JsonStreamsStrategy implements SerializeStrategy {

    @Override
    public void serialize(Resume resume, OutputStream bos) throws IOException {
        try (Writer writer = new OutputStreamWriter(bos)) {
            JsonParser.write(resume, writer);
        }
    }

    @Override
    public Resume deserialize(InputStream bis) throws IOException {
        try (Reader reader = new InputStreamReader(bis)) {
            return JsonParser.read(reader, Resume.class);
        }
    }
}
