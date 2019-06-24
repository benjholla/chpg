package spgqlite.graph.property;

import java.util.HashSet;
import java.util.Set;

import spgqlite.graph.GraphElement;

public abstract class PropertyGraphElement extends GraphElement {
	
	private Set<String> tags;
	
	protected PropertyGraphElement() {
		super();
		this.tags = new HashSet<String>();
	}
	
	public Set<String> tags(){
		return tags;
	}
	
}
