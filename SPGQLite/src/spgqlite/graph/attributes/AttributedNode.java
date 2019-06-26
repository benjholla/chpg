package spgqlite.graph.attributes;

import java.util.Map;

import spgqlite.graph.Node;

public class AttributedNode extends Node implements Attributed {

	protected Map<String,Object> attributes;
	
	public AttributedNode() {
		super();
	}
	
	public Map<String,Object> attributes(){
		return attributes;
	}
	
	public boolean hasAttr(String name) {
		return attributes.containsKey(name);
	}
	
	public Object putAttr(String name, Object value) {
		return attributes.put(name, value);
	}
	
	public Object getAttr(String name) {
		return attributes.get(name);
	}
	
	public Object removeAttr(String name) {
		return attributes.remove(name);
	}
	
	@Override
	public String toString() {
		return "Node [Attributes: " + this.attributes().toString() + "]";
	}
	
}