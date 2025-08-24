public class TestIndexedDoublyLinkedList {
    
    private static int testCount = 0;
    private static int passedTests = 0;
    
    private static void test(String testName, boolean condition) {
        testCount++;
        if (condition) {
            System.out.println("✓ " + testName);
            passedTests++;
        } else {
            System.out.println("✗ " + testName);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Testing IndexedDoublyLinkedList ===\n");
        
        // Test 1: Basic functionality
        System.out.println("--- Basic Functionality Tests ---");
        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(3);
        
        test("Initial list is empty", list.isEmpty());
        test("Initial size is 0", list.size() == 0);
        
        // Test 2: Adding elements
        System.out.println("\n--- Adding Elements Tests ---");
        list.add(10);
        test("Add first element", list.get(0) == 10 && list.size() == 1);
        
        list.add(20);
        list.add(30);
        test("Add multiple elements", list.get(1) == 20 && list.get(2) == 30 && list.size() == 3);
        
        list.add(1, 15);
        test("Insert at middle", list.get(1) == 15 && list.get(2) == 20 && list.size() == 4);
        
        list.add(0, 5);
        test("Insert at beginning", list.get(0) == 5 && list.get(1) == 10 && list.size() == 5);
        
        list.add(5, 35);
        test("Insert at end", list.get(5) == 35 && list.size() == 6);
        
        System.out.print("Current state: ");
        list.printState();
        
        // Test 3: Removing elements
        System.out.println("\n--- Removing Elements Tests ---");
        Integer removed = list.remove(1);
        test("Remove element returns correct value", removed == 10);
        test("Remove element updates list correctly", list.get(1) == 15 && list.size() == 5);
        
        removed = list.remove(0);
        test("Remove first element", removed == 5 && list.get(0) == 15);
        
        removed = list.remove(list.size() - 1);
        test("Remove last element", removed == 35 && list.size() == 3);
        
        System.out.print("After removals: ");
        list.printState();
        
        // Test 4: Search operations
        System.out.println("\n--- Search Operations Tests ---");
        test("Contains existing element", list.contains(20));
        test("Contains non-existing element", !list.contains(100));
        test("IndexOf existing element", list.indexOf(30) == 2);
        test("IndexOf non-existing element", list.indexOf(100) == -1);
        
        // Test 5: Edge cases and error handling
        System.out.println("\n--- Edge Cases Tests ---");
        try {
            list.get(-1);
            test("Get negative index throws exception", false);
        } catch (IndexOutOfBoundsException e) {
            test("Get negative index throws exception", true);
        }
        
        try {
            list.get(list.size());
            test("Get index >= size throws exception", false);
        } catch (IndexOutOfBoundsException e) {
            test("Get index >= size throws exception", true);
        }
        
        try {
            list.add(-1, 99);
            test("Add negative index throws exception", false);
        } catch (IndexOutOfBoundsException e) {
            test("Add negative index throws exception", true);
        }
        
        // Test 6: Large dataset test
        System.out.println("\n--- Large Dataset Test ---");
        IndexedDoublyLinkedList<Integer> bigList = new IndexedDoublyLinkedList<>(5);
        
        // Add 100 elements
        for (int i = 0; i < 100; i++) {
            bigList.add(i * 10);
        }
        test("Large dataset add", bigList.size() == 100);
        test("Large dataset random access", bigList.get(50) == 500);
        
        // Insert in middle
        bigList.add(25, 999);
        test("Large dataset insert", bigList.get(25) == 999 && bigList.get(26) == 250);
        
        // Remove from middle
        Integer removedBig = bigList.remove(75);
        test("Large dataset remove", removedBig == 740 && bigList.size() == 100);
        
        // Test 7: Clear functionality
        System.out.println("\n--- Clear Functionality Test ---");
        list.clear();
        test("Clear makes list empty", list.isEmpty() && list.size() == 0);
        
        // Test 8: Different gap sizes
        System.out.println("\n--- Different Gap Sizes Test ---");
        IndexedDoublyLinkedList<String> stringList = new IndexedDoublyLinkedList<>(2);
        String[] words = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};
        
        for (String word : words) {
            stringList.add(word);
        }
        
        test("String list with gap=2", stringList.get(3).equals("date") && stringList.size() == 7);
        
        stringList.add(2, "blueberry");
        test("String list insert", stringList.get(2).equals("blueberry") && stringList.get(3).equals("cherry"));
        
        // Test 9: Null values
        System.out.println("\n--- Null Values Test ---");
        IndexedDoublyLinkedList<String> nullList = new IndexedDoublyLinkedList<>(3);
        nullList.add("first");
        nullList.add(null);
        nullList.add("third");
        
        test("Null value handling", nullList.get(1) == null && nullList.contains(null));
        test("IndexOf null", nullList.indexOf(null) == 1);
        
        // Final results
        System.out.println("\n=== Test Results ===");
        System.out.println("Passed: " + passedTests + "/" + testCount + " tests");
        
        if (passedTests == testCount) {
            System.out.println("🎉 All tests passed! The implementation is working correctly.");
        } else {
            System.out.println("⚠️  Some tests failed. Check the implementation.");
        }
    }
}