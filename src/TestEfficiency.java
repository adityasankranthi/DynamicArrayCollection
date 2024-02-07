import java.util.Collection;
import java.util.Iterator;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.ArrayPartCollection;
import edu.uwm.cs351.Part;


public class TestEfficiency extends EfficiencyTestCase {
	ArrayPartCollection s;
	Collection<Part> c;
	Iterator<Part> it;
	
	@Override
	public void setUp() {
		s = new ArrayPartCollection();
		c = s;
		try {
			assert 1/s.size() < 0 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
	}

	private static final int POWER = 20;
	private static final int MAX_LENGTH = 1 << POWER;
	
	public void test0() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, c.size());
			s.addPart("arm",new Part("SN-" + i));
		}
	}

	public void test1() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("leg",new Part("SN-"+i));
		}
		assertEquals(new Part("SN-0"), s.removePart("leg"));
	}
		
	public void test2() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("head",new Part("SN#"+i));
		}
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(new Part("SN#" + i), s.getPart(null, i));
		}
	}

	public void test3() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("tail", new Part("SN-" + i));
			assertEquals(new Part("SN-0"), s.iterator("tail").next());
		}
	}

	public void test4() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("ARM", new Part("SN#" + i));
		}
		it = c.iterator();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(new Part("SN#" + i), it.next());
		}
	}

	private static final String[] FUNCS = {"ARM", "LEG", "HEAD", "TAIL" };
	
	public void test5() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String f = FUNCS[i % FUNCS.length];
			s.addPart(f, new Part("SN-" + i));
		}
		for (int offset = 0; offset < FUNCS.length; ++offset) {
			it = s.iterator(FUNCS[offset]);
			for (int i= offset; i < MAX_LENGTH; i += FUNCS.length) {
				assertEquals(new Part("SN-" + i), it.next());
			}
			assertFalse(it.hasNext());
		}
	}

	public void test6() {
		s.addPart("arm", new Part("SN-0"));
		for (int i=1; i < MAX_LENGTH/2; ++i) {
			s.addPart("ARM",new Part("SN-" + i));
		}
		s.addPart(new String("arm"), new Part("SN-" + (MAX_LENGTH/2)));
		for (int i=MAX_LENGTH/2+1; i < MAX_LENGTH; ++i) {
			s.addPart("ARM",new Part("SN-" + i));
		}
		it = s.iterator("arm");
		for (int i = 0; i < MAX_LENGTH; ++i) {
			assertTrue(it.hasNext());
		}
		it.next();
		for (int i = 0; i < MAX_LENGTH; ++i) {
			assertTrue(it.hasNext());
		}
		it.next();
		for (int i = 0; i < MAX_LENGTH; ++i) {
			assertFalse(it.hasNext());
		}
	}	
	
	public void test7() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addPart("leg",new Part("SN-"+i));
		}
		c.clear();
		assertEquals(0, c.size());
	}
}
