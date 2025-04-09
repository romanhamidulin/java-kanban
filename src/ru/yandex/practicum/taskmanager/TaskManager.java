package ru.yandex.practicum.taskmanager;

import java.util.HashMap;
import java.util.ArrayList;

import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.TaskStatus;

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
        return tasks.get(taskId);
    }

    // получить эпик
    public Epic getEpicById(Integer epicId) {

        return epics.get(epicId);
    }

    // получить подзадачу
    public Subtask getSubtaskById(Integer subTaskId) {

        return subtasks.get(subTaskId);
    }
    // получить таски
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }
    // получить эпики
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }
    // получить подзадачи
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
    // получить подзадачи эпика
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtaskList();
    }
    //получить список подзадач эпика по id
    public ArrayList<Subtask> getSubtasks(Integer epicId) {
        Epic epic = getEpicById(epicId);
        if (epic == null) return new ArrayList<>();
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
    // обновляем эпик
    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId == null || !epics.containsKey(epicId)) {
            return null;
        }
        // удаляем подзадачи у обновляемого эпика
        Epic oldEpic = epics.get(epicId);
        ArrayList<Subtask> oldEpicSubList = oldEpic.getSubtaskList();
        if (!oldEpicSubList.isEmpty()) {
            for (Subtask subtask : oldEpicSubList) {
                subtasks.remove(subtask.getId());
            }
        }
        epics.replace(epicId, epic);
        //добавляем подзадачи обновленного эпика
        ArrayList<Subtask> newEpicSubList = epic.getSubtaskList();
        if (!newEpicSubList.isEmpty()) {
            for (Subtask subtask : newEpicSubList) {
                subtasks.put(subtask.getId(), subtask);
            }
        }
        // обновляем статус эпика
        updateEpicStatus(epic);
        return epic;
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
            epics.put(epic.getId(), new Epic(epic.getId(),epic.getName(),epic.getDescription(),epic.getSubtaskList(),TaskStatus.DONE));
        } else if (allTaskIsNew == subList.size()) {
            epics.put(epic.getId(), new Epic(epic.getId(),epic.getName(),epic.getDescription(),epic.getSubtaskList(),TaskStatus.NEW));
        } else {
            epics.put(epic.getId(), new Epic(epic.getId(),epic.getName(),epic.getDescription(),epic.getSubtaskList(),TaskStatus.IN_PROGRESS));

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
            updateEpicStatus(epic);
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
        subtasks.remove(subtask.getId());
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

