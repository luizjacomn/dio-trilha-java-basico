package com.luizjacomn.dira.persistence.dao;

import com.luizjacomn.dira.dto.BoardColumnDTO;
import com.luizjacomn.dira.function.CheckedConsumer;
import com.luizjacomn.dira.persistence.entity.Board;
import com.luizjacomn.dira.persistence.entity.BoardColumn;
import com.luizjacomn.dira.persistence.entity.Card;
import com.luizjacomn.dira.persistence.enumeration.Kind;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BoardColumnDAO extends GenericDAO<BoardColumn> {

    protected BoardColumnDAO(Connection connection) {
        super(connection);
    }

    protected BoardColumnDAO(Connection connection, boolean commitEnabled) {
        super(connection, commitEnabled);
    }

    public static BoardColumnDAO transactional(Connection connection) {
        return new BoardColumnDAO(connection, true);
    }

    public static BoardColumnDAO untransactional(Connection connection) {
        return new BoardColumnDAO(connection);
    }

    @Override
    protected BoardColumn fromResultSet(ResultSet resultSet) throws SQLException {
        var boardColumn = new BoardColumn();
        boardColumn.setName(resultSet.getString("name"));
        boardColumn.setOrder(resultSet.getInt("order"));
        boardColumn.setKind(Kind.valueOf(resultSet.getString("kind")));
        boardColumn.setBoard(new Board(resultSet.getLong("board_id")));

        return boardColumn;
    }

    @Override
    protected String[] getInsertColumns(BoardColumn boardColumn) {
        return new String[] { "name", "`order`", "kind", "board_id" };
    }

    @Override
    protected CheckedConsumer<PreparedStatement, SQLException> setInsertParameters(BoardColumn boardColumn) {
        return preparedStatement -> {
            preparedStatement.setString(1, boardColumn.getName());
            preparedStatement.setInt(2, boardColumn.getOrder());
            preparedStatement.setString(3, boardColumn.getKind().name());
            preparedStatement.setLong(4, boardColumn.getBoard().getId());
        };
    }

    @Override
    public Optional<BoardColumn> findById(final Long boardId) {
        var sql = """
            SELECT
                bc.name,
                bc.kind,
                c.id,
                c.title,
                c.description
            FROM board_column bc
            LEFT JOIN card c ON c.board_column_id = bc.id
            WHERE bc.id = ?;
        """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();

            var resultSet = statement.getResultSet();
            if (resultSet.next()){
                var boardColumn = new BoardColumn();
                boardColumn.setName(resultSet.getString("bc.name"));
                boardColumn.setKind(Kind.valueOf(resultSet.getString("bc.kind")));

                do {
                    var card = new Card();
                    if (Objects.isNull(resultSet.getString("c.title"))){
                        break;
                    }

                    card.setId(resultSet.getLong("c.id"));
                    card.setTitle(resultSet.getString("c.title"));
                    card.setDescription(resultSet.getString("c.description"));
                    boardColumn.getCards().add(card);
                } while (resultSet.next());

                return Optional.of(boardColumn);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BoardColumn> findByBoardId(final Long boardId) throws SQLException{
        var entities = new ArrayList<BoardColumn>();
        var sql = "SELECT id, name, `order`, kind FROM board_column WHERE board_id = ? ORDER BY `order`";

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();

            while (resultSet.next()) {
                var entity = new BoardColumn();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(Kind.valueOf(resultSet.getString("kind")));
                entities.add(entity);
            }

            return entities;
        }
    }

    public List<BoardColumnDTO> findByBoardIdWithDetails(final Long boardId) throws SQLException {
        var dtos = new ArrayList<BoardColumnDTO>();

        var sql = """
            SELECT bc.id,
                   bc.name,
                   bc.kind,
                   (SELECT COUNT(c.id) FROM card c WHERE c.board_column_id = bc.id) AS cards_amount
            FROM board_column bc
            WHERE board_id = ?
            ORDER BY `order`;
        """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();

            var resultSet = statement.getResultSet();
            while (resultSet.next()) {
                var dto = new BoardColumnDTO(
                    resultSet.getLong("bc.id"),
                    resultSet.getString("bc.name"),
                    Kind.valueOf(resultSet.getString("bc.kind")),
                    resultSet.getInt("cards_amount")
                );
                dtos.add(dto);
            }

            return dtos;
        }
    }

}
