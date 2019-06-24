package spgqlite.graph.property.hierarchy;

import spgqlite.graph.Edge;
import spgqlite.graph.Graph;
import spgqlite.graph.GraphElement;
import spgqlite.graph.GraphElementSet;
import spgqlite.graph.Node;

public class SchemaGraph extends Graph {

	/**
	 * Construct an empty schema schema graph
	 */
	public SchemaGraph() {
		super();
	}
	
	/**
	 * Construct a schema schema graph with the given nodes
	 */
	public SchemaGraph(Node... nodes) {
		this();
		for(Node node : nodes) {
			add(node);
		}
	}
	
	/**
	 * Construct a schema graph with the given edges
	 */
	public SchemaGraph(Edge... edges) {
		this();
		for(Edge edge : edges) {
			add(edge);
		}
	}
	
	/**
	 * Construct a schema graph with the given nodes and edges
	 * 
	 * @param nodes
	 * @param edges
	 */
	public SchemaGraph(GraphElementSet<Node> nodes, GraphElementSet<Edge> edges) {
		this();
		addAll(nodes);
		addAll(edges);
	}
	
	/**
	 * Construct a schema graph with the given graph elements (mixed set of nodes and edges)
	 * 
	 * @param graphElements
	 */
	public SchemaGraph(GraphElementSet<? extends GraphElement> graphElements) {
		this();
		addAll(graphElements);
	}
	
	/**
	 * Construct a new schema graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public SchemaGraph(Graph... graphs) {
		this();
		for(Graph graph : graphs) {
			addAll(graph.nodes());
			addAll(graph.edges());
		}
	}
	
}
