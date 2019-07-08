package chpg.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import chpg.graph.Node.NodeDirection;

public abstract class Graph {
	
	public static final Comparator<Graph> GRAPH_SIZE_COMPARATOR = new Comparator<Graph>() {
		@Override
		public int compare(Graph g1, Graph g2) {
			int nodes = Integer.compare(g1.nodes().size(), g2.nodes().size());
			if(nodes != 0) {
				return nodes;
			} else {
				return Integer.compare(g1.edges().size(), g2.edges().size());
			}
		}
	};

	protected GraphElementSet<Node> nodes;
	protected GraphElementSet<Edge> edges;
	
	/**
	 * Construct an empty graph
	 */
	protected Graph() {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
	}
	
	/**
	 * Construct a with the given nodes
	 */
	public Graph toGraph(Node... nodes) {
		Graph result = empty();
		for(Node node : nodes) {
			result.add(node);
		}
		return result;
	}
	
	/**
	 * Construct a graph with the given edges
	 */
	public Graph toGraph(Edge... edges) {
		Graph result = empty();
		for(Edge edge : edges) {
			result.add(edge);
		}
		return result;
	}
	
	/**
	 * Construct a graph with the given nodes and edges
	 * 
	 * @param nodes
	 * @param edges
	 */
	public Graph toGraph(GraphElementSet<Node> nodes, GraphElementSet<Edge> edges) {
		Graph result = empty();
		result.addAll(nodes);
		result.addAll(edges);
		return result;
	}
	
	/**
	 * Construct a graph with the given graph elements (mixed set of nodes and edges)
	 * 
	 * @param graphElements
	 */
	public Graph toGraph(GraphElementSet<? extends GraphElement> graphElements) {
		Graph result = empty();
		result.addAll(graphElements);
		return result;
	}
	
	/**
	 * Construct a new graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public Graph toGraph(Graph... graphs) {
		Graph result = empty();
		for(Graph graph : graphs) {
			result.addAll(graph.nodes());
			result.addAll(graph.edges());
		}
		return result;
	}
	
	/**
	 * Returns the graph element denoted by the given address or null if no graph element corresponds to address
	 * @param address
	 * @return
	 */
	public GraphElement getGraphElementByAddress(int address) {
		for(Node node : nodes()) {
			if(node.getAddress() == address) {
				return node;
			}
		}
		for(Edge edge : edges()) {
			if(edge.getAddress() == address) {
				return edge;
			}
		}
		return null;
	}
	
