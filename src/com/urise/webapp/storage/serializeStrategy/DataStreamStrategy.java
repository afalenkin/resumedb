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
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> pair : contacts.entrySet()) {
                dos.writeUTF(pair.getKey().toString());
                dos.writeUTF(pair.getValue());
            }

            Map<SectionType, Section> sections = resume.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> section : sections.entrySet()
            ) {
                String sectionSpecies = section.getValue().getClass().getSimpleName();
                dos.writeUTF(sectionSpecies);
                dos.writeUTF(section.getKey().toString());

                switch (sectionSpecies) {
                    case "TextSection": {
                        dos.writeUTF(((TextSection) section.getValue()).toString());
                        break;
                    }
                    case "ListSection": {
                        List<String> items = ((ListSection) section.getValue()).getItems();
                        dos.writeInt(items.size());
                        for (String item : items) {
                            dos.writeUTF(item);
                        }
                        break;
                    }
                    case "OrganizationSection": {
                        List<Organization> organizations = ((OrganizationSection) section.getValue()).getOrganizations();
                        dos.writeInt(organizations.size());
                        for (Organization organization : organizations
                        ) {
                            dos.writeUTF(organization.getHomePage().getName());
                            dos.writeUTF(organization.getHomePage().getUrl());

                            List<Organization.Position> positions = organization.getPositions();
                            dos.writeInt(positions.size());

                            for (Organization.Position position : positions) {
                                dos.writeUTF(position.getDescription() == null? "none" : position.getDescription());
                                dos.writeUTF(position.getStartDate().toString());
                                dos.writeUTF(position.getEndDate().toString());
                                dos.writeUTF(position.getTitle());
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

                switch (sectionSpecies) {
                    case "TextSection": {
                        result.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    }
                    case "ListSection": {
                        int itemsCount = dis.readInt();
                        List<String> items = new ArrayList<>();
                        for (int j = 0; j < itemsCount; j++) {
                            items.add(dis.readUTF());
                        }
                        result.addSection(sectionType, new ListSection(items));
                        break;
                    }
                    case "OrganizationSection": {
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
                        result.addSection(sectionType, new OrganizationSection(organizations));
                    }
                }
            }
        }
        return result;
    }
}
