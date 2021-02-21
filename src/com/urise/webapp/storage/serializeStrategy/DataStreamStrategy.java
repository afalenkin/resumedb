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
import java.util.Set;

public class DataStreamStrategy implements SerializeStrategy {

    @Override
    public void serialize(Resume resume, OutputStream bos) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(bos)) {
            writeToFile(dos, resume.getUuid(), resume.getFullName());

            for (Map.Entry<ContactType, String> pair : getSetWriteSize(dos, resume.getContacts())) {
                writeToFile(dos, pair.getKey().toString(), pair.getValue());
            }

            for (Map.Entry<SectionType, Section> section : getSetWriteSize(dos, resume.getSections())
            ) {
                String sectionSpecies = section.getValue().getClass().getSimpleName();
                writeToFile(dos, sectionSpecies, section.getKey().toString());

                switch (sectionSpecies) {
                    case "TextSection" -> writeToFile(dos, section.getValue().toString());
                    case "ListSection" -> {
                        List<String> items = ((ListSection) section.getValue()).getItems();
                        writeToFile(dos, items.size(), items.toArray(String[]::new));
                    }
                    case "OrganizationSection" -> {
                        for (Organization organization : getListWriteSize(dos, ((OrganizationSection) section.getValue()).getOrganizations())) {
                            writeToFile(dos, organization.getHomePage().getName(), organization.getHomePage().getUrl());
                            for (Organization.Position position : getListWriteSize(dos, organization.getPositions())) {
                                writeToFile(dos, position.getDescription() == null ? "none" : position.getDescription(),
                                        position.getStartDate().toString(), position.getEndDate().toString(), position.getTitle());
                            }
                        }
                    }
                }
            }
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
                String sectionSpecies = dis.readUTF();
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                result.addSection(sectionType, switchSection(dis, sectionSpecies));
            }
        }
        return result;
    }

    // util write to file methods
    private static void writeToFile(DataOutputStream dos, String... items) throws IOException {
        for (String item : items) {
            dos.writeUTF(item);
        }
    }

    private static void writeToFile(DataOutputStream dos, int count, String... items) throws IOException {
        dos.writeInt(count);
        writeToFile(dos, items);
    }

    private static <K, V> Set<Map.Entry<K, V>> getSetWriteSize(DataOutputStream dos, Map<K, V> map) throws IOException {
        dos.writeInt(map.size());
        return map.entrySet();
    }

    private static <V> List<V> getListWriteSize(DataOutputStream dos, List<V> list) throws IOException {
        dos.writeInt(list.size());
        return list;
    }

    // util read from file methods
    private Section switchSection(DataInputStream dis, String sectionSpecies) throws IOException {
        Section result = null;
        switch (sectionSpecies) {
            case "TextSection" -> result = new TextSection(dis.readUTF());

            case "ListSection" -> {
                int itemsCount = dis.readInt();
                List<String> items = new ArrayList<>();
                for (int j = 0; j < itemsCount; j++) {
                    items.add(dis.readUTF());
                }
                result = new ListSection(items);
            }
            case "OrganizationSection" -> {
                int organizationsCount = dis.readInt();
                List<Organization> organizations = new ArrayList<>();
                for (int o = 0; o < organizationsCount; o++) {
                    Link link = new Link(dis.readUTF(), dis.readUTF());
                    List<Organization.Position> positions = new ArrayList<>();
                    int positionsCount = dis.readInt();
                    for (int p = 0; p < positionsCount; p++) {
                        String description = dis.readUTF();
                        positions.add(new Organization.Position(
                                LocalDate.parse(dis.readUTF()),
                                LocalDate.parse(dis.readUTF()),
                                dis.readUTF(),
                                description.equals("none") ? null : description
                        ));
                    }
                    organizations.add(new Organization(link, positions));
                }
                result = new OrganizationSection(organizations);
            }
        }
        return result;
    }
}
