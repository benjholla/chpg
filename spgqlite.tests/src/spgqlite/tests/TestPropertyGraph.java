package spgqlite.tests;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spgqlite.graph.Edge;
import spgqlite.graph.Graph;
import spgqlite.graph.Node;
import spgqlite.graph.PropertyGraph;
import spgqlite.graph.schema.SchemaGraph;

class TestPropertyGraph {

	private PropertyGraph graph;
	private Node a;
	private Node b;
	private Node c;
	private Node d;
	private Node e;
	private Node f;
	private Node g;
	
	private Edge e1;
	private Edge e2;
	private Edge e3;
	private Edge e4;
	private Edge e5;
	private Edge e6;
	
	private static final String NAME = "name";
	
	@BeforeEach
	void setUp() throws Exception {
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
	void testForwardTraversal() {
		Graph result = graph.forward(a);
		
		if(!result.nodes().contains(a)) {
			fail("Forward from a should include a");
		}
		
		if(!result.nodes().contains(b)) {
			fail("Forward from a should include b");
		}
		
		if(!result.nodes().contains(c)) {
			fail("Forward from a should include c");
		}
		
		if(!result.nodes().contains(d)) {
			fail("Forward from a should include d");
		}
		
		if(!result.nodes().contains(e)) {
			fail("Forward from a should include e");
		}
		
		if(!result.nodes().contains(g)) {
			fail("Forward from a should include g");
		}
		
		if(result.nodes().contains(f)) {
			fail("Forward from a should NOT include f");
		}
	}

}
