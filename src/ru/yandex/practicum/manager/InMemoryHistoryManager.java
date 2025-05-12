package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList historyList = new CustomLinkedList();

    // Добавление нового просмотра задачи в историю
    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            historyList.linkLast(task);
        }
    }

    // Удаление просмотра из истории
    @Override
    public void remove(int id) {
        historyList.removeNode(historyList.getNode(id));
    }

    // Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    private static class CustomLinkedList {

        private final Map<Integer, Node> history = new HashMap<>();

        private Node head;

        private Node tail;

        private void linkLast(Task task) {
            Node element = new Node();
            element.setTask(task);

            if (history.containsKey(task.getId())) {
                removeNode(history.get(task.getId()));
            }

            if (head == null) {
                tail = element;
                head = element;
                element.setNext(null);
                element.setPrev(null);
            } else {
                element.setPrev(tail);
                element.setNext(null);
                tail.setNext(element);
                tail = element;
            }

            history.put(task.getId(), element);
        }

        private List<Task> getTasks() {
            List<Task> result = new ArrayList<>();
            Node element = head;
            while (element != null) {
                result.add(element.getTask());
                element = element.getNext();
            }
            return result;
        }

        private void removeNode(Node node) {
            if (node != null) {
                history.remove(node.getTask().getId());
                Node prev = node.getPrev();
                Node next = node.getNext();

                if (head == node) {
                    head = node.getNext();
                }
                if (tail == node) {
                    tail = node.getPrev();
                }

                if (prev != null) {
                    prev.setNext(next);
                }

                if (next != null) {
                    next.setPrev(prev);
                }
            }
        }

        private Node getNode(int id) {
            return history.get(id);
        }
    }
}

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
}