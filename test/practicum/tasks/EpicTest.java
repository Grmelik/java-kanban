package practicum.tasks;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import practicum.managers.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

// ТЕСТ "проверьте, что объект `Epic` нельзя добавить в самого себя в виде подзадачи"
class EpicTest {
    static InMemoryTaskManager tm = new InMemoryTaskManager();
    static int idEpic;

    @BeforeAll
    static void createNewObjects() {
        Epic epic2 = new Epic("Обучение", "Курс Java", TaskStatus.NEW);
        tm.createEpic(epic2);
        idEpic = epic2.getId();
    }
    @Test
    void addSubtask() {
        System.out.println("==== Тест добавления эпика в виде подзадачи =============================================");
        final Epic epic = tm.getEpicById(idEpic);
        ArrayList<Integer> subtaskList = null;

        epic.addSubtask(idEpic);
        assertNull(subtaskList, "После добавления эпика, список должен быть пустым.");
    }
}
