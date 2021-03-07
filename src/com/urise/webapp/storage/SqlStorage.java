package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
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

            //delete all contacts for current resume
            helper.executeStatement(connection, "DELETE FROM contact c WHERE c.resume_uuid =?", prepSt -> {
                prepSt.setString(1, uuid);
                prepSt.execute();
                return null;
            });

            //insert new contacts for current resume
            putContacts(connection, resume);
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
            return null;
        });

    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get from database " + uuid);
        return helper.executeQuery("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "     WHERE r.uuid =? ",
                (prepSt) -> {
                    prepSt.setString(1, uuid);
                    ResultSet resultSet = prepSt.executeQuery();
                    if (!resultSet.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    String fullName = resultSet.getString("full_name");
                    Resume result = new Resume(uuid, fullName);
                    readContacts(result, resultSet);
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

        for (Map.Entry<String, Resume> pair : allResume.entrySet()
        ) {
            helper.executeQuery("SELECT * FROM contact WHERE resume_uuid=?", (prepSr) -> {
                prepSr.setString(1, pair.getKey());
                ResultSet resultSet = prepSr.executeQuery();
                if (resultSet.next()) {
                    readContacts(pair.getValue(), resultSet);
                }
                return null;
            });
        }
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
        String uuid = resume.getUuid();
        helper.executeStatement(connection, "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)", prepSt -> {
            for (Map.Entry<ContactType, String> pair : resume.getContacts().entrySet()) {
                prepSt.setString(1, uuid);
                prepSt.setString(2, pair.getKey().name());
                prepSt.setString(3, pair.getValue());
                prepSt.addBatch();
            }
            prepSt.executeBatch();
            return null;
        });
    }

    private void readContacts(Resume resume, ResultSet resultSet) throws SQLException {
        do {
            String value = resultSet.getString("value");
            if (value == null) continue;
            ContactType conType = ContactType.valueOf(resultSet.getString("type"));
            resume.addContact(conType, value);
        } while (resultSet.next());
    }
}
