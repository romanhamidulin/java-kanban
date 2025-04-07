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

    public Task addTask(Task task) {
        task.setId(nextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.replace(taskId, task);
        return task;
    }

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

    public Epic addEpic(Epic epic) {
        epic.setId(nextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(nextId());
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

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

    public void printAllTasks() {
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
    }

    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(Integer taskId) {

        if (tasks.containsKey(taskId)) return tasks.get(taskId);
        return null;
    }

    public Epic getEpicById(Integer epicId) {

        if (epics.containsKey(epicId)) return epics.get(epicId);
        return null;
    }

    public Subtask getSubtaskById(Integer subTaskId) {

        if (subtasks.containsKey(subTaskId)) return subtasks.get(subTaskId);
        return null;
    }

    public void deleteTask(Integer taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(Integer epicId) {
        epics.remove(epicId);
    }

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


}

