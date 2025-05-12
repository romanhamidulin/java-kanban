package ru.yandex.practicum.manager;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.enums.TaskStatus;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected int id = 1;

    @Override
    public int nextId() {
        return id++;
    }

    // добавить таск
    @Override
    public Task addTask(Task task) {
        task.setId(nextId());
        tasks.put(task.getId(), task);
        return task;
    }

    // добавить эпик
    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(nextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    // добавить подзадачу
    @Override
    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(nextId());
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    // получить таск
    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            history.add(task);
        }
        return task;
    }

    // получить эпик
    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            history.add(epic);
        }
        return epic;
    }

    // получить подзадачу
    @Override
    public Subtask getSubtaskById(Integer subTaskId) {
        Subtask subtask = subtasks.get(subTaskId);
        if (subtask != null) {
            history.add(subtask);
        }
        return subtask;
    }

    // получить таски
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    // получить эпики
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // получить подзадачи
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получить подзадачи эпика
    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtaskList();
    }

    //получить список подзадач эпика по id
    @Override
    public ArrayList<Subtask> getSubtasks(Integer epicId) {
        Epic epic = getEpicById(epicId);
        if (epic == null) return new ArrayList<>();
        return new ArrayList<>(epic.getSubtaskList());
    }

    //получить список подзадач эпика по Object
    @Override
    public ArrayList<Subtask> getSubtasks(Epic epic) {
        return new ArrayList<>(epic.getSubtaskList());
    }

    //отображение последних просмотренных пользователем задач
    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history.getHistory());
    }

    // обновляем таск
    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.replace(taskId, task);
        return task;
    }

    // обновляем эпик
    @Override
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
    @Override
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
    @Override
    public void updateEpicStatus(Epic epic) {
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
            epics.put(epic.getId(), new Epic(epic.getId(), epic.getName(), epic.getDescription(), epic.getSubtaskList(), TaskStatus.DONE));
        } else if (allTaskIsNew == subList.size()) {
            epics.put(epic.getId(), new Epic(epic.getId(), epic.getName(), epic.getDescription(), epic.getSubtaskList(), TaskStatus.NEW));
        } else {
            epics.put(epic.getId(), new Epic(epic.getId(), epic.getName(), epic.getDescription(), epic.getSubtaskList(), TaskStatus.IN_PROGRESS));

        }
    }

    // удалить таски
    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    // удалить эпики
    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    // удалить подзадачи
    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            updateEpicStatus(epic);
        }
    }

    // удалить таск по id
    @Override
    public void deleteTask(Integer taskId) {
        if (tasks.get(id) != null) {
        history.remove(taskId);
        tasks.remove(taskId);
        }
    }

    // удалить эпик по id
    @Override
    public void deleteEpic(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
        ArrayList<Subtask> epicSubtasks = epics.get(epicId).getSubtaskList();
        for (Subtask subtask : epicSubtasks) {
            subtasks.remove(subtask.getId());
        }
        history.remove(epicId);
        epics.remove(epicId);
        }
    }

    // удалить подзадачу по id
    @Override
    public void deleteSubtask(Integer subTaskId) {
        Subtask removedSubtask = subtasks.get(subTaskId);
        if (removedSubtask!=null) {
        subtasks.remove(subTaskId);
        int epicId = removedSubtask.getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.remove(removedSubtask);
        epic.setSubtaskList(subtaskList);
        updateEpicStatus(epic);
        history.remove(subTaskId);
        }
    }

    // удалить подзадачу по Object
    @Override
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
    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    // напечатать все таски/эпики/подзадачи
    @Override
    public void printAll() {
        if (tasks.size() > 0) {
            System.out.println("Задачи:");
            for (Task task : tasks.values()) {
                System.out.println(task);
            }
        } else {
            System.out.println("Список задач пуст");
        }
        if (epics.size() > 0) {
            System.out.println("Эпики:");
            for (Epic epic : epics.values()) {
                System.out.println(epic);
            }
        } else {
            System.out.println("Список эпиков пуст");
        }
        if (subtasks.size() > 0) {
            System.out.println("Подзадачи:");
            for (Subtask subtask : subtasks.values()) {
                System.out.println(subtask);
            }
        } else {
            System.out.println("Список подзадач пуст");
        }
        if (history.getHistory().size() > 0) {
            System.out.println("История:");
            for (Task task : history.getHistory()) {
                System.out.println(task);
            }
        } else {
            System.out.println("История просмотров пустая");
        }
    }


}
