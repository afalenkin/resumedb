package com.urise.webapp.model;

import com.urise.webapp.model.sections.Section;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid can not be empty");
        Objects.requireNonNull(fullName, "full name can not be empty");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public void addContact(ContactType type, String content) {
        contacts.put(type, content);
    }

    public void addSection(SectionType type, Section section) {
        sections.put(type, section);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(fullName).append("\n");
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()
        ) {
            result.append(entry.getKey().toString()).append(" : ").append(entry.getValue()).append("\n");
        }

        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            result.append(entry.getKey().toString()).append("\n");
            result.append(entry.getValue().toString()).append("\n");

        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) &&
                fullName.equals(resume.fullName) &&
                contacts.equals(resume.contacts) &&
                sections.equals(resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public int compareTo(Resume o) {
        int compare = fullName.compareTo(o.getFullName());
        return compare != 0 ? compare : uuid.compareTo(o.getUuid());
    }
}
