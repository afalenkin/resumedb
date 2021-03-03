package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    public final ConnectionFactory connectionFactory;
    private final static Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    private final SqlHelper helper = new SqlHelper();

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public SqlStorage(Properties properties) {
        this(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
    }

    @Override
    public void clear() {
        LOG.info("Clear resume table");
        helper.executeQuery(connectionFactory, "DELETE FROM resume");
    }

    @Override
    public void update(Resume r) {
        LOG.info("Update " + r.getUuid());
        helper.executeQuery(connectionFactory, () -> {
            if (helper.getPreparedStatement().executeUpdate() != 1) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        }, "UPDATE resume SET full_name=? WHERE uuid=?", r.getFullName(), r.getUuid());

    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume.getUuid());
        try {
            helper.executeQuery(connectionFactory,
                    "INSERT INTO resume (uuid, full_name) VALUES (?,?)", resume.getUuid(), resume.getFullName());
        } catch (StorageException e) {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get from database " + uuid);
        try {
            return helper.executeQuery(connectionFactory, () -> {
                ResultSet resultSet = helper.getPreparedStatement().executeQuery();
                if (!resultSet.next()) {
                    throw new NotExistStorageException(uuid);
                }
                String fullName = resultSet.getString("full_name");
                return new Resume(uuid.trim(), fullName.trim());
            }, "SELECT * FROM resume r WHERE r.uuid =?", uuid);
        } catch (StorageException e) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete from database " + uuid);
        helper.executeQuery(connectionFactory, () -> {
            if (helper.getPreparedStatement().executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        }, "DELETE FROM resume r WHERE r.uuid =?", uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        return helper.executeQuery(connectionFactory, () -> {
            List<Resume> allResume = new ArrayList<>();
            ResultSet resultSet = helper.getPreparedStatement().executeQuery();
            while (resultSet.next()) {
                allResume.add(get(resultSet.getString("uuid")));
            }
            allResume.sort(RESUME_COMPARATOR);
            return allResume;
        }, "SELECT * FROM resume");
    }

    @Override
    public int size() {
        return helper.executeQuery(connectionFactory, () -> {
            ResultSet resultSet = helper.getPreparedStatement().executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        }, "SELECT COUNT(*) FROM resume");
    }
}
