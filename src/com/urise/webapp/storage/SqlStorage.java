package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.model.sections.ListSection;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.model.sections.TextSection;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper helper;
    static final String JDBC_DRIVER = "org.postgresql.Driver";

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new StorageException(e.getMessage(), e);
        }
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        LOG.info("Clear resume table");
        helper.executeQuery("DELETE FROM resume");
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        LOG.info("Update " + uuid);

        helper.transactionalExecute(connection -> {
            helper.executeStatement(connection, "UPDATE resume SET full_name=? WHERE uuid=?", prepSt -> {
                prepSt.setString(1, resume.getFullName());
                prepSt.setString(2, uuid);
                if (prepSt.executeUpdate() != 1) {
                    throw new NotExistStorageException(uuid);
                }
                return null;
            });

            deleteResumePart(connection, "DELETE FROM contact c WHERE c.resume_uuid =?", uuid);
            deleteResumePart(connection, "DELETE FROM section c WHERE c.resume_uuid =?", uuid);

            putContacts(connection, resume);
            putSections(connection, resume);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume.getUuid());
        helper.transactionalExecute(connection -> {
            helper.executeStatement(connection, "INSERT INTO resume (uuid, full_name) VALUES (?,?)", prepSt -> {
                prepSt.setString(1, resume.getUuid());
                prepSt.setString(2, resume.getFullName());
                prepSt.execute();
                return null;
            });
            putContacts(connection, resume);
            putSections(connection, resume);
            return null;
        });

    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get from database " + uuid);
        return helper.transactionalExecute(connection -> {
            Resume result;

            try (PreparedStatement prepSt = connection.prepareStatement("" +
                    "    SELECT * FROM resume r " +
                    " LEFT JOIN contact c " +
                    "        ON r.uuid = c.resume_uuid " +
                    "     WHERE r.uuid =? ")) {
                prepSt.setString(1, uuid);
                ResultSet resultSet = prepSt.executeQuery();

                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                String fullName = resultSet.getString("full_name");
                result = new Resume(uuid, fullName);
                do {
                    readContacts(result, resultSet);
                } while (resultSet.next());
            }

            try (PreparedStatement prepSt = connection.prepareStatement("" +
                    "    SELECT * FROM section s " +
                    "     WHERE s.resume_uuid=? ")) {
                prepSt.setString(1, uuid);
                ResultSet resultSet = prepSt.executeQuery();
                if (resultSet.next()) {
                    do {
                        readSections(result, resultSet);
                    } while (resultSet.next());
                }
            }

            return result;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete from database " + uuid);
        helper.<Void>executeQuery("DELETE FROM resume r WHERE r.uuid =?", (prepSt) -> {
            prepSt.setString(1, uuid);
            if (prepSt.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        Map<String, Resume> allResume = new LinkedHashMap<>();

        helper.executeQuery("SELECT * FROM resume ORDER BY full_name, uuid", (prepSt) -> {
            ResultSet resultSet = prepSt.executeQuery();
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String full_name = resultSet.getString("full_name");
                allResume.put(uuid, new Resume(uuid, full_name));
            }
            return null;
        });

        //get all contacts from contact table
        helper.executeQuery("SELECT * FROM contact", (prepSr) -> {
            ResultSet resultSet = prepSr.executeQuery();
            while (resultSet.next()) {

                // define the uuid of the resume, on the cortege of which the cursor located
                String currentResumeUuid = resultSet.getString("resume_uuid");

                //read from db contacts for current resume
                readContacts(allResume.get(currentResumeUuid), resultSet);
            }
            return null;
        });

        helper.executeQuery("SELECT * FROM section", prepSt -> {
            ResultSet resultSet = prepSt.executeQuery();
            while (resultSet.next()) {

                // define the uuid of the resume, on the cortege of which the cursor located
                String currentResumeUuid = resultSet.getString("resume_uuid");

                //read from db sections for current resume
                readSections(allResume.get(currentResumeUuid), resultSet);
            }
            return null;
        });
        return new ArrayList<>(allResume.values());
    }

    @Override
    public int size() {
        return helper.executeQuery("SELECT COUNT(*) FROM resume", (prepSt) -> {
            ResultSet resultSet = prepSt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        });
    }

    private void putContacts(Connection connection, Resume resume) throws SQLException {
        Map<ContactType, String> contacts = resume.getContacts();
        if (contacts.isEmpty() || contacts == null) {
            return;
        }
        String uuid = resume.getUuid();
        helper.executeStatement(connection, "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)", prepSt -> {
            for (Map.Entry<ContactType, String> pair : contacts.entrySet()) {
                prepSt.setString(1, uuid);
                prepSt.setString(2, pair.getKey().name());
                prepSt.setString(3, pair.getValue());
                prepSt.addBatch();
            }
            prepSt.executeBatch();
            return null;
        });
    }

    private void putSections(Connection connection, Resume resume) throws SQLException {
        Map<SectionType, Section> sections = resume.getSections();
        if (sections.isEmpty() || sections == null) {
            return;
        }
        String uuid = resume.getUuid();
        helper.executeStatement(connection, "INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)", prepSt -> {
            for (Map.Entry<SectionType, Section> pair : sections.entrySet()) {
                SectionType type = pair.getKey();
                String value = null;

                switch (type) {
                    case PERSONAL, OBJECTIVE -> value = pair.getValue().toString();
                    case ACHIEVEMENT, QUALIFICATIONS -> value = String.join("\n", ((ListSection) pair.getValue()).getItems());
                }

                prepSt.setString(1, uuid);
                prepSt.setString(2, type.name());
                prepSt.setString(3, value);
                prepSt.addBatch();
            }
            prepSt.executeBatch();
            return null;
        });
    }

    private void readContacts(Resume resume, ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            ContactType conType = ContactType.valueOf(resultSet.getString("type"));
            resume.addContact(conType, value);
        }
    }

    private void readSections(Resume resume, ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("type");
        String value = resultSet.getString("value");
        SectionType sectionType = SectionType.valueOf(type);
        switch (sectionType) {
            case PERSONAL, OBJECTIVE -> resume.addSection(sectionType, new TextSection(value));
            case ACHIEVEMENT, QUALIFICATIONS -> resume.addSection(sectionType, new ListSection(List.of(value.split("\n"))));
        }
    }

    private void deleteResumePart(Connection connection, String query, String uuid) throws SQLException {
        helper.executeStatement(connection, query, prepSt -> {
            prepSt.setString(1, uuid);
            prepSt.execute();
            return null;
        });
    }
}
