package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPS = new File("./config/resumes.properties");
    private static final Config INSTANCE = new Config();
    private final Properties properties = new Properties();
    private final File storageDir;
    private final SqlStorage dbStorage;

    private Config() {
        try (InputStream is = new FileInputStream(PROPS)) {
            properties.load(is);
            storageDir = new File(properties.getProperty("storage.dir"));
            dbStorage = new SqlStorage(properties.getProperty("db.url"), properties.getProperty("db.user"), properties.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Properties getProperties() {
        return properties;
    }

    public SqlStorage getDbStorage() {
        return dbStorage;
    }
}
