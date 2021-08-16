import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InsertDataInBase implements Runnable {
    private static int coresCount = Runtime.getRuntime().availableProcessors() * 10;              //40 потоков
    private static ThreadPoolExecutor executor;

    private String url;
    private String user;
    private String password;
    private int number;
    private List<String> commandSet;

    public InsertDataInBase() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(coresCount);
    }

    public InsertDataInBase(String url, String user, String password, List<String> commandSet) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.commandSet = commandSet;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private void tableClear() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE TEST");
            printAboutTableClear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> getListCommandsForInsert(int n) {
        List<List<String>> listCommands = new ArrayList<>();
        List<String> listSubCommands = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            String sqlInquiry = String.format("INSERT INTO test (field) value (%d)", i);
            listSubCommands.add(sqlInquiry);
            if (i % 5000 == 0) {
                listCommands.add(listSubCommands);
                listSubCommands = new ArrayList<>();
            } else if (n < 5000 && i == n) {
                listCommands.add(listSubCommands);
            }
        }
        return listCommands;
    }

    public void addedRowsInMultiThread(List<List<String>> list) {
        if (getCountRowsInDataBase() > 0) {
            tableClear();
        }
        for (List<String> listStr : list) {
            executor.execute(new InsertDataInBase(url, user, password, listStr));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printAboutEndFillBD();
    }

    public int getCountRowsInDataBase() {
        int countRowsInDataBase = 0;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM TEST");
            while (resultSet.next()) {
                countRowsInDataBase = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countRowsInDataBase;
    }

    private static void printAboutTableClear() {
        System.out.println("Таблица очищена, начато заполнение Базы Данных");
    }

    private static void printAboutEndFillBD() {
        System.out.println("Заполнение БД завешено");
    }

    @Override
    public void run() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {
            List<String> list = commandSet;
            for (String sqlInquiry : list) {
                statement.executeUpdate(sqlInquiry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}