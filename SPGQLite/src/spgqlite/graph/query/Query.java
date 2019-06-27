package spgqlite.graph.query;

import spgqlite.graph.Graph;

public class Query {

	private Graph graph;
	
	public Query(Graph graph) {
		this.graph = graph;
	}
	
	public Graph evaluate() {
		return graph;
	}
	
	public Query forward(Query origin) {
		graph = graph.forward(origin.evaluate());
		return this;
	}
	
}
