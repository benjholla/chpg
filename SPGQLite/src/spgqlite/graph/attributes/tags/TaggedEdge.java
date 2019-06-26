package spgqlite.graph.attributes.tags;

import java.util.Set;

import spgqlite.graph.Node;
import spgqlite.graph.attributes.AttributedEdge;

public class TaggedEdge extends AttributedEdge {

	protected Set<String> tags;
	
	public TaggedEdge(Node from, Node to) {
		super(from, to);
	}
	
	public Set<String> tags(){
		return tags;
	}
	
}
