package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class ArrayPartCollection extends AbstractCollection<Part> implements Robot, Cloneable {
    private static final int DEFAULT_INITIAL_CAPACITY = 1;

    private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: " + s);

    private boolean report(String error) {
        reporter.accept(error);
        return false;
    }

    private String[] functions;
    private Part[] parts;
    private int size;
    private int version;

    private boolean wellFormed() {
        // If no problems discovered, return true

        // 1. The "functions" and "parts" arrays must not be null.
        if (functions == null) return report("functions array is null");
        if (parts == null) return report("parts array is null");

        // 2. The "functions" and "parts" arrays are always the same length.
        if (functions.length != parts.length) return report("functions and parts are not of equal length");

        // 3. The size cannot be negative or greater than the length of the arrays.
        if (size < 0) return report("The size is negative");
        if (size > functions.length) return report("The size greater than the length of the array");

        // 4. None of the first “size” elements of either array can be null. (ie. no holes)
        for (int i = 0; i < size; i++) {
            if (functions[i] == null || parts[i] == null) {
                return report("Data at index " + i + " is null");
            }
        }
        return true;
    }

    /**
     * Helper method to grow the arrays so that they are at least the length of the capacity needed, and ensures arrays are the same length with the same entries
     *
     * @param cap the minimum capacity the arrays need to have
     */
    private void ensureCapacity(int cap) {
        if (cap <= functions.length) return;
        int newCap = cap;
        if (cap < functions.length * 2) newCap = functions.length * 2;
        String[] newFunctions = new String[newCap];
        Part[] newParts = new Part[newCap];
        for (int i = 0; i < size; ++i) {
            newFunctions[i] = functions[i];
            newParts[i] = parts[i];
        }
        functions = newFunctions;
        parts = newParts;
    }

    private ArrayPartCollection(boolean ignored) {
    } // do not change this constructor

    /**
     * Construct an array part collection.
     */
    public ArrayPartCollection() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Construct an array part collection with the given initial capacity
     *
     * @param cap initial capacity, must not be negative
     */
    public ArrayPartCollection(int cap) {
        if (cap < 0) throw new IllegalArgumentException("Capacity can't be negative");
        functions = new String[cap];
        parts = new Part[cap];
        assert wellFormed() : "invariant broken by constructor";
    }

    /**
     * Add a part to the robot.
     *
     * @param function the type of part this is (arm, leg, etc), must not be null
     * @param part     the part to add, must not be null.
     * @return whether the part was added.
     * @exception NullPointerException if the function or part is null
     */
    @Override // required
    public boolean addPart(String function, Part part) {
        assert wellFormed() : "invariant broken in addPart";
        if (function == null || part == null) throw new NullPointerException("Function and part cannot be null");
        ensureCapacity(ArrayPartCollection.this.size + 1);
        functions[size] = function;
        parts[size] = part;
        ArrayPartCollection.this.size++;
        version++;
        assert wellFormed() : "invariant broken by addPart";
        return true;
    }

    /**
     * Return the part with the given function.
     *
     * @param function the type of part to look for, null means <em>any</em> part
     * @param index    zero-based index of part to return of the given type, must not be negative
     * @return indicated part, or null if no such part (index is at least the number of this kind of kind)
     */
    @Override // required
    public Part getPart(String function, int index) {
        assert wellFormed() : "invariant broken in getPart";
        if (index < 0) throw new IllegalArgumentException("Index can not be negative");
        for (int i = 0; i < ArrayPartCollection.this.size; ++i) {
            if (parts[i] == null) continue;
            if (function == null || function.equals(functions[i])) {
                if (index == 0) return parts[i];
                --index;
            }
        }
        assert wellFormed() : "invariant broken by getPart";
        return null;
    }

    /**
     * Remove a part from the robot if there is one with this function.
     *
     * @param function the type of part to remove, null means <em>any</em> part
     * @return part that was removed
     */
    @Override // required
    public Part removePart(String function) {
        assert wellFormed() : "invariant broken in removePart";
        for (int i = 0; i < ArrayPartCollection.this.size; ++i) {
            if (parts[i] == null) continue;
            if (function == null || function.equals(functions[i])) {
                Part p = parts[i];
                // adjust the dynamic array
                for (int j = i; j < ArrayPartCollection.this.size - 1; j++) {
                    functions[j] = functions[j + 1];
                    parts[j] = parts[j + 1];
                }
                parts[size - 1] = null;
                functions[size - 1] = null;
                // adjust the size;
                ArrayPartCollection.this.size--;
                // null the elements after the size for addPart to add
                version++;
                return p;
            }
        }
        assert wellFormed() : "invariant broken by removePart";
        return null;
    }

    /**
     * clones an object of type ArrayPartCollection
     *
     * @return ArrayPartCollection object.
     */
    @Override // decorate
    public ArrayPartCollection clone() {
        assert wellFormed() : "invariant broken in clone";
        ArrayPartCollection result;
        try {
            result = (ArrayPartCollection) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("forgot to implement cloneable?");
        }
        result.size = this.size;
        result.version = this.version;
        result.functions = new String[result.size];
        result.parts = new Part[result.size];
        for (int i = 0; i < this.size; i++) {
            result.functions[i] = this.functions[i];
            result.parts[i] = this.parts[i];
        }
        assert result.wellFormed() : "invariant broken in result of clone";
        assert wellFormed() : "invariant broken by clone";
        return result;
    }

    /**
     * zero argument Iterator
     */
    @Override // required
    public Iterator<Part> iterator() {
        return iterator(null);
    }

    /**
     * Iterator for the given function.
     *
     * @param function of iterator
     * @return iterator for the given function
     */
    public Iterator<Part> iterator(String string) {
        assert wellFormed() : "invariant broken in iterator";
        return new MyIterator(string);
    }

    /**
     * returns the size of the dynamic array
     *
     * @return size.
     */
    @Override // required
    public int size() {
        assert wellFormed() : "invariant broken in size";
        return ArrayPartCollection.this.size;
    }

    private class MyIterator implements Iterator<Part>
    {
        int cur, next; // must be a valid index or size
        int colVersion;
        String function;
        boolean nextCalled; // returns if next method has been called or not.

        private boolean wellFormed() {

            // 1. the outer invariant using the syntax
            if (!ArrayPartCollection.this.wellFormed()) {
                return false;
            }

            // 2. If version doesn’t match, return true without further checks
            if (colVersion != ArrayPartCollection.this.version) {
                return true;
            }

            // 3. The cur and next fields are valid indices or equal to the size.
            if (cur < 0 || cur > ArrayPartCollection.this.size || next < 0 || next > ArrayPartCollection.this.size) {
                return report("cur or next fields do not verify");
            }

            // 4. Check if iterator is working with a specific function
            // the cur and next fields must each refer to a part of that function
            if (function != null) {
                if ((cur < ArrayPartCollection.this.size && !function.equals(functions[cur])) || (next < ArrayPartCollection.this.size && !function.equals(functions[next])))
                    return report("cur and next fields must each refer to a part of that function");
            }

            // 5. Check if cur index is the same as or the closest legal index before next
            int index = size;
            if (function == null) {
                for (int i = cur + 1; i < ArrayPartCollection.this.size; i++) {
                    if (functions[i] != null) {
                        index = i;
                        break;
                    }
                }
            } else {
                for (int i = cur + 1; i < ArrayPartCollection.this.size; i++) {
                    if (function.equals(functions[i])) {
                        index = i;
                        break;
                    }
                }
            }
            if (cur != next && cur + 1 != next && index != next) {
                return report("cur index must be the same as or the closest legal index before next");
            }
            return true;
        }

        MyIterator(boolean ignored) {
        } // do not change this constructor

        MyIterator() {
            this(null);
        }

        MyIterator(String func) {
            this.function = func;
            this.cur = ArrayPartCollection.this.size;
            this.next = ArrayPartCollection.this.size;
            this.colVersion = ArrayPartCollection.this.version;
            this.nextCalled = false;
            assert wellFormed() : "invariant broken by iterator constructor";
        }

        /**
         * tells us if there are elements that have not yet been accessed by this iterator.
         *
         * @return Return true if there are elements that have not yet been accessed by this iterator.
         */
        @Override // required
        public boolean hasNext() {
            assert wellFormed() : "invariant broken in hasNext";

            if (colVersion != ArrayPartCollection.this.version) {
                throw new ConcurrentModificationException("Collection was modified during iteration");
            }
            int i = cur + 1;
            if (cur == ArrayPartCollection.this.size) i = 0;
            for (; i < ArrayPartCollection.this.size; ++i) {
                if ((function == null && parts[i] != null) || function.equals(functions[i])) {
                    return true;
                }
            }
            assert wellFormed() : "invariant broken by hasNext";
            return false;
        }

        /**
         * Advances the iterator, accessing and returning an element that had not yet been accessed (now the current element)
         *
         * @return Part, the next element of the collection.
         * @throws NoSuchElementException, if there is no such element.
         */
        @Override // required
        public Part next() {
            assert wellFormed() : "invariant broken in next";
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            nextCalled = true;
            boolean nextFound = false;
            cur = next;
            int i = cur + 1;
            if (cur == ArrayPartCollection.this.size) i = 0;
            for (; i < ArrayPartCollection.this.size; ++i) {
                if ((function == null && parts[i] != null) || function.equals(functions[i])) {
                    next = i;
                    nextFound = true;
                    break;
                }
            }
            if (cur == ArrayPartCollection.this.size) {
                nextFound = false;
                cur = next;
                int j = cur + 1;
                for (; j < ArrayPartCollection.this.size; ++j) {
                    if ((function == null && parts[j] != null) || function.equals(functions[j])) {
                        next = j;
                        nextFound = true;
                        break;
                    }
                }
            }
            if (!nextFound) next = size;
            assert wellFormed() : "invariant broken by next";
            return parts[cur];
        }

        /**
         * Remove the current element (the one previously returned by next()).
         *
         * @throws IllegalStateException,, if there is no current element.
         */
        @Override // required
        public void remove() {
            assert wellFormed() : "invariant broken in remove";
            if (colVersion != ArrayPartCollection.this.version) {
                throw new ConcurrentModificationException("Collection was modified during iteration");
            }
            if (!nextCalled) {
                throw new IllegalStateException("No element to remove");
            }

            // Adjust the dynamic array
            for (int i = cur; i < ArrayPartCollection.this.size - 1; ++i) {
                parts[i] = parts[i + 1];
                functions[i] = functions[i + 1];
            }
            parts[size - 1] = null;
            functions[size - 1] = null;
            ArrayPartCollection.this.size--;

            // Find last legal index
            int previousIndex = cur - 1;
            cur = size;
            next = size;
            for (; previousIndex >= 0; --previousIndex) {
                if ((function == null && parts[previousIndex] != null) || function.equals(functions[previousIndex])) {
                    break;
                }
            }
            // Reset to the last legal index
            if (cur != previousIndex && previousIndex >= 0) {
                cur = previousIndex;
            }
            for (int i = cur + 1; i < size; i++) {
                if ((function == null && parts[i] != null) || function.equals(functions[i])) {
                    next = i;
                    break;
                }
            }

            colVersion++;
            version++;
            nextCalled = false;
            assert wellFormed() : "invariant broken by remove";
        }
    }

    public static class Spy {
        /**
         * Return the sink for invariant error messages
         *
         * @return current reporter
         */
        public Consumer<String> getReporter() {
            return reporter;
        }

        /**
         * Change the sink for invariant error messages.
         *
         * @param r where to send invariant error messages.
         */
        public void setReporter(Consumer<String> r) {
            reporter = r;
        }

        /**
         * Create a testing instance of a dynamic array robot with the given
         * data structure.
         *
         * @param f the array of functions
         * @param p the array of parts
         * @param s the size
         * @return a new testing dynamic array robot with this data structure.
         */
        public ArrayPartCollection newInstance(String[] f, Part[] p, int s, int v) {
            ArrayPartCollection result = new ArrayPartCollection(false);
            result.functions = f;
            result.parts = p;
            result.size = s;
            result.version = v;
            return result;
        }

        /**
         * Creates a testing instance of an iterator
         *
         * @param outer the ArrayListRobot attached to the iterator
         * @param f     function to be checked
         * @param i     the starting index
         * @param n     the remaining value, how many elements are left
         * @param cv    the colVersion
         */
        public Iterator<Part> newIterator(ArrayPartCollection outer, String f, int i, int n, int cv) {
            MyIterator result = outer.new MyIterator(false);
            result.function = f;
            result.cur = i;
            result.next = n;
            result.colVersion = cv;
            return result;
        }

        /**
         * Check the invariant on the given dynamic array robot.
         *
         * @param r robot to check, must not be null
         * @return whether the invariant is computed as true
         */
        public boolean wellFormed(ArrayPartCollection r) {
            return r.wellFormed();
        }

        /**
         * Check the invariant on the given Iterator.
         *
         * @param it iterator to check, must not be null
         * @return whether the invariant is computed as true
         */
        public boolean wellFormed(Iterator<Part> it) {
            MyIterator myIt = (MyIterator) it;
            return myIt.wellFormed();
        }
    }
}
