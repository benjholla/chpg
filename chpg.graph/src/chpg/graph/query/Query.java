package chpg.graph.query;

import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.GraphElementSet;
import chpg.graph.Node;

public class Query {

	private Graph graph;
	private Graph referenceGraph;
	
	/**
	 * Constructs a new query without an underlying reference graph for querying containment relationships
	 * @param graph
	 */
	public Query(Graph graph) {
		if(graph != null) {
			this.graph = graph;
			this.referenceGraph = null;
		} else {
			throw new IllegalArgumentException("Graph must be non-null");
		}
	}
	
	/**
	 * Constructs a new query with an underlying reference graph for querying containment relationships
	 * @param graph
	 * @param referenceGraph
	 */
	public Query(Graph graph, Graph referenceGraph) {
		if(graph != null) {
			this.graph = graph;
			if(referenceGraph != null) {
				this.referenceGraph = referenceGraph;
			} else {
				throw new IllegalArgumentException("Reference graph must be non-null");
			}
		} else {
			throw new IllegalArgumentException("Graph must be non-null");
		}
	}
	
	/**
	 * Returns the underlying reference graph used for containment queries
	 * @return
	 */
	public Graph getReferenceGraph() {
		return referenceGraph;
	}
	
	/**
	 * Sets the underlying reference graph used for containment queries
	 * @param referenceGraph
	 */
	public void setReferenceGraph(Graph referenceGraph) {
		this.referenceGraph = referenceGraph;
	}
	
	public Graph evaluate() {
		return graph;
	}
	
