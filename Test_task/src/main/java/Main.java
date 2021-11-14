import java.util.Scanner;

public class Main {
    //Учетные данные для подключения к БД
    private static final String bdAndServer = "mysql://localhost:3306/testing_db";
    private static final String url = "jdbc:" + bdAndServer +
            "?useUnicode=true&serverTimezone=Europe/Moscow&characterEncoding=UTF-8";

    public static void main(String[] args) {
        String userLogin = "tanderuser";
        String userPassword = "tanderuser";
        int numberN = 1000000;
//        int numberN = 50;

        Scanner scanner = new Scanner(System.in);
        printUserLogin();
//        String userLogin = scanner.nextLine();
        printUserPassword();
//        String userPassword = scanner.nextLine();
        printInputNumber();
//        int numberN = scanner.nextInt();

        //Данный фрагмент не менять
        long timeStart = System.currentTimeMillis();

        //Инициализация объекта
        InsertDataInBase insertDataInBase = new InsertDataInBase();
        insertDataInBase.setUrl(url);
        insertDataInBase.setUser(userLogin);
        insertDataInBase.setPassword(userPassword);
        insertDataInBase.setNumber(numberN);

        //Запуск команды по добавлению строк в БД
        insertDataInBase.addedRowsInMultiThread(insertDataInBase.getListCommandsForInsert(numberN));

        //Получение строк из БД и запись в xml файл
        LocalFile.writeToInXmlFile(LocalFile
                .getValuesFromDataBase(url, userLogin, userPassword));

        printSumAllNumbers();
        printCountRowsInDataBase(insertDataInBase.getCountRowsInDataBase());
        long timeEnd = System.currentTimeMillis();
        printTimeWorkProgram(timeStart, timeEnd);
    }

    private static void printUserLogin() {
        System.out.println("Введите логин пользователя для доступа к БД");
    }

    private static void printUserPassword() {
        System.out.println("Введите пароль пользователя для доступа к БД");
    }

    private static void printInputNumber() {
        System.out.println("Введите число N");
    }

    private static void printTimeWorkProgram(long start, long end) {
        System.out.printf("Время выполнения программы %d мс%n", end - start);
    }

    private static void printCountRowsInDataBase(int countRows) {
        System.out.printf("Количество записей в таблице %d%n", countRows);
    }

    private static void printSumAllNumbers() {
        System.out.printf("Сумма всех чисел в файле %d%n", LocalFile.getSumAllNumbers());
    }
}