package spgqlite.graph.query;

import spgqlite.graph.Edge;
import spgqlite.graph.Graph;
import spgqlite.graph.GraphElementSet;
import spgqlite.graph.Node;

public class Query {

	private Graph graph;
	
	public Query(Graph graph) {
		this.graph = graph;
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
		graph = graph.toGraph(graph.nodes());
		return this;
	}

	/**
	 * Return the edges of the graph
	 * 
	 * @return
	 */
	public Query edges() {
		graph = graph.toGraph(graph.edges());
		return this;
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
		graph = graph.toGraph(graph.leaves());
		return this;
	}
	
	/**
	 * Selects the nodes of this graph that have no predecessors
	 * 
	 * Convenience for limit(NodeDirection.IN)
	 * @return
	 */
	public Query roots(){
		graph = graph.toGraph(graph.roots());
		return this;
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(Node... origin){
		graph = graph.toGraph(graph.predecessors(origin));
		return this;
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(GraphElementSet<Node> origin){
		graph = graph.toGraph(graph.predecessors(origin));
		return this;
	}

	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(Graph origin){
		graph = graph.toGraph(graph.predecessors(origin));
		return this;
	}
	
	/**
	 * Gets the predecessor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from incoming edges to the given nodes
	 */
	public Query predecessors(Query origin){
		graph = graph.toGraph(graph.predecessors(origin.evaluate()));
		return this;
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(Node... origin){
		graph = graph.toGraph(graph.successors(origin));
		return this;
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(GraphElementSet<Node> origin){
		graph = graph.toGraph(graph.successors(origin));
		return this;
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(Graph origin){
		graph = graph.toGraph(graph.successors(origin));
		return this;
	}
	
	/**
	 * Gets the successor nodes of the given node for this graph's edges
	 * 
	 * @param node
	 * 
	 * @return The set of nodes reachable from outgoing edges from the given nodes
	 */
	public Query successors(Query origin){
		graph = graph.toGraph(graph.successors(origin.evaluate()));
		return this;
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
		graph = graph.toGraph(graph.forwardStep(origin));
		return this;
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
		graph = graph.toGraph(graph.forwardStep(origin));
		return this;
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
		graph = graph.toGraph(graph.forwardStep(origin));
		return this;
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
		graph = graph.toGraph(graph.forwardStep(origin.evaluate()));
		return this;
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
		graph = graph.toGraph(graph.reverseStep(origin));
		return this;
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
		graph = graph.toGraph(graph.reverseStep(origin));
		return this;
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
		graph = graph.toGraph(graph.reverseStep(origin));
		return this;
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
		graph = graph.toGraph(graph.reverseStep(origin.evaluate()));
		return this;
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
		graph = graph.toGraph(graph.union(nodes));
		return this;
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
		graph = graph.toGraph(graph.union(edges));
		return this;
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
		graph = graph.toGraph(graph.union(graphs));
		return this;
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
		graph = graph.toGraph(graph.union(graphs));
		return this;
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
		graph = graph.toGraph(graph.difference(nodes));
		return this;
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
		graph = graph.toGraph(graph.difference(edges));
		return this;
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
		graph = graph.toGraph(graph.difference(graphs));
		return this;
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
		graph = graph.toGraph(graph.difference(graphs));
		return this;
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Query differenceEdges(Edge... edges){
		graph = graph.toGraph(graph.differenceEdges(edges));
		return this;
	}
	
	/**
	 * Select this graph, excluding the edges from the given graphs. 
	 * 
	 * @param graphs
	 * @return
	 */
	public Query differenceEdges(Graph... graphs){
		graph = graph.toGraph(graph.differenceEdges(graphs));
		return this;
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
		graph = graph.toGraph(graph.differenceEdges(graphs));
		return this;
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
		graph = graph.toGraph(graph.intersection(nodes));
		return this;
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
		graph = graph.toGraph(graph.intersection(edges));
		return this;
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
		graph = graph.toGraph(graph.intersection(graphs));
		return this;
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
		graph = graph.toGraph(graph.intersection(graphs));
		return this;
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
		graph = graph.toGraph(graph.betweenStep(from, to));
		return this;
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
		graph = graph.toGraph(graph.betweenStep(from, to));
		return this;
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
		graph = graph.toGraph(graph.betweenStep(from.nodes(), to.nodes()));
		return this;
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
		graph = graph.toGraph(graph.betweenStep(from.evaluate().nodes(), to.evaluate().nodes()));
		return this;
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
		graph = graph.toGraph(graph.between(from, to));
		return this;
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
		graph = graph.toGraph(graph.between(from, to));
		return this;
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
		graph = graph.toGraph(graph.between(from.nodes(), to.nodes()));
		return this;
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
		graph = graph.toGraph(graph.between(from.evaluate().nodes(), to.evaluate().nodes()));
		return this;
	}

	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(Node... origin){
		graph = graph.forward(origin);
		return this;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(GraphElementSet<Node> origin){
		graph = graph.forward(origin);
		return this;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(Graph origin){
		graph = graph.forward(origin.nodes());
		return this;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using forward transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query forward(Query origin){
		graph = graph.forward(origin.evaluate().nodes());
		return this;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(Node... origin){
		graph = graph.reverse(origin);
		return this;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(GraphElementSet<Node> origin){
		graph = graph.reverse(origin);
		return this;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(Graph origin){
		graph = graph.reverse(origin);
		return this;
	}
	
	/**
	 * From this graph, selects the subgraph reachable from the given nodes
	 * using reverse transitive traversal.
	 * 
	 * @param origin
	 * @return
	 */
	public Query reverse(Query origin){
		graph = graph.reverse(origin.evaluate().nodes());
		return this;
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
		graph = graph.induce(edges);
		return this;
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
		graph = graph.induce(graphs);
		return this;
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
		graph = graph.induce(edges);
		return this;
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
		graph = graph.induce(graphs);
		return this;
	}

	/**
	 * A convenience method for nodes(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public Query nodes(String... tags){
		graph = graph.toGraph(graph.nodes(tags));
		return this;
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query nodesTaggedWithAny(String... tags){
		graph = graph.toGraph(graph.nodesTaggedWithAny(tags));
		return this;
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query nodesTaggedWithAll(String... tags){
		graph = graph.toGraph(graph.nodesTaggedWithAll(tags));
		return this;
	}
	
	/**
	 * A convenience method for edges(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public Query edges(String... tags){
		graph = graph.toGraph(graph.edges(tags));
		return this;
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query edgesTaggedWithAny(String... tags){
		graph = graph.toGraph(graph.edgesTaggedWithAny(tags));
		return this;
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public Query edgesTaggedWithAll(String... tags){
		graph = graph.toGraph(graph.edgesTaggedWithAll(tags));
		return this;
	}
	
	/**
	 * Select subgraph containing edges that have the given attribute key defined, with any value.
	 * @param key
	 * @return
	 */
	public Query selectEdges(String attribute){
		graph = graph.toGraph(graph.selectEdges(attribute));
		return this;
	}
	
	/**
	 * Select subgraph contain edges that have the given attribute key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public Query selectEdges(String attribute, Object... values){
		graph = graph.toGraph(graph.selectEdges(attribute, values));
		return this;
	}
	
	/**
	 * Select subgraph containing nodes that have a given key defined, with any value.
	 * @param key
	 * @return
	 */
	public Query selectNodes(String attribute){
		graph = graph.toGraph(graph.selectNodes(attribute));
		return this;
	}
	
	/**
	 * Select subgraph containing nodes that have a given key with any value specified in the given values.
	 * @param key
	 * @return
	 */
	public Query selectNodes(String attribute, Object... values){
		graph = graph.toGraph(graph.selectNodes(attribute, values));
		return this;
	}
	
}
