package spgqlite.graph;

import java.util.HashSet;
import java.util.Set;

public abstract class GraphElementSet<E extends GraphElement> extends HashSet<E> implements Set<E>, Iterable<E> {

	private static final long serialVersionUID = 1L;

	public GraphElementSet() {
		super();
	}

	public GraphElementSet(GraphElementSet<E> set) {
		super(set);
	}
	
	public GraphElementSet(E element) {
		super();
		add(element);
	}
	
	/**
	 * Returns one item from the set
	 * @return
	 */
	public abstract E one();
	
	/**
	 * Adds the specified non-null element to this set if it is not already present.
	 * More formally, adds the specified element e to this set if this set contains no
	 * element e2 such that Objects.equals(e, e2). If this set already contains the
	 * element, the call leaves the set unchanged and returns false.
	 */
	public boolean add(E e) {
		if(e == null) {
			throw new RuntimeException("Graph element cannot be null!");
		} else {
			return super.add(e);
		}
	}
	
	/**
	 * Returns a graph element set filtered to elements with the attribute key and value
	 * @param attr
	 * @param value
	 * @return
	 */
	public abstract GraphElementSet<E> filter(String attr, Object value);
	
}
