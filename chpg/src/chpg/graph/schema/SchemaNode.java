package chpg.graph.schema;

import chpg.graph.Node;

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
