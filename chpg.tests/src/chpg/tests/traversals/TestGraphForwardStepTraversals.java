package chpg.tests.traversals;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.Node;
import chpg.graph.PropertyGraph;
import chpg.graph.schema.SchemaGraph;

public class TestGraphForwardStepTraversals {

	protected PropertyGraph graph;
	
	protected Node a;
	protected Node b;
	protected Node c;
	protected Node d;
	protected Node e;
	protected Node f;
	protected Node g;
	
	protected Edge e1;
	protected Edge e2;
	protected Edge e3;
	protected Edge e4;
	protected Edge e5;
	protected Edge e6;
	
	public static final String NAME = "name";
	
	@Before
	public void setUp() throws Exception {
		resetGraph();
	}

	private void resetGraph() {
		graph = new PropertyGraph(new SchemaGraph());
		
		a = new Node();
		a.putAttr(NAME, "a");
		graph.add(a);
		
		b = new Node();
		b.putAttr(NAME, "b");
		graph.add(b);
		
		c = new Node();
		c.putAttr(NAME, "c");
		graph.add(c);
		
		d = new Node();
		d.putAttr(NAME, "d");
		graph.add(d);
		
		e = new Node();
		e.putAttr(NAME, "e");
		graph.add(e);
		
		f = new Node();
		f.putAttr(NAME, "f");
		graph.add(f);
		
		g = new Node();
		g.putAttr(NAME, "g");
		graph.add(g);
		
		e1 = new Edge(a,b);
		e1.putAttr(NAME, "e1");
		graph.add(e1);
		
		e2 = new Edge(b,c);
		e2.putAttr(NAME, "e2");
		graph.add(e2);
		
		e3 = new Edge(c,b);
		e3.putAttr(NAME, "e3");
		graph.add(e3);
		
		e4 = new Edge(c,d);
		e4.putAttr(NAME, "e4");
		graph.add(e4);
		
		e5 = new Edge(d,e);
		e5.putAttr(NAME, "e5");
		graph.add(e5);
		
		e6 = new Edge(d,g);
		e6.putAttr(NAME, "e6");
		graph.add(e6);
	}

	@Test
	public void testForwardStepATraversal() {
		Graph result = graph.forwardStep(a);
		inspectForwardStepA(result);
	}

	protected void inspectForwardStepA(Graph result) {
		if(result.nodes().size() != 2){
			fail("ForwardStep from a should include 2 nodes");
		}
		
		if(!result.nodes().contains(a)) {
			fail("ForwardStep from a should include a");
		}
		
		if(!result.nodes().contains(b)) {
			fail("ForwardStep from a should include b");
		}
		
		if(result.edges().size() != 1){
			fail("ForwardStep from a should include 1 edge");
		}
		
		if(!result.edges().contains(e1)) {
			fail("ForwardStep from a should include e1");
		}
	}
	
	@Test
	public void testForwardStepBTraversal() {
		Graph result = graph.forwardStep(b);
		inspectForwardStepB(result);
	}

	protected void inspectForwardStepB(Graph result) {
		if(result.nodes().size() != 2){
			fail("ForwardStep from b should include 2 nodes");
		}
		
		if(!result.nodes().contains(b)) {
			fail("ForwardStep from b should include b");
		}
		
		if(!result.nodes().contains(c)) {
			fail("ForwardStep from b should include c");
		}
		
		if(result.edges().size() != 1){
			fail("ForwardStep from b should include 1 edge");
		}

		if(!result.edges().contains(e2)) {
			fail("ForwardStep from b should include e2");
		}
	}
	
	@Test
	public void testForwardStepCTraversal() {
		Graph result = graph.forwardStep(c);
		inspectForwardStepC(result);
	}

	protected void inspectForwardStepC(Graph result) {
		if(result.nodes().size() != 3){
			fail("ForwardStep from c should include 3 nodes");
		}
		
		if(!result.nodes().contains(c)) {
			fail("ForwardStep from c should include c");
		}
		
		if(!result.nodes().contains(b)) {
			fail("ForwardStep from c should include b");
		}
		
		if(!result.nodes().contains(d)) {
			fail("ForwardStep from c should include d");
		}
		
		if(result.edges().size() != 2){
			fail("ForwardStep from c should include 2 edges");
		}
		
		if(!result.edges().contains(e3)) {
			fail("ForwardStep from c should include e3");
		}
		
		if(!result.edges().contains(e4)) {
			fail("ForwardStep from c should include e4");
		}
	}
	
	@Test
	public void testForwardStepEGTraversal() {
		Graph result = graph.forwardStep(e,g);
		inspectForwardStepEG(result);
	}

	protected void inspectForwardStepEG(Graph result) {
		if(result.nodes().size() != 2){
			fail("ForwardStep from eg should include 2 nodes");
		}
		
		if(!result.nodes().contains(e)) {
			fail("ForwardStep from eg should include e");
		}
		
		if(!result.nodes().contains(g)) {
			fail("ForwardStep from eg should include g");
		}
		
		if(!result.edges().isEmpty()){
			fail("ForwardStep from eg should include 0 edges");
		}
	}
	
	@Test
	public void testForwardStepFTraversal() {
		Graph result = graph.forwardStep(f);
		inspectForwardStepF(result);
	}

	protected void inspectForwardStepF(Graph result) {
		if(result.nodes().size() != 1){
			fail("ForwardStep from f should include 1 nodes");
		}
		
		if(!result.nodes().contains(f)) {
			fail("ForwardStep from f should include f");
		}

		if(!result.edges().isEmpty()){
			fail("ForwardStep from f should include 0 edges");
		}
	}

}
