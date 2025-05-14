# java-kanban
Repository for homework project.

---
int _generateId_()    - **Генерация идентификатора**

`List<Task>` _getAllTasks_()    - **Получение списка всех задач**

`List<Epic>` _getAllEpics()_ 

`List<Subtask>` _getAllSubtasks()_

`void` _deleteAllTasks()_ - **Удаление всех задач**

`void` _deleteAllEpics()_

`void` _deleteAllSubtasks()_

`Task` _getTaskById(int id)_ - **Получение по идентификатору**

`Epic` _getEpicById(int id)_

`Subtask` _getSubtaskById(int id)_

`void` _createTask(Task task)_ - **Создание. Сам объект должен передаваться в качестве параметра**

`void` _createEpic(Epic epic)_

`void` _createSubtask(Subtask subtask)_ 

`void` _updateTask_ - **Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.**

`void` _updateEpic(Epic epic)_

`void` _updateSubtask(Subtask subtask)_

`void` _updateEpicStatus(int id)_ - **Обновление статуса эпика**

`void` _deleteTaskById(int id)_ - **Удаление по идентификатору.**

`void` _deleteEpicById(int id)_

`void` _deleteSubtaskById(int id)_

`ArrayList<Subtask>` _getSubtasksOfEpic(int epicId)_ - **Получение списка всех подзадач определённого эпика.**

`List<Task>` _getHistory()_ - **Получение данных из памяти**

`void` _printAllTasks()_ - **Печать всех заданий, эпиков и подзадач**

`void` _printAllHistory()_ - **Печать истории**