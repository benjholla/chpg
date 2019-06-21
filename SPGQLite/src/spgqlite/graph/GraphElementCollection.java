package spgqlite.graph;

import java.util.Collection;
import java.util.Iterator;

public abstract class GraphElementCollection<E extends GraphElement> implements Iterable<E> {
	
	/**
	 * Returns this collection as a standard collection type
	 * @return
	 */
	public abstract Collection<E> toStandardCollection();
	
	/**
	 * Returns the size of the collection
	 * @return
	 */
	public abstract int size();
	
	/**
	 * Returns true if the collection is empty, false otherwise
	 * @return
	 */
	public abstract boolean isEmpty();

	/**
	 * Returns one item from the collection of null if there is none
	 * @return
	 */
	public abstract E one();
	
	/**
	 * Adds the specified non-null element to this collection if it is not already present.
	 * More formally, adds the specified element e to this collection if this collection contains no
	 * element e2 such that Objects.equals(e, e2). If this collection already contains the
	 * element, the call leaves the collection unchanged and returns false.
	 */
	public abstract boolean add(E e);
	
	/**
	 * Adds the iterable elements to the graph element collection
	 * @param iterable
	 * @return
	 */
	public boolean addAll(Iterable<E> iterable) {
		boolean result = false;
		Iterator<E> iterator = iterable.iterator();
		while(iterator.hasNext()) {
			E e = iterator.next();
			result |= add(e);
		}
		return result;
	}
	
	/**
	 * Removes an element from the graph element collection
	 * @param e
	 * @return
	 */
	public abstract boolean remove(E e);
	
	/**
	 * Removes the iterable elements from the graph element collection
	 * @param e
	 * @return
	 */
	public boolean removeAll(Iterable<E> iterable) {
		boolean result = false;
		Iterator<E> iterator = iterable.iterator();
		while(iterator.hasNext()) {
			E e = iterator.next();
			result |= remove(e);
		}
		return result;
	}
	
	/**
	 * Removes all of the elements from this collection. The collection will be
	 * empty after this call returns.
	 */
	public abstract void clear();
	
	/**
	 * Returns true if this collection contains the specified element. More formally,
	 * returns true if and only if this collection contains an element e such that
	 * Objects.equals(o, e).
	 * 
	 * @param e
	 * @return
	 */
	public abstract boolean contains(E e);
	
	/**
	 * Returns true if this collection contains all of the elements of the specified
	 * collection.
	 * 
	 * @param iterable
	 * @return
	 */
	public boolean containsAll(Iterable<E> iterable) {
		for (E e : iterable) {
			if (!contains(e)) {
                return false;
			}
		}
        return true;
	}
	
	/**
	 * Retains only the elements in this collection that are contained in the specified
	 * collection (optional operation). In other words, removes from this collection all of
	 * its elements that are not contained in the specified collection. If the
	 * specified collection is also a collection, this operation effectively modifies this
	 * collection so that its value is the intersection of the two collections.
	 * 
	 * @param collection
	 * @return
	 */
	public abstract boolean retainAll(GraphElementCollection<E> collection);
	
}
