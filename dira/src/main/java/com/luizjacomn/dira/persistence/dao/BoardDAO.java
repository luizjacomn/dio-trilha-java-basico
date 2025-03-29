package com.luizjacomn.dira.persistence.dao;

import com.luizjacomn.dira.function.CheckedConsumer;
import com.luizjacomn.dira.persistence.entity.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardDAO extends GenericDAO<Board> {

    protected BoardDAO(Connection connection) {
        super(connection);
    }

    protected BoardDAO(Connection connection, boolean commitEnabled) {
        super(connection, commitEnabled);
    }

    public static BoardDAO transactional(Connection connection) {
        return new BoardDAO(connection, true);
    }

    public static BoardDAO untransactional(Connection connection) {
        return new BoardDAO(connection);
    }

    @Override
    protected Board fromResultSet(ResultSet resultSet) throws SQLException {
        var board = new Board();
        board.setId(resultSet.getLong("id"));
        board.setName(resultSet.getString("name"));

        return board;
    }

    @Override
    protected String[] getInsertColumns(Board board) {
        return new String[] { "name" };
    }

    @Override
    protected CheckedConsumer<PreparedStatement, SQLException> setInsertParameters(Board board) {
        return preparedStatement -> {
            preparedStatement.setString(1, board.getName());
        };
    }

}
