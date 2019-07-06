package chpg.graph.algorithms;

import chpg.graph.Node;
import chpg.graph.PropertyGraph;

public class UniqueEntryExitGraph extends PropertyGraph {

	private Node entry;
	private Node exit;
	
	public UniqueEntryExitGraph(PropertyGraph graph, Node entry, Node exit) {
		super(graph);
		if(entry == null) {
			throw new IllegalArgumentException("Entry node cannot be null");
		}
		if(exit == null) {
			throw new IllegalArgumentException("Exit node cannot be null");
		}
		if(this.between(entry, exit).isEmpty()) {
			throw new IllegalArgumentException("Graph must contain a path from the given entry to the given exit node");
		}
		this.entry = entry;
		this.exit = exit;
	}
	
	/**
	 * Returns the master entry node
	 */
	public Node getEntryNode() {
		return entry;
	}

	/**
	 * Returns the master exit node
	 */
	public Node getExitNode() {
		return exit;
	}
	
}
