package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class ArrayPartCollection // TODO: extends ... implements ...
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
		
	private class MyIterator // TODO: implements ...
	{
		int cur, next; // must be a valid index or size
		int colVersion;
		String function;
			
		private boolean wellFormed() {
			// TODO
			return true;
		}
			
		MyIterator(boolean ignored) {} // do not change this constructor
			

		MyIterator(String func) {
			// TODO: initialize fields
			// (We use a helper method)
			assert wellFormed() : "iterator constructor didn't satisfy invariant";
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
	
	

