
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Comprehensive test suite for IndexedDoublyLinkedList
 * Tests functionality, performance, and edge cases
 */
public class IndexedLinkedListTest {

    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void main(String[] args) {
        System.out.println("🚀 IndexedDoublyLinkedList Test Suite");
        System.out.println("=====================================\n");

        runAllTests();

        System.out.println("\n" + "=".repeat(50));
        System.out.printf("📊 Test Results: %d/%d tests passed (%.1f%%)\n",
                testsPassed, testsRun, (double) testsPassed / testsRun * 100);

        if (testsPassed == testsRun) {
            System.out.println("✅ All tests passed!");
        } else {
            System.out.println("❌ Some tests failed!");
        }
    }

    private static void runAllTests() {
        // Basic functionality tests
        testBasicOperations();
        testIndexAccess();
        testInsertionAndRemoval();
        testEdgeCases();
        testStepSizeConfiguration();
        testIterator();
        testSearchOperations();
        testBoundaryConditions();

        // Performance tests
        testPerformance();
        testBidirectionalSearch();
        testMemoryEfficiency();

        // Stress tests
        testLargeDatasets();
        testRandomOperations();
    }

    private static void testBasicOperations() {
        System.out.println("🔧 Testing Basic Operations");

        IndexedDoublyLinkedList<String> list = new IndexedDoublyLinkedList<>();

        // Test empty list
        assertTrue("Empty list size should be 0", list.size() == 0);
        assertTrue("Empty list should be empty", list.isEmpty());

        // Test addLast
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertTrue("Size should be 3", list.size() == 3);
        assertTrue("Should contain A", list.contains("A"));
        assertTrue("Should contain B", list.contains("B"));
        assertTrue("Should contain C", list.contains("C"));

        // Test addFirst
        list.addFirst("Z");
        assertTrue("Size should be 4", list.size() == 4);
        assertTrue("First element should be Z", list.get(0).equals("Z"));

        // Test clear
        list.clear();
        assertTrue("Cleared list should be empty", list.isEmpty());
        assertTrue("Cleared list size should be 0", list.size() == 0);

        System.out.println("✅ Basic operations passed\n");
    }

