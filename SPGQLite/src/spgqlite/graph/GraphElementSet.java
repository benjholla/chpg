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
	
	/**
	 * A convenience method for elementsTaggedWithAny(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<E> elements(String... tags){
		return elementsTaggedWithAny(tags);
	}
	
	/**
	 * Returns the set of elements from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<E> elementsTaggedWithAny(String... tags){
		GraphElementSet<E> result = new GraphElementHashSet<E>();
		for(E e : this){
			for(String tag : tags){
				if(e.tags().contains(tag)){
					result.add(e);
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the set of elements from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<E> elementsTaggedWithAll(String... tags){
		GraphElementSet<E> result = new GraphElementHashSet<E>();
		for(E e : this){
			boolean add = true;
			for(String tag : tags){
				if(!e.tags().contains(tag)){
					add = false;
					break;
				}
			}
			if(add){
				result.add(e);
			}
		}
		return result;
	}
	
	/**
	 * Returns a graph element set filtered to elements with the attribute key and value
	 * @param attribute
	 * @param value
	 * @return
	 */
	public GraphElementSet<E> filter(String attribute, Object value){
		GraphElementSet<E> result = new GraphElementHashSet<E>();
		if(attribute != null && value != null){
			for(E e : this){
				if(e.hasAttr(attribute) 
					&& e.attributes().get(attribute) != null 
					&& e.attributes().get(attribute).equals(value)){
					result.add(e);
				}
			}
		}
		return result;
	}
	
}
