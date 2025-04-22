package ru.yandex.practicum.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    //проверяем равенство Epic по id
    @Test
    public void epicIsShouldBeEqual() {
        Epic epic1 = new Epic(1, "Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        Epic epic2 = new Epic(1, "Эпик 1", "Описание эпика 2",
                TaskStatus.IN_PROGRESS);
        assertEquals(epic1, epic2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }

    //проверяем что эпик не может быть подзадачей самого себя
    @Test
    public void epicIsShouldNotOwnSubtask() {
        Epic epic1 = new Epic(1, "Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(1, "Первый сабтаск","Описание первого сабтаскa", TaskStatus.NEW,epic1.getId());
        assertNotEquals(epic1.getId(), subtask1.getId(),
                "Ошибка! Epic не может быть подзадачей у самого себя");
    }
}