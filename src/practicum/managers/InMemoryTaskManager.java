package practicum.managers;

import java.util.*;
import practicum.tasks.*;

public class InMemoryTaskManager implements TaskManager {
    public static int newId = 1000;
    private Map<Integer, Task> tasksMap = new HashMap<>();
    private Map<Integer, Epic> epicsMap = new HashMap<>();
    private Map<Integer, Subtask> subtasksMap = new HashMap<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();

    // Генерация идентификатора
    private int generateId() {
        newId = newId + 1;
        return newId;
    }

    // a.Получение списка всех задач
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    // b.Удаление всех задач
    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        epicsMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasksMap.clear();
        for (Epic epic : epicsMap.values()){
            epic.clearSubtasksList();
            updateEpicStatus(epic.getId());
        }
    }

    // c.Получение по идентификатору
    @Override
    public Task getTaskById(int id) {
        //System.out.println("A.IMTM.getTaskById(int id) id=" + id + ", tasksMap.get(id)=" + tasksMap.get(id));
        historyManager.add(tasksMap.get(id));
        return tasksMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        //System.out.println("B.IMTM.getEpicById(int id) id=" + id + ", epicsMap.get(id)=" + epicsMap.get(id));
        historyManager.add(epicsMap.get(id));
        return epicsMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        //System.out.println("C.IMTM.getSubtaskById(int id) id=" + id + ", subtasksMap.get(id)=" + subtasksMap.get(id));
        historyManager.add(subtasksMap.get(id));
        return subtasksMap.get(id);
    }

    // d.Создание. Сам объект должен передаваться в качестве параметра
    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask, int epicId) {
        subtask.setId(generateId());
        Epic epic = getEpicById(epicId);
        epic.addSubtask(subtask.getId());
        subtasksMap.put(subtask.getId(), subtask);
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        Task savedTask = tasksMap.get(task.getId());
        savedTask.setName(task.getName());
        savedTask.setDescription(task.getDescription());
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epicsMap.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask savedSubtask = subtasksMap.get(subtask.getId());
        savedSubtask.setName(subtask.getName());
        savedSubtask.setDescription(subtask.getDescription());
        updateEpicStatus(subtask.getEpicId());
    }

    // Обновление статуса эпика
    private void updateEpicStatus(int id) {
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
    @Override
    public void deleteTaskById(int id) {
        tasksMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicsMap.remove(id);
        historyManager.remove(id);
        for (Integer subtaskId : epic.getSubtasksList()) {
            subtasksMap.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasksMap.remove(id);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.getSubtasksList().remove(id);
        updateEpicStatus(subtask.getEpicId());
        historyManager.remove(id);
    }

    // g. Получение списка всех подзадач определённого эпика.
    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epicsMap.get(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasksList()) {
            subtasks.add(subtasksMap.get(subtaskId));
        }

        return new ArrayList<>(subtasks);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Взято из сценария 5-го спринта - печать событий
    @Override
    public void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : getAllEpics()) {
            System.out.println(epic);

            for (Task task : getSubtasksOfEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : getAllSubtasks()) {
            System.out.println(subtask);
        }
    }

    @Override
    public void printAllHistory() {
        System.out.println("История:");
        for (Task task : getHistory()) {
            System.out.println(task);
        }
    }
}
