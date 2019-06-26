package spgqlite.graph.attributes.tags;

import java.util.Set;

import spgqlite.graph.attributes.AttributedNode;

public class TaggedNode extends AttributedNode {
	
	protected Set<String> tags;
	
	public TaggedNode() {
		super();
	}
	
	public Set<String> tags(){
		return tags;
	}

}
