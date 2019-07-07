package chpg.graph.schema;

import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.GraphElementHashSet;
import chpg.graph.GraphElementSet;
import chpg.graph.Node;

public class SchemaGraph extends Graph {

	/**
	 * The base tag for structural containment relationships
	 */
	public static final String Contains = "CHPG.Contains";
	
	/**
	 * Construct an empty schema graph
	 */
	public SchemaGraph() {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
		add(new SchemaNode(Contains));
	}
	
	@Override
	public Graph empty() {
		return new SchemaGraph();
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
	
	public GraphElementSet<SchemaEdge> getSchemaEdges(){
		GraphElementSet<SchemaEdge> schemaEdges = new GraphElementHashSet<SchemaEdge>();
		for(Edge edge : edges) {
			if(edge instanceof SchemaEdge) {
				schemaEdges.add((SchemaEdge) edge);
			}
		}
		return schemaEdges;
	}
	
	public GraphElementSet<SchemaNode> getSchemaNodes(){
		GraphElementSet<SchemaNode> schemaNodes = new GraphElementHashSet<SchemaNode>();
		for(Node node : nodes) {
			if(node instanceof SchemaNode) {
				schemaNodes.add((SchemaNode) node);
			}
		}
		return schemaNodes;
	}
	
}
