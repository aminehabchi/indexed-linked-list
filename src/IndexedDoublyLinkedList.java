
import java.util.*;

public class IndexedDoublyLinkedList<T> implements Iterable<T> {
    private class Node {
        T data;
        Node prev, next;
        Node(T data) { this.data = data; }
    }
    
    private Node head, tail;
    private int size;
    private List<Node> indexNodes; // milestone array
    private int step; // configurable step size
    
    public IndexedDoublyLinkedList() {
        this(10); // default step size
    }
    
    public IndexedDoublyLinkedList(int step) {
        if (step <= 0) throw new IllegalArgumentException("Step must be positive");
        this.step = step;
        head = new Node(null); // sentinel head
        tail = new Node(null); // sentinel tail
        head.next = tail;
        tail.prev = head;
        size = 0;
        indexNodes = new ArrayList<>();
    }
    
    public void addLast(T data) {
        Node node = new Node(data);
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
        size++;
        
        // maintain index array
        if (size % step == 0) {
            indexNodes.add(node);
        }
    }
    
    public void addFirst(T data) {
        Node node = new Node(data);
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
        size++;
        
        // shift all milestone indices by 1, then rebuild if necessary
        shiftIndicesForward();
    }
    
    public void add(int index, T data) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (index == 0) {
            addFirst(data);
            return;
        }
        if (index == size) {
            addLast(data);
            return;
        }
        
        Node target = getNodeAtIndex(index);
        Node node = new Node(data);
        node.prev = target.prev;
        node.next = target;
        target.prev.next = node;
        target.prev = node;
        size++;
        