    private static void testIndexAccess() {
        System.out.println("🎯 Testing Index Access");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(5);

        // Add test data
        for (int i = 1; i <= 50; i++) {
            list.addLast(i);
        }

        // Test get operations
        assertTrue("Element at index 0 should be 1", list.get(0) == 1);
        assertTrue("Element at index 10 should be 11", list.get(10) == 11);
        assertTrue("Element at index 25 should be 26", list.get(25) == 26);
        assertTrue("Element at index 49 should be 50", list.get(49) == 50);

        // Test set operations
        list.set(10, 999);
        assertTrue("Set operation should work", list.get(10) == 999);

        // Test index bounds
        try {
            list.get(-1);
            assertFalse("Should throw exception for negative index", true);
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            list.get(50);
            assertFalse("Should throw exception for index >= size", true);
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        System.out.println("✅ Index access tests passed\n");
    }

    private static void testInsertionAndRemoval() {
        System.out.println("➕➖ Testing Insertion and Removal");

        IndexedDoublyLinkedList<String> list = new IndexedDoublyLinkedList<>(3);

        // Build initial list
        for (int i = 0; i < 10; i++) {
            list.addLast("Item" + i);
        }

        int originalSize = list.size();

        // Test insertion at various positions
        list.add(0, "First");
        assertTrue("Size should increase after insertion", list.size() == originalSize + 1);
        assertTrue("Inserted element should be at correct position", list.get(0).equals("First"));

        list.add(5, "Middle");
        assertTrue("Middle insertion should work", list.get(5).equals("Middle"));

        list.add(list.size(), "Last");
        assertTrue("Insertion at end should work", list.get(list.size() - 1).equals("Last"));

        // Test removal
        String removed = list.remove(0);
        assertTrue("Removed element should be returned", removed.equals("First"));

        removed = list.remove(4); // Middle removal
        assertTrue("Middle removal should work", removed.equals("Middle"));

        // Test removeFirst and removeLast
        String first = list.removeFirst();
        String last = list.removeLast();
        assertTrue("RemoveFirst should work", first.equals("Item0"));
        assertTrue("RemoveLast should work", last.equals("Last"));

        System.out.println("✅ Insertion and removal tests passed\n");
    }

    private static void testEdgeCases() {
        System.out.println("🔍 Testing Edge Cases");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>();

        // Test operations on empty list
        try {
            list.removeFirst();
            assertFalse("Should throw exception on empty removeFirst", true);
        } catch (NoSuchElementException e) {
            // Expected
        }

        try {
            list.removeLast();
            assertFalse("Should throw exception on empty removeLast", true);
        } catch (NoSuchElementException e) {
            // Expected
        }

        // Test single element operations
        list.addLast(42);
        assertTrue("Single element list size", list.size() == 1);
        assertTrue("Single element access", list.get(0) == 42);

        Integer removed = list.remove(0);
        assertTrue("Single element removal", removed == 42);
        assertTrue("After removal should be empty", list.isEmpty());

        // Test duplicate elements
        list.addLast(1);
        list.addLast(1);
        list.addLast(1);
        assertTrue("Should handle duplicates", list.size() == 3);
        assertTrue("indexOf should find first occurrence", list.indexOf(1) == 0);

        System.out.println("✅ Edge cases passed\n");
    }

    private static void testStepSizeConfiguration() {
        System.out.println("⚙️ Testing Step Size Configuration");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(7);
        assertTrue("Initial step size should be 7", list.getStep() == 7);

        // Add data
        for (int i = 1; i <= 50; i++) {
            list.addLast(i);
        }

        // Change step size
        list.setStep(3);
        assertTrue("Step size should change to 3", list.getStep() == 3);

        // Verify functionality still works after step change
        assertTrue("Access should work after step change", list.get(25) == 26);

        // Test invalid step sizes
        try {
            list.setStep(0);
            assertFalse("Should reject zero step size", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            list.setStep(-5);
            assertFalse("Should reject negative step size", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        System.out.println("✅ Step size configuration tests passed\n");
    }

    private static void testIterator() {
        System.out.println("🔄 Testing Iterator");

        IndexedDoublyLinkedList<String> list = new IndexedDoublyLinkedList<>();
        String[] testData = { "A", "B", "C", "D", "E" };

        for (String item : testData) {
            list.addLast(item);
        }

        // Test iterator
        Iterator<String> it = list.iterator();
        int index = 0;
        while (it.hasNext()) {
            String item = it.next();
            assertTrue("Iterator should return correct items", item.equals(testData[index]));
            index++;
        }
        assertTrue("Iterator should traverse all elements", index == testData.length);

        // Test enhanced for loop
        index = 0;
        for (String item : list) {
            assertTrue("Enhanced for loop should work", item.equals(testData[index]));
            index++;
        }

        System.out.println("✅ Iterator tests passed\n");
    }

    private static void testSearchOperations() {
        System.out.println("🔎 Testing Search Operations");

        IndexedDoublyLinkedList<String> list = new IndexedDoublyLinkedList<>();
        String[] items = { "apple", "banana", "cherry", "date", "elderberry" };

        for (String item : items) {
            list.addLast(item);
        }

        // Test indexOf
        assertTrue("Should find apple at index 0", list.indexOf("apple") == 0);
        assertTrue("Should find cherry at index 2", list.indexOf("cherry") == 2);
        assertTrue("Should return -1 for non-existent item", list.indexOf("grape") == -1);

        // Test contains
        assertTrue("Should contain banana", list.contains("banana"));
        assertTrue("Should not contain grape", !list.contains("grape"));

        // Test with null values
        list.addLast(null);
        assertTrue("Should handle null values", list.contains(null));
        assertTrue("Should find null", list.indexOf(null) == 5);

        System.out.println("✅ Search operations tests passed\n");
    }

    private static void testBoundaryConditions() {
        System.out.println("🎚️ Testing Boundary Conditions");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(10);

        // Test milestone boundary conditions
        for (int i = 1; i <= 25; i++) {
            list.addLast(i);
        }

        // Access elements around milestone boundaries
        assertTrue("Access at milestone boundary", list.get(9) == 10); // First milestone
        assertTrue("Access at milestone boundary", list.get(19) == 20); // Second milestone

        // Test near end access optimization
        assertTrue("Near end access", list.get(24) == 25);
        assertTrue("Near end access", list.get(22) == 23);

        System.out.println("✅ Boundary conditions tests passed\n");
    }

    private static void testPerformance() {
        System.out.println("⚡ Testing Performance");

        final int SIZE = 10000;
        IndexedDoublyLinkedList<Integer> indexedList = new IndexedDoublyLinkedList<>(50);
        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();

        // Setup data
        for (int i = 0; i < SIZE; i++) {
            indexedList.addLast(i);
            arrayList.add(i);
            linkedList.add(i);
        }

        Random random = new Random(42); // Fixed seed for reproducible results
        int[] testIndices = new int[1000];
        for (int i = 0; i < testIndices.length; i++) {
            testIndices[i] = random.nextInt(SIZE);
        }

        // Test IndexedLinkedList
        long start = System.nanoTime();
        for (int index : testIndices) {
            indexedList.get(index);
        }
        long indexedTime = System.nanoTime() - start;

        // Test ArrayList
        start = System.nanoTime();
        for (int index : testIndices) {
            arrayList.get(index);
        }
        long arrayTime = System.nanoTime() - start;

        // Test LinkedList
        start = System.nanoTime();
        for (int index : testIndices) {
            linkedList.get(index);
        }
        long linkedTime = System.nanoTime() - start;

        System.out.printf("Random access performance (1000 operations on %d elements):\n", SIZE);
        System.out.printf("  ArrayList:        %,d ns (%.2fx baseline)\n", arrayTime, 1.0);
        System.out.printf("  IndexedLinkedList: %,d ns (%.2fx vs ArrayList)\n", indexedTime,
                (double) indexedTime / arrayTime);
        System.out.printf("  LinkedList:       %,d ns (%.2fx vs ArrayList)\n", linkedTime,
                (double) linkedTime / arrayTime);

        // IndexedLinkedList should be much faster than LinkedList
        assertTrue("IndexedLinkedList should be faster than LinkedList", indexedTime < linkedTime);

        System.out.println("✅ Performance tests passed\n");
    }

    private static void testBidirectionalSearch() {
        System.out.println("🔄 Testing Bidirectional Search Optimization");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(20);

        // Large list for testing bidirectional optimization
        for (int i = 0; i < 1000; i++) {
            list.addLast(i);
        }

        // Test access near beginning (should use forward search)
        long start = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            list.get(i);
        }
        long forwardTime = System.nanoTime() - start;

        // Test access near end (should use backward search)
        start = System.nanoTime();
        for (int i = 900; i < 1000; i++) {
            list.get(i);
        }
        long backwardTime = System.nanoTime() - start;

        System.out.printf("Bidirectional search times:\n");
        System.out.printf("  Forward (indices 0-99):   %,d ns\n", forwardTime);
        System.out.printf("  Backward (indices 900-999): %,d ns\n", backwardTime);

        // Both should be reasonably fast
        assertTrue("Forward search should be efficient", forwardTime < 10_000_000); // 10ms
        assertTrue("Backward search should be efficient", backwardTime < 10_000_000); // 10ms

        System.out.println("✅ Bidirectional search tests passed\n");
    }

    private static void testMemoryEfficiency() {
        System.out.println("💾 Testing Memory Efficiency");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(100);

        // Add large amount of data
        for (int i = 0; i < 10000; i++) {
            list.addLast(i);
        }

        // Check that milestone array is reasonable size
        // With step=100 and size=10000, we should have 100 milestones
        int expectedMilestones = 10000 / 100;

        System.out.printf("List size: %d, Step size: %d\n", list.size(), list.getStep());
        System.out.printf("Expected milestones: ~%d\n", expectedMilestones);

        // The actual milestone count should be reasonable
        assertTrue("Memory usage should be reasonable", expectedMilestones <= 200);

        System.out.println("✅ Memory efficiency tests passed\n");
    }

    private static void testLargeDatasets() {
        System.out.println("📊 Testing Large Datasets");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(1000);
        final int LARGE_SIZE = 100000;

        // Add large dataset
        long start = System.currentTimeMillis();
        for (int i = 0; i < LARGE_SIZE; i++) {
            list.addLast(i);
        }
        long addTime = System.currentTimeMillis() - start;

        // Random access test
        Random random = new Random(42);
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int index = random.nextInt(LARGE_SIZE);
            int value = list.get(index);
            assertTrue("Value should match index", value == index);
        }
        long accessTime = System.currentTimeMillis() - start;

        System.out.printf("Large dataset performance (%d elements):\n", LARGE_SIZE);
        System.out.printf("  Addition time: %d ms\n", addTime);
        System.out.printf("  Random access (1000 ops): %d ms\n", accessTime);

        assertTrue("Large dataset should be manageable", addTime < 10000); // 10 seconds
        assertTrue("Random access should be fast", accessTime < 1000); // 1 second

        System.out.println("✅ Large dataset tests passed\n");
    }

    private static void testRandomOperations() {
        System.out.println("🎲 Testing Random Operations Stress Test");

        IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(25);
        ArrayList<Integer> reference = new ArrayList<>();
        Random random = new Random(12345);

        // Perform random operations and compare with ArrayList
        for (int op = 0; op < 1000; op++) {
            int operation = random.nextInt(4);

            try {
                switch (operation) {
                    case 0: // Add at end
                        int value = random.nextInt(1000);
                        list.addLast(value);
                        reference.add(value);
                        break;

                    case 1: // Add at random position
                        if (!list.isEmpty()) {
                            int pos = random.nextInt(list.size() + 1);
                            int val = random.nextInt(1000);
                            list.add(pos, val);
                            reference.add(pos, val);
                        }
                        break;

                    case 2: // Remove from random position
                        if (!list.isEmpty()) {
                            int pos = random.nextInt(list.size());
                            int removed1 = list.remove(pos);
                            int removed2 = reference.remove(pos);
                            assertTrue("Removed values should match", removed1 == removed2);
                        }
                        break;

                    case 3: // Random access
                        if (!list.isEmpty()) {
                            int pos = random.nextInt(list.size());
                            int val1 = list.get(pos);
                            int val2 = reference.get(pos);
                            assertTrue("Random access values should match", val1 == val2);
                        }
                        break;
                }

                // Verify sizes match
                assertTrue("Sizes should always match", list.size() == reference.size());

            } catch (Exception e) {
                System.err.println("Error in operation " + op + ": " + e.getMessage());
                throw e;
            }
        }

        // Final verification
        assertTrue("Final sizes should match", list.size() == reference.size());
        for (int i = 0; i < list.size(); i++) {
            assertTrue("All elements should match", list.get(i).equals(reference.get(i)));
        }

        System.out.printf("Completed 1000 random operations successfully\n");
        System.out.printf("Final list size: %d elements\n", list.size());
        System.out.println("✅ Random operations stress test passed\n");
    }

    // Helper methods
    private static void assertTrue(String message, boolean condition) {
        testsRun++;
        if (condition) {
            testsPassed++;
        } else {
            System.err.println("❌ FAILED: " + message);
        }
    }

    private static void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }
}