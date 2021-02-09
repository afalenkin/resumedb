package com.urise.webapp.model;

public enum ContactType {
    PHONE("Телефон"),
    SKYPE("Профиль Skype"),
    MAIL("e-mail"),
    GITHUB("Профиль GitHub"),
    STACKOVFLW("Профиль StackOverFlow"),
    LINKEDIN("Профиль LinkedIn");

    private final String type;

    ContactType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
