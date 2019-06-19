package spgqlite.graph;

import spgqlite.graph.Node.NodeDirection;

public class Graph {

	private GraphElementSet<Node> nodes;
	private GraphElementSet<Edge> edges;
	
	/**
	 * Construct an empty graph
	 */
	public Graph() {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
	}
	
	/**
	 * Construct a graph with the given nodes and edges
	 * @param nodes
	 * @param edges
	 */
	public Graph(GraphElementSet<Node> nodes, GraphElementSet<Edge> edges) {
		this.nodes = new GraphElementHashSet<Node>(nodes);
		this.edges = new GraphElementHashSet<Edge>(edges);
		for(Edge edge : edges) {
			this.nodes.add(edge.from());
			this.nodes.add(edge.to());
		}
	}
	
	/**
	 * Construct a graph with the given graph elements (mixed set of nodes and edges)
	 * @param graphElements
	 */
	public Graph(GraphElementSet<? extends GraphElement> graphElements) {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
		add(graphElements);
	}
	
	/**
	 * Construct a new graph with a shallow copy of the nodes and edges in the given graph
	 * @param graph
	 */
	public Graph(Graph graph) {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
		add(graph.nodes);
		add(graph.edges);
	}

	/**
	 * Add a graph element to the graph
	 * @param graphElement
	 */
	public void add(GraphElement graphElement) {
		if(graphElement instanceof Node) {
			Node node = (Node) graphElement;
			this.nodes.add(node);
		} else if(graphElement instanceof Edge) {
			Edge edge = (Edge) graphElement;
			this.edges.add(edge);
			this.nodes.add(edge.from());
			this.nodes.add(edge.to());
		}
	}
	
	/**
	 * Add graph elements to the graph
	 * @param graphElements
	 */
	public void add(GraphElementSet<? extends GraphElement> graphElements) {
		for(GraphElement graphElement : graphElements) {
			add(graphElement);
		}
	}

	/**
	 * Return the nodes of the graph
	 * @return
	 */
	public GraphElementSet<Node> nodes() {
		return nodes;
	}

	/**
	 * Return the edges of the graph
	 * @return
	 */
	public GraphElementSet<Edge> edges() {
		return edges;
	}
	
