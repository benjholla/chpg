package spgqlite.graph.property;

import spgqlite.graph.Edge;
import spgqlite.graph.Graph;
import spgqlite.graph.GraphElement;
import spgqlite.graph.GraphElementSet;
import spgqlite.graph.Node;

public class PropertyGraph extends Graph {

	/**
	 * Construct an empty graph
	 */
	public PropertyGraph() {
		super();
	}
	
	/**
	 * Construct a with the given nodes
	 */
	public PropertyGraph(Node... nodes) {
		for(Node node : nodes) {
			add(node);
		}
	}
	
	/**
	 * Construct a graph with the given edges
	 */
	public PropertyGraph(Edge... edges) {
		for(Edge edge : edges) {
			add(edge);
		}
	}
	
	/**
	 * Construct a graph with the given nodes and edges
	 * 
	 * @param nodes
	 * @param edges
	 */
	public PropertyGraph(GraphElementSet<Node> nodes, GraphElementSet<Edge> edges) {
		addAll(nodes);
		addAll(edges);
	}
	
	/**
	 * Construct a graph with the given graph elements (mixed set of nodes and edges)
	 * 
	 * @param graphElements
	 */
	public PropertyGraph(GraphElementSet<? extends GraphElement> graphElements) {
		addAll(graphElements);
	}
	
	/**
	 * Construct a new graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public PropertyGraph(Graph... graphs) {
		for(Graph graph : graphs) {
			addAll(graph.nodes());
			addAll(graph.edges());
		}
	}
	
}
