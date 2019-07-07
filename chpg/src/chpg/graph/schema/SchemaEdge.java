package chpg.graph.schema;

import chpg.graph.Edge;

public class SchemaEdge extends Edge {

	public SchemaEdge(SchemaNode from, SchemaNode to) {
		super(from, to);
	}

	@Override
	public String toString() {
		return "SchemaEdge [from=" + from() + ", to=" + to() + "]";
	}
	
}