	/**
	 * Returns the node denoted by the given address or null if no node corresponds to address
	 * @param address
	 * @return
	 */
	public Node getNodeByAddress(int address) {
		for(Node node : nodes()) {
			if(node.getAddress() == address) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * Returns the edge denoted by the given address or null if no edge corresponds to address
	 * @param address
	 * @return
	 */
	public Edge getEdgeByAddress(int address) {
		for(Edge edge : edges()) {
			if(edge.getAddress() == address) {
				return edge;
			}
		}
		return null;
	}
	
	/**
	 * Creates an empty graph
	 * @return
	 */
	public abstract Graph empty();

	/**
	 * Add a graph element to the graph
	 * 
	 * @param graphElement
	 */
	public boolean add(GraphElement graphElement) {
		boolean result = false;
		if(graphElement instanceof Node) {
			Node node = (Node) graphElement;
			result |= this.nodes.add(node);
		} else if(graphElement instanceof Edge) {
			Edge edge = (Edge) graphElement;
			result |= this.edges.add(edge);
			result |= this.nodes.add(edge.from());
			result |= this.nodes.add(edge.to());
		}
		return result;
	}
	
	/**
	 * Add graph elements to the graph
	 * 
	 * @param graphElements
	 * 
	 * @return Returns true if the graph changed as a result of the add operation
	 */
	public boolean addAll(Iterable<? extends GraphElement> graphElements) {
		boolean result = false;
		for(GraphElement graphElement : graphElements) {
			result |= add(graphElement);
		}
		return result;
	}
	
	/**
	 * Removes a graph element from the graph. If the element is an edge only the
	 * edge will be removed if it exists. If the element is a node the node will be
	 * removed and any edges connected to the node will be removed if they exist.
	 * 
	 * @param graphElement
	 * 
	 * @return Returns true if the graph changed as a result of the remove operation
	 */
	public boolean remove(GraphElement graphElement) {
		if(graphElement instanceof Edge) {
			Edge edge = (Edge) graphElement;
			return edges.remove(edge);
		} else {
			boolean result = false;
			Node node = (Node) graphElement;
			result |= nodes.remove(node);
			Iterator<Edge> edgeIterator = edges.iterator();
			while(edgeIterator.hasNext()) {
				Edge edge = edgeIterator.next();
				if(edge.from().equals(node) || edge.to().equals(node)) {
					edgeIterator.remove();
					result = true;
				}
			}
			return result;
		}
	}

	/**
	 * Return the nodes of the graph
	 * 
	 * @return
	 */
	public GraphElementSet<Node> nodes() {
		return nodes;
	}

	/**
	 * Return the edges of the graph
	 * 
	 * @return
	 */
	public GraphElementSet<Edge> edges() {
		return edges;
	}
	
	/**
	 * Returns true if the graph empty (has no nodes)
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return nodes.isEmpty();
	}
	
	/**
	 * Gets the node's predecessor or successor edges in this graph
	 * 
	 * @param node
	 * @param direction
	 * @return
	 */
	public GraphElementSet<Edge> edges(Node node, NodeDirection direction){
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges()){
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
	 * 
	 * @param direction
	 * @return
	 */
	public GraphElementSet<Node> limit(NodeDirection direction){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes()){
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
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public GraphElementSet<Node> predecessors(Node... origin){
		return predecessors(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public GraphElementSet<Node> predecessors(Graph origin){
		return predecessors(origin.nodes());
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public GraphElementSet<Node> predecessors(GraphElementSet<Node> origin){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : origin){
			GraphElementSet<Edge> inEdges = getInEdgesToNode(node);
			for(Edge edge : inEdges){
				result.add(edge.from());
			}
		}
		return result;
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public GraphElementSet<Node> successors(Node... origin){
		return successors(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public GraphElementSet<Node> successors(Graph origin){
		return successors(origin.nodes());
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public GraphElementSet<Node> successors(GraphElementSet<Node> origin){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : origin){
			GraphElementSet<Edge> outEdges = getOutEdgesFromNode(node);
			for(Edge edge : outEdges){
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
	public Graph forwardStep(Node... origin){
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
	public Graph forwardStep(Graph origin){
		return forwardStep(origin.nodes());
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
		Graph result = empty();
		result.addAll(origin);
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
	public Graph reverseStep(Node... origin){
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
	public Graph reverseStep(Graph origin){
		return reverseStep(origin.nodes());
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
		Graph result = empty();
		result.addAll(origin);
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
	 * @param graphs
	 * @return
	 */
	public Graph union(Node... nodes){
		return union(toGraph(nodes));
	}
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph union(Edge... edges){
		return union(toGraph(edges));
	}
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph union(Graph... graphs){
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		sortedGraphs.add(this);
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR.reversed());
		Graph initial = sortedGraphs.remove(0);
		if(initial.isEmpty()) {
			return empty();
		}
		Graph union = toGraph(initial.nodes(), initial.edges());
		for(Graph graph : sortedGraphs){
			union.nodes().addAll(graph.nodes());
			union.edges().addAll(graph.edges());
		}
		return union;
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
	 * @param graphs
	 * @return
	 */
	public Graph difference(Node... nodes){
		return difference(toGraph(nodes));
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
	 * @param graphs
	 * @return
	 */
	public Graph difference(Edge... edges){
		return difference(toGraph(edges));
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
	 * @param graphs
	 * @return
	 */
	public Graph difference(Graph... graphs){
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		sortedGraphs.add(this);
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR.reversed());
		Graph initial = sortedGraphs.remove(0);
		if(initial.isEmpty()) {
			return empty();
		}
		Graph difference = toGraph(initial.nodes(), initial.edges());
		for(Graph graph : sortedGraphs){
			difference.nodes().removeAll(graph.nodes());
			difference.edges().removeAll(graph.edges());
			if(difference.edges().isEmpty()) {
				break;
			}
		}
		return difference;
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph differenceEdges(Edge... edges){
		return differenceEdges(toGraph(edges));
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph differenceEdges(Graph... graphs){
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		sortedGraphs.add(this);
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR.reversed());
		Graph initial = sortedGraphs.remove(0);
		if(initial.isEmpty()) {
			return empty();
		}
		Graph difference = toGraph(initial.nodes(), initial.edges());
		for(Graph graph : sortedGraphs){
			difference.edges().removeAll(graph.edges());
			if(difference.edges().isEmpty()) {
				break;
			}
		}
		return difference;
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph intersection(Node... nodes){
		return intersection(toGraph(nodes));
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph intersection(Edge... edges){
		return intersection(toGraph(edges));
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph intersection(Graph... graphs){
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		sortedGraphs.add(this);
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR);
		Graph initial = sortedGraphs.remove(0);
		if(initial.isEmpty()) {
			return empty();
		} else {
			Graph intersection = toGraph(initial);
			for(Graph graph : graphs){
				intersection.nodes().retainAll(graph.nodes());
				intersection.edges().retainAll(graph.edges());
				if(intersection.isEmpty()) {
					break;
				}
			}
			return intersection;
		}
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
		return betweenStep(new GraphElementHashSet<Node>(from), new GraphElementHashSet<Node>(to));
	}
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from in a single step
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph betweenStep(Graph from, Graph to){
		return betweenStep(from.nodes(), to.nodes());
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
		if(from.isEmpty() || to.isEmpty()) {
			return empty();
		}
		Graph forward = forwardStep(from);
		if(forward.isEmpty()) {
			return empty();
		}
		Graph reverse = reverseStep(to);
		if(reverse.isEmpty()) {
			return empty();
		}
		return forward.intersection(reverse);
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
		return between(new GraphElementHashSet<Node>(from), new GraphElementHashSet<Node>(to));
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
	public Graph between(Graph from, Graph to) {
		return between(from.nodes(), to.nodes());
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
		if(from.isEmpty() || to.isEmpty()) {
			return empty();
		}
		Graph forward = forward(from);
		if(forward.isEmpty()) {
			return empty();
		}
		Graph reverse = reverse(to);
		if(reverse.isEmpty()) {
			return empty();
		}
		return forward.intersection(reverse);
	}

	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(Node... origin){
		return forward(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(Graph origin){
		return forward(origin.nodes());
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(GraphElementSet<Node> origin){
		Graph result = empty();
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
	public Graph reverse(Node... origin){
		return reverse(new GraphElementHashSet<Node>(origin));
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverse(Graph origin){
		return reverse(origin.nodes());
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverse(GraphElementSet<Node> origin){
		Graph result = empty();
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
	 * 
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
	 * 
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
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph induce(Edge... edges){
		return induce(new GraphElementHashSet<Edge>(edges));
	}
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph induce(Graph... graphs){
		GraphElementSet<Edge> inducibleEdges = new GraphElementHashSet<Edge>();
		for(Graph graph : graphs){
			inducibleEdges.addAll(graph.edges());
		}
		return induce(inducibleEdges);
	}
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph induce(GraphElementSet<Edge> edges){
		Graph result = toGraph(this);
		for(Edge edge : edges) {
			if(result.nodes().contains(edge.from()) && result.nodes().contains(edge.to())) {
				result.edges().add(edge);
			}
		}
		return result;
	}
	
	/**
	 * A convenience method for nodes(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodes(String... tags){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAny(String... tags){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAll(String... tags){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * A convenience method for edges(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edges(String... tags){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAny(String... tags){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAll(String... tags){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Select subgraph containing edges that have any of the given names defined
	 * @param name
	 * @return
	 */
	public GraphElementSet<Edge> selectEdgesByName(String... names){
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		if(names != null) {
			Set<String> nameSet = new HashSet<String>();
			for(String name : names) {
				nameSet.add(name);
			}
			for(Edge edge : edges()) {
				if(edge.hasName()) {
					if(nameSet.contains(edge.getName())) {
						result.add(edge);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Select subgraph containing nodes that have any of the given names defined
	 * @param name
	 * @return
	 */
	public GraphElementSet<Node> selectNodesByName(String... names){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		if(names != null) {
			Set<String> nameSet = new HashSet<String>();
			for(String name : names) {
				nameSet.add(name);
			}
			for(Node node : nodes()) {
				if(node.hasName()) {
					if(nameSet.contains(node.getName())) {
						result.add(node);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Select subgraph containing edges that have the given attribute key defined, with any value.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Edge> selectEdges(String attribute){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Select subgraph contain edges that have the given attribute key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Edge> selectEdges(String attribute, Object... values){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Select subgraph containing nodes that have a given key defined, with any value.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Node> selectNodes(String attribute){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	/**
	 * Select subgraph containing nodes that have a given key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Node> selectNodes(String attribute, Object... values){
		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
	}
	
	// TODO: how to implement without a single underlying universal graph?
	
//	/**
//	 * For each node in this graph, select the nodes that are successors along CHPG.Contains or CHPG.Contains subtypes, not including the origin
//	 * @return
//	 */
//	public GraphElementSet<Node> children(){
//		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
//	}
//	
//	/**
//	 * For each node in this graph, select the nodes that are predecessor along CHPG.Contains or CHPG.Contains subtypes, not including the origin
//	 * @return
//	 */
//	public GraphElementSet<Node> parent(){
//		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
//	}
//	
//	/**
//	 * Selects the nodes that are descendants along CHPG.Contains or CHPG.Contains subtypes, not including the origin
//	 * @return
//	 */
//	public GraphElementSet<Node> contained(){
//		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
//	}
//	
//	/**
//	 * Selects the nodes that are ancestors along CHPG.Contains or CHPG.Contains subtypes, not including the origin
//	 * @return
//	 */
//	public GraphElementSet<Node> containers(){
//		throw new RuntimeException("Operation not implemented for graph type " + this.getClass().getName());
//	}
	
}
