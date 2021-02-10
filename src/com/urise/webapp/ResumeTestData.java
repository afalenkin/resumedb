package com.urise.webapp;

import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Content.Content;
import com.urise.webapp.model.Content.ListContent;
import com.urise.webapp.model.Content.TextContent;
import com.urise.webapp.model.Position;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;

import java.util.List;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume test = new Resume("1", "Григорий Кислин");
        test.contacts.put(ContactType.PHONE, "+7(921) 855-0482");
        test.contacts.put(ContactType.MAIL, "gkislin@yandex.ru");
        test.contacts.put(ContactType.GITHUB, "https://github.com/gkislin");
        test.contacts.put(ContactType.SKYPE, "grigory.kislin");
        test.contacts.put(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        test.contacts.put(ContactType.STACKOVFLW, "https://stackoverflow.com/users/548473");

        Content<String> position = new TextContent("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        Content<String> qualities = new TextContent("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");

        Content<String> achievments = new ListContent<>(List.of(("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                        "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). " +
                        "Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов." +
                        "Более 1000 выпускников."),
                ("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                        "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk."),
                ("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С," +
                        " Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery." +
                        " Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.")));
        ListContent<String> qualification = new ListContent<>(List.of(("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2"),
                ("Version control: Subversion, Git, Mercury, ClearCase, Perforce"),
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,"));

        ListContent<Position> jops = new ListContent<>("JavaOps", List.of(new Position("Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.", "10/2013", "10/2020")));
        ListContent<Position> wrike = new ListContent<>("Wrike", List.of(new Position("Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike" +
                " (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.",
                "10/2014", "01/2016")));

        Content<ListContent<Position>> experience = new ListContent<>(List.of(jops, wrike));

        ListContent<Position> coursera = new ListContent<>("Coursera", List.of(new Position("Functional Programming Principles in Scala by Martin Odersky",
                "02/2013", "05/2013")));
        ListContent<Position> luxoft = new ListContent<>("Luxoft", List.of(new Position("Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"",
                "02/2011", "04/2011")));
        ListContent<Position> spbu = new ListContent<>("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                List.of(new Position("Аспирантура (программист С, С++)",
                                "09/1993", "07/1996"),
                        new Position("Инженер (программист Fortran, C)",
                                "09/1987", "07/1993")));

        Content<ListContent<Position>> education = new ListContent<>(List.of(coursera, luxoft, spbu));

        test.sections.put(SectionType.OBJECTIVE, position);
        test.sections.put(SectionType.PERSONAL, qualities);
        test.sections.put(SectionType.QUALIFICATIONS, qualification);
        test.sections.put(SectionType.ACHIEVEMENT, achievments);
        test.sections.put(SectionType.EXPERIENCE, experience);
        test.sections.put(SectionType.EDUCATION, education);

        System.out.println(test);
    }
}
