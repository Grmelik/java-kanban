package practicum.managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import practicum.tasks.*;

import static java.util.Comparator.comparing;

public class InMemoryTaskManager implements TaskManager {
    public static int newId = 1000;
    private Map<Integer, Task> tasksMap = new HashMap<>();
    private Map<Integer, Epic> epicsMap = new HashMap<>();
    private Map<Integer, Subtask> subtasksMap = new HashMap<>();
    private static Set<Task> tasksSet = new HashSet<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {
        newId = newId + 1;
        return newId;
    }

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
        for (Epic epic : epicsMap.values()) {
            epic.clearSubtasksList();
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public void clearingTaskSet() {
        tasksSet.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasksMap.containsKey(id)) {
            historyManager.add(tasksMap.get(id));
            return tasksMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicsMap.containsKey(id)) {
            historyManager.add(epicsMap.get(id));
            return epicsMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasksMap.containsKey(id)) {
            historyManager.add(subtasksMap.get(id));
            return subtasksMap.get(id);
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        task.setId(generateId());
        if (isCheckIntersection(task)) {
            tasksMap.put(task.getId(), task);
            addingToPriorityList(task);
        } else {
            System.out.println("Задача с ID=[" + task.getId() + "] пересекается по времени с существующей задачей.");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateId());
        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask, int epicId) {
        subtask.setId(generateId());
        if (isCheckIntersection(subtask)) {
            subtask.setEpicId(epicId);
            Epic epic = getEpicById(epicId);
            epic.addSubtask(subtask.getId());
            subtasksMap.put(subtask.getId(), subtask);
            setEpicDateTime(epic);
            addingToPriorityList(subtask);
        } else {
            System.out.println("Задача (подзадача) с ID=[" + subtask.getId() + "] пересекается по времени с существующей задачей.");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            Task savedTask = tasksMap.get(task.getId());
            savedTask.setName(task.getName());
            savedTask.setDescription(task.getDescription());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            Epic savedEpic = epicsMap.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasksMap.containsKey(subtask.getId())) {
            Subtask savedSubtask = subtasksMap.get(subtask.getId());
            savedSubtask.setName(subtask.getName());
            savedSubtask.setDescription(subtask.getDescription());
            updateEpicStatus(subtask.getEpicId());
        }
    }

    private void updateEpicStatus(int id) {
        ArrayList<Epic> epics = new ArrayList<>(epicsMap.values());
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        TaskStatus status;
        TaskStatus statusTmp;

        Epic epic = epicsMap.get(id);
        if (epics.contains(epic)) {
            status = epic.getStatus();
            ArrayList<Integer> subtasksList = epic.getSubtasksList();

            if (!subtasksList.isEmpty()) {
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

    @Override
    public void deleteTaskById(int id) {
        if (tasksMap.containsKey(id)) {
            removingFromPriorityList(getTaskById(id));
            tasksMap.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicsMap.remove(id);
        historyManager.remove(id);
        for (Integer subtaskId : epic.getSubtasksList()) {
            if (subtasksMap.containsKey(subtaskId)) {
                removingFromPriorityList(getSubtaskById(subtaskId));
                subtasksMap.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasksMap.containsKey(id)) {
            removingFromPriorityList(getSubtaskById(id));
            Subtask subtask = subtasksMap.remove(id);
            Epic epic = epicsMap.get(subtask.getEpicId());
            epic.getSubtasksList().remove(id);
            updateEpicStatus(subtask.getEpicId());
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epicsMap.get(epicId);
        ArrayList<Subtask> subtasks = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasksList()) {
            subtasks.add(subtasksMap.get(subtaskId));
        }
        return new ArrayList<>(subtasks);
    }

    protected void setEpicDateTime(Epic epic) {
        Duration duration = epic.getSubtasksList().stream()
                .map(subtasksMap::get)
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        LocalDateTime startTime = epic.getSubtasksList().stream()
                .map(subtasksMap::get)
                .map(Task::getStartTime)
                .min(comparing(startDate -> startDate))
                .orElse(null);

        LocalDateTime endTime = epic.getSubtasksList().stream()
                .map(subtasksMap::get)
                .map(Task::getEndTime)
                .max(comparing(endDate -> endDate))
                .orElse(null);

        epic.setDuration(duration);
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
    }

    @Override
    public void printIterator() {   // Проверка заполняемости коллекции
        Iterator<Task> iterator = tasksSet.iterator();

        while (iterator.hasNext()) {
            Task element = iterator.next();
            System.out.println("Element = " + element);
        }
    }

    private void addingToPriorityList(Task task) {
        tasksSet.add(task);
    }

    private void removingFromPriorityList(Task task) {
        tasksSet.remove(task);
    }

    private static Set<Task> prioritizedTasks() {
        Comparator<Task> comparator = Comparator.comparing(task -> task.getStartTime());
        Set<Task> sortedTasks = new TreeSet<Task>(comparator);
        sortedTasks.addAll(tasksSet);

        return sortedTasks;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks();
    }

    @Override
    public void printAllPrioritizedTasks() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        if (tasksSet.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            System.out.println("Все задачи с сортировкой по дате старта:");
        }

        for (Task task : getPrioritizedTasks()) {
            System.out.println("    * Задача с ID=[" + task.getId() + "], дата старта: " +
                    task.getStartTime().format(formatter));
        }
    }

    private boolean isCheckIntersection(Task task) {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        //System.out.println("1. isCheckIntersection. ID=[" + task.getId() + "] startTime=" +
        //        task.getStartTime().format(formatter) + ", endTime=" + task.getEndTime().format(formatter));

        LocalDateTime startTime = getPrioritizedTasks().stream()
                .map(Task::getStartTime)
                .min(comparing(startDate -> startDate))
                .orElse(null);

        LocalDateTime endTime = getPrioritizedTasks().stream()
                .map(Task::getEndTime)
                .max(comparing(endDate -> endDate))
                .orElse(null);

        /*if (startTime != null && endTime != null) {
            System.out.println("2. isCheckIntersection. ID=[" + task.getId() + "] startTime=" +
                    startTime.format(formatter) + ", endTime=" + endTime.format(formatter));
        }*/

        if (startTime == null && endTime == null) {
            return true;
        } else if (startTime.isAfter(task.getEndTime())) {
            return true;
        } else if (endTime.isBefore(task.getStartTime())) {
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : getAllTasks()) {
            System.out.println("ID=" + task);
        }
        System.out.println("Эпики:");
        for (Task epic : getAllEpics()) {
            System.out.println("ID=" + epic);

            for (Task task : getSubtasksOfEpic(epic.getId())) {
                System.out.println("-->" + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : getAllSubtasks()) {
            System.out.println("ID=" + subtask);
        }
    }

    @Override
    public void printAllHistory() {
        System.out.println("История:");
        for (Task task : getHistory()) {
            System.out.println("ID=" + task);
        }
    }
}
