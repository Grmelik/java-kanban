import practicum.managers.Managers;
import practicum.managers.TaskManager;
import practicum.managers.FileBackedTaskManager;
import practicum.support.*;

import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final String dirname = System.getProperty("user.home");
        final String filename = "backed.csv";
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();

            int cmd = scanner.nextInt();

            if (cmd == 1) {
                TaskManager tm = Managers.getDefault();
                Support.testMemory(tm);
            } else if (cmd == 2) {
                TaskManager tm = Managers.getDefaultFileBacked(dirname, filename);
                Support.testFile(tm);
                if (Support.checkFile(dirname, filename))
                    System.out.println("Состояние менеджера задач записано в файл "
                            + Paths.get(dirname, filename));
                else
                    System.out.println("Возникли ошибки с сохранением менеджера задач в файл "
                            + Paths.get(dirname, filename));
            } else if (cmd == 3) {
                FileBackedTaskManager fb = Managers.getDefaultFileBacked();
                fb.loadFromFile(Paths.get(dirname, filename).toFile());
            } else if (cmd == 4) {
                System.out.println("Завершение работы");
                return;
            } else {
                System.out.println("Такой команды нет");
            }
        }
    }

    public static void printMenu() {
        System.out.println("=========================================================================================");
        System.out.println("Выберите пункт тестирования:");
        System.out.println("1. Менеджер задач в оперативной памяти");
        System.out.println("2. Сохранение менеджера задач в файл");
        System.out.println("3. Восстановление менеджера задач из файла");
        System.out.println("4. Выход");
    }

}
