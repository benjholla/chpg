package spgqlite.graph;

public abstract class GraphElementSet<E extends GraphElement> extends GraphElementCollection<E> {

	public GraphElementSet() {}

	public GraphElementSet(Iterable<E> iterable) {
		addAll(iterable);
	}

	public GraphElementSet(E element) {
		add(element);
	}
	
}
