package com.luizjacomn.dira.service;

import com.luizjacomn.dira.persistence.dao.BoardColumnDAO;
import com.luizjacomn.dira.persistence.entity.BoardColumn;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnDAO boardColumnDAO;

    public Optional<BoardColumn> findById(final Long id) throws SQLException {
        return boardColumnDAO.findById(id);
    }

}
