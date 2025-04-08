package ru.practicum.yandex.tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
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

    public void setStatus(TaskStatus status) {
        int allTaskDoneCount = 0;
        int allTaskIsNew = 0;

        for (Subtask subtask : subtaskList) {
            if (subtask.getStatus() == TaskStatus.DONE) {
                allTaskDoneCount++;
            }
            if (subtask.getStatus() == TaskStatus.NEW) {
                allTaskIsNew++;
            }
        }
        if (allTaskDoneCount == subtaskList.size() && allTaskDoneCount > 0) {
            //setStatus(TaskStatus.DONE);
            super.setStatus(TaskStatus.DONE);
        } else if (allTaskIsNew == subtaskList.size()) {
            //setStatus(TaskStatus.NEW);
            super.setStatus(TaskStatus.NEW);
        } else {
            //setStatus(TaskStatus.IN_PROGRESS);
            super.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
