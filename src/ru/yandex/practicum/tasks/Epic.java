package ru.yandex.practicum.tasks;

import ru.yandex.practicum.enums.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }
    public Epic(int id, String name, String description, ArrayList<Subtask> subList,TaskStatus status) {
        super(id, name, description, status);
        this.subtaskList = subList;
    }
    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    public ArrayList<Subtask> getSubtaskList() {
        //return subtaskList;
        return new ArrayList<>(subtaskList);
    }

    public void setSubtaskList(ArrayList<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name= " + getName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id=" + getId() + ",";
        if (subtaskList != null) {
            result += " subtaskList.size = " + subtaskList.size();
        } else {
            result += " subtaskList.size = null";
        }
        result += ", status = " + getStatus() +
                '}';
        return result;
    }

}
