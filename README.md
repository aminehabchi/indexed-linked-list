# IndexedDoublyLinkedList

An optimized doubly linked list implementation with indexed access for improved performance.

## Overview

This project implements an **IndexedDoublyLinkedList** - an enhanced version of a traditional doubly linked list that maintains index nodes for faster random access operations. Instead of O(n) traversal time, this implementation achieves better average-case performance through strategic indexing.

## Key Improvements

### 🚀 **Performance Optimization**
- **Indexed Access**: Maintains index nodes at regular intervals (configurable gap)
- **Faster Traversal**: Reduces average access time from O(n) to O(gap + n/gap)
- **Bidirectional Navigation**: Uses both forward and backward traversal for optimal pathfinding

### 🛠️ **Enhanced Features**
- **Configurable Gap Size**: Customize the indexing interval based on your use case
- **Automatic Index Management**: Index nodes are automatically maintained during insertions and deletions
- **Full LinkedList Interface**: Implements all standard list operations
- **Value-Based Operations**: Search by value with `contains()` and `indexOf()` methods
- **Remove by Value**: Remove elements by their value, not just by index
- **Null Value Support**: Properly handles null elements

### 🔧 **Technical Improvements**
- **Smart Rebuilding**: Efficiently rebuilds index nodes only when necessary
- **Memory Efficient**: Minimal overhead for index storage
- **Exception Safe**: Proper bounds checking and error handling

## How It Works

The IndexedDoublyLinkedList maintains a separate list of "index nodes" that point to nodes at regular intervals in the main list:

```
Main List:  [A] ↔ [B] ↔ [C] ↔ [D] ↔ [E] ↔ [F] ↔ [G] ↔ [H]
             ↑           ↑           ↑           ↑
Index:      [0]         [3]         [6]        ...
           (gap=3)
```

When accessing an element:
1. Find the closest index node
2. Traverse from there (forward or backward)
3. Significantly reduces traversal distance

## Usage

### Basic Example

```java
// Create list with gap size of 3
IndexedDoublyLinkedList<Integer> list = new IndexedDoublyLinkedList<>(3);

// Add elements
list.add(10);
list.add(20);
list.add(30);

// Insert at specific position
list.add(1, 15);  // [10, 15, 20, 30]

// Access elements (optimized)
Integer value = list.get(2);  // Returns 20

// Remove elements
Integer removed = list.remove(1);  // Removes 15

// Search operations
boolean exists = list.contains(20);     // Search by value
int position = list.indexOf(30);        // Find position of value
int notFound = list.indexOf(999);       // Returns -1 if not found

// Remove by value (if supported)
// boolean removed = list.removeByValue(20);  // Remove first occurrence
```

### Performance Comparison

| Operation | Traditional LinkedList | IndexedDoublyLinkedList |
|-----------|----------------------|------------------------|
| Access by Index | O(n)           | O(gap + n/gap)         |
| Insert by Index | O(n)           | O(gap + n/gap)         |
| Remove by Index | O(n)           | O(gap + n/gap)         |
| Search by Value | O(n)           | O(n)*                  |
| Contains        | O(n)           | O(n)*                  |
| IndexOf         | O(n)           | O(n)*                  |

*Search operations still require full traversal but benefit from cache-friendly access patterns.

## Building and Running

### Manual Compilation

```bash
# Compile all files
javac *.java

# Run comprehensive tests
java TestIndexedDoublyLinkedList

# Run basic demo
java IndexedDoublyLinkedList
```

## File Structure

```
├── IndexedDoublyLinkedList.java    # Main implementation
├── TestIndexedDoublyLinkedList.java # Comprehensive test suite
└── README.md                       # This file
```

## Testing

The project includes a comprehensive test suite that validates:

- ✅ Basic operations (add, remove, get)
- ✅ Search by value operations (contains, indexOf)
- ✅ Edge cases and error handling
- ✅ Large dataset performance
- ✅ Index node management
- ✅ Null value handling
- ✅ Different gap sizes

Run `java TestIndexedDoublyLinkedList` to execute all tests and verify correctness.

## Configuration

### Gap Size Selection

Choose gap size based on your use case:

- **Small gap (2-5)**: Better for frequent random access, higher memory usage
- **Medium gap (10-20)**: Balanced performance and memory
- **Large gap (50+)**: Lower memory overhead, less access optimization

### Example Configurations

```java
// For frequent random access
new IndexedDoublyLinkedList<>(3);

// For balanced usage
new IndexedDoublyLinkedList<>(10);  // Default

// For memory-conscious applications
new IndexedDoublyLinkedList<>(50);
```

## Implementation Details

### Core Components

- **Node<T>**: Doubly linked node with data, next, and previous pointers
- **IndexNodes**: ArrayList maintaining references to nodes at gap intervals
- **Rebuild Methods**: Efficiently maintain index consistency during modifications

### Key Algorithms

1. **Smart Traversal**: Choose shortest path (forward/backward) from closest index
2. **Incremental Rebuilding**: Only rebuild affected index segments
3. **Dynamic Index Management**: Automatically add/remove index nodes as needed


## Contributing

Feel free to submit issues, feature requests, or pull requests to improve the implementation further!