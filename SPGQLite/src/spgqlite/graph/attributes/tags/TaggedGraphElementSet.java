package spgqlite.graph.attributes.tags;

import spgqlite.graph.GraphElementSet;

public abstract class TaggedGraphElementSet<E extends TaggedGraphElement> extends GraphElementSet<E> {

	public TaggedGraphElementSet() {}

	public TaggedGraphElementSet(Iterable<E> iterable) {
		addAll(iterable);
	}

	public TaggedGraphElementSet(E element) {
		add(element);
	}

	public TaggedGraphElementSet(E[] elements) {
		for(E element : elements) {
			add(element);
		}
	}
	
}
