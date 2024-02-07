import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.ArrayPartCollection;
import edu.uwm.cs351.Part;

public class TestDirect extends LockedTestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (Throwable ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}
	
	protected ArrayPartCollection self;
	protected Iterator<Part> it;

	@Override // implementation
	protected void setUp() {
		try {
			assert it.hasNext();
			System.out.println("Assertions must be enabled for this test.");
			System.out.println("In Eclipse: Go to Run > Run Configurations ...");
			System.out.println("In the Arguments tab, add '-ea' (without quotes) to the VM Arguments box");
			assertFalse("Assertions must be enabled to run this test", true);
		} catch (NullPointerException ex) {
			assertTrue("All is OK", true);
		}
		self = new ArrayPartCollection();
		
	}
	protected String asString(Supplier<?> supp) {
		try {
			return "" + supp.get();
		} catch(RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	
	protected String asString(Runnable r) {
		return asString(() -> { r.run(); return "void"; });
	}

	
	public void testE0() {
		assertEquals(0, self.size());
	}
	
	public void testE1() {
		assertTrue(self.isEmpty());
	}
	
	public void testE8() {
		Part p1 = new Part("p1");
		// What is the result of Collection's "add" method?
		assertEquals(Ts(2106895595), asString(() -> self.add(p1)));
	}
	
	public void testE9() {
		Part p1 = new Part("p1");
		// What about Robot's addPart method ?
		assertEquals(Ts(757738343), asString(() -> self.addPart("arm", p1)));
	}
	
	public void testF1() {
		self.addPart("arm", new Part());
		assertEquals(1, self.size());
	}
	
	public void testF2() {
		self.addPart("arm", new Part());
		self.addPart("head", new Part());
		assertEquals(2, self.size());
	}
	
	public void testF3() {
		self.addPart("leg", new Part());
		self.addPart("leg", new Part());
		assertEquals(2, self.size());
	}
	
	public void testF4() {
		self.addPart("arm", new Part());
		self.addPart("leg", new Part());
		self.addPart("arm", new Part());
		assertEquals(3, self.size());
	}
	
	public void testF5() {
		self.addPart("head", new Part());
		assertFalse(self.isEmpty());
	}

	
	public void testI0() {
		it = self.iterator();
		assertFalse(it.hasNext());
	}
	
	public void testI1() {
		it = self.iterator("tail");
		assertFalse(it.hasNext());
	}
	
	public void testI2() {
		it = self.iterator();
		assertEquals(Ts(677847564), asString(() -> it.next()));
	}
	
	public void testI3() {
		it = self.iterator("arm");
		assertException(NoSuchElementException.class, () -> it.next());
	}
	
	public void testI4() {
		self.addPart("tail", new Part("I4"));
		it = self.iterator();
		assertTrue(it.hasNext());
	}
	
	public void testI5() {
		self.addPart("face", new Part("I5"));
		it = self.iterator("back");
		assertFalse(it.hasNext());
	}
	
	public void testI6() {
		self.addPart("back", new Part("I5"));
		it = self.iterator("back");
		assertTrue(it.hasNext());
	}
	
	public void testI7() {
		self.addPart("arm", new Part("I7"));
		it = self.iterator();
		assertEquals(Ts(1734273065), asString( () -> it.next()));
	}
	
	public void testI8() {
		self.addPart("leg", new Part("I8"));
		it = self.iterator("head");
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void testI9() {
		self.addPart("front", new Part("I9"));
		it = self.iterator("front");
		assertEquals(new Part("I9"), it.next());
	}
	
	public void testJ0() {
		self.addPart("head", new Part("#0"));
		it = self.iterator(null);
		it.next();
		assertFalse(it.hasNext());
	}
	
	public void testJ1() {
		self.addPart("antenna", new Part("#1"));
		it = self.iterator("antenna");
		it.next();
		assertException(NoSuchElementException.class, () -> it.next());
	}
	
	public void testJ2() {
		self.addPart("arm", new Part("#2"));
		self.addPart("leg", new Part("J"));
		it = self.iterator("arm");
		it.next();
		assertFalse(it.hasNext());
	}
	
	public void testJ3() {
		self.addPart("arm", new Part("#3"));
		self.addPart("leg", new Part("J"));
		it = self.iterator("leg");
		assertEquals(new Part("J"), it.next());
	}
	
	public void testJ4() {
		self.addPart("arm", new Part("#4"));
		self.addPart("leg", new Part("J"));
		it = self.iterator("head");
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void testJ5() {
		self.addPart("arm", new Part("#4"));
		self.addPart("leg", new Part("J"));
		it = self.iterator(null);
		assertEquals(new Part("#4"), it.next());
		assertEquals(new Part("J"), it.next());
		assertFalse(it.hasNext());
	}
	
	public void testJ6() {
		self.addPart("head", new Part("#6"));
		self.addPart("head", new Part("J"));
		it = self.iterator("head");
		assertEquals(new Part("#6"), it.next());
		assertEquals(new Part("J"), it.next());
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void testJ7() {
		self.addPart("arm", new Part("#7"));
		self.addPart("leg", new Part("J"));
		self.iterator().next();
		it = self.iterator(null);
		assertEquals(new Part("#7"), it.next());
	}
	
	public void testJ8() {
		self.addPart("arm", new Part("#8"));
		self.addPart("leg", new Part("J"));
		it = self.iterator(null);
		self.iterator("arm").next();
		it.next();
		assertEquals(new Part("J"), it.next());
	}
	
	public void testJ9() {
		self.addPart("head", new Part("#9"));
		self.addPart("head", new Part("J"));
		it = self.iterator("head");
		self.iterator("arm");
		assertEquals(new Part("#9"), it.next());
	}
	
	public void testK0() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator();
		assertEquals(new Part("A"), it.next());
		assertEquals(new Part("B"), it.next());
		assertEquals(new Part("C"), it.next());
		assertEquals(new Part("D"), it.next());
		assertEquals(new Part("E"), it.next());
		assertEquals(new Part("F"), it.next());
		assertEquals(new Part("G"), it.next());
		assertFalse(it.hasNext());
	}

	public void testK1() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator("arm");
		assertEquals(new Part("A"), it.next());
		assertTrue(it.hasNext());
		assertEquals(new Part("E"), it.next());
		assertFalse(it.hasNext());
	}

	public void testK2() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart(new String("leg"), new Part("G"));
		it = self.iterator(new String("leg"));
		assertTrue(it.hasNext());
		assertEquals(new Part("B"), it.next());
		assertEquals(new Part("C"), it.next());
		assertEquals(new Part("G"), it.next());
		assertFalse(it.hasNext());
	}

	public void testK3() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator("head");
		assertEquals(new Part("D"), it.next());
		assertEquals(new Part("F"), it.next());
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void testK4() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator("ARM");
		assertFalse(it.hasNext());
	}
	
	public void testK5() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		Iterator<Part> it1 = self.iterator("leg");
		assertEquals(new Part("B"), it1.next());
		Iterator<Part> it2 = self.iterator(null);
		assertEquals(new Part("A"), it2.next());
		Iterator<Part> it3 = self.iterator("head");
		assertEquals(new Part("D"), it3.next());
		assertEquals(new Part("C"), it1.next());
		assertEquals(new Part("B"), it2.next());
		assertTrue(it3.hasNext());
	}

	
	/// testL/Mx: tests of iterator removal, and then clear
	
	public void testL0() {
		it = self.iterator();
		assertException(IllegalStateException.class, () -> it.remove());
	}
	
	public void testL1() {
		self.addPart("ARM",  new Part("101"));
		it = self.iterator("ARM");
		assertEquals(Ts(656288844), asString(() -> it.remove()));
	}
	
	public void testL2() {
		self.addPart("arm", new Part("86"));
		it = self.iterator(null);
		assertException(IllegalStateException.class, () -> it.remove());
	}
	
	public void testL3() {
		self.addPart("leg", new Part("256"));
		it = self.iterator(null);
		it.next();
		it.remove();
		assertTrue(self.isEmpty());
	}
	
	public void testL4() {
		self.addPart("head", new Part("#16"));
		it = self.iterator("head");
		it.next();
		it.remove();
		assertFalse(it.hasNext());
	}
	
	public void testL5() {
		self.addPart("leg", new Part("55"));
		it = self.iterator("leg");
		it.next();
		it.remove();
		it = self.iterator();
		assertFalse(it.hasNext());
	}
	
	public void testL6() {
		self.addPart("arm", new Part("66"));
		self.addPart("head", new Part("7"));
		it = self.iterator();
		it.next();
		it.remove();
		assertEquals(new Part("7"), self.iterator().next());
	}
	
	public void testL7() {
		self.addPart("tail", new Part("666"));
		self.addPart(new String("tail"), new Part("007"));
		it = self.iterator("tail");
		it.next();
		it.remove();
		assertTrue(it.hasNext());
	}
	
	public void testL8() {
		self.addPart("head", new Part("81"));
		self.addPart("body", new Part("12"));
		it = self.iterator("body");
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(new Part("81"), self.iterator().next());
	}
	
	public void testL9() {
		self.addPart("head", new Part("A"));
		self.addPart("arm", new Part("B"));
		self.addPart("head", new Part("C"));
		it = self.iterator("head");
		it.next();
		it.remove();
		assertEquals(new Part("C"), it.next());
	}
	
	public void testM0() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator();
		it.next();
		it.remove();
		it.next();
		it.next();
		it.remove();
		assertEquals(new Part("D"), it.next());
		assertEquals(5, self.size());
		it = self.iterator();
		assertEquals(new Part("B"), it.next());
	}
	
	public void testM1() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator(new String("arm"));
		it.next();
		it.remove();
		it.next();
		assertFalse(it.hasNext());
		assertEquals(6, self.size());
		it = self.iterator();
		assertEquals(new Part("B"), it.next());
	}
	
	public void testM2() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart(new String("leg"), new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator("leg");
		it.next();
		it.remove();
		it.next();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(5, self.size());
		it = self.iterator(null);
		assertEquals(new Part("A"), it.next());
		assertEquals(new Part("C"), it.next());
	}
	
	public void testM3() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart(new String("leg"), new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator("head");
		it.next();
		it.remove();
		it.next();
		assertException(NoSuchElementException.class, () -> it.next());
		assertEquals(6, self.size());
		it = self.iterator();
		assertEquals(new Part("A"), it.next());
		assertEquals(new Part("B"), it.next());
		assertEquals(new Part("C"), it.next());
		assertEquals(new Part("E"), it.next());
	}
	
	public void testM4() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart(new String("leg"), new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator();
		it.next();
		it.remove();
		it.next();
		it.remove();
		it.next();
		it.remove();
		it.next();
		it.next();
		it.remove();
		it.next();
		it.remove();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(1, self.size());
		assertEquals(new Part("D"), self.iterator().next());
	}
	
	public void testM5() {
		self.clear();
		assertEquals(0, self.size());
	}
	
	public void testM6() {
		self.addPart("arm", new Part("1"));
		self.clear();
		assertFalse(self.iterator().hasNext());
	}
	
	public void testM7() {
		self.addPart("leg",  new Part("2"));
		self.addPart("head", new Part("3"));
		self.clear();
		assertTrue(self.isEmpty());
	}
	
	public void testM8() {
		self.addPart("arm", new Part("4"));
		self.addPart("arm", new Part("5"));
		self.addPart("arm", new Part("6"));
		self.clear();
		assertException(NoSuchElementException.class, () -> self.iterator().next());
	}
	
	public void testM9() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		self.clear();
		assertEquals(0, self.size());
	}
	
	
	/// testNx: fail fast tests
	
	public void testN0() {
		it = self.iterator();
		self.addPart("arm", new Part("0"));
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void testN1() {
		self.addPart("leg", new Part("1"));
		it = self.iterator("leg");
		self.addPart("arm", new Part("N1"));
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void testN2() {
		self.addPart("head",  new Part("N2"));
		it = self.iterator();
		assertException(RuntimeException.class, () -> self.addPart(null, new Part("2")));
		assertTrue(it.hasNext());
	}
	
	public void testN3() {
		self.addPart("LEG", new Part("N3"));
		it = self.iterator("LEG");
		it.next();
		self.removePart(null);
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void testN4() {
		self.addPart("tail", new Part("N4"));
		self.addPart("body", new Part("#4"));
		it = self.iterator("tail");
		self.removePart("body");
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void testN5() {
		self.addPart("ARM", new Part("178"));
		it = self.iterator(null);
		Iterator<Part> it1 = self.iterator("ARM");
		it1.next();
		it1.remove();
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void testN6() {
		self.addPart("body", new Part("1024"));
		self.addPart("tail", new Part("1215"));
		it = self.iterator();
		self.addPart("head", new Part("1296"));
		self.removePart("tail");
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void testN7() {
		self.addPart("arm", new Part("43"));
		it = self.iterator();
		it.next();
		self.clear();
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void testN8() {
		it = self.iterator();
		self.clear();
		assertFalse(it.hasNext());
	}
	
	public void testN9() {
		self.addPart("arm", new Part("X"));
		it = self.iterator("arm");
		self.clear();
		self.addPart("leg", new Part("Y"));
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	
	/// testTx: clone tests
	
	protected ArrayPartCollection c, d;
	
	public void testT0() {
		c = self.clone();
		assertTrue(c.isEmpty());
	}
	
	public void testT1() {
		c = self.clone();
		d = self.clone();
		c.addPart("arm", new Part("A"));
		d.addPart("leg", new Part("L"));
		assertEquals(new Part("A"), c.iterator().next());
	}
	
	public void testT2() {
		self.addPart("head", new Part("H1"));
		self.addPart("head", new Part("H2"));
		it = self.iterator();
		c = self.clone();
		it.next();
		it.remove();
		assertEquals(new Part("H1"), c.iterator().next());
	}
	
	public void testT3() {
		self.addPart("ARM", new Part("A"));
		self.addPart("Leg", new Part("B"));
		it = self.iterator("ARM");
		c = self.clone();
		Iterator<Part> itc = c.iterator();
		itc.next();
		itc.remove();
		assertEquals(new Part("A"), it.next());
	}
	
	public void testT4() {
		self.addPart("arm", new Part("1"));
		self.addPart("leg", new Part("2"));
		self.addPart("ARM", new Part("3"));
		self.addPart(new String("arm"), new Part("4"));
		c = self.clone();
		it = c.iterator(new String("arm"));
		assertEquals(new Part("1"), it.next());
		self.addPart("arm", new Part("5"));
		assertEquals(new Part("4"), it.next());
		assertFalse(it.hasNext());
	}
	
	public void testT9() {
		class Secret extends ArrayPartCollection {
			public Secret() {
				super(0);
			}
		};
		self = new Secret();
		self.addPart("ARM", new Part("A"));
		c = self.clone();
		assertTrue(c instanceof Secret);
	}
	
	public void testX0() {
		self.addPart("arm", new Part("A"));
		self.addPart("leg", new Part("B"));
		self.addPart("leg", new Part("C"));
		self.addPart("head",new Part("D"));
		self.addPart("arm", new Part("E"));
		self.addPart("head",new Part("F"));
		self.addPart("leg", new Part("G"));
		it = self.iterator("leg");
		assertEquals(new Part("A"), self.getPart("arm", 0));
		it.next();
		it.remove();
		assertEquals(new Part("C"), self.getPart(null, 1));
		it.next();
		assertEquals(new Part("G"), it.next());
		it.remove();
		assertEquals(new Part("E"), self.getPart("arm", 1));
	}
}
