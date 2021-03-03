package com.urise.webapp.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlExecutor<T> {
    T execute() throws SQLException;
}
