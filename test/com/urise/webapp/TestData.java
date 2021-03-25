package com.urise.webapp;

import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Organization;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.model.sections.ListSection;
import com.urise.webapp.model.sections.OrganizationSection;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.model.sections.TextSection;

import java.time.Month;
import java.util.List;
import java.util.UUID;

public class TestData {
    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();

    public static final Resume RESUME_1 = newResume(UUID_1, "Zack");
    public static final Resume RESUME_2 = newResume(UUID_2, "Arnold");
    public static final Resume RESUME_3 = newResume(UUID_3, "Arnold");
    public static final Resume RESUME_4 = newResume("Jorge");
    public static final Resume RESUME_5 = new Resume("Zukor");

    public static Resume newResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        return fillResume(resume);
    }

    public static Resume newResume(String fullName) {
        Resume resume = new Resume(fullName);
        return fillResume(resume);
    }

    private static Resume fillResume(Resume result) {
        result.addContact(ContactType.PHONE, "+7(921) 855-0482");
        result.addContact(ContactType.MAIL, "gkislin@yandex.ru");
        result.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        result.addContact(ContactType.SKYPE, "grigory.kislin");
        result.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        result.addContact(ContactType.STACKOVFLW, "https://stackoverflow.com/users/548473");

        Section objective = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        Section personal = new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");

        Section achievments = new ListSection(List.of(("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", " +
                        "\"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). " +
                        "Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов." +
                        "Более 1000 выпускников."),
                ("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. " +
                        "Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk."),
                ("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С," +
                        " Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery." +
                        " Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.")));
        Section qualification = new ListSection(List.of(("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2"),
                ("Version control: Subversion, Git, Mercury, ClearCase, Perforce"),
                "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,"));

        Organization jops = new Organization("Java Online Projects", "http://javaops.ru/", new Organization.Position(2013, Month.OCTOBER, "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок."));
        Organization wrike = new Organization("Wrike", "https://www.wrike.com/", new Organization.Position(2014, Month.OCTOBER, 2016, Month.JANUARY,
                "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike..."));
        Organization withEmptyLink = new Organization("EmptyLink", null, new Organization.Position(2014, Month.OCTOBER, 2016, Month.JANUARY,
                "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike..."));

        Section experience = new OrganizationSection(List.of(jops, wrike, withEmptyLink));

        Organization coursera = new Organization("Coursera", "https://www.coursera.org/course/progfun", new Organization.Position(2013, Month.FEBRUARY, 2013, Month.MARCH,
                "Functional Programming Principles in Scala by Martin Odersky", null));
        Organization withoutUrl = new Organization("WithoutUrl", null, new Organization.Position(2013, Month.FEBRUARY, 2013, Month.MARCH,
                "Functional Programming Principles in Scala by Martin Odersky", null));
        Organization spbu = new Organization("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
                "http://www.ifmo.ru/", new Organization.Position(1993, Month.SEPTEMBER, 1996, Month.JULY, "Аспирантура (программист С, С++)", null),
                new Organization.Position(1987, Month.SEPTEMBER, 1993, Month.JULY, "Инженер (программист Fortran, C)", null));
        Section education = new OrganizationSection(List.of(withoutUrl, coursera, spbu));


        result.addSection(SectionType.OBJECTIVE, objective);
        result.addSection(SectionType.PERSONAL, personal);
        result.addSection(SectionType.QUALIFICATIONS, qualification);
        result.addSection(SectionType.ACHIEVEMENT, achievments);
        result.addSection(SectionType.EXPERIENCE, experience);
        result.addSection(SectionType.EDUCATION, education);

        return result;
    }
}
