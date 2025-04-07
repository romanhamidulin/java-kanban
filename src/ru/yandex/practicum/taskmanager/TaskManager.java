package ru.yandex.practicum.taskmanager;

import java.util.HashMap;
import java.util.ArrayList;

import ru.yandex.practicum.taskmanager.tasks.Task;
import ru.yandex.practicum.taskmanager.tasks.Epic;
import ru.yandex.practicum.taskmanager.tasks.Subtask;
import ru.yandex.practicum.taskmanager.tasks.TaskStatus;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int id = 1;

    private int nextId() {
        return id++;
    }

    // добавить таск
    public Task addTask(Task task) {
        task.setId(nextId());
        tasks.put(task.getId(), task);
        return task;
    }

    // добавить эпик
    public Epic addEpic(Epic epic) {
        epic.setId(nextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    // добавить подзадачу
    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(nextId());
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    // получить таск
    public Task getTaskById(Integer taskId) {

        if (tasks.containsKey(taskId)) return tasks.get(taskId);
        return null;
    }

    // получить эпик
    public Epic getEpicById(Integer epicId) {

        if (epics.containsKey(epicId)) return epics.get(epicId);
        return null;
    }

    // получить подзадачу
    public Subtask getSubtaskById(Integer subTaskId) {

        if (subtasks.containsKey(subTaskId)) return subtasks.get(subTaskId);
        return null;
    }
    //получить список подзадач эпика по id
    public ArrayList<Subtask> getSubtasks(Integer epicId) {
        Epic epic = getEpicById(epicId);
        return new ArrayList<>(epic.getSubtaskList());
    }
    //получить список подзадач эпика по Object
    public ArrayList<Subtask> getSubtasks(Epic epic) {
        return new ArrayList<>(epic.getSubtaskList());
    }

    // обновляем таск
    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.replace(taskId, task);
        return task;
    }

    // обновить подзадачу
    public Subtask updateSubtask(Subtask subtask) {
        Integer subtaskId = subtask.getId();
        if (subtaskId == null || !subtasks.containsKey(subtaskId)) {
            return null;
        }
        int epicId = subtask.getEpicId();
        Subtask oldSubtask = subtasks.get(subtaskId);
        subtasks.replace(subtaskId, subtask);
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.remove(oldSubtask);
        subtaskList.add(subtask);
        epic.setSubtaskList(subtaskList);
        updateEpicStatus(epic);
        return subtask;
    }

    // обновить статус эпика
    private void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> subList = epic.getSubtaskList();
        int allTaskDoneCount = 0;
        int allTaskIsNew = 0;

        for (Subtask subtask : subList) {
            if (subtask.getStatus() == TaskStatus.DONE) {
                allTaskDoneCount++;
            }
            if (subtask.getStatus() == TaskStatus.NEW) {
                allTaskIsNew++;
            }
        }
        if (allTaskDoneCount == subList.size() && allTaskDoneCount > 0) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allTaskIsNew == subList.size() && allTaskIsNew > 0) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    // удалить таски
    public void deleteTasks() {
        tasks.clear();
    }

    // удалить эпики
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    // удалить подзадачи
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(TaskStatus.NEW);
        }
    }

    // удалить таск по id
    public void deleteTask(Integer taskId) {
        tasks.remove(taskId);
    }

    // удалить эпик по id
    public void deleteEpic(Integer epicId) {
        ArrayList<Subtask> epicSubtasks = epics.get(epicId).getSubtaskList();
        epics.remove(epicId);
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
    }

    // удалить подзадачу по id
    public void deleteSubtask(Integer subTaskId) {
        Subtask removedSubtask = subtasks.get(subTaskId);
        subtasks.remove(subTaskId);
        int epicId = removedSubtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.remove(removedSubtask);
        epic.setSubtaskList(subtaskList);
        updateEpicStatus(epic);
    }

    // удалить подзадачу по Object
    public void deleteSubtask(Subtask subtask) {
        //Subtask removedSubtask = subtasks.get(subTaskId);
        subtasks.remove(subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.remove(subtask);
        epic.setSubtaskList(subtaskList);
        updateEpicStatus(epic);
    }

    //удалить все
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    // напечатать все таски/эпики/подзадачи
    public void printAll() {
        if (tasks.size() > 0) {
            for (Task task : tasks.values()) {
                System.out.println(task);
            }
        } else {
            System.out.println("Список задач пуст");
        }
        if (epics.size() > 0) {
            for (Epic epic : epics.values()) {
                System.out.println(epic);
            }
        } else {
            System.out.println("Список эпиков пуст");
        }
        if (subtasks.size() > 0) {
            for (Subtask subtask : subtasks.values()) {
                System.out.println(subtask);
            }
        } else {
            System.out.println("Список подзадач пуст");
        }
    }


}

