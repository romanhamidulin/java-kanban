package ru.yandex.practicum.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    //проверяем равенство Epic по id
    @Test
    public void epicIsShouldBeEqual() {
        Epic epic1 = new Epic(1, "Эпик 1", "Описание эпика 1", TaskStatus.NEW, LocalDateTime.now(),10);
        Epic epic2 = new Epic(1, "Эпик 1", "Описание эпика 2",
                TaskStatus.IN_PROGRESS, LocalDateTime.now(),10);
        assertEquals(epic1, epic2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }

    //проверяем что эпик не может быть подзадачей самого себя
    @Test
    public void epicIsShouldNotOwnSubtask() {
        Epic epic1 = new Epic(1, "Эпик 1", "Описание эпика 1", TaskStatus.NEW, LocalDateTime.now(),10);
        Subtask subtask1 = new Subtask(2, "Первый сабтаск","Описание первого сабтаскa", TaskStatus.NEW, LocalDateTime.now(),10,epic1.getId());
        assertNotEquals(epic1.getId(), subtask1.getId(),
                "Ошибка! Epic не может быть подзадачей у самого себя");
    }
}