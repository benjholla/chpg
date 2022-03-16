package chpg.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import chpg.graph.Node.NodeDirection;

public abstract class AbstractGraph implements Graph {

	protected GraphElementSet<Node> nodes;
	protected GraphElementSet<Edge> edges;
	
	protected AbstractGraph() {
		this.nodes = new GraphElementHashSet<Node>();
		this.edges = new GraphElementHashSet<Edge>();
	}
	
	/**
	 * Gets incoming edges to node
	 * 
	 * @param node
	 * @return The set of incoming edges to the given node
	 */
	protected GraphElementSet<Edge> getInEdgesToNode(Node node){
		GraphElementSet<Edge> inEdges = new GraphElementHashSet<Edge>();
		for(Edge edge : edges()){
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
	protected GraphElementSet<Edge> getOutEdgesFromNode(Node node){
		GraphElementSet<Edge> outEdges = new GraphElementHashSet<Edge>();
		for(Edge edge : edges()){
			if(edge.from().equals(node)){
				outEdges.add(edge);
			}
		}
		return outEdges;
	}
	
	@Override
	public Graph toGraph(Node... nodes) {
		Graph result = empty();
		for(Node node : nodes) {
			result.add(node);
		}
		return result;
	}
	
	@Override
	public Graph toGraph(Edge... edges) {
		Graph result = empty();
		for(Edge edge : edges) {
			result.add(edge);
		}
		return result;
	}
	
	@Override
	public Graph toGraph(GraphElementSet<Node> nodes, GraphElementSet<Edge> edges) {
		Graph result = empty();
		result.addAll(nodes);
		result.addAll(edges);
		return result;
	}
	
	@Override
	public Graph toGraph(GraphElementSet<? extends GraphElement> graphElements) {
		Graph result = empty();
		result.addAll(graphElements);
		return result;
	}
	
	@Override
	public Graph toGraph(Graph... graphs) {
		Graph result = empty();
		for(Graph graph : graphs) {
			result.addAll(graph.nodes());
			result.addAll(graph.edges());
		}
		return result;
	}
	
	@Override
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
	
	@Override
	public Node getNodeByAddress(int address) {
		for(Node node : nodes()) {
			if(node.getAddress() == address) {
				return node;
			}
		}
		return null;
	}
	
	@Override
	public Edge getEdgeByAddress(int address) {
		for(Edge edge : edges()) {
			if(edge.getAddress() == address) {
				return edge;
			}
		}
		return null;
	}
	
	@Override
	public abstract Graph empty();

	@Override
	public boolean add(GraphElement graphElement) {
		boolean result = false;
		if(graphElement instanceof Node) {
			Node node = (Node) graphElement;
			result |= this.nodes().add(node);
		} else if(graphElement instanceof Edge) {
			Edge edge = (Edge) graphElement;
			result |= this.edges().add(edge);
			result |= this.nodes().add(edge.from());
			result |= this.nodes().add(edge.to());
		}
		return result;
	}
	
	@Override
	public boolean addAll(Iterable<? extends GraphElement> graphElements) {
		boolean result = false;
		for(GraphElement graphElement : graphElements) {
			result |= add(graphElement);
		}
		return result;
	}
	
	@Override
	public boolean remove(GraphElement graphElement) {
		if(graphElement instanceof Edge) {
			Edge edge = (Edge) graphElement;
			return edges().remove(edge);
		} else {
			boolean result = false;
			Node node = (Node) graphElement;
			result |= nodes().remove(node);
			Iterator<Edge> edgeIterator = edges().iterator();
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

	@Override
	public GraphElementSet<Node> nodes() {
		return nodes;
	}

	@Override
	public GraphElementSet<Edge> edges() {
		return edges;
	}
	
	@Override
	public boolean isEmpty() {
		return nodes.isEmpty();
	}
	
	@Override
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
	
	@Override
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
	
	@Override
	public GraphElementSet<Node> leaves(){
		return limit(NodeDirection.OUT);
	}
	
	@Override
	public GraphElementSet<Node> roots(){
		return limit(NodeDirection.IN);
	}
	
	@Override
	public GraphElementSet<Node> predecessors(Node... origin){
		return predecessors(new GraphElementHashSet<Node>(origin));
	}
	
	@Override
	public GraphElementSet<Node> predecessors(Graph origin){
		return predecessors(origin.nodes());
	}
	
	@Override
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
	
	@Override
	public GraphElementSet<Node> successors(Node... origin){
		return successors(new GraphElementHashSet<Node>(origin));
	}
	
	@Override
	public GraphElementSet<Node> successors(Graph origin){
		return successors(origin.nodes());
	}
	
	@Override
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
	
	@Override
	public Graph forwardStep(Node... origin){
		return forwardStep(new GraphElementHashSet<Node>(origin));
	}
	
	@Override
	public Graph forwardStep(Graph origin){
		Graph result = empty();
		result.addAll(origin.nodes());
		for(Node node : origin.nodes()){
			GraphElementSet<Edge> outEdges = getOutEdgesFromNode(node);
			for(Edge edge : outEdges){
				result.nodes().add(edge.from());
				result.nodes().add(edge.to());
				result.edges().add(edge);
			}
		}
		return result.union(origin);
	}
	
	@Override
	public Graph forwardStep(GraphElementSet<Node> origin){
		return forwardStep(this.toGraph(origin));
	}
	
	@Override
	public Graph reverseStep(Node... origin){
		return reverseStep(new GraphElementHashSet<Node>(origin));
	}
	
	@Override
	public Graph reverseStep(Graph origin){
		Graph result = empty();
		result.addAll(origin.nodes());
		for(Node node : origin.nodes()){
			GraphElementSet<Edge> inEdges = getInEdgesToNode(node);
			for(Edge edge : inEdges){
				result.nodes().add(edge.from());
				result.nodes().add(edge.to());
				result.edges().add(edge);
			}
		}
		return result.union(origin);
	}
	
	@Override
	public Graph reverseStep(GraphElementSet<Node> origin){
		return reverseStep(this.toGraph(origin));
	}
	
	@Override
	public Graph union(Node... nodes){
		return union(toGraph(nodes));
	}
	
	@Override
	public Graph union(Edge... edges){
		return union(toGraph(edges));
	}
	
	@Override
	public Graph union(Graph... graphs){
		// union operations commute, so we order all graphs including this graph
		// by largest to smallest so that we start with the largest set and minimize add operations
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		sortedGraphs.add(this);
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR.reversed());
		Graph initial = sortedGraphs.remove(0);
		
		Graph union = toGraph(initial.nodes(), initial.edges());
		for(Graph graph : sortedGraphs){
			union.nodes().addAll(graph.nodes());
			union.edges().addAll(graph.edges());
		}
		return union;
	}
	
	@Override
	public Graph difference(Node... nodes){
		return difference(toGraph(nodes));
	}
	
	@Override
	public Graph difference(Edge... edges){
		return difference(toGraph(edges));
	}
	
	@Override
	public Graph difference(Graph... graphs){
		// sorting the graphs to difference from this graph by largest to smallest
		// in order to remove the most information up front
		// note that this ordering does not include this graph because difference
		// operations do not commute (the given graphs are effectively a union)
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR.reversed());
		
		Graph difference = toGraph(this.nodes(), this.edges());
		for(Graph graph : sortedGraphs){
			if(difference.isEmpty()) {
				break;
			}
			// traverse the current result graph to discover the edges that should be removed as
			// a result of removing nodes in the given graph
			GraphElementSet<Edge> incomingEdges = difference.reverseStep(graph.nodes()).edges();
			GraphElementSet<Edge> outgoingEdges = difference.forwardStep(graph.nodes()).edges();
			
			// remove nodes from given graph
			difference.nodes().removeAll(graph.nodes());
			
			// remove edges from given graph, including edges incoming and outgoing from removed nodes
			difference.edges().removeAll(graph.edges());
			difference.edges().removeAll(incomingEdges);
			difference.edges().removeAll(outgoingEdges);
		}
		return difference;
	}
	
	@Override
	public Graph differenceEdges(Edge... edges){
		return differenceEdges(toGraph(edges));
	}
	
	@Override
	public Graph differenceEdges(Graph... graphs){
		// sorting the graphs to difference from this graph by largest to smallest
		// in order to remove the most information up front
		// note that this ordering does not include this graph because difference
		// operations do not commute (the given graphs are effectively a union)
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR.reversed());
		
		Graph difference = toGraph(this.nodes(), this.edges());
		for(Graph graph : sortedGraphs){
			if(difference.edges().isEmpty()) {
				break;
			}
			difference.edges().removeAll(graph.edges());
		}
		return difference;
	}
	
	@Override
	public Graph intersection(Node... nodes){
		return intersection(toGraph(nodes));
	}
	
	@Override
	public Graph intersection(Edge... edges){
		return intersection(toGraph(edges));
	}
	
	@Override
	public Graph intersection(Graph... graphs){
		// intersections commute, so we order the given graphs including this graph 
		// by the smallest to largest graph in order to start with the smallest set
		// and minimize retain operations
		ArrayList<Graph> sortedGraphs = new ArrayList<Graph>(Arrays.asList(graphs));
		sortedGraphs.add(this);
		Collections.sort(sortedGraphs, GRAPH_SIZE_COMPARATOR);
		Graph initial = sortedGraphs.remove(0);
		
		Graph intersection = toGraph(initial.nodes(), initial.edges());
		for(Graph graph : sortedGraphs){
			if(intersection.isEmpty()) {
				break;
			}
			intersection.nodes().retainAll(graph.nodes());
			intersection.edges().retainAll(graph.edges());
		}
		return intersection;
	}
	
	@Override
	public Graph betweenStep(Node from, Node to){
		return betweenStep(new GraphElementHashSet<Node>(from), new GraphElementHashSet<Node>(to));
	}
	
	@Override
	public Graph betweenStep(Graph from, Graph to){
		return betweenStep(from.nodes(), to.nodes());
	}
	
	@Override
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
	
	@Override
	public Graph between(Node from, Node to) {
		return between(new GraphElementHashSet<Node>(from), new GraphElementHashSet<Node>(to));
	}
	
	@Override
	public Graph between(Graph from, Graph to) {
		return between(from.nodes(), to.nodes());
	}
	
	@Override
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

	@Override
	public Graph forward(Node... origin){
		return forward(new GraphElementHashSet<Node>(origin));
	}
	
	@Override
	public Graph forward(Graph origin){
		Graph result = empty();
		result.nodes().addAll(origin.nodes());
		GraphElementSet<Node> frontier = new GraphElementHashSet<Node>(origin.nodes());
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
		return result.union(origin);
	}
	
	@Override
	public Graph forward(GraphElementSet<Node> origin){
		return forward(this.toGraph(origin));
	}
	
	@Override
	public Graph reverse(Node... origin){
		return reverse(new GraphElementHashSet<Node>(origin));
	}
	
	@Override
	public Graph reverse(Graph origin){
		Graph result = empty();
		result.nodes().addAll(origin.nodes());
		GraphElementSet<Node> frontier = new GraphElementHashSet<Node>(origin.nodes());
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
		return result.union(origin);
	}
	
	@Override
	public Graph reverse(GraphElementSet<Node> origin){
		return reverse(this.toGraph(origin));
	}
	
	@Override
	public Graph induce(Edge... edges){
		return induce(new GraphElementHashSet<Edge>(edges));
	}
	
	@Override
	public Graph induce(Graph... graphs){
		GraphElementSet<Edge> inducibleEdges = new GraphElementHashSet<Edge>();
		for(Graph graph : graphs){
			inducibleEdges.addAll(graph.edges());
		}
		return induce(inducibleEdges);
	}
	
	@Override
	public Graph induce(GraphElementSet<Edge> edges){
		Graph result = toGraph(this);
		for(Edge edge : edges) {
			if(result.nodes().contains(edge.from()) && result.nodes().contains(edge.to())) {
				result.edges().add(edge);
			}
		}
		return result;
	}
	
	@Override
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
	
	@Override
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
	
	@Override
	public GraphElementSet<Node> nodes(String... tags){
		throw new RuntimeException("Operation not implemented for " + this.getClass().getName());
	}
	
	@Override
	public GraphElementSet<Node> nodesTaggedWithAny(String... tags){
		throw new RuntimeException("Operation not implemented for " + this.getClass().getName());
	}
	
	@Override
	public GraphElementSet<Node> nodesTaggedWithAll(String... tags){
		throw new RuntimeException("Operation not implemented for " + this.getClass().getName());
	}
	
	@Override
	public GraphElementSet<Edge> edges(String... tags){
		throw new RuntimeException("Operation not implemented for " + this.getClass().getName());
	}
	
	@Override
	public GraphElementSet<Edge> edgesTaggedWithAny(String... tags){
		throw new RuntimeException("Operation not implemented for " + this.getClass().getName());
	}
	
	@Override
	public GraphElementSet<Edge> edgesTaggedWithAll(String... tags){
		throw new RuntimeException("Operation not implemented for " + this.getClass().getName());
	}
	
}

