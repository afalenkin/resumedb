package com.urise.webapp.storage.serializeStrategy;

import com.urise.webapp.model.*;
import com.urise.webapp.model.sections.ListSection;
import com.urise.webapp.model.sections.OrganizationSection;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.model.sections.TextSection;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamStrategy implements SerializeStrategy {

    @Override
    public void serialize(Resume resume, OutputStream bos) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(bos)) {
            writeToFile(dos, resume.getUuid(), resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            writeToFile(dos, contacts.size());
            contacts.forEach((key, value) -> writeToFile(dos, key.name(), value));

            Map<SectionType, Section> sections = resume.getSections();
            writeToFile(dos, sections.size());
            sections.entrySet().forEach(pair -> switchSectionWhenWrite(dos, pair));
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
                result.addSection(sectionType, switchSectionWhenRead(dis, sectionType));
            }
        }
        return result;
    }

    // util write to file methods

    private void switchSectionWhenWrite(DataOutputStream dos, Map.Entry<SectionType, Section> section) {
        SectionType sectionType = section.getKey();
        writeToFile(dos, sectionType.name());
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> writeToFile(dos, section.getValue().toString());
            case ACHIEVEMENT, QUALIFICATIONS -> {
                List<String> items = ((ListSection) section.getValue()).getItems();
                writeToFile(dos, items.size(), items.toArray(String[]::new));
            }
            case EXPERIENCE, EDUCATION -> {
                List<Organization> organizations = ((OrganizationSection) section.getValue()).getOrganizations();
                writeToFile(dos, organizations.size());
                for (Organization organization : organizations) {
                    writeToFile(dos, organization.getHomePage().getName(), canBeEmptyWhenWrite(organization.getHomePage().getUrl()));
                    List<Organization.Position> positions = organization.getPositions();
                    writeToFile(dos, positions.size());
                    for (Organization.Position position : positions) {
                        writeToFile(dos, canBeEmptyWhenWrite(position.getDescription()),
                                position.getStartDate().toString(), position.getEndDate().toString(), position.getTitle());
                    }
                }
            }
        }
    }

    private static void writeToFile(DataOutputStream dos, String... items) {
        for (String item : items) {
            try {
                dos.writeUTF(item);
            } catch (IOException e) {
                throw new RuntimeException("Rethrow IO exception when write to file", e);
            }
        }
    }

    private static void writeToFile(DataOutputStream dos, int count, String... items) {
        try {
            dos.writeInt(count);
        } catch (IOException e) {
            throw new RuntimeException("Rethrow IO exception when write to file", e);
        }
        writeToFile(dos, items);
    }

    private String canBeEmptyWhenWrite(String field) {
        return field == null ? "none" : field;
    }

    // util read from file methods
    private Section switchSectionWhenRead(DataInputStream dis, SectionType sectionType) throws IOException {
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
                    Link link = new Link(dis.readUTF(), canBeEmptyWhenRead(dis.readUTF()));
                    List<Organization.Position> positions = new ArrayList<>();
                    int positionsCount = dis.readInt();
                    for (int p = 0; p < positionsCount; p++) {
                        String description = dis.readUTF();
                        positions.add(new Organization.Position(
                                LocalDate.parse(dis.readUTF()),
                                LocalDate.parse(dis.readUTF()),
                                dis.readUTF(),
                                canBeEmptyWhenRead(description)));
                    }
                    organizations.add(new Organization(link, positions));
                }
                result = new OrganizationSection(organizations);
            }
        }
        return result;
    }

    private String canBeEmptyWhenRead(String field) {
        return field.equals("none") ? null : field;
    }
}


