package com.luizjacomn.dira.persistence.dao;

import com.luizjacomn.dira.function.CheckedConsumer;
import com.luizjacomn.dira.persistence.converter.OffsetDateTimeConverter;
import com.luizjacomn.dira.persistence.entity.Block;
import com.luizjacomn.dira.persistence.entity.Card;
import com.luizjacomn.dira.persistence.entity.DiraTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

public class BlockDAO extends GenericDAO<Block> {

    protected BlockDAO(Connection connection) {
        super(connection);
    }

    protected BlockDAO(Connection connection, boolean commitEnabled) {
        super(connection, commitEnabled);
    }

    public static BlockDAO transactional(Connection connection) {
        return new BlockDAO(connection, true);
    }

    public static BlockDAO untransactional(Connection connection) {
        return new BlockDAO(connection);
    }

    @Override
    protected Block fromResultSet(ResultSet resultSet) throws SQLException {
        var block = new Block();
        block.setId(resultSet.getLong("id"));
        block.setBlockedAt(OffsetDateTimeConverter.toOffsetDateTime(resultSet.getTimestamp("blocked_at")));
        block.setBlockReason(resultSet.getString("block_reason"));
        block.setUnblockedAt(OffsetDateTimeConverter.toOffsetDateTime(resultSet.getTimestamp("unblocked_at")));
        block.setUnblockReason(resultSet.getString("unblock_reason"));
        block.setCard(new Card(resultSet.getLong("card_id")));

        return block;
    }

    @Override
    protected String[] getInsertColumns(Block block) {
        if (block.getUnblockedAt() != null && block.getUnblockReason() != null) {
            return new String[] { "blocked_at", "block_reason", "card_id", "unblocked_at", "unblock_reason" };
        }

        return new String[] { "blocked_at", "block_reason", "card_id" };
    }

    @Override
    protected CheckedConsumer<PreparedStatement, SQLException> setInsertParameters(Block block) {
        return preparedStatement -> {
            preparedStatement.setTimestamp(1, OffsetDateTimeConverter.toTimestamp(block.getBlockedAt()));
            preparedStatement.setString(2, block.getBlockReason());
            preparedStatement.setLong(3, block.getCard().getId());

            if (block.getUnblockedAt() != null && block.getUnblockReason() != null) {
                preparedStatement.setTimestamp(4, OffsetDateTimeConverter.toTimestamp(block.getUnblockedAt()));
                preparedStatement.setString(5, block.getUnblockReason());
            }
        };
    }

    public Block block(Long cardId, String reason) throws SQLException {
        var block = new Block();
        block.setBlockedAt(OffsetDateTime.now());
        block.setBlockReason(reason);
        block.setCard(new Card(cardId));

        this.insert(block);

        return this.findById(block.getId()).orElseThrow();
    }

    public void unblock(Long id, String reason) throws SQLException {
        var block = this.findByCardId(id).orElseThrow();
        block.setUnblockedAt(OffsetDateTime.now());
        block.setUnblockReason(reason);

        this.update(id, block);
    }

    private Optional<Block> findByCardId(Long id) {
        var sql = "SELECT * FROM block WHERE card_id = ? AND unblock_reason IS NULL";

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

}
