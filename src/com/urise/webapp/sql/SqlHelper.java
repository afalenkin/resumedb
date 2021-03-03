package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private PreparedStatement preparedStatement;

    public <T> T executeQuery(ConnectionFactory connectionFactory, SqlExecutor<T> executor, String query, String... strings) {
        try (Connection connection = connectionFactory.getConnection()) {
            preparedStatement = connection.prepareStatement(query);
            int n = strings.length;
            if (n > 0) {
                for (int i = 0; i < n; i++) {
                    preparedStatement.setString(i + 1, strings[i]);
                }
            }
            if (executor == null) {
                preparedStatement.execute();
                return null;
            } else {
                return executor.execute();
            }
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    public <T> T executeQuery(ConnectionFactory connectionFactory, String query, String... strings) {
        return executeQuery(connectionFactory, null, query, strings);
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}

