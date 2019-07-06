package chpg.tests.traversals;

import org.junit.Before;
import org.junit.Test;

import chpg.graph.Graph;
import chpg.graph.query.Query;

public class TestQueryForwardTraversals extends TestGraphForwardStepTraversals {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testForwardATraversal() {
		Graph result = new Query(graph).forwardStep(a).evaluate();
		super.inspectForwardStepA(result);
	}
	
	@Test
	public void testForwardBTraversal() {
		Graph result = new Query(graph).forwardStep(b).evaluate();
		super.inspectForwardStepB(result);
	}
	
	@Test
	public void testForwardCTraversal() {
		Graph result = new Query(graph).forwardStep(c).evaluate();
		super.inspectForwardStepC(result);
	}
	
	@Test
	public void testForwardEGTraversal() {
		Graph result = new Query(graph).forwardStep(e,g).evaluate();
		super.inspectForwardStepEG(result);
	}
	
	@Test
	public void testForwardFTraversal() {
		Graph result = new Query(graph).forwardStep(f).evaluate();
		super.inspectForwardStepF(result);
	}

}
