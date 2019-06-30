package spgqlite.tests.traversals;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
				// forward
				TestGraphForwardTraversals.class,
				TestQueryForwardTraversals.class,
				// forward step
				TestGraphForwardStepTraversals.class,
				TestQueryForwardStepTraversals.class
			  })
public class AllTests {}
