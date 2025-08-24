
import java.util.*;

interface LinkedList<T> {

    void add(T value);

    void add(int index, T value);

    T get(int index);

    T remove(int index);

    int size();

    boolean isEmpty();

    void clear();

    boolean contains(T value);

    int indexOf(T value);
}

class Node<T> {

    T data;
    Node<T> next;
    Node<T> prev;

    Node(T data) {
        this.data = data;
    }
}

class IndexedDoublyLinkedList<T> implements LinkedList<T> {

    private Node<T> head, tail;
    private int size;
    private List<Node<T>> indexNodes;
    private int gap;

    public IndexedDoublyLinkedList() {
        this(10);
    }

    public IndexedDoublyLinkedList(int gap) {
        if (gap <= 0) {
            throw new IllegalArgumentException("Gap must be positive");
        }
        this.gap = gap;
        head = null;
        tail = null;
        size = 0;
        indexNodes = new ArrayList<>();
    }

    private Node<T> next(Node<T> node) {
        return node == null ? null : node.next;
    }

    private Node<T> prev(Node<T> node) {
        return node == null ? null : node.prev;
    }

    private void rebuildAfterRemove(int index) {
        int start = index / gap;

        for (int i = start; i < indexNodes.size(); i++) {
            int targetIndex = i * gap;
            if (targetIndex >= size) {
                indexNodes.subList(i, indexNodes.size()).clear();
                break;
            }
            indexNodes.set(i, findNodeAtPosition(targetIndex));
        }
    }

    private void rebuildAfterAdd(int index) {
        int start = index / gap;

        for (int i = start; i < indexNodes.size(); i++) {
            int targetIndex = i * gap;
            indexNodes.set(i, findNodeAtPosition(targetIndex));
        }

        while ((indexNodes.size() * gap) < size) {
            int targetIndex = indexNodes.size() * gap;
            indexNodes.add(findNodeAtPosition(targetIndex));
        }
    }

    private Node<T> findNodeAtPosition(int position) {
        if (position >= size) {
            return null;
        }

        Node<T> current = head;
        for (int i = 0; i < position; i++) {
            current = current.next;
        }
        return current;
    }

    private Node<T> nodeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        int block = index / gap;
        Node<T> current;
        int pos;

        if (block < indexNodes.size()) {
            current = indexNodes.get(block);
            pos = block * gap;
        } else {
            current = tail;
            pos = size - 1;
        }

        while (pos < index) {
            current = current.next;
            pos++;
        }
        while (pos > index) {
            current = current.prev;
            pos--;
        }

        return current;
    }

    @Override
    public void add(T value) {
        Node<T> newNode = new Node<>(value);
        if (tail == null) {
            head = tail = newNode;
            indexNodes.add(head);
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;

        if (!indexNodes.isEmpty()) {
            rebuildAfterAdd(size - 1);
        }
    }

    @Override
    public void add(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == size) {
            add(value);
            return;
        }

        Node<T> nextNode = nodeAtIndex(index);
        Node<T> prevNode = nextNode.prev;
        Node<T> newNode = new Node<>(value);

        newNode.next = nextNode;
        newNode.prev = prevNode;
        nextNode.prev = newNode;
        if (prevNode != null) {
            prevNode.next = newNode;
        } else {
            head = newNode;
        }

        size++;
        rebuildAfterAdd(index);
    }

    @Override
    public T get(int index) {
        Node<T> node = nodeAtIndex(index);
        return node.data;
    }

    @Override
    public T remove(int index) {
        Node<T> node = nodeAtIndex(index);
        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }

        size--;
        rebuildAfterRemove(index);
        return node.data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
        indexNodes.clear();
    }

    @Override
    public boolean contains(T value) {
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(current.data, value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int indexOf(T value) {
        Node<T> current = head;
        int i = 0;
        while (current != null) {
            if (Objects.equals(current.data, value)) {
                return i;
            }
            current = current.next;
            i++;
        }
        return -1;
    }

    // Helper method for testing - shows the current state
    public void printState() {
        System.out.print("List: [");
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(", ");
            }
            current = current.next;
        }
        System.out.println("] Size: " + size + ", IndexNodes: " + indexNodes.size());
    }

    public static void main(String[] args) {
        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(2);
        list.add(10);
        list.add(20);
        list.add(1, 15);
        System.out.println(list.get(0)); // 10
        System.out.println(list.get(1)); // 15
        System.out.println(list.get(2)); // 20
        list.remove(1);
        System.out.println(list.get(1)); // 20

        // Additional test cases
        System.out.println("Size: " + list.size()); // 2
        System.out.println("Contains 20: " + list.contains(20)); // true
        System.out.println("Index of 10: " + list.indexOf(10)); // 0
    }
}
