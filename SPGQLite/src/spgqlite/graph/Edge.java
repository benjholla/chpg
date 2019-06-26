package spgqlite.graph;

public class Edge extends GraphElement {

	protected Node from;
	protected Node to;
	
	public Edge(Node from, Node to) {
		super();
		this.from = from;
		this.to = to;
	}

	public Node from() {
		return from;
	}

	public Node to() {
		return to;
	}

	@Override
	public String toString() {
		return "Edge [\n\tFrom " + from + "\n\tTo " + to + "\n]";
	}
	
}
