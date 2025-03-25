package com.luizjacomn.dira.ui;

import com.luizjacomn.dira.dto.BoardColumnInfoDTO;
import com.luizjacomn.dira.persistence.config.ConnectionConfig;
import com.luizjacomn.dira.persistence.dao.BlockDAO;
import com.luizjacomn.dira.persistence.dao.BoardColumnDAO;
import com.luizjacomn.dira.persistence.dao.BoardDAO;
import com.luizjacomn.dira.persistence.dao.CardDAO;
import com.luizjacomn.dira.persistence.entity.Board;
import com.luizjacomn.dira.persistence.entity.BoardColumn;
import com.luizjacomn.dira.persistence.entity.Card;
import com.luizjacomn.dira.service.BoardColumnService;
import com.luizjacomn.dira.service.BoardService;
import com.luizjacomn.dira.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final Board board;

    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada\n", board.getId());

            var option = -1;
            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver coluna com cards");
                System.out.println("8 - Ver card");
                System.out.println("9 - Voltar para o menu anterior um card");
                System.out.println("10 - Sair");
                option = scanner.nextInt();

                switch (option) {
                    case  1 -> createCard();
                    case  2 -> moveCardToNextColumn();
                    case  3 -> blockCard();
                    case  4 -> unblockCard();
                    case  5 -> cancelCard();
                    case  6 -> showBoard();
                    case  7 -> showColumn();
                    case  8 -> showCard();
                    case  9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção do menu");
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() throws SQLException {
        var card = new Card();

        System.out.println("Informe o título do card");
        card.setTitle(scanner.next());

        System.out.println("Informe a descrição do card");
        card.setDescription(scanner.next());

        card.setBoardColumn(board.getInitialColumn());

        try (var connection = ConnectionConfig.getConnection()) {
            new CardService(CardDAO.transactional(connection), BlockDAO.transactional(connection)).insert(card);
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a próxima coluna");
        var cardId = scanner.nextLong();

        var boardColumnsInfo = board.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try (var connection = ConnectionConfig.getConnection()){
            new CardService(CardDAO.transactional(connection), BlockDAO.transactional(connection)).moveToNextColumn(cardId, boardColumnsInfo);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void blockCard() throws SQLException {
        System.out.println("Informe o id do card que será bloqueado");
        var cardId = scanner.nextLong();

        System.out.println("Informe o motivo do bloqueio do card");
        var reason = scanner.next();

        var boardColumnsInfo = board.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try (var connection = ConnectionConfig.getConnection()){
            new CardService(CardDAO.transactional(connection), BlockDAO.transactional(connection)).block(cardId, reason, boardColumnsInfo);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void unblockCard() throws SQLException {
        System.out.println("Informe o id do card que será desbloqueado");
        var cardId = scanner.nextLong();

        System.out.println("Informe o motivo do desbloqueio do card");
        var reason = scanner.next();

        try (var connection = ConnectionConfig.getConnection()) {
            new CardService(CardDAO.transactional(connection), BlockDAO.transactional(connection)).unblock(cardId, reason);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void cancelCard() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();

        var cancelColumn = board.getCancelColumn();

        var boardColumnsInfo = board.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        try (var connection = ConnectionConfig.getConnection()) {
            new CardService(CardDAO.transactional(connection), BlockDAO.transactional(connection)).cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
        } catch (RuntimeException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try (var connection = ConnectionConfig.getConnection()) {
            var optional = new BoardService(BoardDAO.transactional(connection), BoardColumnDAO.transactional(connection)).showBoardDetails(board.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c ->
                    System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showColumn() throws SQLException {
        var columnsIds = board.getBoardColumns().stream().map(BoardColumn::getId).toList();
        var selectedColumnId = -1L;

        while (!columnsIds.contains(selectedColumnId)){
            System.out.printf("Escolha uma coluna do board %s pelo id\n", board.getName());
            board.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumnId = scanner.nextLong();
        }

        try (var connection = ConnectionConfig.getConnection()) {
            var column = new BoardColumnService(BoardColumnDAO.transactional(connection)).findById(selectedColumnId);
            column.ifPresent(co -> {
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s",
                        ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }
    }

    private void showCard() throws SQLException {
        System.out.println("Informe o id do card que deseja visualizar");
        var selectedCardId = scanner.nextLong();

        try (var connection  = ConnectionConfig.getConnection()) {
            new CardService(CardDAO.transactional(connection), BlockDAO.transactional(connection)).findById(selectedCardId)
                .ifPresentOrElse(
                    c -> {
                        System.out.printf("Card %s - %s.\n", c.id(), c.title());
                        System.out.printf("Descrição: %s\n", c.description());
                        System.out.println(c.blocked() ?
                                "Está bloqueado. Motivo: " + c.blockReason() :
                                "Não está bloqueado");
                        System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                        System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                    },
                    () -> System.out.printf("Não existe um card com o id %s\n", selectedCardId)
                );
        }
    }

}
