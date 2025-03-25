package com.luizjacomn.dira.service;

import com.luizjacomn.dira.dto.BoardColumnInfoDTO;
import com.luizjacomn.dira.dto.CardDetailsDTO;
import com.luizjacomn.dira.persistence.dao.BlockDAO;
import com.luizjacomn.dira.persistence.dao.CardDAO;
import com.luizjacomn.dira.persistence.entity.Card;
import com.luizjacomn.dira.persistence.enumeration.Kind;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CardService {

    private final CardDAO cardDAO;

    private final BlockDAO blockDAO;

    public void insert(final Card card) throws SQLException {
        cardDAO.insert(card);
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        return cardDAO.findByIdWithDetails(id);
    }

    public void moveToNextColumn(final Long cardId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        var optional = cardDAO.findByIdWithDetails(cardId);

        var dto = optional.orElseThrow(
            () -> new IllegalStateException("O card de id %s não foi encontrado".formatted(cardId))
        );

        if (dto.blocked()) {
            var message = "O card %s está bloqueado, é necesário desbloquea-lo para mover".formatted(cardId);
            throw new IllegalStateException(message);
        }

        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.columnId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));

        if (Kind.FINAL.equals(currentColumn.kind())){
            throw new IllegalStateException("O card já foi finalizado");
        }

        var nextColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.order() == currentColumn.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));

        cardDAO.moveToColumn(nextColumn.id(), cardId);
    }

    public void cancel(final Long cardId, final Long cancelColumnId ,
                       final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException{
        var optional = cardDAO.findByIdWithDetails(cardId);

        var dto = optional.orElseThrow(
            () -> new IllegalStateException("O card de id %s não foi encontrado".formatted(cardId))
        );

        if (dto.blocked()) {
            var message = "O card %s está bloqueado, é necesário desbloquea-lo para mover".formatted(cardId);
            throw new IllegalStateException(message);
        }

        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.columnId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));

        if (Kind.FINAL.equals(currentColumn.kind())) {
            throw new IllegalStateException("O card já foi finalizado");
        }

        boardColumnsInfo.stream()
                .filter(bc -> bc.order() == currentColumn.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));

        cardDAO.moveToColumn(cancelColumnId, cardId);
    }

    public void block(final Long id, final String reason, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        var optional = cardDAO.findByIdWithDetails(id);

        var dto = optional.orElseThrow(
            () -> new IllegalStateException("O card de id %s não foi encontrado".formatted(id))
        );

        if (dto.blocked()) {
            var message = "O card %s já está bloqueado".formatted(id);
            throw new IllegalStateException(message);
        }

        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.columnId()))
                .findFirst()
                .orElseThrow();

        if (Kind.FINAL.equals(currentColumn.kind()) || Kind.CANCEL.equals(currentColumn.kind())) {
            var message = "O card está em uma coluna do tipo %s e não pode ser bloqueado"
                    .formatted(currentColumn.kind());
            throw new IllegalStateException(message);
        }

        blockDAO.block(id, reason);
    }

    public void unblock(final Long id, final String reason) throws SQLException {
        var optional = cardDAO.findByIdWithDetails(id);

        var dto = optional.orElseThrow(
            () -> new IllegalStateException("O card de id %s não foi encontrado".formatted(id))
        );

        if (!dto.blocked()) {
            var message = "O card %s não está bloqueado".formatted(id);
            throw new IllegalStateException(message);
        }

        blockDAO.unblock(id, reason);
    }
    
}
