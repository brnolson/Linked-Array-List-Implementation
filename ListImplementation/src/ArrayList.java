// Brenen Olson, ols00175

public class ArrayList<T extends Comparable<T>> implements List<T> {
    private static final int DEFAULT_CAP = 2;
    private T[] array;
    private int size;
    private boolean isSorted;

    // constructor initializes the array
    public ArrayList() {
        array = (T[]) new Comparable[DEFAULT_CAP];
        size = 0;
        isSorted = true;
    }

    @Override
    public boolean add(T element) {
        if (element == null) { // return if element does not contain data
            return false;
        }

        if (size == array.length) { // resize the array if it's full
            resizeArray();
        }

        array[size] = element;
        size++;

        if (isSorted && size > 1) { // update sorted status
            isSorted = (element.compareTo(array[size - 2]) >= 0);
        }
        return true;
    }

    @Override
    public boolean add(int index, T element) {
        if (element == null || index < 0 || index > size) { // returns if element is null or if the index is out of list bounds
            return false;
        }

        if (size == array.length) { // resize array if full
            resizeArray();
        }

        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size++;

        isSorted = false; // update sorted status
        return true;
    }

    // helper method to resize the array by creating a new array and copying elements
    private void resizeArray() {
        T[] newArray = (T[]) new Comparable[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    @Override
    public void clear() {
        // resets list values
        array = (T[]) new Comparable[DEFAULT_CAP];
        size = 0;
        isSorted = true;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        return array[index]; // return element at desired index
    }

    @Override
    public int indexOf(T element) {
        if (element == null) return -1; // check if data is entered

        if (isSorted) {
            // binary search for sorted list
            int low = 0;
            int high = size - 1;

            while (low <= high) {
                int mid = low + (high - low) / 2;
                int comparison = element.compareTo(array[mid]);

                if (comparison < 0) {
                    high = mid - 1;
                } else if (comparison > 0) {
                    low = mid + 1;
                } else {
                    while (mid > 0 && element.compareTo(array[mid - 1]) == 0) {
                        mid--;
                    }
                    return mid;
                }
            }
        } else {
            // linear search for unsorted list
            for (int i = 0; i < size; i++) {
                if (element.compareTo(array[i]) == 0) {
                    return i;
                }
            }
        }
        return -1; // element not found
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // check if list is empty
    }

    @Override
    public int size() {
        return size; // return size of list
    }

    @Override
    public void sort() {
        if (isSorted || size <= 1) return;

        // bubble sort
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (array[j] != null && array[j + 1] != null &&
                        ((T) array[j]).compareTo((T) array[j + 1]) > 0) {
                    // swap array[j] and array[j + 1]
                    T temp = (T) array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }

        isSorted = true;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) return null; // check if index is in lists bounds

        // remove data at index and shift all other elements
        T removedElement = array[index];
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        size--;
        isSorted = false;
        return removedElement;
    }

    @Override
    public void equalTo(T element) {
        if (element == null || size <= 0) return; // check for valid input

        if (isSorted) {
            int writeIndex = 0;

            // binary search method for sorted list
            for (int i = 0; i < size; i++) {
                if (array[i].compareTo(element) == 0) {
                    array[writeIndex++] = array[i];
                }
            }

            size = writeIndex;
            isSorted = false;
        } else {
            int writeIndex = 0;

            // linear search method for unsorted list
            for (int i = 0; i < size; i++) {
                if (array[i].compareTo(element) == 0) {
                    array[writeIndex++] = array[i];
                }
            }

            size = writeIndex;
            isSorted = false;
        }
    }

    @Override
    public void reverse() { // reverse list elements
        for (int i = 0; i < size / 2; i++) {
            T temp = array[i];
            array[i] = array[size - i - 1];
            array[size - i - 1] = temp;
        }
        isSorted = false;
    }

    @Override
    public void intersect(List<T> otherList) {
        if (otherList == null) {
            return;
        }

        // check if the other list is an ArrayList
        if (otherList instanceof ArrayList) {
            ArrayList<T> other = (ArrayList<T>) otherList;
            ArrayList<T> result = new ArrayList<>();

            // iterate through the elements in this list
            for (int i = 0; i < size; i++) {
                T currentElement = (T) array[i];

                // check if the current element is present in both lists and not already in the result
                if (other.indexOf(currentElement) != -1 && result.indexOf(currentElement) == -1) {
                    result.add(currentElement);
                }
            }

            // update this list to intersection result
            array = (T[]) new Comparable[result.size];
            for (int i = 0; i < result.size; i++) {
                array[i] = result.get(i);
            }
            size = result.size;
            isSorted = true;
        }
    }

    @Override
    public T getMin() {
        if (isSorted) return array[0];
        T min = array[0];
        for (int i = 1; i < size; i++) {
            if (min.compareTo(array[i]) > 0) {
                min = array[i]; // new minimum found
            }
        }
        return min;
    }

    @Override
    public T getMax() {
        if (size == 0) return null;

        if (isSorted) return array[size - 1];
        T max = array[0];
        for (int i = 1; i < size; i++) {
            if (max.compareTo(array[i]) < 0) {
                max = array[i]; // new max found
            }
        }
        return max;
    }

    @Override
    public String toString() { // string representation of list.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(array[i]).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean isSorted() {
        if (size <= 1) return true;

        // iterate through list, comparing previous element to current.
        for (int i = 1; i < size; i++) {
            if (array[i - 1].compareTo(array[i]) > 0) {
                return false; // list is not sorted
            }
        }
        return true; // list is sorted
    }
}

// Written by Brenen Olson, ols00175