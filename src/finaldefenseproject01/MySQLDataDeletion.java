package finaldefenseproject01;

import java.sql.*;

public class MySQLDataDeletion {
    public void run() {
        String username = "root";
        String password = "";
        String databaseName = "mta_db";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, username, password)) {
            // Disable foreign key checks
            try (Statement disableFK = connection.createStatement()) {
                disableFK.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            }

            // Get table names with foreign keys
            java.sql.DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tablesResultSet = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});

            // Delete data from tables without foreign keys first
            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");
                if (!tableName.equals("employee") && !hasForeignKeys(connection, tableName)) {
                    deleteData(connection, tableName);
                }
            }

            // Delete data from tables with foreign keys
            tablesResultSet.beforeFirst();
            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");
                if (!tableName.equals("employee") && hasForeignKeys(connection, tableName)) {
                    deleteData(connection, tableName);
                }
            }

            // Enable foreign key checks
            try (Statement enableFK = connection.createStatement()) {
                enableFK.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
            }

            System.out.println("Data deletion successful.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean hasForeignKeys(Connection connection, String tableName) throws SQLException {
        java.sql.DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getImportedKeys(connection.getCatalog(), null, tableName);
        return resultSet.next();
    }

    private static void deleteData(Connection connection, String tableName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM " + tableName);
        }
    }
}
