package chpg.tests.visualzation;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.junit.Test;

import chpg.graph.Graph;
import chpg.io.GraphIO;
import chpg.visualizations.GraphView;


public class TestVisualization {

	@Test
	public void testGraphIO() throws IOException {
		System.out.print("RUNNING...");
		 
		// from Atlas run
		// var propertyGraph = chpg.atlas.support.GraphConversion.convert(Graph.U)
		// chpg.io.GraphIO.exportGraph(propertyGraph, new java.io.File("MyProject.chpg"))

		// import serialized graph to notebook
		File serializedGraph = new File("C:/Graphs/graph1.chpg");
		Graph graph = GraphIO.importGraph(serializedGraph);

		// C:\Users\USER\Desktop

		Path dirPath = Path.of("C:/Graphs/Test1");
		
		GraphView.setDebug(true);
		
		GraphView.createHTML(graph, dirPath);

		// this works
		// InputStream inputStream = GraphView.class.getResourceAsStream("templates/index.html.template");


//		GraphView.show(graph);
	}
	
}
