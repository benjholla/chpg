package chpg.graph.schema;

import java.util.Objects;

import chpg.graph.Node;

public class SchemaNode extends Node {

	public SchemaNode(String tagName) {
		super(tagName);
	}
	
	public String getTagName() {
		return getName();
	}
	
	@Override
	public String toString() {
		return "SchemaNode [name=" + getName() + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SchemaNode))
			return false;
		SchemaNode other = (SchemaNode) obj;
		return Objects.equals(getName(), other.getName());
	}
	
}
