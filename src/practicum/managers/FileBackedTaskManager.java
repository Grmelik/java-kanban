package practicum.managers;

import practicum.tasks.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Map<Integer, Task> tasksMap = new HashMap<>();
    private final Map<Integer, Epic> epicsMap = new HashMap<>();
    private final Map<Integer, Subtask> subtasksMap = new HashMap<>();
    File file;
    Path backFile;
    private List<String> taskList = new ArrayList<>();

    public FileBackedTaskManager() {

    }

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public FileBackedTaskManager(String dirname, String filename) {
        try {
            if (!Files.exists(Paths.get(dirname, filename))) {
                backFile = Files.createFile(Paths.get(dirname, filename));
            } else {
                backFile = Paths.get(dirname, filename);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage() + "Ошибка с файлом " + backFile + ".");
        }
    }

    public void loadFromFile(File file) {
        FileBackedTaskManager fb = new FileBackedTaskManager(file);
        Charset charset = Charset.forName("Windows-1251");  // Кодировка Win-1251, иначе в Excel кракозябры вместо русских букв

        try (BufferedReader br = new BufferedReader(new FileReader(file, charset))) {
            while (br.ready()) {
                String line = br.readLine();
                taskList.add(line);
            }
            br.close();

            if (taskList.isEmpty()) {
                System.out.println("Файл пуст. Работа завершена.");
                System.exit(-1);
            } else {
                taskList.removeFirst();
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException(e.getMessage() + " Файл не найден.");
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage() + " Произошла ошибка во время чтения файла.");
        }

        System.out.println("Состояние менеджера задач восстановлено из файла.");
        for (String task : taskList) {
            fromString(task);
        }
    }

    public List<String> getTasksFromFile() {
        return taskList;
    }

    private void save() {
        Charset charset = Charset.forName("Windows-1251");  // Кодировка Win-1251, иначе в Excel кракозябры вместо русских букв

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(backFile.toFile(), charset))) {
            bw.write("id,type,name,status,description,duration,startdate,enddate,epic\n");
            StringBuilder sb = new StringBuilder();

            for (Task task : getAllTasks()) {
                sb.append(toString(task));
            }

            for (Epic epic : getAllEpics()) {
                sb.append(toString(epic));
            }

            for (Subtask subtask : getAllSubtasks()) {
                sb.append(toString(subtask));
            }

            bw.write(String.valueOf(sb));
            bw.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage() + " Произошла ошибка во время записи в файл.");
        }
    }

    private String getFormattedDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        return dateTime.format(formatter);
    }

    private LocalDateTime getDateFromString(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        return LocalDateTime.parse(dateTime, formatter);
    }

    private String toString(Task task) {

        String str = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getDuration().toMinutes() + "," +
                getFormattedDate(task.getStartTime()) + "," + getFormattedDate(task.getEndTime());

        if (task.getType() == TaskType.SUBTASK) {
            str = str + "," + ((Subtask)task).getEpicId();
        }
        str = str + "\n";

        return str;
    }

    private void fromString(String value) {
        String[] values = value.split(",");
        TaskStatus status = null;
        Duration duration = Duration.ofMinutes(Integer.parseInt(values[5]));
        LocalDateTime startTime = getDateFromString(values[6]);

        switch (values[3]) {
            case "NEW":
                status = TaskStatus.NEW;
                break;
            case "IN_PROGRESS":
                status = TaskStatus.IN_PROGRESS;
                break;
            case "DONE":
                status = TaskStatus.DONE;
                break;
        }

        switch (values[1]) {
            case "TASK" -> {
                Task task = new Task(values[2], values[4], status, duration, startTime);
                task.setId(Integer.parseInt(values[0]));
                tasksMap.put(task.getId(), task);
            }
            case "EPIC" -> {
                Epic epic = new Epic(values[2], values[4], status, duration, startTime);
                epic.setId(Integer.parseInt(values[0]));
                epicsMap.put(epic.getId(), epic);
            }
            case "SUBTASK" -> {
                Subtask subtask = new Subtask(values[2], values[4], status, duration, startTime);
                subtask.setId(Integer.parseInt(values[0]));
                subtasksMap.put(subtask.getId(), subtask);
                Epic epic = epicsMap.get(Integer.parseInt(values[8]));
                epic.addSubtask(Integer.parseInt(values[0]));
            }
        }
    }

    public void printTasksFromFile() {
        System.out.println("Задачи:");
        for (Task task : tasksMap.values()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : epicsMap.values()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : subtasksMap.values()) {
            System.out.println(subtask);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, int epicId) {
        super.createSubtask(subtask, epicId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }
}
