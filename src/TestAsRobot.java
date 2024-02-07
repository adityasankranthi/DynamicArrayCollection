import java.util.Iterator;

import edu.uwm.cs351.ArrayPartCollection;
import edu.uwm.cs351.Part;

public class TestAsRobot extends AbstractTestRobot {
	protected ArrayPartCollection self;
	protected Iterator<Part> it;
	
	@Override
	protected void initRobot() {
		r = self = new ArrayPartCollection(1);
	}

	
}