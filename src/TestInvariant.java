import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.ArrayPartCollection;
import edu.uwm.cs351.Part;
import junit.framework.TestCase;

public class TestInvariant extends TestCase {
	
	protected ArrayPartCollection.Spy spy;
	protected int reports;
	protected ArrayPartCollection r;
	protected Iterator<Part> it;
	
	protected void assertReporting(boolean expected, Supplier<Boolean> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (expected) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get().booleanValue());
			if (!expected) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertWellFormed(boolean expected, ArrayPartCollection r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}
	protected void assertWellFormed(boolean expected, Iterator<Part> it) {
		assertReporting(expected, () -> spy.wellFormed(it));
	}

	@Override // implementation
	protected void setUp() {
		spy = new ArrayPartCollection.Spy();
	}
	
	// tests of null arrays
	public void testA00() {
		r = spy.newInstance(null, null, 0, 0);
		assertWellFormed(false, r);
	}
	
	public void testA01() {
		Part[] p = new Part[3];
		r = spy.newInstance(null, p, 0, 0);
		assertWellFormed(false, r);
	}
	
	public void testA02() {
		String[] f = new String[1];
		r = spy.newInstance(f, null, 0, 0);
		assertWellFormed(false, r);
	}
	
	// tests of non-null empty arrays
	public void testB00() {
		String[] f = new String[0];
		Part[] p = new Part[0];
		r = spy.newInstance(f, p, 0, 1);
		assertWellFormed(true, r);
	}
	
