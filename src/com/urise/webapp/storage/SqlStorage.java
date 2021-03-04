package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    private final static Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    private final SqlHelper helper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        LOG.info("Clear resume table");
        helper.executeSimpleQuery("DELETE FROM resume");
    }

    @Override
    public void update(Resume r) {
        LOG.info("Update " + r.getUuid());
        helper.executeQuery("UPDATE resume SET full_name=? WHERE uuid=?", (prepSt) -> {
            helper.setStrings(prepSt, r.getFullName(), r.getUuid());
            if (prepSt.executeUpdate() != 1) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume.getUuid());
        helper.executeSimpleQuery("INSERT INTO resume (uuid, full_name) VALUES (?,?)", resume.getUuid(), resume.getFullName());
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get from database " + uuid);
        return helper.executeQuery("SELECT * FROM resume r WHERE r.uuid =?", (prepSt) -> {
            helper.setStrings(prepSt, uuid);
            ResultSet resultSet = prepSt.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            String fullName = resultSet.getString("full_name");
            return new Resume(uuid.trim(), fullName.trim());
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete from database " + uuid);
        helper.executeQuery("DELETE FROM resume r WHERE r.uuid =?", (prepSt) -> {
            helper.setStrings(prepSt, uuid);
            if (prepSt.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        return helper.executeQuery("SELECT * FROM resume", (prepSt) -> {
            List<Resume> allResume = new ArrayList<>();
            ResultSet resultSet = prepSt.executeQuery();
            while (resultSet.next()) {
                allResume.add(get(resultSet.getString("uuid")));
            }
            allResume.sort(RESUME_COMPARATOR);
            return allResume;
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
}
