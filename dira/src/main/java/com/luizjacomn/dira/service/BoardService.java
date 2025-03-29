package com.luizjacomn.dira.service;

import com.luizjacomn.dira.dto.BoardDetailsDTO;
import com.luizjacomn.dira.persistence.dao.BoardColumnDAO;
import com.luizjacomn.dira.persistence.dao.BoardDAO;
import com.luizjacomn.dira.persistence.entity.Board;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class BoardService {

    private final BoardDAO boardDAO;

    private final BoardColumnDAO boardColumnDAO;

    public void insert(final Board entity) throws SQLException {
        boardDAO.insert(entity);

        var columns = entity.getBoardColumns().stream().peek(c -> c.setBoard(entity)).toList();

        for (var column : columns) {
            boardColumnDAO.insert(column);
        }
    }

    public Optional<Board> findById(final Long id) throws SQLException {
        var optional = boardDAO.findById(id);

        if (optional.isPresent()){
            var entity = optional.get();
            entity.setBoardColumns(boardColumnDAO.findByBoardId(entity.getId()));
            return Optional.of(entity);
        }

        return Optional.empty();
    }

    public boolean delete(Long id) {
        try {
            boardDAO.delete(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException {
        var optional = boardDAO.findById(id);

        if (optional.isPresent()){
            var entity = optional.get();
            var columns = boardColumnDAO.findByBoardIdWithDetails(entity.getId());
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
            return Optional.of(dto);
        }

        return Optional.empty();
    }

}
