package spgqlite.graph;

import java.util.Objects;

public abstract class GraphElement {
	
	private static Integer addresses = 0;
	private Integer address;
	
	protected GraphElement() {
		this.address = addresses++;
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
