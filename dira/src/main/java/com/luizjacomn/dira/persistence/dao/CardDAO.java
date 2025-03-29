package com.luizjacomn.dira.persistence.dao;

import com.luizjacomn.dira.dto.CardDetailsDTO;
import com.luizjacomn.dira.function.CheckedConsumer;
import com.luizjacomn.dira.persistence.converter.OffsetDateTimeConverter;
import com.luizjacomn.dira.persistence.entity.BoardColumn;
import com.luizjacomn.dira.persistence.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class CardDAO extends GenericDAO<Card> {

    protected CardDAO(Connection connection) {
        super(connection);
    }

    protected CardDAO(Connection connection, boolean commitEnabled) {
        super(connection, commitEnabled);
    }

    public static CardDAO transactional(Connection connection) {
        return new CardDAO(connection, true);
    }

    public static CardDAO untransactional(Connection connection) {
        return new CardDAO(connection);
    }

    @Override
    protected Card fromResultSet(ResultSet resultSet) throws SQLException {
        var card = new Card();
        card.setId(resultSet.getLong("id"));
        card.setTitle(resultSet.getString("title"));
        card.setDescription(resultSet.getString("description"));
        card.setBoardColumn(new BoardColumn(resultSet.getLong("board_column_id")));

        return card;
    }

    @Override
    protected String[] getInsertColumns(Card card) {
        return new String[] { "title", "description", "board_column_id" };
    }

    @Override
    protected CheckedConsumer<PreparedStatement, SQLException> setInsertParameters(Card card) {
        return preparedStatement -> {
            preparedStatement.setString(1, card.getTitle());
            preparedStatement.setString(2, card.getDescription());
            preparedStatement.setLong(3, card.getBoardColumn().getId());
        };
    }

    public Optional<CardDetailsDTO> findByIdWithDetails(final Long id) throws SQLException {
        var sql = """
            SELECT
               c.id,
               c.title,
               c.description,
               b.blocked_at,
               b.block_reason,
               c.board_column_id,
               bc.name,
               (SELECT COUNT(sub_b.id) FROM block sub_b WHERE sub_b.card_id = c.id) AS blocks_amount
            FROM card c
            LEFT JOIN block b ON c.id = b.card_id AND b.unblocked_at IS NULL
            INNER JOIN board_column bc ON bc.id = c.board_column_id
            WHERE c.id = ?;
        """;

        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();
            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                    resultSet.getLong("c.id"),
                    resultSet.getString("c.title"),
                    resultSet.getString("c.description"),
                    Objects.nonNull(resultSet.getString("b.block_reason")),
                    OffsetDateTimeConverter.toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")),
                    resultSet.getString("b.block_reason"),
                    resultSet.getInt("blocks_amount"),
                    resultSet.getLong("c.board_column_id"),
                    resultSet.getString("bc.name")
                );

                return Optional.of(dto);
            }
        }

        return Optional.empty();
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException{
        try {
            var sql = "UPDATE card SET board_column_id = ? WHERE id = ?;";

            try (var statement = connection.prepareStatement(sql)) {
                statement.setLong(1, columnId);
                statement.setLong(2, cardId);
                statement.executeUpdate();
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

}
