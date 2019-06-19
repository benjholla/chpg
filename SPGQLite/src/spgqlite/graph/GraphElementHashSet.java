package spgqlite.graph;

import java.util.Set;

public class GraphElementHashSet<E extends GraphElement> extends GraphElementSet<E> implements Set<E>, Iterable<E> {

	private static final long serialVersionUID = 1L;

	public GraphElementHashSet() {
		super();
	}

	public GraphElementHashSet(GraphElementSet<E> set) {
		super(set);
	}
	
	public GraphElementHashSet(E element) {
		super();
		add(element);
	}
	
	@Override
	public E one(){
		for(E e : this){
			return e;
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
}
