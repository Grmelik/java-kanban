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
        subtasksMap.clear();
        for (Epic epic : epicsMap.values()){
            epic.clearSubtasksList();
            updateEpicStatus(epic.getId());
        }
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

    // d.Создание. Сам объект должен передаваться в качестве параметра
    public void createTask(Task task) {
        task.setId(task.getId()+1);
        tasksMap.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(epic.getId()+1);
        epicsMap.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(subtask.getId()+1);
        Epic epic = getEpicById(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        subtasksMap.put(subtask.getId(), subtask);
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        Task savedTask = tasksMap.get(task.getId());
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
    }

    public void updateEpic(Epic epic) {
        Epic savedEpic = epicsMap.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    public void updateSubtask(Subtask subtask) {
        Subtask savedSubtask = subtasksMap.get(subtask.getId());
        savedSubtask.setName(subtask.getName());
        savedSubtask.setDescription(subtask.getDescription());
        updateEpicStatus(subtask.getEpicId());
    }

    // Обновление статуса эпика
    public void updateEpicStatus(int id) {
        ArrayList<Epic> epics = new ArrayList<>(epicsMap.values());
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        TaskStatus status;
        TaskStatus statusTmp;

        Epic epic = epicsMap.get(id);
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
        }
    }

    // f. Удаление по идентификатору.
    public void deleteTaskById(int id) {
        tasksMap.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epicsMap.remove(id);
        for (Integer subtaskId : epic.getSubtasksList()) {
            subtasksMap.remove(subtaskId);
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasksMap.remove(id);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.getSubtasksList().remove(id);
        updateEpicStatus(subtask.getEpicId());
    }

    // g. Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epicsMap.get(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasksList()) {
            subtasks.add(subtasksMap.get(subtaskId));
        }

        return subtasks;
    }
}