	public void testB01() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 0, 1);
		assertWellFormed(true, r);
	}
	
	public void testB02() {
		String[] f = new String[4];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 0, 1);
		assertWellFormed(false, r);
	}
	
	public void testB03() {
		String[] f = new String[5];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 0, 1);
		assertWellFormed(false, r);
	}
	
	// tests of size
	public void testC00() {
		String[] f = new String[0];
		Part[] p = new Part[0];
		r = spy.newInstance(f, p, 1, 2);
		assertWellFormed(false, r);
	}
	
	public void testC01() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 0, 2);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(true, r);
	}
	
	public void testC02() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 2);
		f[0] = "leg";
		p[0] = new Part();
		f[4] = "body";
		p[4] = new Part();
		assertWellFormed(false, r);
	}
	
	public void testC03() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 1, 2);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(true, r);
	}
	
	public void testC04() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 2);
		f[0] = "head";
		p[0] = new Part();
		f[4] = "arm";
		p[4] = new Part();
		assertWellFormed(false, r);
	}
	
	// tests of out-of-sync arrays
	public void testD00() {
		String[] f = new String[5];
		Part[] p = new Part[6];
		r = spy.newInstance(f, p, 2, 3);
		f[0] = "arm";
		f[4] = "arm";
		p[0] = new Part("RSN123");
		p[4] = new Part("RSN123");
		assertWellFormed(false, r);
	}
	
	public void testD01() {
		String[] f = new String[10];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 3);
		f[0] = "leg";
		p[0] = new Part();
		f[4] = "head";
		p[4] = new Part();
		assertWellFormed(false, r);
	}
	
	public void testD02() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 3);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[2] = new Part();
		assertWellFormed(false, r);
	}
	
	public void testD03() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 3);
		f[0] = "head";
		p[0] = new Part();
		f[2] = "arm";
		p[1] = new Part();
		assertWellFormed(false, r);
	}
	
	public void testD04() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 3);
		f[1] = "leg";
		p[1] = new Part();
		f[2] = "arm";
		p[2] = new Part();
		assertWellFormed(false, r);
	}
	
	public void testD05() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 1, 3);
		f[0] = "head";
		p[0] = new Part();
		f[2] = "arm";
		assertWellFormed(true, r);
	}
	
	public void testD06() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 3);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(false, r);
		f[2] = "HEAD";
		p[2] = new Part();
		assertWellFormed(false, r);
	}
	
	
	public void testD08() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 4, 3);
		f[2] = "head";
		p[0] = new Part();
		f[3] = "arm";
		p[1] = new Part("arm");
		f[1] = "body";
		p[3] = new Part();
		f[0] = "leg";
		p[2] = new Part();
		assertWellFormed(true, r);
	}
	
	
	/// Tests of Iterator with empty lists
	
	public void testE00() {
		String[] f = new String[0];
		Part[] p = new Part[0];
		r = spy.newInstance(f, p, 0, 5);
		it = spy.newIterator(r, null, 0, 0, 5);
		assertWellFormed(true, it);
	}
	
	public void testE01() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 0, 5);
		it = spy.newIterator(r, "arm", 0, 0, 5);
		assertWellFormed(true, it);
	}
	
	public void testE02() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 0, 5);
		it = spy.newIterator(r, null, -1, 0, 5);
		assertWellFormed(false, it);
	}
	
	public void testE03() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 0, 5);
		f[0] = "arm";
		p[0] = new Part();
		it = spy.newIterator(r, null, 0, 1, 5);
		assertWellFormed(false, it);
	}
	
	public void testE04() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 0, 5);
		f[0] = "arm";
		p[0] = new Part();
		it = spy.newIterator(r, null, 0, 1, 4);
		assertWellFormed(true, it);
	}
	
	public void testE05() {
		String[] f = new String[0];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 0, 5);
		it = spy.newIterator(r, null, 0, 0, 4);
		assertWellFormed(false, it);
	}
	
	
	/// Tests of Iterator with a single entry
	
	public void testF00() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 1, 0);
		it = spy.newIterator(r, null, -1, 0, 0);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testF01() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 1, 1);
		it = spy.newIterator(r, null, 0, -1, 1);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testF02() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 1, 2);
		it = spy.newIterator(r, null, 0, 0, 2);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testF03() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 1, 2);
		it = spy.newIterator(r, "head", 0, 0, 2);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testF05() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 1, 4);
		it = spy.newIterator(r, "leg", 0, 1, 4);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testF06() {
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 1, 5);
		it = spy.newIterator(r, "head", 1, 0, 5);
		f[1] = "head";
		p[1] = new Part();
		assertWellFormed(false, it);
	}
	
	
	/// Tests of Iterator with multiple entries
	
	public void testG00() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 0);
		it = spy.newIterator(r, null, 0, 0, 0);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testG01() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 1);
		it = spy.newIterator(r, null, 0, 1, 1);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testG02() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 2);
		it = spy.newIterator(r, null, 0, 2, 2);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testG03() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 3);
		it = spy.newIterator(r, "head", 0, 1, 3);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testG04() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 4);
		it = spy.newIterator(r, "arm", 0, 1, 4);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testG05() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 5);
		it = spy.newIterator(r, "head", 0, 1, 5);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "head";
		p[1] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testG06() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 6);
		it = spy.newIterator(r, "head", 1, 2, 6);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "head";
		p[1] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testG07() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 7);
		it = spy.newIterator(r, null, 0, 1, 7);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		f[2] = "body";
		p[2] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testG08() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 8);
		it = spy.newIterator(r, null, 3, 2, 8);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		f[2] = "body";
		p[2] = new Part();
		assertWellFormed(false, it);
	}	
	
	public void testG09() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 9);
		it = spy.newIterator(r, "head", 0, 2, 9);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		f[2] = "head";
		p[2] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testG10() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 9);
		it = spy.newIterator(r, "head", 0, 1, 9);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		f[2] = "head";
		p[2] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testG11() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 9);
		it = spy.newIterator(r, "head", 1, 2, 9);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		f[2] = "head";
		p[2] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testG12() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 9);
		it = spy.newIterator(r, "head", 2, 0, 9);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		f[2] = "head";
		p[2] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testG13() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 9);
		it = spy.newIterator(r, "head", 0, 2, 9);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "head";
		p[1] = new Part();
		f[2] = "head";
		p[2] = new Part();
		assertWellFormed(false, it);
	}
	public void testG14() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 9);
		it = spy.newIterator(r, "head", 0, 1, 9);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "head";
		p[1] = new Part();
		f[2] = "head";
		p[2] = new Part();
		assertWellFormed(true, it);
	}
	
	
	/// Tests of Iterator and outer invariant
	
	public void testH00() { 
		String[] f = new String[1];
		r = spy.newInstance(f, null, 0, 0);
		it = spy.newIterator(r, null, 0, 0, 0);
		assertWellFormed(false, it);
	}
	
	public void testH01() { 
		String[] f = new String[4];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 0, 1);
		it = spy.newIterator(r, null, 0, 0, 1);
		assertWellFormed(false, it);
	}
	
	public void testH02() { 
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 2);
		it = spy.newIterator(r, "head", 0, 0, 2);
		f[0] = "head";
		p[0] = new Part();
		f[4] = "arm";
		p[4] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testH03() { 
		String[] f = new String[10];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 3);
		it = spy.newIterator(r, null, 0, 0, 3);
		f[0] = "leg";
		p[0] = new Part();
		f[4] = "head";
		p[4] = new Part();
		assertWellFormed(false, it);
	}
	
	public void testH04() { 
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 3);
		it = spy.newIterator(r, null, 0, 0, 3);
		f[1] = "leg";
		p[1] = new Part();
		f[2] = "arm";
		p[2] = new Part();
		assertWellFormed(false, it);
	}
	
	
	/// Tests of stale Iterator
	
	public void testI00() { 
		String[] f = new String[0];
		Part[] p = new Part[0];
		r = spy.newInstance(f, p, 0, 5);
		it = spy.newIterator(r, null, 0, 0, 4);
		assertWellFormed(true, it);
	}
	
	public void testI01() { 
		String[] f = new String[4];
		Part[] p = new Part[4];
		r = spy.newInstance(f, p, 1, 2);
		it = spy.newIterator(r, "head", 0, 0, 3);
		f[0] = "head";
		p[0] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testI02() { 
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 2, 2);
		it = spy.newIterator(r, "head", 0, 1, 0);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "head";
		p[1] = new Part();
		assertWellFormed(true, it);
	}
	
	public void testI03() {
		String[] f = new String[5];
		Part[] p = new Part[5];
		r = spy.newInstance(f, p, 3, 8);
		it = spy.newIterator(r, "head", 0, 2, 9);
		f[0] = "head";
		p[0] = new Part();
		f[1] = "arm";
		p[1] = new Part();
		f[2] = "head";
		p[2] = new Part();
		assertWellFormed(true, it);
	}
}