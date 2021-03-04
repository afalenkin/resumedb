package com.urise.webapp.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String uuid) {
        super("Resume " + uuid + " is NOT exist!", uuid);
    }

    public NotExistStorageException(Exception e) {
        super(e.getMessage());
    }
}
