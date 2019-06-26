package spgqlite.graph.attributes.tags.hierarchy;

import spgqlite.graph.Edge;
import spgqlite.graph.Graph;
import spgqlite.graph.Node;

public class SchemaGraph extends Graph {
	
	public class SchemaNode extends Node {
		private String name;
		
		public SchemaNode(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public class SchemaEdge extends Edge {
		public SchemaEdge(SchemaNode from, SchemaNode to) {
			super(from, to);
		}
	}

}
