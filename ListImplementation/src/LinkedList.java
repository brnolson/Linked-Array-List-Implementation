// Brenen Olson, ols00175

public class LinkedList<T extends Comparable<T>> implements List<T> {
    private Node<T> head;
    private int size; // keeps track of list size
    private boolean isSorted;

    public LinkedList() {
        head = null;
        size = 0;
        isSorted = true;
    }

    @Override
    public boolean add(T element) {
        if (element == null) return false; // check for null element

        if (head == null) {
            head = new Node<>(element); // create new head node with the element if list is empty
        } else {
            Node<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(new Node<>(element));
        }
        size++;
        if (isSorted && size > 1) { // update sorting status
            isSorted = (element.compareTo(get(size - 2)) >= 0);
        }
        return true; // element added successfully
    }

    @Override
    public boolean add(int index, T element) {
        if (element == null || index < 0 || index > size) return false; // check for valid input

        if (index == 0) { // insert at the beginning if index is 0
            Node<T> newHead = new Node<>(element);
            newHead.setNext(head);
            head = newHead;
        } else {
            // insert new node at specified index
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            Node<T> hold = current.getNext();
            current.setNext(new Node<>(element));
            current.getNext().setNext(hold);
        }
        size++; // increase list size
        isSorted = false; // adding breaks the sorted order
        return true; // insert success
    }

    @Override
    public void clear() {
        // reset list
        head = null;
        size = 0;
        isSorted = true;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) return null; // check for valid index

        // move to specified index and retrieve element
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }

    @Override
    public int indexOf(T element) {
        if (element == null) return -1; // check for null elements

        if (isSorted) {
            // binary search for sorted lists
            int low = 0;
            int high = size - 1;
            int first = -1;

            while (low <= high) {
                int mid = low + (high - low) / 2;
                int comparisonResult = element.compareTo(get(mid));

                if (comparisonResult == 0) {
                    // in the case of duplicates, find the first occurrence
                    first = mid;
                    high = mid - 1;
                } else if (comparisonResult < 0) {
                    high = mid - 1; // search in the left half
                } else {
                    low = mid + 1; // search in the right half
                }
            }
            return first; // return first occurrence of element
        } else {
            // linear search for unsorted lists
            Node<T> current = head;
            for (int i = 0; i < size; i++) {
                if (element.equals(current.getData())) {
                    return i; // element index
                }
                current = current.getNext();
            }
            return -1; // element not found
        }
    }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public int size() { return size; }

    @Override
    public void sort() {
        if (isSorted || size <= 1) return; // only sort if list is not already sorted and size is greater than 1 as a list of 1 or 0 is always sorted

        Node<T> sorted = null;

        while (head != null) {
            Node<T> current = head;
            head = head.getNext();

            if (sorted == null || sorted.getData().compareTo(current.getData()) > 0) {
                // insert at the beginning of the sorted list
                current.setNext(sorted);
                sorted = current;
            } else {
                // move through the sorted list to find the correct position
                Node<T> temp = sorted;
                while (temp.getNext() != null && temp.getNext().getData().compareTo(current.getData()) <= 0) {
                    temp = temp.getNext();
                }

                // insert the current node into the sorted list
                current.setNext(temp.getNext());
                temp.setNext(current);
            }
        }

        head = sorted; // update the head of the linked list
        isSorted = true; // update sorted status
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) return null; // check for valid index

        Node<T> current = head;
        if (index == 0) {
            // remove the head node and update pointers
            head = head.getNext();
            size--;
            isSorted = false;

            // return the data of the removed node (head node)
            return current.getData();
        } else {
            // move to the node before the specified index
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }

            Node<T> removedNode = current.getNext();
            if (removedNode != null) {
                // remove the node and update pointers
                current.setNext(removedNode.getNext());
                size--;
                isSorted = false;

                return removedNode.getData(); // return the data of the removed node
            } else {
                return null; // node to be removed is null
            }
        }
    }

    @Override
    public void equalTo(T element) {
        if (element == null || size <= 0) return; // Check for valid input

        if (isSorted) {
            Node<T> current = head;
            Node<T> previous = null;
            int writeIndex = 0;

            // binary search for sorted list
            while (current != null) {
                if (current.getData().compareTo(element) == 0) {
                    // match found, keep the node
                    if (previous != null) {
                        previous.setNext(current);
                    } else {
                        head = current;
                    }
                    previous = current;
                    writeIndex++;
                } else {
                    // remove the node
                    if (previous != null) {
                        previous.setNext(null);
                    }
                }
                current = current.getNext();
            }

            size = writeIndex;
            isSorted = false; // equality breaks the sorted order
        } else {
            Node<T> current = head;
            Node<T> previous = null;
            int writeIndex = 0;

            // linear search for unsorted list
            while (current != null) {
                if (current.getData().compareTo(element) == 0) {
                    // match found, keep the node
                    if (previous != null) {
                        previous.setNext(current);
                    } else {
                        head = current;
                    }
                    previous = current;
                    writeIndex++;
                } else {
                    // remove the node
                    if (previous != null) {
                        previous.setNext(null);
                    }
                }
                current = current.getNext();
            }

            size = writeIndex;
            isSorted = false;
        }
    }

    @Override
    public void reverse() {
        if (size <= 1) return; // check if the list has 1 or 0 elements

        Node<T> prev = null;
        Node<T> current = head;
        Node<T> next;

        // iterate through list and reverse order of nodes
        while (current != null) {
            next = current.getNext();
            current.setNext(prev);
            prev = current;
            current = next;
        }
        head = prev;
        isSorted = false; // reversal breaks the sorted order
    }

    @Override
    public void intersect(List<T> otherList) {
        if (otherList == null) return;

        if (otherList instanceof LinkedList) { // check that provided list is instance of linked list
            LinkedList<T> other = (LinkedList<T>) otherList;
            LinkedList<T> result = new LinkedList<>();

            // iterate through current list and add common elements to result
            for (int i = 0; i < size; i++) {
                T currentElement = get(i);
                if (other.indexOf(currentElement) != -1 && result.indexOf(currentElement) == -1) {
                    result.add(currentElement);
                }
            }
            // clear current list in order to add elements from result
            clear();
            for (int i = 0; i < result.size(); i++) {
                add(result.get(i));
            }
            sort(); // sort the current list
            isSorted = true;
        }
    }

    @Override
    public T getMin() {
        if (isSorted) return get(0);
        T min = get(0);
        for (int i = 1; i < size; i++) {
            if (min.compareTo(get(i)) > 0) {
                min = get(i); // new minimum found
            }
        }
        return min;
    }

    @Override
    public T getMax() {
        if (isSorted) return get(size - 1);
        T max = get(0);
        for (int i = 1; i < size; i++) {
            if (max.compareTo(get(i)) < 0) {
                max = get(i); // new maximum found
            }
        }
        return max;
    }

    @Override
    public String toString() { // string representation of list
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.getData()).append("\n");
            current = current.getNext();
        }
        return sb.toString();
    }

    @Override
    public boolean isSorted() {
        // check if the list is empty or has only one element.
        if (size <= 1) {
            return true;
        }

        // initialize a pointer to the head and next node.
        Node<T> current = head;
        Node<T> next = head.getNext();

        // iterate over the list, comparing each node to the next one.
        while (next != null) {
            if (current.getData().compareTo(next.getData()) > 0) {
                return false;
            }

            // update pointers.
            current = next;
            next = next.getNext();
        }

        return true; // list is sorted
    }
}

// Written by Brenen Olson, ols00175