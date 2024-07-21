package manager;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager, Comparator<Task> {

    private final MyLinkedList history = new MyLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        history.linkList(task);
    }

    @Override
    public void remove(int id) {
        history.remoteNode(history.myMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public int compare(Task o1, Task o2) {
        if(o1.getId() > o2.getId()){
            return 1;
        } else if(o1.getId() < o2.getId()){
            return -1;
        } else{
            return 0;
        }
    }
    public static class Node{
        private Node prev;
        private final Task task;
        private Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

    }
    public static class MyLinkedList{
        private Node head;
        private Node tail;
        private final Map<Integer, Node> myMap = new HashMap<>();

        private void linkList(Task task){
            Node newNode = new Node(task, tail, null);

            if (tail == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            myMap.put(task.getId(), newNode);
        }
        private void remoteNode(Node node){
            if (node != null) {
                myMap.remove(node.task.getId());
                Node prev = node.prev;
                Node next = node.next;

                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }

                node.prev = null;
                node.next = null;

                if (node == head) {
                    head = next;
                }
                if (node == tail) {
                    tail = prev;
                }
            }
        }
        private List<Task> getTasks(){
            List<Task> tasksList = new ArrayList<>();
            Node element = head;

            while(element != null){
                tasksList.add(element.task);
                element = element.next;
            }
            return tasksList;
        }
    }
}