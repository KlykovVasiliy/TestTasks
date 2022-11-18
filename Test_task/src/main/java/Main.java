import util.ConfigFileToConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        long timeStart = System.currentTimeMillis();
        createFileConfig();
        EditingATable.setCountString(getNumberOfLinesForDB());
//        EditingATable.setCountString(1000000);
        printSumAllNumbers();
        long timeEnd = System.currentTimeMillis();

        printTimeWorkProgram(timeStart, timeEnd);
    }

    private static void createFileConfig() {
        ConfigFileToConnection config = ConfigFileToConnection.getConfigFileToConnection();
//        String userLogin = "tanderuser";
//        String userPassword = "tanderuser";
        String url = "jdbc:mysql://localhost:3306/";
        String userLogin = null;
        String userPassword = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            printUrl();
            url += reader.readLine();
            printUserLogin();
            userLogin = reader.readLine();
            printUserPassword();
            userPassword = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.setConfig(userLogin, userPassword, url, getPoolSize());
    }

    private static String getPoolSize() {
        return String.valueOf(Runtime.getRuntime().availableProcessors());
    }

    private static Integer getNumberOfLinesForDB() {
        printInputNumber();
        int countLine = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            countLine = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countLine;
    }

    private static void printUrl() {
        System.out.println("Enter name DataBase:");
    }

    private static void printUserLogin() {
        System.out.println("Enter name user");
    }

    private static void printUserPassword() {
        System.out.println("Enter password");
    }

    private static void printInputNumber() {
        System.out.println("Enter number");
    }

    private static void printTimeWorkProgram(long start, long end) {
        System.out.printf("Время выполнения программы %d мс%n", end - start);
    }

    private static void printSumAllNumbers() {
        System.out.printf("Сумма всех чисел в файле %d%n", FileXML.getSumAllNumbers());
    }
}