package com.luizjacomn.dira;

import com.luizjacomn.dira.persistence.config.ConnectionConfig;
import com.luizjacomn.dira.persistence.config.MigrationConfig;
import com.luizjacomn.dira.ui.MainMenu;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        try (var connection = ConnectionConfig.getConnection()) {
            new MigrationConfig(connection).execute();
        }

        new MainMenu().execute();
    }

}
