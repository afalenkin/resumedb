package com.urise.webapp.model;

import com.urise.webapp.model.sections.ListSection;
import com.urise.webapp.model.sections.OrganizationSection;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.model.sections.TextSection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

import static com.urise.webapp.model.SectionType.*;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    private final static long serialVersionUID = 1L;

    private String uuid;
    private String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    //resume for WEB
    private final static Resume EMPTY = new Resume("");

    static {
        EMPTY.uuid = "";
        EMPTY.addSection(PERSONAL, new TextSection(""));
        EMPTY.addSection(OBJECTIVE, new TextSection(""));
        EMPTY.addSection(ACHIEVEMENT, new ListSection(new ArrayList<>()));
        EMPTY.addSection(QUALIFICATIONS, new ListSection(new ArrayList<>()));
        EMPTY.addSection(EXPERIENCE, new OrganizationSection(List.of(new Organization("", "", new Organization.Position()))));
        EMPTY.addSection(EDUCATION, new OrganizationSection(List.of(new Organization("", "", new Organization.Position()))));
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume() {
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

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
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
            result.append(entry.getKey().getTitle()).append(" : ").append(entry.getValue()).append("\n");
        }

        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            result.append(entry.getKey().getTitle()).append("\n");
            result.append(entry.getValue().toString()).append("\n");

        }
        return result.toString();
    }

    public static Resume getEmptyResume() {
        return EMPTY;
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
