package spgqlite.graph.attributes;

import java.util.Map;

import spgqlite.graph.Edge;
import spgqlite.graph.Node;

public class AttributedEdge extends Edge implements Attributed {
	
	protected Map<String,Object> attributes;

	public AttributedEdge(Node from, Node to) {
		super(from, to);
	}

	@Override
	public String toString() {
		return "Edge [\n\tFrom " + from + "\n\tTo " + to + "\n\tAttributes: " + this.attributes().toString() + "\n]";
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

}
