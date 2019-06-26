package spgqlite.graph.attributes;

import java.util.Map;

public interface Attributed {

	public Map<String,Object> attributes();
	
	public boolean hasAttr(String name);
	
	public Object putAttr(String name, Object value);
	
	public Object getAttr(String name);
	
	public Object removeAttr(String name);
	
}
