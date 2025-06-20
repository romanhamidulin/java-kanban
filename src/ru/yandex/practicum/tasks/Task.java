package ru.yandex.practicum.tasks;

import ru.yandex.practicum.enums.TaskStatus;
import ru.yandex.practicum.enums.TaskTypes;

public class Task {

    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    protected TaskTypes type;

    public Task(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskTypes.TASK;;

    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TaskTypes.TASK;;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        //return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
        //        && status == task.status;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (description != null) {
            hash = hash + description.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", type=" + type +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Object getType() {
        return type;
    }
//    public void setStatus(TaskStatus status) {
//        this.status = status;
//    }
}
