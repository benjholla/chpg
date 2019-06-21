package spgqlite.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GraphElementHashSet<E extends GraphElement> extends GraphElementSet<E> implements Iterable<E> {

	private Set<E> set;

	public GraphElementHashSet() {
		super();
	}

	public GraphElementHashSet(Iterable<E> iterable) {
		super(iterable);
	}

	public GraphElementHashSet(E element) {
		super(element);
	}
	
	public GraphElementHashSet(E[] elements) {
		super(elements);
	}

	@Override
	public int size() {
		if(set == null) {
			return 0;
		} else {
			return set.size();
		}
	}
	
	@Override
	public boolean isEmpty() {
		if(set == null) {
			return true;
		} else {
			return set.isEmpty();
		}
	}
	
	/**
	 * Adds the specified non-null element to this set if it is not already present.
	 * More formally, adds the specified element e to this set if this set contains no
	 * element e2 such that Objects.equals(e, e2). If this set already contains the
	 * element, the call leaves the set unchanged and returns false.
	 */
	public boolean add(E e) {
		requireNonNullGraphElement(e);
		if(set == null) {
			set = new HashSet<E>();
		}
		return set.add(e);
	}
	
	/**
	 * Removes an element from the graph element set
	 * @param e
	 * @return
	 */
	public boolean remove(E e) {
		requireNonNullGraphElement(e);
		if(set != null) {
			boolean modified = set.remove(e);
			if(set.isEmpty()) {
				set = null;
			}
			return modified;
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		if(set == null) {
			return Collections.emptyIterator();
		} else {
			return set.iterator();
		}
	}

	@Override
	public E one(){
		Iterator<E> iterator = this.iterator();
		if(iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}
	
	@Override
	public void clear() {
		set = null;
	}

	@Override
	public boolean contains(E e) {
		requireNonNullGraphElement(e);
		if(set != null) {
			return set.contains(e);
		} else {
			return false;
		}
	}

	@Override
	public boolean retainAll(GraphElementCollection<E> collection) {
		if(set == null) {
			return false;
		} else {
			boolean modified = false;
	        Iterator<E> iterator = set.iterator();
	        while (iterator.hasNext()) {
	            if (!collection.contains(iterator.next())) {
	            	iterator.remove();
	                modified = true;
	            }
	        }
	        if(set.isEmpty()) {
            	set = null;
            }
	        return modified;
		}
	}

	/**
	 * Returns a shallow copy of the graph element set as a <code>java.util.Set</code>
	 */
	@Override
	public Set<E> toStandardSet() {
		if(set != null) {
			return new HashSet<E>(set);
		} else {
			return new HashSet<E>();
		}
	}
	
	private void requireNonNullGraphElement(E element) {
		if(element == null) {
			throw new IllegalArgumentException("Graph element cannot be null!");
		}
	}

}
