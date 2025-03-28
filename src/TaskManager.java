import java.util.*;

public class TaskManager {
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

    // a.Получение списка всех задач.
    public void getListOfAllTasks() {
        for (Task task : tasksMap.values()) {
            System.out.println(task);
        }
    }

    public void getListOfAllEpics() {
        for (Epic epic : epicsMap.values()) {
            System.out.println(epic);
            getListOfAllSubtasks(epic.getId());
        }
    }

    public void getListOfAllSubtasks(int epicId) {
        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.epicId == epicId)
                System.out.println(subtask);
        }
    }

    // b.Удаление всех задач.
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    public void deleteAllEpics() {
        epicsMap.clear();
    }

    public void deleteAllSubtasks() {
        subtasksMap.clear();
    }

    // c.Получение по идентификатору.
    public Task getTaskById(int id) {
        for (Task task : tasksMap.values()) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public Epic getEpicById(int id) {
        for (Epic epic : epicsMap.values()) {
            if (epic.getId() == id) {
                return epic;
            }
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.getId() == id) {
                return subtask;
            }
        }
        return null;
    }

    // d.Создание. Сам объект должен передаваться в качестве параметра.
    public void createTask(Task task) {
        int id = generateId();
        task.setId(id);
        tasksMap.put(id, task);
    }

    public void createEpic(Epic epic) {
        int id = generateId();
        epic.setId(id);
        epicId = epic.getId();
        epicsMap.put(id, epic);
    }

    public void createSubtask(Subtask subtask) {
        int id = generateId();
        subtask.setId(id);
        subtasksMap.put(id, subtask);
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
        ArrayList<Task> epics = new ArrayList<>(epicsMap.values());
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        TaskStatus status;
        TaskStatus statusTmp;

        if (epics.contains(epic)) {
            status = epic.getStatus();
            for (Subtask subtask : subtasksMap.values()) {
                if (subtask.epicId == epic.getId()) {
                    statuses.add(subtask.getStatus());
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
        deleteSubtaskById(id, 0);

        epicsMap.entrySet().removeIf(entry -> entry.getKey().equals(id));
    }

    public void deleteSubtaskById(int epicId, int id) {
        if (epicId > 0) {
            for (Subtask subtask : subtasksMap.values()) {
                if (subtask.epicId == epicId) {
                   subtasksMap.entrySet().removeIf(entry -> entry.getKey().equals(subtask.getId()));
                }
            }
        } else {
            subtasksMap.entrySet().removeIf(entry -> entry.getKey().equals(id));
        }
    }

    // g. Получение списка всех подзадач определённого эпика.
    public void getSubtasksOfEpic(int epicId) {
        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.epicId == epicId) {
                System.out.println("Эпик ID = " + epicId);
                System.out.println(subtask);
            }
        }
    }
}
