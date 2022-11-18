import util.ConnectionManager;

import java.sql.*;

public class EditingATable {

    private static StringBuilder insertQuery = new StringBuilder();
    private static int countString;

    static {
        deleteTableBeforeWork();
        createTable();
    }

    private EditingATable() {
    }

    public static void setCountString(int countString) {
        EditingATable.countString = countString;
        formARowGroup();
    }

    private static void deleteTableBeforeWork() {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS test");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable() {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE test(field INT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        printAboutTableClear();
    }

    private static void addRowsAsAGroup() {
        String sql = "INSERT INTO test(field) VALUES" + insertQuery.toString();
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void formARowGroup() {
        for (int index = 1; index <= countString; index++) {
            String num = String.valueOf(index);
            insertQuery.append(insertQuery.length() == 0 ? "" : ",")
                    .append("('").append(num).append("')");
            if (insertQuery.length() >= 100000) {
                addRowsAsAGroup();
                insertQuery = new StringBuilder();
            }
        }
        if (insertQuery.length() > 0) {
            addRowsAsAGroup();
        }
        printAboutEndFillBD();
    }

    private static void printAboutTableClear() {
        System.out.println("Таблица очищена, начато заполнение Базы Данных");
    }

    private static void printAboutEndFillBD() {
        System.out.println("Заполнение БД завешено");
    }
}