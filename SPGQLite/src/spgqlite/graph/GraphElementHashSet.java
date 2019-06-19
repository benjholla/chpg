package spgqlite.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class GraphElementHashSet<E extends GraphElement> extends GraphElementSet<E> implements Iterable<E> {

	private HashSet<E> set;

	public GraphElementHashSet() {}

	public GraphElementHashSet(Iterable<E> iterable) {
		super(iterable);
	}

	public GraphElementHashSet(E element) {
		super(element);
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
		if(e == null) {
			throw new RuntimeException("Graph element cannot be null!");
		} else {
			if(set == null) {
				set = new HashSet<E>();
			}
			return set.add(e);
		}
	}
	
	/**
	 * Removes an element from the graph element set
	 * @param e
	 * @return
	 */
	public boolean remove(E e) {
		if(set != null) {
			return set.remove(e);
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
	public GraphElementSet<E> filter(String attr, Object value){
		GraphElementSet<E> result = new GraphElementHashSet<E>();
		if(attr != null && value != null){
			for(E e : this){
				if(e.hasAttr(attr) && e.attributes().get(attr) != null && e.attributes().get(attr).equals(value)){
					result.add(e);
				}
			}
		}
		return result;
	}

	@Override
	public void clear() {
		if(set != null) {
			set.clear();
		}
	}

	@Override
	public boolean contains(E e) {
		if(set != null) {
			return set.contains(e);
		} else {
			return false;
		}
	}

	@Override
	public boolean retainAll(GraphElementCollection<E> collection) {
		Objects.requireNonNull(collection);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
	}

	@Override
	public Collection<E> toStandardCollection() {
		if(set != null) {
			return set;
		} else {
			set = new HashSet<E>();
			return set;
		}
	}

}