        // intelligently update index array
        updateIndexAfterInsertion(index);
    }
    
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        
        Node target = getNodeAtIndex(index);
        T data = target.data;
        
        target.prev.next = target.next;
        target.next.prev = target.prev;
        size--;
        
        // intelligently update index array
        updateIndexAfterRemoval(index);
        return data;
    }
    
    public T removeFirst() {
        if (size == 0) throw new NoSuchElementException("List is empty");
        return remove(0);
    }
    
    public T removeLast() {
        if (size == 0) throw new NoSuchElementException("List is empty");
        return remove(size - 1);
    }
    
    public T get(int index) {
        return getNodeAtIndex(index).data;
    }
    
    public T set(int index, T data) {
        Node node = getNodeAtIndex(index);
        T oldData = node.data;
        node.data = data;
        return oldData;
    }
    
    // Improved search with bidirectional optimization
    public Node getNodeAtIndex(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        
        // For small lists or small indices, traverse from head
        if (index < step || indexNodes.isEmpty()) {
            Node current = head.next;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        }
        
        // For indices near the end, consider traversing from tail
        if (index > size - step) {
            Node current = tail.prev;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
        
        // Binary search in milestone nodes
        int left = 0, right = indexNodes.size() - 1;
        Node start = head.next;
        int startIdx = 0;
        
        while (left <= right) {
            int mid = (left + right) / 2;
            int midIndex = (mid + 1) * step - 1; // milestone index
            
            if (midIndex == index) {
                return indexNodes.get(mid);
            } else if (midIndex < index) {
                left = mid + 1;
                start = indexNodes.get(mid);
                startIdx = midIndex;
            } else {
                right = mid - 1;
            }
        }
        
        // Choose direction: forward from milestone or backward from next milestone
        int distanceForward = index - startIdx;
        int distanceBackward = Integer.MAX_VALUE;
        Node backStart = null;
        int backStartIdx = 0;
        
        // Check if we can go backward from next milestone
        if (left < indexNodes.size()) {
            backStartIdx = left * step - 1;
            backStart = indexNodes.get(left);
            distanceBackward = backStartIdx - index;
        } else {
            // Use tail as backward start
            backStartIdx = size - 1;
            backStart = tail.prev;
            distanceBackward = backStartIdx - index;
        }
        
        // Choose the shorter path
        if (distanceForward <= distanceBackward) {
            // Traverse forward from milestone
            Node current = start;
            for (int i = startIdx; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            // Traverse backward
            Node current = backStart;
            for (int i = backStartIdx; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }
    
    // Efficient index maintenance methods
    private void shiftIndicesForward() {
        // When inserting at front, all milestone positions shift by 1
        // Check if we need to add/remove milestones
        if (size % step == 0) {
            // New milestone at the end
            Node current = tail.prev;
            indexNodes.add(current);
        }
        
        // Check if first milestone is no longer at correct position
        if (!indexNodes.isEmpty() && size > step) {
            int firstMilestoneIndex = step - 1;
            Node current = head.next;
            for (int i = 0; i < firstMilestoneIndex; i++) {
                current = current.next;
            }
            indexNodes.set(0, current);
            
            // Update subsequent milestones if needed
            updateSubsequentMilestones(1);
        }
    }
    
    private void updateIndexAfterInsertion(int insertIndex) {
        // Determine which milestones are affected
        int firstAffectedMilestone = insertIndex / step;
        
        // If insertion creates a new milestone
        if (size % step == 0) {
            Node current = tail.prev;
            indexNodes.add(current);
        }
        
        // Update affected milestones from the insertion point
        updateMilestonesFromIndex(firstAffectedMilestone);
    }
    
    private void updateIndexAfterRemoval(int removeIndex) {
        // Determine which milestones are affected
        int firstAffectedMilestone = removeIndex / step;
        
        // If removal eliminates a milestone
        if ((size + 1) % step == 0 && !indexNodes.isEmpty()) {
            indexNodes.remove(indexNodes.size() - 1);
        }
        
        // Update affected milestones from the removal point
        if (firstAffectedMilestone < indexNodes.size()) {
            updateMilestonesFromIndex(firstAffectedMilestone);
        }
    }
    
    private void updateMilestonesFromIndex(int startMilestone) {
        if (startMilestone >= indexNodes.size()) return;
        
        // Find the starting node for updates
        Node current;
        int currentIndex;
        
        if (startMilestone == 0) {
            current = head.next;
            currentIndex = 0;
        } else {
            // Start from previous milestone
            current = indexNodes.get(startMilestone - 1);
            currentIndex = (startMilestone - 1) * step + (step - 1);
        }
        
        // Update milestones from startMilestone onwards
        for (int i = startMilestone; i < indexNodes.size(); i++) {
            int targetIndex = i * step + (step - 1);
            
            // Navigate to target position
            while (currentIndex < targetIndex && current.next != tail) {
                current = current.next;
                currentIndex++;
            }
            
            if (currentIndex == targetIndex) {
                indexNodes.set(i, current);
            } else {
                // Target index doesn't exist, remove remaining milestones
                for (int j = indexNodes.size() - 1; j >= i; j--) {
                    indexNodes.remove(j);
                }
                break;
            }
        }
    }
    
    private void updateSubsequentMilestones(int startMilestone) {
        if (startMilestone >= indexNodes.size()) return;
        
        Node current = indexNodes.get(startMilestone - 1);
        int currentIndex = (startMilestone - 1) * step + (step - 1);
        
        for (int i = startMilestone; i < indexNodes.size(); i++) {
            int targetIndex = i * step + (step - 1);
            
            while (currentIndex < targetIndex) {
                current = current.next;
                currentIndex++;
            }
            
            indexNodes.set(i, current);
        }
    }
    
    // Fallback method - only used when step size changes
    private void rebuildIndexArray() {
        indexNodes.clear();
        Node current = head.next;
        int count = 1;
        
        while (current != tail) {
            if (count % step == 0) {
                indexNodes.add(current);
            }
            current = current.next;
            count++;
        }
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        head.next = tail;
        tail.prev = head;
        size = 0;
        indexNodes.clear();
    }
    
    public int indexOf(T data) {
        Node current = head.next;
        int index = 0;
        while (current != tail) {
            if (Objects.equals(current.data, data)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }
    
    public boolean contains(T data) {
        return indexOf(data) != -1;
    }
    
    public void setStep(int newStep) {
        if (newStep <= 0) throw new IllegalArgumentException("Step must be positive");
        this.step = newStep;
        rebuildIndexArray();
    }
    
    public int getStep() {
        return step;
    }
    
    public void printForward() {
        Node current = head.next;
        while (current != tail) {
            System.out.print(current.data + " <-> ");
            current = current.next;
        }
        System.out.println("NULL");
    }
    
    public void printBackward() {
        Node current = tail.prev;
        while (current != head) {
            System.out.print(current.data + " <-> ");
            current = current.prev;
        }
        System.out.println("NULL");
    }
    
    public void printIndexNodes() {
        System.out.print("Index nodes (every " + step + "th): ");
        for (int i = 0; i < indexNodes.size(); i++) {
            System.out.print("[" + ((i + 1) * step - 1) + "]=" + indexNodes.get(i).data + " ");
        }
        System.out.println();
    }
    
    // Iterator support
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node current = head.next;
            
            @Override
            public boolean hasNext() {
                return current != tail;
            }
            
            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }
    
    // Test and demonstration
    public static void main(String[] args) {
        System.out.println("=== Testing IndexedDoublyLinkedList ===\n");
        
        // Test with default step
        IndexedDoublyLinkedList<Integer> dll = new IndexedDoublyLinkedList<>();
        System.out.println("Created list with default step: " + dll.getStep());
        
        // Add elements
        for (int i = 1; i <= 55; i++) {
            dll.addLast(i);
        }
        
        System.out.println("Added 55 elements");
        System.out.println("List size: " + dll.size());
        dll.printIndexNodes();
        
        // Test search performance
        System.out.println("\n=== Search Tests ===");
        int[] testIndices = {5, 15, 25, 35, 45, 50, 33};
        
        for (int idx : testIndices) {
            System.out.println("Value at index " + idx + ": " + dll.get(idx));
        }
        
        // Test with different step size
        System.out.println("\n=== Changing Step Size ===");
        dll.setStep(5);
        System.out.println("Changed step to: " + dll.getStep());
        dll.printIndexNodes();
        
        // Test insertion and removal
        System.out.println("\n=== Insertion/Removal Tests ===");
        System.out.println("Before insertion - Index nodes:");
        dll.printIndexNodes();
        
        dll.add(10, 999);
        System.out.println("Inserted 999 at index 10");
        System.out.println("After insertion - Index nodes:");
        dll.printIndexNodes();
        System.out.println("Value at index 10: " + dll.get(10));
        System.out.println("List size: " + dll.size());
        
        System.out.println("Removed: " + dll.remove(10));
        System.out.println("After removal - Index nodes:");
        dll.printIndexNodes();
        System.out.println("Value at index 10: " + dll.get(10));
        System.out.println("List size: " + dll.size());
        
        // Test multiple insertions
        System.out.println("\n=== Multiple Operations Test ===");
        dll.addFirst(0);
        System.out.println("Added 0 at front - Index nodes:");
        dll.printIndexNodes();
        
        dll.add(30, 777);
        dll.add(35, 888);
        System.out.println("After multiple insertions - Index nodes:");
        dll.printIndexNodes();
        System.out.println("List size: " + dll.size());
        
        // Test bidirectional search
        System.out.println("\n=== Testing Large List (Bidirectional Search) ===");
        IndexedDoublyLinkedList<Integer> bigList = new IndexedDoublyLinkedList<>(20);
        for (int i = 1; i <= 1000; i++) {
            bigList.addLast(i);
        }
        
        System.out.println("Created list with 1000 elements, step = " + bigList.getStep());
        System.out.println("Testing access near end:");
        System.out.println("Value at index 950: " + bigList.get(950));
        System.out.println("Value at index 980: " + bigList.get(980));
        System.out.println("Value at index 999: " + bigList.get(999));
    }
}