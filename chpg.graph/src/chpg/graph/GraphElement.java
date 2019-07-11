package chpg.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class GraphElement {
	
	private static Integer addresses = 0;
	private Integer address;
	
	// name is a first class property
	private String name;
	
	// tags denote set member ship and have a hierarchy defined by the graph schema
	private Set<String> tags;
	
	// attributes define specialized graph properties
	private Map<String,Object> attributes;
	
	protected GraphElement() {
		this.address = addresses++;
		this.tags = new HashSet<String>();
		this.attributes = new HashMap<String,Object>();
	}
	
	/**
	 * Creates a new graph element with the given name
	 * @param name
	 */
	protected GraphElement(String name) {
		this();
		this.name = name;
	}
	
	/**
	 * Returns true if a name is assigned to the graph element
	 * @return
	 */
	public boolean hasName() {
		return name != null;
	}
	
	/**
	 * Returns the graph element name or null if a name is not defined
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> tags(){
		return tags;
	}
	
	public Map<String,Object> attributes(){
		return attributes;
	}
	
	public boolean hasAttr(String name) {
		return attributes.containsKey(name);
	}
	
	public Object putAttr(String name, Object value) {
		return attributes.put(name, value);
	}
	
	public Object getAttr(String name) {
		return attributes.get(name);
	}
	
	public Object removeAttr(String name) {
		return attributes.remove(name);
	}
	
	public Integer getAddress() {
		return address;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(address);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GraphElement))
			return false;
		GraphElement other = (GraphElement) obj;
		return Objects.equals(address, other.address);
	}
	
}
