package spgqlite.graph.property.hierarchy;

import spgqlite.graph.Edge;
import spgqlite.graph.Graph;
import spgqlite.graph.GraphElement;
import spgqlite.graph.GraphElementSet;
import spgqlite.graph.Node;

public class HierarchalPropertyGraph extends Graph {

	private SchemaGraph schema;
	
	/**
	 * Construct an empty graph
	 */
	public HierarchalPropertyGraph(SchemaGraph schema) {
		super();
		this.schema = schema;
	}
	
	/**
	 * Construct a with the given nodes
	 */
	public HierarchalPropertyGraph(SchemaGraph schema, Node... nodes) {
		this(schema);
		for(Node node : nodes) {
			add(node);
		}
	}
	
	/**
	 * Construct a graph with the given edges
	 */
	public HierarchalPropertyGraph(SchemaGraph schema, Edge... edges) {
		this(schema);
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
	public HierarchalPropertyGraph(SchemaGraph schema, GraphElementSet<Node> nodes, GraphElementSet<Edge> edges) {
		this(schema);
		addAll(nodes);
		addAll(edges);
	}
	
	/**
	 * Construct a graph with the given graph elements (mixed set of nodes and edges)
	 * 
	 * @param graphElements
	 */
	public HierarchalPropertyGraph(SchemaGraph schema, GraphElementSet<? extends GraphElement> graphElements) {
		this(schema);
		addAll(graphElements);
	}
	
	/**
	 * Construct a new graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public HierarchalPropertyGraph(SchemaGraph schema, Graph... graphs) {
		this(schema);
		for(Graph graph : graphs) {
			addAll(graph.nodes());
			addAll(graph.edges());
		}
	}
	
}
