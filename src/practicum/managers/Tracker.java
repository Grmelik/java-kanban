package practicum.managers;

import java.util.*;
import practicum.tasks.*;

public class Tracker {
    public static int newId = 1000;
    public int epicId;
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
        for (Epic epic : epicsMap.values()) {
            int sizeOfArray = epic.getSubtasksList().size() - 1;

            for (int i = sizeOfArray; i >= 0; i--) {
                epic.clearSubtasksList(i);
            }
            epic.setStatus(TaskStatus.NEW);
            epicsMap.put(epic.getId(), epic);
        }
        subtasksMap.clear();
    }

    // c.Получение по идентификатору
    public Task getTaskById(int id) {
        for (Map.Entry<Integer, Task> entry: tasksMap.entrySet()) {
            if (entry.getKey() == id) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Epic getEpicById(int id) {
        for (Map.Entry<Integer, Epic> entry : epicsMap.entrySet()) {
            if (entry.getKey() == id) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        for (Map.Entry<Integer, Subtask> entry : subtasksMap.entrySet()) {
            if (entry.getKey() == id) {
                return entry.getValue();
            }
        }
        return null;
    }

    // d.Создание. Сам объект должен передаваться в качестве параметра +
    public void createTask(Task task) {
        tasksMap.put(task.getId() /*.id*/, task);
    }

    public void createEpic(Epic epic) {
        epicId = epic.getId();
        epicsMap.put(epic.getId() /*.id*/, epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setEpicId(epicId);

        for (Map.Entry<Integer, Epic> entry : epicsMap.entrySet()) {
            if (entry.getKey() == epicId) {
                Epic epic = getEpicById(entry.getKey());
                epic.addSubtask(subtask.getId());
            }
        }
        subtasksMap.put(subtask.getId() /*.id*/, subtask);
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        ArrayList<Task> tasks = new ArrayList<>(tasksMap.values());

        if (tasks.contains(task)) {
            if (task.getStatus() == TaskStatus.NEW) {
                task.setStatus(TaskStatus.IN_PROGRESS);
            } else if (task.getStatus() == TaskStatus.IN_PROGRESS) {
                task.setStatus(TaskStatus.DONE);
            }
            tasksMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        ArrayList<Epic> epics = new ArrayList<>(epicsMap.values());
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        TaskStatus status;
        TaskStatus statusTmp;

        if (epics.contains(epic)) {
            status = epic.getStatus();

            ArrayList<Integer> subtasksList = epic.getSubtasksList();
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

            epic.setStatus(status);
            epicsMap.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        ArrayList<Subtask> subtasks = new ArrayList<>(subtasksMap.values());

        if (subtasks.contains(subtask)) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                subtask.setStatus(TaskStatus.IN_PROGRESS);
            } else if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                subtask.setStatus(TaskStatus.DONE);
            }
            subtasksMap.put(subtask.getId(), subtask);
        }
    }

    // f. Удаление по идентификатору.
    public void deleteTaskById(int id) {
        tasksMap.entrySet().removeIf(entry -> entry.getKey().equals(id));
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

        epicsMap.entrySet().removeIf(entry -> entry.getKey().equals(id));
    }

    public void deleteSubtaskById(int id) {
        subtasksMap.entrySet().removeIf(entry -> entry.getKey().equals(id));
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
