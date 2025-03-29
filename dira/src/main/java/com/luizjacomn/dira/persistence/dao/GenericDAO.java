package com.luizjacomn.dira.persistence.dao;

import com.luizjacomn.dira.function.CheckedConsumer;
import com.luizjacomn.dira.persistence.entity.DiraEntity;
import com.luizjacomn.dira.persistence.entity.DiraTable;
import com.mysql.cj.jdbc.StatementImpl;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class GenericDAO<T extends DiraEntity> {

    protected final Connection connection;

    protected final boolean commitEnabled;

    private final Class<T> entityClass;

    @SuppressWarnings("all")
    protected GenericDAO(Connection connection, boolean commitEnabled) {
        this.connection = connection;
        this.commitEnabled = commitEnabled;
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected GenericDAO(Connection connection) {
        this(connection, false);
    }

    protected abstract T fromResultSet(ResultSet resultSet) throws SQLException;

    protected abstract String[] getInsertColumns(T t);

    protected abstract CheckedConsumer<PreparedStatement, SQLException> setInsertParameters(T t);

    public void insert(T t) throws SQLException {
        var sql = "INSERT INTO %s (%s) VALUES (%s);"
            .formatted(
                entityClass.getAnnotation(DiraTable.class).value(),
                String.join(", ", this.getInsertColumns(t)),
                Stream.of(this.getInsertColumns(t)).map(column -> "?").collect(Collectors.joining(", "))
            );

        try (var preparedStatement = connection.prepareStatement(sql)) {
            this.setInsertParameters(t).accept(preparedStatement);
            preparedStatement.executeUpdate();

            if (preparedStatement instanceof StatementImpl impl) {
                t.setId(impl.getLastInsertID());
            }

            if (commitEnabled) {
                connection.commit();
            }
        } catch (SQLException e) {
            if (commitEnabled) {
                connection.rollback();
            }

            throw new RuntimeException(e);
        }
    }

    public void update(Long id, T t) throws SQLException {
        var sql = "UPDATE %s SET %s WHERE id = ?;"
            .formatted(
                entityClass.getAnnotation(DiraTable.class).value(),
                Stream.of(this.getInsertColumns(t)).map(column -> column + " = ?").collect(Collectors.joining(", "))
            );

        try (var preparedStatement = connection.prepareStatement(sql)) {
            this.setInsertParameters(t).accept(preparedStatement);
            preparedStatement.setLong(this.getInsertColumns(t).length + 1, id);
            preparedStatement.executeUpdate();

            if (commitEnabled) {
                connection.commit();
            }
        } catch (SQLException e) {
            if (commitEnabled) {
                connection.rollback();
            }

            throw new RuntimeException(e);
        }
    }

    protected String findByIdSelect() {
        return "*";
    }

    public Optional<T> findById(Long id) {
        var sql = "SELECT %s FROM %s WHERE id = ?".formatted(this.findByIdSelect(), entityClass.getAnnotation(DiraTable.class).value());

        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeQuery();
            if (preparedStatement.getResultSet().next()) {
                var entity = this.fromResultSet(preparedStatement.getResultSet());
                return Optional.of(entity);
            }

            throw new IllegalStateException();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean exists(Long id) {
        var sql = "SELECT 1 FROM %s WHERE id = ?".formatted(entityClass.getAnnotation(DiraTable.class).value());

        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeQuery();
            return preparedStatement.getResultSet().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) throws SQLException {
        var sql = "DELETE FROM %s WHERE id = ?".formatted(entityClass.getAnnotation(DiraTable.class).value());

        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            if (commitEnabled) {
                connection.commit();
            }
        } catch (SQLException e) {
            if (commitEnabled) {
                connection.rollback();
            }

            throw new RuntimeException(e);
        }
    }

}
