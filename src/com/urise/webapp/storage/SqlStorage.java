package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

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

            putContactsToDb(connection, resume);
            putSectionsToDb(connection, resume);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume.getUuid());
        helper.transactionalExecute(connection -> {

            try(PreparedStatement statement = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")){
                statement.setString(1, resume.getUuid());
                statement.setString(2, resume.getFullName());
                statement.execute();
            }

            putContactsToDb(connection, resume);
            putSectionsToDb(connection, resume);
            return null;
        });

    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get from database " + uuid);
        return helper.transactionalExecute(connection -> {
            Resume result;
            try (PreparedStatement statement = connection.prepareStatement( "    SELECT * FROM resume WHERE uuid =? ")){
                statement.setString(1, uuid);
                ResultSet resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                result = new Resume(uuid, resultSet.getString("full_name"));
            }

            try (PreparedStatement prepSt = connection.prepareStatement("SELECT * FROM contact WHERE resume_uuid=? ")) {
                prepSt.setString(1, uuid);
                ResultSet resultSet = prepSt.executeQuery();
                if (resultSet.next()) {
                    do {
                        readDbContacts(result, resultSet);
                    } while (resultSet.next());
                }
            }

            try (PreparedStatement prepSt = connection.prepareStatement("SELECT * FROM section WHERE resume_uuid=? ")) {
                prepSt.setString(1, uuid);
                ResultSet resultSet = prepSt.executeQuery();
                if (resultSet.next()) {
                    do {
                        readDbSections(result, resultSet);
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

       return helper.transactionalExecute(connection -> {
            Map<String, Resume> allResume = new LinkedHashMap<>();

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")){
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String uuid = resultSet.getString("uuid");
                    allResume.put(uuid, new Resume(uuid, resultSet.getString("full_name")));
                }
            }

            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {

                    // define the uuid of the resume, on the cortege of which the cursor located
                    String currentResumeUuid = resultSet.getString("resume_uuid");

                    //read from db contacts for current resume
                    readDbContacts(allResume.get(currentResumeUuid), resultSet);
                }
            }

            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM section")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {

                    // define the uuid of the resume, on the cortege of which the cursor located
                    String currentResumeUuid = resultSet.getString("resume_uuid");

                    //read from db contacts for current resume
                    readDbSections(allResume.get(currentResumeUuid), resultSet);
                }
            }

            return new ArrayList<>(allResume.values());
        });
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

    private void putContactsToDb(Connection connection, Resume resume) throws SQLException {
        Map<ContactType, String> contacts = resume.getContacts();
        if (!contacts.isEmpty()) {
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
    }

    private void putSectionsToDb(Connection connection, Resume resume) throws SQLException {
        Map<SectionType, Section> sections = resume.getSections();
        if (!sections.isEmpty() ) {
            String uuid = resume.getUuid();
            helper.executeStatement(connection, "INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)", prepSt -> {
                for (Map.Entry<SectionType, Section> pair : sections.entrySet()) {
                    SectionType type = pair.getKey();
                    Section value = pair.getValue();
                    prepSt.setString(1, uuid);
                    prepSt.setString(2, type.name());
                    prepSt.setString(3, JsonParser.write(value, Section.class));
                    prepSt.addBatch();
                }
                prepSt.executeBatch();
                return null;
            });
        }
    }

    private void readDbContacts(Resume resume, ResultSet resultSet) throws SQLException {
        String value = resultSet.getString("value");
        if (value != null) {
            ContactType conType = ContactType.valueOf(resultSet.getString("type"));
            resume.addContact(conType, value);
        }
    }

    private void readDbSections(Resume resume, ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("type");
        String value = resultSet.getString("value");
        if (value != null) {
            SectionType sectionType = SectionType.valueOf(type);
            resume.addSection(sectionType, JsonParser.read(value, Section.class));
        }
    }

    private void deleteResumePart(Connection connection, String query, String uuid) throws SQLException {
            try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, uuid);
            statement.execute();
        }
    }
}
