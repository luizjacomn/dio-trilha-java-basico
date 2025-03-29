package com.luizjacomn.dira.persistence.config;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;

@RequiredArgsConstructor
public class MigrationConfig {

    private final Connection connection;

    @SuppressWarnings("all")
    public void execute() {
        var originalOut = System.out;
        var originalError = System.err;

        try (var fos = new FileOutputStream("liquibase.log");
             var connection = ConnectionConfig.getConnection();
             var jdbcConnection = new JdbcConnection(connection);
             var liquibase = new Liquibase(
                 "db/changelog/db.changelog-master.yml",
                 new ClassLoaderResourceAccessor(),
                 jdbcConnection
             )) {
            System.setOut(new PrintStream(fos));
            System.setErr(new PrintStream(fos));
            liquibase.update();
        } catch (Exception e) {
            e.printStackTrace();
            System.setErr(originalError);
        } finally {
            System.setOut(originalOut);
            System.setErr(originalError);
        }
    }

}
