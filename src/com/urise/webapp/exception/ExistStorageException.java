package com.urise.webapp.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super("Resume "+ uuid + " currently exist!",uuid);
    }


}
