package ru.yandex.practicum.tasks;

import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.enums.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {

    private ArrayList<Subtask> subtaskList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        type = TaskTypes.EPIC;
    }

    public Epic(int id, String name, String description, ArrayList<Subtask> subList, TaskStatus status, LocalDateTime startTime, Duration duration) {
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
        updateStartTime();
        updateEndTime();
        updateDuration();
    }

    public void clearSubtasks() {
        subtaskList.clear();
        updateStartTime();
        updateEndTime();
        updateDuration();
    }

    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskList);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setSubtaskList(ArrayList<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
        updateStartTime();
        updateEndTime();
        updateDuration();
    }

    public void updateDuration() {
        List<Subtask> validSubtasks = subtaskList.stream()
                .filter(subTask -> subTask.getStartTime() != null && subTask.getEndTime() != null)
                .sorted(Comparator.comparing(Subtask::getStartTime)) // Сортируем по времени начала
                .toList();

        if (validSubtasks.isEmpty()) {
            super.setStartTime(null);
            endTime = null;
            super.setDuration(Duration.ZERO);
            return;
        }

        Optional<LocalDateTime> startTime = validSubtasks.stream()
                .findFirst()
                .map(Subtask::getStartTime);

        Optional<LocalDateTime> endTime = validSubtasks.stream()
                .reduce((first, second) -> second) // Берем последний элемент
                .map(Subtask::getEndTime);

        super.setDuration(Duration.between(endTime.get(), startTime.get()));
    }

    public void updateStartTime() {
        super.setStartTime(
                subtaskList.stream()
                        .filter(subTask -> subTask.getStartTime() != null && subTask.getEndTime() != null)
                        .min(Comparator.comparing(Subtask::getStartTime)) // Находим подзадачу с минимальным (первым) временем
                        .map(Subtask::getStartTime)
                        .orElse(null)
        );
    }

    public void updateEndTime() {
        this.endTime = subtaskList.stream()
                .filter(subTask -> subTask.getStartTime() != null && subTask.getEndTime() != null)
                .max(Comparator.comparing(Subtask::getStartTime))
                .map(Subtask::getEndTime)
                .orElse(null);
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
                ", duration=" + duration.toMinutes() + '}';
        return result;
    }

}
