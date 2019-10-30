package chpg.tests.visualzation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import chpg.graph.Graph;
import chpg.io.GraphIO;
import chpg.visualizations.GraphView;

public class TestVisualization {

	@Test
	public void testGraphIO() throws IOException {
		// Import serialized control flow graph
		File serializedGraph = new File("C:/Graphs/graph1.chpg");
		Graph graph = GraphIO.importGraph(serializedGraph);

		// Create path to directory where HTML graph will be written
		Path dirPath = Path.of("C:/Graphs/Test5");

		// Create the HTML
		GraphView.setDebug(true);
		GraphView.createHTMLDocument(graph, dirPath);
	}
}
