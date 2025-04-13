package practicum.tasks;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import practicum.managers.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

// ТЕСТ "проверьте, что объект `Epic` нельзя добавить в самого себя в виде подзадачи"
class EpicTest {
    static InMemoryTaskManager tm = new InMemoryTaskManager();
    static int idEpic = 1003;

    @BeforeAll
    static void createNewObjects() {
        tm.createEpic(new Epic("createEpic", "Test createEpic description", idEpic, TaskStatus.NEW));
    }
    @Test
    void addSubtask() {
        System.out.println("==== Тест добавления эпика в виде подзадачи =============================================");
        final Epic epic = tm.getEpicById(idEpic+1);
        ArrayList<Integer> subtaskList = new ArrayList<>();

        epic.addSubtask(idEpic+1);
        subtaskList = epic.getSubtasksList();
        assertNull(subtaskList, "После добавления эпика, список должен быть пустым.");
    }
}

// ToDo:
// 1. Переработать методы создания объектов в части возвращения идентификатора объекта (?)
// 2. Сделать проверку того, чтобы эпик не добавлялся в список подзадач (?)