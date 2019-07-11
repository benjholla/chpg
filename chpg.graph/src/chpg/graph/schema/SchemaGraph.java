package chpg.graph.schema;

import chpg.graph.AbstractGraph;
import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.GraphElementHashSet;
import chpg.graph.GraphElementSet;
import chpg.graph.Node;

/**
 * A graph schema for defining tag hierarchies.
 * 
 * Graph must form a tree (DAG)
 */
public class SchemaGraph extends AbstractGraph {

	/**
	 * The base tag for structural containment relationships
	 */
	public static final String Contains = "CHPG.Contains";
	
	/**
	 * The root schema node for containment relationships
	 */
	public final SchemaNode ContainsSchemaNode;
	
	/**
	 * Construct an empty schema graph
	 */
	public SchemaGraph() {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
		ContainsSchemaNode = new SchemaNode(Contains);
		add(ContainsSchemaNode);
	}
	
	@Override
	public Graph empty() {
		return new SchemaGraph();
	}
	
	/**
	 * Returns true if the schema is well formed
	 * @return
	 */
	public boolean isWellFormed() {
		for(Node node : nodes()) {
			GraphElementSet<Edge> incomingEdges = this.getInEdgesToNode(node);
			if(incomingEdges.size() > 1) {
				// schema cannot have more than 1 incoming edge
				return false;
			} else if(incomingEdges.size() == 1) {
				Edge edge = incomingEdges.one();
				if(edge.from().equals(edge.to())) {
					// no self cycles
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Construct a schema graph with the given nodes
	 */
	public SchemaGraph(SchemaNode... nodes) {
		this();
		for(Node node : nodes) {
			add(node);
		}
	}
	
	/**
	 * Construct a schema graph with the given edges
	 */
	public SchemaGraph(SchemaEdge... edges) {
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
	public SchemaGraph(GraphElementSet<SchemaNode> nodes, GraphElementSet<SchemaEdge> edges) {
		this();
		addAll(nodes);
		addAll(edges);
	}
	
	/**
	 * Construct a new schema graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public SchemaGraph(SchemaGraph... graphs) {
		this();
		for(Graph graph : graphs) {
			addAll(graph.nodes());
			addAll(graph.edges());
		}
	}
	
	/**
	 * Returns the schema node denoted by the given node name (tag)
	 * @param address
	 * @return
	 */
	public SchemaNode getSchemaNodeByName(String name) {
		for(Node node : nodes()) {
			if(node instanceof SchemaNode) {
				if(node.getName().equals(name)) {
					return (SchemaNode) node;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the schema node denoted by the given address or null if no node corresponds to address
	 * @param address
	 * @return
	 */
	public SchemaNode getSchemaNodeByAddress(int address) {
		for(Node node : nodes()) {
			if(node instanceof SchemaNode) {
				if(node.getAddress() == address) {
					return (SchemaNode) node;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the schema edge denoted by the given address or null if no edge corresponds to address
	 * @param address
	 * @return
	 */
	public SchemaEdge getSchemaEdgeByAddress(int address) {
		for(Edge edge : edges()) {
			if(edge instanceof SchemaEdge) {
				if(edge.getAddress() == address) {
					return (SchemaEdge) edge;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the set of schema edges
	 * @return
	 */
	public GraphElementSet<SchemaEdge> getSchemaEdges(){
		GraphElementSet<SchemaEdge> schemaEdges = new GraphElementHashSet<SchemaEdge>();
		for(Edge edge : edges()) {
			if(edge instanceof SchemaEdge) {
				schemaEdges.add((SchemaEdge) edge);
			}
		}
		return schemaEdges;
	}
	
	/**
	 * Returns the set of schema nodes
	 * @return
	 */
	public GraphElementSet<SchemaNode> getSchemaNodes(){
		GraphElementSet<SchemaNode> schemaNodes = new GraphElementHashSet<SchemaNode>();
		for(Node node : nodes()) {
			if(node instanceof SchemaNode) {
				schemaNodes.add((SchemaNode) node);
			}
		}
		return schemaNodes;
	}
	
}
