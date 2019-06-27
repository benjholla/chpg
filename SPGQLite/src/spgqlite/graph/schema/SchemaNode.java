package spgqlite.graph.schema;

import spgqlite.graph.Node;

public class SchemaNode extends Node {

	private String tagName;
	
	public SchemaNode(String tagName) {
		super();
		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}
	
}
