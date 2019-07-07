package chpg.graph.schema;

import chpg.graph.Node;

public class SchemaNode extends Node {

	public SchemaNode(String tagName) {
		super(tagName);
	}
	
	public String getTagName() {
		return getName();
	}
	
}
