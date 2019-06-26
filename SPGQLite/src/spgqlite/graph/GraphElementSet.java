package spgqlite.graph;

import java.util.Collection;

public abstract class GraphElementSet<E extends GraphElement> extends GraphElementCollection<E> {

	public GraphElementSet() {}

	public GraphElementSet(Iterable<E> iterable) {
		addAll(iterable);
	}

	public GraphElementSet(E element) {
		add(element);
	}

	public GraphElementSet(E[] elements) {
		for(E element : elements) {
			add(element);
		}
	}
	
	/**
	 * Returns this collection as a standard set type
	 * @return
	 */
	public abstract Collection<E> toStandardSet();
	
	/**
	 * Returns this collection as a standard collection type
	 * @return
	 */
	public Collection<E> toStandardCollection(){
		return toStandardSet();
	}
	
}