	/**
	 * Returns true if the graph empty (has no nodes)
	 * @return
	 */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}
	
	/**
	 * Gets the node's predecessor or successor edges in this graph
	 * @param node
	 * @param direction
	 * @return
	 */
	public GraphElementSet<Edge> edges(Node node, NodeDirection direction){
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			if(direction == NodeDirection.IN){
				if(edge.to().equals(node)){
					result.add(edge);
				}
			} else {
				if(edge.from().equals(node)){
					result.add(edge);
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the nodes in the graph without edges from the given direction
	 * @param direction
	 * @return
	 */
	public GraphElementSet<Node> limit(NodeDirection direction){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes){
			GraphElementSet<Edge> connections = edges(node, direction);
			if(connections.isEmpty()){
				result.add(node);
			}
		}
		return result;
	}
	
	/**
	 * Selects the nodes of this graph that have no successors
	 * 
	 * Convenience for limit(NodeDirection.OUT)
	 * @return
	 */
	public GraphElementSet<Node> leaves(){
		return limit(NodeDirection.OUT);
	}
	
	/**
	 * Selects the nodes of this graph that have no predecessors
	 * 
	 * Convenience for limit(NodeDirection.IN)
	 * @return
	 */
	public GraphElementSet<Node> roots(){
		return limit(NodeDirection.IN);
	}
	
	/**
	 * A convenience method for nodes(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodes(String... tags){
		return nodesTaggedWithAny(tags);
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAny(String... tags){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes){
			for(String tag : tags){
				if(node.tags().contains(tag)){
					result.add(node);
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAll(String... tags){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes){
			boolean add = true;
			for(String tag : tags){
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
	
	/**
	 * A convenience method for edges(String... tags)
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edges(String... tags){
		return edgesTaggedWithAny(tags);
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAny(String... tags){
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			for(String tag : tags){
				if(edge.tags().contains(tag)){
					result.add(edge);
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAll(String... tags){
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			boolean add = true;
			for(String tag : tags){
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
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * @param node
	 * @return The set of incoming edges to the given node
	 */
	public GraphElementSet<Node> predecessors(Node node){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Edge edge : edges){
			if(edge.to().equals(node)){
				result.add(edge.from());
			}
		}
		return result;
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * @param node
	 * @return The set of out-coming edges from the given node
	 */
	public GraphElementSet<Node> successors(Node node){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Edge edge : edges){
			if(edge.from().equals(node)){
				result.add(edge.to());
			}
		}
		return result;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * along a path length of 1 in the forward direction.
	 * 
	 * The final result includes the given nodes, the traversed edges, and the
	 * reachable nodes.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forwardStep(Node origin){
		return forwardStep(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * along a path length of 1 in the forward direction.
	 * 
	 * The final result includes the given nodes, the traversed edges, and the
	 * reachable nodes.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forwardStep(GraphElementSet<Node> origin){
		Graph result = new Graph();
		for(Node node : origin){
			GraphElementSet<Edge> outEdges = getOutEdgesFromNode(node);
			for(Edge edge : outEdges){
				result.nodes().add(edge.from());
				result.nodes().add(edge.to());
				result.edges().add(edge);
			}
		}
		return result;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * along a path length of 1 in the reverse direction.
	 * 
	 * The final result includes the given nodes, the traversed edges, and the
	 * reachable nodes.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverseStep(Node origin){
		return reverseStep(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * along a path length of 1 in the reverse direction.
	 * 
	 * The final result includes the given nodes, the traversed edges, and the
	 * reachable nodes.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverseStep(GraphElementSet<Node> origin){
		Graph result = new Graph();
		for(Node node : origin){
			GraphElementSet<Edge> inEdges = getInEdgesToNode(node);
			for(Edge edge : inEdges){
				result.nodes().add(edge.from());
				result.nodes().add(edge.to());
				result.edges().add(edge);
			}
		}
		return result;
	}
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param a
	 * @param graphs
	 * @return
	 */
	public Graph union(Graph... graphs){
		Graph result = new Graph();
		GraphElementSet<Node> nodes = new GraphElementHashSet<Node>(nodes());
		GraphElementSet<Edge> edges = new GraphElementHashSet<Edge>(edges());
		for(Graph graph : graphs){
			nodes.addAll(graph.nodes());
			edges.addAll(graph.edges());
		}
		result.nodes().addAll(nodes);
		result.edges().addAll(edges);
		return result;
	}
	
	/**
	 * Select this graph, excluding the graphs g. Note that, because
	 * an edge is only in a graph if it's nodes are in a graph, removing an edge
	 * will necessarily remove the nodes it connects as well. Removing either
	 * node would remove the edge as well.
	 * 
	 * This behavior may seem counter-intuitive if one is thinking in terms of
	 * removing a single edge from a graph. Consider the graphs: - g1: a -> b ->
	 * c - g2: a -> b g1.remove(g2) yields the graph containing only node c:
	 * because b is removed, so b -> c is also removed. In general, this
	 * operation is useful for removing nodes from a graph, but may not be as
	 * useful for operating on edges.
	 * 
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Graph difference(Graph... graphs){
		Graph result = new Graph();
		GraphElementSet<Node> nodes = new GraphElementHashSet<Node>(nodes());
		GraphElementSet<Edge> edges = new GraphElementHashSet<Edge>(edges());
		for(Graph graph : graphs){
			nodes.removeAll(graph.nodes());
			edges.removeAll(graph.edges());
		}
		result.nodes().addAll(nodes);
		result.edges().addAll(edges);
		return result;
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * @param a
	 * @param b
	 * @return
	 */
	public Graph differenceEdges(Graph... graphs){
		Graph result = new Graph();
		GraphElementSet<Node> nodes = new GraphElementHashSet<Node>(nodes());
		GraphElementSet<Edge> edges = new GraphElementHashSet<Edge>(edges());
		for(Graph graph : graphs){
			edges.retainAll(graph.edges());
		}
		result.nodes().addAll(nodes);
		result.edges().addAll(edges);
		return result;
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Graph intersection(Graph... graphs){
		Graph result = new Graph();
		GraphElementSet<Node> nodes = new GraphElementHashSet<Node>(nodes());
		GraphElementSet<Edge> edges = new GraphElementHashSet<Edge>(edges());
		for(Graph graph : graphs){
			nodes.retainAll(graph.nodes());
			edges.retainAll(graph.edges());
		}
		result.nodes().addAll(nodes);
		result.edges().addAll(edges);
		return result;
	}
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from in a single step
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph betweenStep(Node from, Node to){
		return forwardStep(new GraphElementHashSet<Node>(from)).intersection(reverseStep(new GraphElementHashSet<Node>(to)));
	}
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from in a single step
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph betweenStep(GraphElementSet<Node> from, GraphElementSet<Node> to){
		return forwardStep(from).intersection(reverseStep(to));
	}
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from using forward traversal.
	 * 
	 * Logically equivalent to
	 * graph.forward(from).intersection(graph.reverse(to)) .
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph between(Node from, Node to) {
		return forward(new GraphElementHashSet<Node>(from)).intersection(reverse(new GraphElementHashSet<Node>(to)));
	}
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from using forward traversal.
	 * 
	 * Logically equivalent to
	 * graph.forward(from).intersection(graph.reverse(to)) .
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph between(GraphElementSet<Node> from, GraphElementSet<Node> to) {
		return forward(from).intersection(reverse(to));
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(Node origin){
		return forward(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(GraphElementSet<Node> origin){
		Graph result = new Graph();
		result.nodes().addAll(origin);
		GraphElementSet<Node> frontier = new GraphElementHashSet<Node>(origin);
		while(!frontier.isEmpty()){
			Node next = frontier.one();
			frontier.remove(next);
			for(Edge edge : forwardStep(next).edges()){
				if(result.nodes().add(edge.to())){
					frontier.add(edge.to());
				}
				result.edges().add(edge);
			}
		}
		return result;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverse(Node origin){
		return reverse(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverse(GraphElementSet<Node> origin){
		Graph result = new Graph();
		result.nodes().addAll(origin);
		GraphElementSet<Node> frontier = new GraphElementHashSet<Node>(origin);
		while(!frontier.isEmpty()){
			Node next = frontier.one();
			frontier.remove(next);
			for(Edge edge : reverseStep(next).edges()){
				if(result.nodes().add(edge.from())){
					frontier.add(edge.from());
				}
				result.edges().add(edge);
			}
		}
		return result;
	}
	
	/**
	 * Gets incoming edges to node
	 * @param node
	 * @return The set of incoming edges to the given node
	 */
	private GraphElementSet<Edge> getInEdgesToNode(Node node){
		GraphElementSet<Edge> inEdges = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			if(edge.to().equals(node)){
				inEdges.add(edge);
			}
		}
		return inEdges;
	}
	
	/**
	 * Gets out-coming edges from node
	 * @param node
	 * @return The set of out-coming edges from the given node
	 */
	private GraphElementSet<Edge> getOutEdgesFromNode(Node node){
		GraphElementSet<Edge> outEdges = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			if(edge.from().equals(node)){
				outEdges.add(edge);
			}
		}
		return outEdges;
	}
	
}
