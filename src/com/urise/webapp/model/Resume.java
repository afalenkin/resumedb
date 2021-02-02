package com.urise.webapp.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume {
    // Unique identifier
    private final String uuid;
    private String fullName;

    public Resume() {
        this(UUID.randomUUID().toString());
        this.fullName = "Default";
    }

    public Resume(String uuid) {
        this.uuid = uuid;
        this.fullName = "Default";
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return ("Resume № " + uuid + "| Full name: " + fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resume)) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

}
