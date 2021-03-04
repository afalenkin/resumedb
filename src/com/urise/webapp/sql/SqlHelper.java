package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T executeQuery(String query, SqlExecutor<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return executor.execute(preparedStatement);
        } catch (SQLException e) {
            throw switchException(e);
        }
    }

    public void executeSimpleQuery(String query, String... strings) {
        executeQuery(query, (preparedStatement) -> {
            if (strings.length > 0) {
                setStrings(preparedStatement, strings);
            }
            preparedStatement.execute();
            return null;
        });
    }

    public void setStrings(PreparedStatement preparedStatement, String... strings) throws SQLException {
        for (int i = 0; i < strings.length; i++) {
            preparedStatement.setString(i + 1, strings[i]);
        }
    }

    private StorageException switchException(SQLException e) {
        switch (e.getErrorCode()) {
            case 0:
                return new ExistStorageException(e);
        }
        return new StorageException(e.getMessage(), e);
    }


}

