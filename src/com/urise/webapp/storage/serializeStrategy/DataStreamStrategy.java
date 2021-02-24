package com.urise.webapp.storage.serializeStrategy;

import com.urise.webapp.model.*;
import com.urise.webapp.model.sections.ListSection;
import com.urise.webapp.model.sections.OrganizationSection;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.model.sections.TextSection;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamStrategy implements SerializeStrategy {

    @Override
    public void serialize(Resume resume, OutputStream bos) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(bos)) {
            write(dos, resume.getUuid(), resume.getFullName());
            writeWithException(dos, resume.getContacts().entrySet(),
                    (Map.Entry<ContactType, String> pair) -> write(dos, pair.getKey().name(), pair.getValue()));
            writeWithException(dos, resume.getSections().entrySet(), (Map.Entry<SectionType, Section> pair) -> writeSection(dos, pair));
        }
    }

    @Override
    public Resume deserialize(InputStream bis) throws IOException {
        Resume result;
        try (DataInputStream dis = new DataInputStream(bis)) {
            result = new Resume(dis.readUTF(), dis.readUTF());

            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                result.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                result.addSection(sectionType, readSection(dis, sectionType));
            }
        }
        return result;
    }

    // util write to file methods
    private static <T> void writeWithException(DataOutputStream dos, Collection<T> collection, ElementTransformer<T> transformer) throws IOException {
        Objects.requireNonNull(transformer);
        dos.writeInt(collection.size());
        for (T t : collection) {
            transformer.transform(t);
        }
    }

    private void writeSection(DataOutputStream dos, Map.Entry<SectionType, Section> section) throws IOException {
        SectionType sectionType = section.getKey();
        write(dos, sectionType.name());
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> write(dos, section.getValue().toString());
            case ACHIEVEMENT, QUALIFICATIONS -> {
                List<String> items = ((ListSection) section.getValue()).getItems();
                write(dos, items.size(), items.toArray(String[]::new));
            }
            case EXPERIENCE, EDUCATION -> {
                List<Organization> organizations = ((OrganizationSection) section.getValue()).getOrganizations();
                writeWithException(dos, organizations, (Organization o) -> {

                    //write all positions in this organization to file
                    writeWithException(dos, o.getPositions(),
                            (Organization.Position position) -> write(dos, getNoneIfNull(position.getDescription()),
                                    position.getStartDate().toString(), position.getEndDate().toString(), position.getTitle()));

                    //link of the organization will be written after all positions
                    write(dos, o.getHomePage().getName(), getNoneIfNull(o.getHomePage().getUrl()));
                });
            }
        }
    }

    private static void write(DataOutputStream dos, String... items) throws IOException {
        for (String item : items) {
            dos.writeUTF(item);
        }
    }

    private static void write(DataOutputStream dos, int count, String... items) throws IOException {
        dos.writeInt(count);
        write(dos, items);
    }

    private String getNoneIfNull(String field) {
        return field == null ? "none" : field;
    }

    // util read from file methods
    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        Section result = null;
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> result = new TextSection(dis.readUTF());
            case ACHIEVEMENT, QUALIFICATIONS -> {
                int itemsCount = dis.readInt();
                List<String> items = new ArrayList<>();
                for (int j = 0; j < itemsCount; j++) {
                    items.add(dis.readUTF());
                }
                result = new ListSection(items);
            }
            case EXPERIENCE, EDUCATION -> {
                int organizationsCount = dis.readInt();
                List<Organization> organizations = new ArrayList<>();
                for (int o = 0; o < organizationsCount; o++) {
                    List<Organization.Position> positions = new ArrayList<>();
                    int positionsCount = dis.readInt();
                    for (int p = 0; p < positionsCount; p++) {
                        String description = dis.readUTF();
                        positions.add(new Organization.Position(
                                LocalDate.parse(dis.readUTF()),
                                LocalDate.parse(dis.readUTF()),
                                dis.readUTF(),
                                getNullIfNone(description)));
                    }
                    Link link = new Link(dis.readUTF(), getNullIfNone(dis.readUTF()));
                    organizations.add(new Organization(link, positions));
                }
                result = new OrganizationSection(organizations);
            }
        }
        return result;
    }

    private String getNullIfNone(String field) {
        return field.equals("none") ? null : field;
    }
}

@FunctionalInterface
interface ElementTransformer<T> {
    void transform(T t) throws IOException;
}


