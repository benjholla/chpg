package chpg.graph;

import java.util.Comparator;

import chpg.graph.Node.NodeDirection;

public interface Graph {
	
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
	
	/**
	 * Construct a with the given nodes
	 */
	public Graph toGraph(Node... nodes);
	
	/**
	 * Construct a graph with the given edges
	 */
	public Graph toGraph(Edge... edges);
	
	/**
	 * Construct a graph with the given nodes and edges
	 * 
	 * @param nodes
	 * @param edges
	 */
	public Graph toGraph(GraphElementSet<Node> nodes, GraphElementSet<Edge> edges);
	
	/**
	 * Construct a graph with the given graph elements (mixed set of nodes and edges)
	 * 
	 * @param graphElements
	 */
	public Graph toGraph(GraphElementSet<? extends GraphElement> graphElements);
	
	/**
	 * Construct a new graph with a shallow copy of the nodes and edges in the given graph
	 * 
	 * @param graph
	 */
	public Graph toGraph(Graph... graphs);
	
	/**
	 * Returns the graph element denoted by the given address or null if no graph element corresponds to address
	 * @param address
	 * @return
	 */
	public GraphElement getGraphElementByAddress(int address);
	
	/**
	 * Returns the node denoted by the given address or null if no node corresponds to address
	 * @param address
	 * @return
	 */
	public Node getNodeByAddress(int address);
	
	/**
	 * Returns the edge denoted by the given address or null if no edge corresponds to address
	 * @param address
	 * @return
	 */
	public Edge getEdgeByAddress(int address);
	
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
	public boolean add(GraphElement graphElement);
	
	/**
	 * Add graph elements to the graph
	 * 
	 * @param graphElements
	 * 
	 * @return Returns true if the graph changed as a result of the add operation
	 */
	public boolean addAll(Iterable<? extends GraphElement> graphElements);
	
	/**
	 * Removes a graph element from the graph. If the element is an edge only the
	 * edge will be removed if it exists. If the element is a node the node will be
	 * removed and any edges connected to the node will be removed if they exist.
	 * 
	 * @param graphElement
	 * 
	 * @return Returns true if the graph changed as a result of the remove operation
	 */
	public boolean remove(GraphElement graphElement);

	/**
	 * Return the nodes of the graph
	 * 
	 * @return
	 */
	public GraphElementSet<Node> nodes();

	/**
	 * Return the edges of the graph
	 * 
	 * @return
	 */
	public GraphElementSet<Edge> edges();
	
	/**
	 * Returns true if the graph empty (has no nodes)
	 * 
	 * @return
	 */
	public boolean isEmpty();
	
	/**
	 * Gets the node's predecessor or successor edges in this graph
	 * 
	 * @param node
	 * @param direction
	 * @return
	 */
	public GraphElementSet<Edge> edges(Node node, NodeDirection direction);
	
	/**
	 * Returns the nodes in the graph without edges from the given direction
	 * 
	 * @param direction
	 * @return
	 */
	public GraphElementSet<Node> limit(NodeDirection direction);
	
	/**
	 * Selects the nodes of this graph that have no successors
	 * 
	 * Convenience for limit(NodeDirection.OUT)
	 * @return
	 */
	public GraphElementSet<Node> leaves();
	
	/**
	 * Selects the nodes of this graph that have no predecessors
	 * 
	 * Convenience for limit(NodeDirection.IN)
	 * @return
	 */
	public GraphElementSet<Node> roots();
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public GraphElementSet<Node> predecessors(Node... origin);
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public GraphElementSet<Node> predecessors(Graph origin);
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public GraphElementSet<Node> predecessors(GraphElementSet<Node> origin);
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public GraphElementSet<Node> successors(Node... origin);
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public GraphElementSet<Node> successors(Graph origin);
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public GraphElementSet<Node> successors(GraphElementSet<Node> origin);
	
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
	public Graph forwardStep(Node... origin);
	
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
	public Graph forwardStep(Graph origin);
	
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
	public Graph forwardStep(GraphElementSet<Node> origin);
	
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
	public Graph reverseStep(Node... origin);
	
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
	public Graph reverseStep(Graph origin);
	
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
	public Graph reverseStep(GraphElementSet<Node> origin);
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph union(Node... nodes);
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph union(Edge... edges);
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph union(Graph... graphs);
	
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
	public Graph difference(Node... nodes);
	
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
	public Graph difference(Edge... edges);
	
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
	public Graph difference(Graph... graphs);
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph differenceEdges(Edge... edges);
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph differenceEdges(Graph... graphs);
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph intersection(Node... nodes);
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph intersection(Edge... edges);
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph intersection(Graph... graphs);
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from in a single step
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph betweenStep(Node from, Node to);
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from in a single step
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph betweenStep(Graph from, Graph to);
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from in a single step
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Graph betweenStep(GraphElementSet<Node> from, GraphElementSet<Node> to);
	
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
	public Graph between(Node from, Node to);
	
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
	public Graph between(Graph from, Graph to);
	
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
	public Graph between(GraphElementSet<Node> from, GraphElementSet<Node> to);

	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(Node... origin);
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(Graph origin);
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph forward(GraphElementSet<Node> origin);
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverse(Node... origin);
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverse(Graph origin);
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Graph reverse(GraphElementSet<Node> origin);
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph induce(Edge... edges);
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph induce(Graph... graphs);
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Graph induce(GraphElementSet<Edge> edges);
	
	/**
	 * A convenience method for nodes(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodes(String... tags);
	
	/**
	 * Returns the set of nodes from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAny(String... tags);
	
	/**
	 * Returns the set of nodes from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAll(String... tags);
	
	/**
	 * A convenience method for edges(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edges(String... tags);
	
	/**
	 * Returns the set of edges from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAny(String... tags);
	
	/**
	 * Returns the set of edges from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAll(String... tags);
	
	/**
	 * Select subgraph containing edges that have any of the given names defined
	 * @param name
	 * @return
	 */
	public GraphElementSet<Edge> selectEdgesByName(String... names);
	
	/**
	 * Select subgraph containing nodes that have any of the given names defined
	 * @param name
	 * @return
	 */
	public GraphElementSet<Node> selectNodesByName(String... names);
	
	/**
	 * Select subgraph containing edges that have the given attribute key defined, with any value.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Edge> selectEdges(String attribute);
	
	/**
	 * Select subgraph contain edges that have the given attribute key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Edge> selectEdges(String attribute, Object... values);
	
	/**
	 * Select subgraph containing nodes that have a given key defined, with any value.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Node> selectNodes(String attribute);
	
	/**
	 * Select subgraph containing nodes that have a given key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public GraphElementSet<Node> selectNodes(String attribute, Object... values);
	
}
