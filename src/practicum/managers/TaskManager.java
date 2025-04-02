package practicum.managers;

import java.util.*;
import practicum.tasks.*;

public class TaskManager {
    public static int newId = 1000;
    private HashMap<Integer, Task> tasksMap = new HashMap<>();
    private HashMap<Integer, Epic> epicsMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksMap = new HashMap<>();

    // Генерация идентификатора
    public int generateId() {
        newId = newId + 1;
        return newId;
    }

    // a.Получение списка всех задач
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    // b.Удаление всех задач
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    public void deleteAllEpics() {
        deleteAllSubtasks();
        epicsMap.clear();
    }

    public void deleteAllSubtasks() {
        // Итерация по мапе эпиков
        for (Epic epic : epicsMap.values()) {
            // Размер списка подзадач
            int sizeOfArray = epic.getSubtasksList().size() - 1;

            // Очистка списка подзадач эпика
            for (int i = sizeOfArray; i >= 0; i--) {
                // Удаление подзадачи
                epic.clearSubtasksList(i);
            }
            // Установка нового статуса эпика через метод обновления статуса эпика
            updateEpic(epic);
        }
        // Очистить коллекцию подзадач
        subtasksMap.clear();
    }

    // c.Получение по идентификатору
    public Task getTaskById(int id) {
        return tasksMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicsMap.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasksMap.get(id);
    }

    // d.Создание. Сам объект должен передаваться в качестве параметра +
    public void createTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        Epic epic = getEpicById(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        subtasksMap.put(subtask.getId(), subtask);
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        ArrayList<Epic> epics = new ArrayList<>(epicsMap.values());
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        TaskStatus status;
        TaskStatus statusTmp;

        if (epics.contains(epic)) {
            status = epic.getStatus();
            ArrayList<Integer> subtasksList = epic.getSubtasksList();

            if(!subtasksList.isEmpty()) {
                for (Map.Entry<Integer, Subtask> entry : subtasksMap.entrySet()) {
                    for (Integer subtaskKey : subtasksList) {
                        if (entry.getKey().equals(subtaskKey)) {
                            Subtask subtask = getSubtaskById(entry.getKey());
                            statuses.add(subtask.getStatus());
                        }
                    }
                }

                if (!statuses.isEmpty()) {
                    int statusesSize = statuses.size();
                    int countStatusNew = 0;
                    int countStatusDone = 0;

                    for (TaskStatus taskStatus : statuses) {
                        statusTmp = taskStatus;
                        if (statusTmp == TaskStatus.NEW) {
                            countStatusNew++;
                        } else if (statusTmp == TaskStatus.DONE) {
                            countStatusDone++;
                        }
                    }

                    if (countStatusNew == statusesSize) {
                        status = TaskStatus.NEW;
                    } else if (countStatusDone == statusesSize) {
                        status = TaskStatus.DONE;
                    } else {
                        status = TaskStatus.IN_PROGRESS;
                    }
                }
            } else {
                status = TaskStatus.NEW;
            }
            epic.setStatus(status);
            epicsMap.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        subtasksMap.put(subtask.getId(), subtask);
    }

    // f. Удаление по идентификатору.
    public void deleteTaskById(int id) {
        tasksMap.remove(id);
    }

    public void deleteEpicById(int id) {
        for (Map.Entry<Integer, Epic> entry : epicsMap.entrySet()) {
            if (entry.getKey() == id) {
                Epic epic = getEpicById(entry.getKey());
                int sizeOfArray = epic.getSubtasksList().size() - 1;
                ArrayList<Integer> listOfSubtasks = epic.getSubtasksList();

                if (!listOfSubtasks.isEmpty()) {
                    for (int i = sizeOfArray; i >= 0; i--) {
                        deleteSubtaskById(listOfSubtasks.get(i));
                        epic.clearSubtasksList(i);
                    }
                }
                epic.setStatus(TaskStatus.NEW);
                epicsMap.put(epic.getId(), epic);
            }
        }

        epicsMap.remove(id);
    }

    public void deleteSubtaskById(int id) {
        subtasksMap.remove(id);
    }

    // g. Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        ArrayList<Subtask> subtasks = new ArrayList<>();

        for (Map.Entry<Integer, Epic> entry : epicsMap.entrySet()) {
            if (entry.getKey() == epicId) {
                Epic epic = getEpicById(entry.getKey());
                ArrayList<Integer> subtasksList = epic.getSubtasksList();

                for (Integer subtask : subtasksList) {
                    for (Map.Entry<Integer, Subtask> entrySub : subtasksMap.entrySet()) {
                        if (Objects.equals(entrySub.getKey(), subtask)) {
                            subtasks.add(entrySub.getValue());
                        }
                    }
                }
            }
        }
        return subtasks;
    }
}
