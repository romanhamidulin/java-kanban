package ru.yandex.practicum.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    //проверяем равенство Subtask по id
    @Test
    public void subtaskIsShouldBeEqual() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask(1, "Первый сабтаск","Описание первого сабтаскa", TaskStatus.NEW,epic1.getId());
        Subtask subtask2 = new Subtask(1, "Первый сабтаск","Описание первого сабтаскa", TaskStatus.DONE,epic1.getId());
        assertEquals(subtask1, subtask1,"Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id");
    }
}