package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class ArrayPartCollection implements Robot, Collection<Part>// TODO: extends ... implements ...

{ 
	private static final int DEFAULT_INITIAL_CAPACITY = 1;
	
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);

	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	private String[] functions;
	private Part[] parts;
	private int size;
	private int version;
	
	private boolean wellFormed() {
		//TODO: Complete this.  Simpler than than the same for HW 2
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
	 * Helper method to grow the arrays so that 
	 * they are at least the length of the capacity needed, and ensures arrays are
	 * the same length with the same entries
	 * @param cap the minimum capacity the arrays need to have
	 */
	private void ensureCapacity(int cap) {
		if (cap <= functions.length) return;
		int newCap = cap;
		if (cap < functions.length*2) newCap = functions.length*2;
		String[] newFunctions = new String[newCap];
		Part[] newParts = new Part[newCap];
		for (int i=0; i < size; ++i) {
			newFunctions[i] = functions[i];
			newParts[i]= parts[i];
		}
		functions = newFunctions;
		parts = newParts;
	}
	
	
	private ArrayPartCollection(boolean ignored) {} // do not change this constructor
	
	/** Construct an array part collection.
	 */
	public ArrayPartCollection(){
		this(DEFAULT_INITIAL_CAPACITY);
	}
	
	/**
	 * Construct an array part collection with the given initial capacity
	 * @param cap initial capacity, must not be negative
	 */
	public ArrayPartCollection(int cap){
		functions = new String[cap];
		parts = new Part[cap];
		assert wellFormed(): "invariant broken by constructor";
	}

	// TODO: All the methods!
	// Make sure to properly document each.
	// You are welcome to copy code in from the solution 
	// to Homework #2, especially if you re-type it yourself!
	
	@Override // required
	public boolean addPart(String arg0, Part arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public Part getPart(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // required
	public Part removePart(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
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
        for (int i = 0; i< this.size; i++) {
            result.functions[i] = this.functions[i];
            result.parts[i] = this.parts[i];
        }
        assert result.wellFormed() : "invariant broken in result of clone";
        assert wellFormed() : "invariant broken by clone";
        return result;
    }
	public Iterator<Part> iterator(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override // required
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public Iterator<Part> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // required
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // required
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // required
	public boolean add(Part e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public boolean addAll(Collection<? extends Part> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override // required
	public void clear() {
		// TODO Auto-generated method stub
		
	}
		
	private class MyIterator implements Iterator<Part>// TODO: implements ...

	{
		int cur, next; // must be a valid index or size
		int colVersion;
		String function;
			
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
		        if ((!functions[cur].equals(function)) || ((!functions[next].equals(function)))) {
		            return report("cur and next fields must\r\n"
		            		+ "each refer to a part of that function");
		        }
		    }
		    
		    // 5. Check if cur index is the same as or the closest legal index before next
		    if (cur != next && (cur + 1 != next || cur >= ArrayPartCollection.this.size)) {
		        return report("cur index must be the same as or the closest legal index before next");
		    }
		    
		    return true;
		}
			
		MyIterator(boolean ignored) {} // do not change this constructor
			

		MyIterator(String func) {
			// TODO: initialize fields
			// (We use a helper method)
			assert wellFormed() : "iterator constructor didn't satisfy invariant";
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Part next() {
			// TODO Auto-generated method stub
			return null;
		}
			
		// TODO: Body of iterator class
	}
		
	public static class Spy {
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}

		/**
		 * Create a testing instance of a dynamic array robot with the given
		 * data structure.
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
		 * @param outer the ArrayListRobot attached to the iterator
		 * @param f TODO
		 * @param i the starting index
		 * @param n the remaining value, how many elements are left
		 * @param cv the colVersion
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
		 * @param r robot to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public boolean wellFormed(ArrayPartCollection r) {
			return r.wellFormed();
		}
			
		/**
		 * Check the invariant on the given Iterator.
		 * @param it iterator to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public boolean wellFormed(Iterator<Part> it) {
			MyIterator myIt = (MyIterator)it;
			return myIt.wellFormed();
		}
	}
}
	
	

