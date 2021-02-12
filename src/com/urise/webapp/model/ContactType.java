package com.urise.webapp.model;

public enum ContactType {
    PHONE("Телефон"),
    HOME_PHONE("Домашний тел."),
    SKYPE("Профиль Skype"),
    MAIL("e-mail"),
    GITHUB("Профиль GitHub"),
    STACKOVFLW("Профиль StackOverFlow"),
    LINKEDIN("Профиль LinkedIn"),
    HOME_PAGE("Домашняя страница");

    private final String type;

    ContactType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
