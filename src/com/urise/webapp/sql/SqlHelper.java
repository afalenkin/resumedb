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

    public void executeQuery(String query) {
        executeQuery(query, PreparedStatement::execute);
    }

    private StorageException switchException(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            return new ExistStorageException(e);
        }
        return new StorageException(e.getMessage(), e);
    }

    public <T> T transactionalExecute(SqlTransaction<T> transExecutor) {
        try (Connection connection = connectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                T result = transExecutor.execute(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw switchException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }


}

