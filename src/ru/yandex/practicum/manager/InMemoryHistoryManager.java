package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    //private static final int MAX_HISTORY_STORAGE = 10;
    //private final ArrayList<Task> historyList = new ArrayList<>();
    //создаем свой двусвязный список просмотренных задач
    final private CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
       /* if (historyList.size() == MAX_HISTORY_STORAGE) {
            historyList.removeFirst();
        }
        historyList.add(task);*/
        historyList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyList.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        //return new ArrayList<>(historyList);
        return historyList.getTasks();
    }
    private static class CustomLinkedList {

        //внутренний класс для создания узла в коллекции
        private static class Node<T> {
            public T task;
            public Node<T> next;
            public Node<T> prev;

            public Node(Node<T> prev, T task, Node<T> next) {
                this.task = task;
                this.next = next;
                this.prev = prev;
            }
        }

        private Node<Task> head;
        private Node<Task> tail;

        //мапа для хранения пары номер задачи - узел
        //(для удаления предыдущего просмотра задачи из истории за O(1))
        final private Map<Integer, Node<Task>> history = new HashMap<>();

        //метод добавляет задачу в конец списка и удаляет ее предыдущий просмотр
        public void linkLast(Task task) {
            if (history.containsKey(task.getId())) {
                removeNode(history.get(task.getId()));
            }
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            history.put(task.getId(), newNode);
        }

        //метод принимает id задачи на удаление и передает соответствующий узел в сл метод по удалению
        private void removeNode(int id) {
            if (history.containsKey(id)) {
                removeNode(history.get(id));
            }
        }

        //метод удаляет из мапы узел с уже просмотренной и добавляемой вновь задачей
        private void removeNode(Node<Task> node) {
            final Node<Task> prev = node.prev;
            final Node<Task> next = node.next;
            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }
            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
            node.task = null;
        }

        //метод формирует список задач для просмотра истории
        private List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            for (Node<Task> node = head; node != null; node = node.next) {
                tasks.add(node.task);
            }
            return tasks;
        }
    }
}
