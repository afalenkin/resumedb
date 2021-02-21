package com.urise.webapp.storage.serializeStrategy;

import com.urise.webapp.model.Link;
import com.urise.webapp.model.Organization;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.sections.ListSection;
import com.urise.webapp.model.sections.OrganizationSection;
import com.urise.webapp.model.sections.TextSection;
import com.urise.webapp.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamsStrategy implements SerializeStrategy {
    private XmlParser parser;

    public XmlStreamsStrategy() {
        parser = new XmlParser(Resume.class, Organization.class, Organization.Position.class, Link.class, ListSection.class,
                TextSection.class, OrganizationSection.class);
    }

    @Override
    public void serialize(Resume resume, OutputStream bos) throws IOException {
        try (Writer writer = new OutputStreamWriter(bos, StandardCharsets.UTF_8)) {
            parser.marshall(resume, writer);
        }
    }

    @Override
    public Resume deserialize(InputStream bis) throws IOException {
        try (Reader reader = new InputStreamReader(bis, StandardCharsets.UTF_8)) {
            return parser.unmarshall(reader);
        }
    }
}
