package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

class Node {
    private Task task;
    private Node prev;
    private Node next;

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public Task getTask() {
        return task;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Node node = (Node) object;
        return next == node.next && prev == node.prev && task == node.task;
        //return id == task.id;
    }
}
