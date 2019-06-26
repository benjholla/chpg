package spgqlite.graph.attributes.tags.hierarchy;

import spgqlite.graph.GraphElement;
import spgqlite.graph.GraphElementSet;
import spgqlite.graph.attributes.tags.TaggedEdge;
import spgqlite.graph.attributes.tags.TaggedGraph;
import spgqlite.graph.attributes.tags.TaggedNode;

public class HierarchalGraph extends TaggedGraph {

	private SchemaGraph schema;
	
	/**
	 * Construct an empty graph
	 */
	public HierarchalGraph(SchemaGraph schema) {
		super();
		this.schema = schema;
	}
	
	/**
	 * Construct a with the given nodes
	 */
	public HierarchalGraph(SchemaGraph schema, TaggedNode... nodes) {
		this(schema);
		for(TaggedNode node : nodes) {
			add(node);
		}
	}
	
	/**
	 * Construct a graph with the given edges
	 */
	public HierarchalGraph(SchemaGraph schema, TaggedEdge... edges) {
		this(schema);
		for(TaggedEdge edge : edges) {
			add(edge);
		}
	}
	
	/**
	 * Construct a graph with the given nodes and edges
	 * 
	 * @param nodes
	 * @param edges
	 */
	public HierarchalGraph(SchemaGraph schema, GraphElementSet<TaggedNode> nodes, GraphElementSet<TaggedEdge> edges) {
		this(schema);
		addAll(nodes);
		addAll(edges);
	}
	
	/**
	 * Construct a graph with the given graph elements (mixed set of nodes and edges)
	 * 
	 * @param graphElements
	 */
	public HierarchalGraph(SchemaGraph schema, GraphElementSet<? extends GraphElement> graphElements) {
		this(schema);
		addAll(graphElements);
	}
	
	/**
	 * Construct a new graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public HierarchalGraph(SchemaGraph schema, TaggedGraph... graphs) {
		this(schema);
		for(TaggedGraph graph : graphs) {
			addAll(graph.nodes());
			addAll(graph.edges());
		}
	}

	public SchemaGraph getSchema() {
		return schema;
	}

	public void setSchema(SchemaGraph schema) {
		this.schema = schema;
	}
	
}
