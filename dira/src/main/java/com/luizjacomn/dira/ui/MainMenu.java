package com.luizjacomn.dira.ui;

import com.luizjacomn.dira.persistence.config.ConnectionConfig;
import com.luizjacomn.dira.persistence.dao.BoardColumnDAO;
import com.luizjacomn.dira.persistence.dao.BoardDAO;
import com.luizjacomn.dira.persistence.entity.Board;
import com.luizjacomn.dira.persistence.entity.BoardColumn;
import com.luizjacomn.dira.persistence.enumeration.Kind;
import com.luizjacomn.dira.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        var option = -1;

        while (true) {
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }

    private void createBoard() throws SQLException {
        var board = new Board();
        System.out.println("Informe o nome do seu board");
        board.setName(scanner.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim informe quantas, senão digite '0'");
        var additionalColumns = scanner.nextInt();

        var columns = new ArrayList<BoardColumn>();

        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, Kind.INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente do board");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, Kind.PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, Kind.FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do board");
        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, Kind.CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        board.setBoardColumns(columns);
        try (var connection = ConnectionConfig.getConnection()) {
            var service = new BoardService(BoardDAO.transactional(connection), BoardColumnDAO.transactional(connection));
            service.insert(board);
        }

    }

    private void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar");
        var id = scanner.nextLong();

        try (var connection = ConnectionConfig.getConnection()) {
            var service = new BoardService(BoardDAO.transactional(connection), BoardColumnDAO.transactional(connection));
            var optional = service.findById(id);
            optional.ifPresentOrElse(
                b -> new BoardMenu(b).execute(),
                () -> System.out.printf("Não foi encontrado um board com id %s\n", id)
            );
        }
    }

    private void deleteBoard() throws SQLException {
        System.out.println("Informe o id do board que será excluido");
        var id = scanner.nextLong();

        try (var connection = ConnectionConfig.getConnection()) {
            var service = new BoardService(BoardDAO.transactional(connection), BoardColumnDAO.transactional(connection));

            if (service.delete(id)) {
                System.out.printf("O board %s foi excluido\n", id);
            } else {
                System.out.printf("Não foi encontrado um board com id %s\n", id);
            }
        }
    }

    private BoardColumn createColumn(final String name, final Kind kind, final int order) {
        var boardColumn = new BoardColumn();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }

}
