package chpg.graph;

import java.util.HashSet;
import java.util.Set;

import chpg.graph.schema.SchemaGraph;
import chpg.graph.schema.SchemaNode;

public class PropertyGraph extends Graph {

	private SchemaGraph schema;
	
	/**
	 * Construct an empty graph
	 */
	public PropertyGraph(SchemaGraph schema) {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
	}
	
	/**
	 * Construct an empty graph with an empty schema
	 */
	public PropertyGraph() {
		this(new SchemaGraph());
	}
	
	/**
	 * Construct a graph that is a copy of the schema, nodes, and edges in the given graph
	 */
	public PropertyGraph(PropertyGraph graph) {
		this(graph.getSchema());
		addAll(graph.nodes());
		addAll(graph.edges());
	}
	
	/**
	 * Construct a graph with the given nodes
	 */
	public PropertyGraph(SchemaGraph schema, Node... nodes) {
		this(schema);
		for(Node node : nodes) {
			add(node);
		}
	}
	
	/**
	 * Construct a graph with the given edges
	 */
	public PropertyGraph(SchemaGraph schema, Edge... edges) {
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
	public PropertyGraph(SchemaGraph schema, GraphElementSet<Node> nodes, GraphElementSet<Edge> edges) {
		this(schema);
		addAll(nodes);
		addAll(edges);
	}
	
	/**
	 * Construct a graph with the given graph elements (mixed set of nodes and edges)
	 * 
	 * @param graphElements
	 */
	public PropertyGraph(SchemaGraph schema, GraphElementSet<? extends GraphElement> graphElements) {
		this(schema);
		addAll(graphElements);
	}
	
	/**
	 * Construct a new graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public PropertyGraph(SchemaGraph schema, Graph... graphs) {
		this(schema);
		for(Graph graph : graphs) {
			addAll(graph.nodes());
			addAll(graph.edges());
		}
	}
	
	/**
	 * Returns the property graph schema
	 * @return
	 */
	public SchemaGraph getSchema() {
		return schema;
	}
	
	private Set<String> getInheritedTags(String... tags) {
		Set<String> allTags = new HashSet<String>();
		for(String explicitTag : tags) {
			allTags.add(explicitTag);
		}
		if(schema != null) {
			for(SchemaNode explicitTagNode : schema.getSchemaNodes()) {
				if(allTags.contains(explicitTagNode.getTagName())) {
					for(Node implicitTagNode : schema.forward(explicitTagNode).difference(explicitTagNode).nodes()) {
						allTags.add(((SchemaNode)implicitTagNode).getTagName());
					}
				}
			}
		}
		return allTags;
	}
	
	@Override
	public Graph empty() {
		return new PropertyGraph(schema);
	}
	
	@Override
	public GraphElementSet<Node> nodes(String... tags){
		return nodesTaggedWithAny(tags);
	}
	
	@Override
	public GraphElementSet<Node> nodesTaggedWithAny(String... tags){
		Set<String> allTags = getInheritedTags(tags);
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes){
			for(String tag : allTags){
				if(node.tags().contains(tag)){
					result.add(node);
					break;
				}
			}
		}
		return result;
	}
	
	@Override
	public GraphElementSet<Node> nodesTaggedWithAll(String... tags){
		Set<String> allTags = getInheritedTags(tags);
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes){
			boolean add = true;
			for(String tag : allTags){
				if(!node.tags().contains(tag)){
					add = false;
					break;
				}
			}
			if(add){
				result.add(node);
			}
		}
		return result;
	}
	
	@Override
	public GraphElementSet<Edge> edges(String... tags){
		return edgesTaggedWithAny(tags);
	}
	
	@Override
	public GraphElementSet<Edge> edgesTaggedWithAny(String... tags){
		Set<String> allTags = getInheritedTags(tags);
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			for(String tag : allTags){
				if(edge.tags().contains(tag)){
					result.add(edge);
					break;
				}
			}
		}
		return result;
	}
	
	@Override
	public GraphElementSet<Edge> edgesTaggedWithAll(String... tags){
		Set<String> allTags = getInheritedTags(tags);
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			boolean add = true;
			for(String tag : allTags){
				if(!edge.tags().contains(tag)){
					add = false;
					break;
				}
			}
			if(add){
				result.add(edge);
			}
		}
		return result;
	}
	
	@Override
	public GraphElementSet<Edge> selectEdges(String attribute){
		return edges.filter(attribute);
	}
	
	@Override
	public GraphElementSet<Edge> selectEdges(String attribute, Object... values){
		return edges.filter(attribute, values);
	}
	
	@Override
	public GraphElementSet<Node> selectNodes(String attribute){
		return nodes.filter(attribute);
	}
	
	@Override
	public GraphElementSet<Node> selectNodes(String attribute, Object... values){
		return nodes.filter(attribute, values);
	}
	
}
