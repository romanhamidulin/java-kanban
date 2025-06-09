package ru.yandex.practicum.tasks;

import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.enums.TaskTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, TaskStatus status, LocalDateTime startTime, int duration ) {
        super(id, name, description, status, startTime, duration);
        type = TaskTypes.EPIC;
    }

    public Epic(int id, String name, String description, ArrayList<Subtask> subList,TaskStatus status, LocalDateTime startTime, int duration) {
        super(id, name, description, status, startTime, duration);
        this.subtaskList = subList;
        type = TaskTypes.EPIC;
    }

    public Epic(String name, String description) {
        super(name, description);
        type = TaskTypes.EPIC;
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
                ", type=" + type + ", startTime=" + startTime +
                ", duration=" + duration + '}';
        return result;
    }

}
