package chpg.tests.traversals;

import org.junit.Before;
import org.junit.Test;

import chpg.graph.Graph;
import chpg.graph.query.Query;

public class TestQueryForwardStepTraversals extends TestGraphForwardTraversals {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testForwardATraversal() {
		Graph result = new Query(graph).forward(a).evaluate();
		super.inspectForwardA(result);
	}
	
	@Test
	public void testForwardBTraversal() {
		Graph result = new Query(graph).forward(b).evaluate();
		super.inspectForwardB(result);
	}
	
	@Test
	public void testForwardCTraversal() {
		Graph result = new Query(graph).forward(c).evaluate();
		super.inspectForwardC(result);
	}
	
	@Test
	public void testForwardEGTraversal() {
		Graph result = new Query(graph).forward(e,g).evaluate();
		super.inspectForwardEG(result);
	}
	
	@Test
	public void testForwardFTraversal() {
		Graph result = new Query(graph).forward(f).evaluate();
		super.inspectForwardF(result);
	}

}