	/**
	 * Return the nodes of the graph
	 * 
	 * @return
	 */
	public Query nodes() {
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.nodes()), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.nodes()));
		}
	}

	/**
	 * Return the edges of the graph
	 * 
	 * @return
	 */
	public Query edges() {
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.edges()), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.edges()));
		}
	}
	
	/**
	 * Returns true if the graph empty (has no nodes)
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return graph.isEmpty();
	}
	
	/**
	 * Selects the nodes of this graph that have no successors
	 * 
	 * Convenience for limit(NodeDirection.OUT)
	 * @return
	 */
	public Query leaves(){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.leaves()), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.leaves()));
		}
	}
	
	/**
	 * Selects the nodes of this graph that have no predecessors
	 * 
	 * Convenience for limit(NodeDirection.IN)
	 * @return
	 */
	public Query roots(){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.roots()), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.roots()));
		}
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(Node... origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.predecessors(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.predecessors(origin)));
		}
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(GraphElementSet<Node> origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.predecessors(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.predecessors(origin)));
		}
	}

	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(Graph origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.predecessors(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.predecessors(origin)));
		}
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(Query origin){
		return predecessors(origin.evaluate());
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(Node... origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.successors(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.successors(origin)));
		}
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(GraphElementSet<Node> origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.successors(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.successors(origin)));
		}
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(Graph origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.successors(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.successors(origin)));
		}
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(Query origin){
		return successors(origin.evaluate());
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
	public Query forwardStep(Node... origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.forwardStep(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.forwardStep(origin)));
		}
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
	public Query forwardStep(GraphElementSet<Node> origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.forwardStep(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.forwardStep(origin)));
		}
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
	public Query forwardStep(Graph origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.forwardStep(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.forwardStep(origin)));
		}
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
	public Query forwardStep(Query origin){
		return forwardStep(origin.evaluate());
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
	public Query reverseStep(Node... origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.reverseStep(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.reverseStep(origin)));
		}
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
	public Query reverseStep(GraphElementSet<Node> origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.reverseStep(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.reverseStep(origin)));
		}
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
	public Query reverseStep(Graph origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.reverseStep(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.reverseStep(origin)));
		}
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
	public Query reverseStep(Query origin){
		return reverseStep(origin.evaluate());
	}
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query union(Node... nodes){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.union(nodes)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.union(nodes)));
		}
	}
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query union(Edge... edges){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.union(edges)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.union(edges)));
		}
	}
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query union(Graph... graphs){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.union(graphs)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.union(graphs)));
		}
	}
	
	/**
	 * Yields the union of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the union of all nodes, and likewise for
	 * edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query union(Query... queries){
		Graph[] graphs = new Graph[queries.length];
		for(int i=0; i<queries.length; i++) {
			graphs[i] = queries[i].evaluate();
		}
		return union(graphs);
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
	public Query difference(Node... nodes){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.difference(nodes)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.difference(nodes)));
		}
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
	public Query difference(Edge... edges){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.difference(edges)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.difference(edges)));
		}
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
	public Query difference(Graph... graphs){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.difference(graphs)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.difference(graphs)));
		}
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
	public Query difference(Query... queries){
		Graph[] graphs = new Graph[queries.length];
		for(int i=0; i<queries.length; i++) {
			graphs[i] = queries[i].evaluate();
		}
		return difference(graphs);
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Query differenceEdges(Edge... edges){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.differenceEdges(edges)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.differenceEdges(edges)));
		}
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Query differenceEdges(Graph... graphs){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.differenceEdges(graphs)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.differenceEdges(graphs)));
		}
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Query differenceEdges(Query... queries){
		Graph[] graphs = new Graph[queries.length];
		for(int i=0; i<queries.length; i++) {
			graphs[i] = queries[i].evaluate();
		}
		return differenceEdges(graphs);
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query intersection(Node... nodes){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.intersection(nodes)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.intersection(nodes)));
		}
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query intersection(Edge... edges){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.intersection(edges)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.intersection(edges)));
		}
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query intersection(Graph... graphs){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.intersection(graphs)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.intersection(graphs)));
		}
	}
	
	/**
	 * Yields the intersection of this graph and the given graphs. That is, the
	 * resulting graph's nodes are the intersection of all node sets, and
	 * likewise for edges.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query intersection(Query... queries){
		Graph[] graphs = new Graph[queries.length];
		for(int i=0; i<queries.length; i++) {
			graphs[i] = queries[i].evaluate();
		}
		return intersection(graphs);
	}
	
	/**
	 * From this graph, selects the subgraph such that the given nodes in to are
	 * reachable from the nodes in from in a single step
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Query betweenStep(Node from, Node to){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.betweenStep(from, to)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.betweenStep(from, to)));
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
	public Query betweenStep(GraphElementSet<Node> from, GraphElementSet<Node> to){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.betweenStep(from, to)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.betweenStep(from, to)));
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
	public Query betweenStep(Graph from, Graph to){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.betweenStep(from, to)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.betweenStep(from, to)));
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
	public Query betweenStep(Query from, Query to){
		return betweenStep(from.evaluate(), to.evaluate());
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
	public Query between(Node from, Node to) {
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.between(from, to)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.between(from, to)));
		}
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
	public Query between(GraphElementSet<Node> from, GraphElementSet<Node> to) {
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.between(from, to)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.between(from, to)));
		}
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
	public Query between(Graph from, Graph to) {
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.between(from.nodes(), to.nodes())), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.between(from.nodes(), to.nodes())));
		}
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
	public Query between(Query from, Query to) {
		return between(from.evaluate(), to.evaluate());
	}

	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(Node... origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.forward(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.forward(origin)));
		}
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(GraphElementSet<Node> origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.forward(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.forward(origin)));
		}
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(Graph origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.forward(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.forward(origin)));
		}
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(Query origin){
		return forward(origin.evaluate());
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(Node... origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.reverse(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.reverse(origin)));
		}
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(GraphElementSet<Node> origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.reverse(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.reverse(origin)));
		}
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(Graph origin){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.reverse(origin)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.reverse(origin)));
		}
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(Query origin){
		return reverse(origin.evaluate());
	}
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query induce(Edge... edges){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.induce(edges)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.induce(edges)));
		}
	}
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query induce(Graph... graphs){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.induce(graphs)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.induce(graphs)));
		}
	}
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query induce(GraphElementSet<Edge> edges){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.induce(edges)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.induce(edges)));
		}
	}
	
	/**
	 * Yields the induced graph formed from the nodes in the current graph and all
	 * of the edges in the given graph that connect pairs of nodes in the current
	 * graph.
	 * 
	 * @param graphs
	 * @return
	 */
	public Query induce(Query... queries){
		Graph[] graphs = new Graph[queries.length];
		for(int i=0; i<queries.length; i++) {
			graphs[i] = queries[i].evaluate();
		}
		return induce(graphs);
	}

	/**
	 * A convenience method for nodes(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public Query nodes(String... tags){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.nodes(tags)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.nodes(tags)));
		}
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query nodesTaggedWithAny(String... tags){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.nodesTaggedWithAny(tags)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.nodesTaggedWithAny(tags)));
		}
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query nodesTaggedWithAll(String... tags){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.nodesTaggedWithAll(tags)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.nodesTaggedWithAll(tags)));
		}
	}
	
	/**
	 * A convenience method for edges(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public Query edges(String... tags){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.edges(tags)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.edges(tags)));
		}
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query edgesTaggedWithAny(String... tags){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.edgesTaggedWithAny(tags)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.edgesTaggedWithAny(tags)));
		}
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query edgesTaggedWithAll(String... tags){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.edgesTaggedWithAll(tags)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.edgesTaggedWithAll(tags)));
		}
	}
	
	/**
	 * Select subgraph containing edges that have the given attribute key defined, with any value.
	 * @param key
	 * @return
	 */
	public Query selectEdges(String attribute){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.selectEdges(attribute)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.selectEdges(attribute)));
		}
	}
	
	/**
	 * Select subgraph contain edges that have the given attribute key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public Query selectEdges(String attribute, Object... values){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.selectEdges(attribute, values)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.selectEdges(attribute, values)));
		}
	}
	
	/**
	 * Select subgraph containing edges that have any of the given names defined
	 * @param name
	 * @return
	 */
	public Query selectEdgesByName(String... names){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.selectEdgesByName(names)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.selectEdgesByName(names)));
		}
	}
	
	/**
	 * Select subgraph containing nodes that have any of the given names defined
	 * @param name
	 * @return
	 */
	public Query selectNodesByName(String... names){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.selectNodesByName(names)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.selectNodesByName(names)));
		}
	}
	
	/**
	 * Select subgraph containing nodes that have a given key defined, with any value.
	 * @param key
	 * @return
	 */
	public Query selectNodes(String attribute){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.selectNodes(attribute)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.selectNodes(attribute)));
		}
	}
	
	/**
	 * Select subgraph containing nodes that have a given key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public Query selectNodes(String attribute, Object... values){
		if(referenceGraph != null) {
			return new Query(graph.toGraph(graph.selectNodes(attribute, values)), referenceGraph);
		} else {
			return new Query(graph.toGraph(graph.selectNodes(attribute, values)));
		}
	}
	
	/**
	 * For each node in this graph, select the nodes that are successors along CHPG.Contains or CHPG.Contains subtypes, not including the origin
	 * @return
	 */
	public Query children(){
		if(referenceGraph == null) {
			throw new IllegalArgumentException("Reference graph must be set to query children relationships");
		}
		if(referenceGraph != null) {
			return new Query(graph.toGraph(referenceGraph.successors(graph.nodes())), referenceGraph);
		} else {
			return new Query(graph.toGraph(referenceGraph.successors(graph.nodes())));
		}
	}
	
	/**
	 * For each node in this graph, select the nodes that are predecessor along CHPG.Contains or CHPG.Contains subtypes, not including the origin
	 * @return
	 */
	public Query parent(){
		if(referenceGraph == null) {
			throw new IllegalArgumentException("Reference graph must be set to query parent relationships");
		}
		if(referenceGraph != null) {
			return new Query(graph.toGraph(referenceGraph.predecessors(graph.nodes())), referenceGraph);
		} else {
			return new Query(graph.toGraph(referenceGraph.predecessors(graph.nodes())));
		}
	}
	
	/**
	 * Selects the nodes that are descendants along CHPG.Contains or CHPG.Contains subtypes, not including the origin
	 * @return
	 */
	public Query contained(){
		if(referenceGraph == null) {
			throw new IllegalArgumentException("Reference graph must be set to query contained relationships");
		}
		if(referenceGraph != null) {
			return new Query(graph.toGraph(referenceGraph.forward(graph.nodes()).nodes()), referenceGraph);
		} else {
			return new Query(graph.toGraph(referenceGraph.forward(graph.nodes()).nodes()));
		}
	}
	
	/**
	 * Selects the nodes that are ancestors along CHPG.Contains or CHPG.Contains subtypes, not including the origin
	 * @return
	 */
	public Query containers(){
		if(referenceGraph == null) {
			throw new IllegalArgumentException("Reference graph must be set to query containers relationships");
		}
		if(referenceGraph != null) {
			return new Query(graph.toGraph(referenceGraph.reverse(graph.nodes()).nodes()), referenceGraph);
		} else {
			return new Query(graph.toGraph(referenceGraph.reverse(graph.nodes()).nodes()));
		}
	}
	
}
