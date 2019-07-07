package chpg.tests.io;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import chpg.graph.Edge;
import chpg.graph.Node;
import chpg.graph.PropertyGraph;
import chpg.io.GraphExport;
import chpg.io.GraphImport;

public class TestGraphIO {

	@Test
	public void testGraphIO() throws IOException {
		PropertyGraph graph = new PropertyGraph();

		List<Node> nodes = new ArrayList<Node>();
		for(int i=0; i<30; i++){
		  Node n = new Node();
		  graph.add(n);
		  nodes.add(n);
		}

		Random rnd = new Random();
		for(int i=0; i<35; i++){
		  Edge e = new Edge(nodes.get(rnd.nextInt(30)), nodes.get(rnd.nextInt(30)));
		  graph.add(e);
		}
		
		File tmp = File.createTempFile("test", "chpg");
		GraphExport.exportGraph(graph, tmp);
		
		PropertyGraph graph2 = GraphImport.importGraph(tmp);
		tmp.delete();
		
		if(graph.getSchema().nodes().size() != graph2.getSchema().nodes().size()) {
			fail("Imported graph should have the same number of schema nodes as exported graph");
		}
		
		if(graph.getSchema().edges().size() != graph2.getSchema().edges().size()) {
			fail("Imported graph should have the same number of schema edges as exported graph");
		}
		
		if(graph.nodes().size() != graph2.nodes().size()) {
			fail("Imported graph should have the same number of nodes as exported graph");
		}
		
		if(graph.edges().size() != graph2.edges().size()) {
			fail("Imported graph should have the same number of edges as exported graph");
		}
	}

}
