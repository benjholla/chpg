package spgqlite.graph.attributes;

import spgqlite.graph.GraphElementHashSet;
import spgqlite.graph.GraphElementSet;

public abstract class AttributedGraphElementSet<E extends AttributedGraphElement> extends GraphElementSet<E> {

	public AttributedGraphElementSet() {}

	public AttributedGraphElementSet(Iterable<E> iterable) {
		addAll(iterable);
	}

	public AttributedGraphElementSet(E element) {
		add(element);
	}

	public AttributedGraphElementSet(E[] elements) {
		for(E element : elements) {
			add(element);
		}
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
