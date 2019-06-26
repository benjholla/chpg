package spgqlite.graph.attributes.tags;

import spgqlite.graph.Edge;
import spgqlite.graph.GraphElementHashSet;
import spgqlite.graph.GraphElementSet;
import spgqlite.graph.Node;
import spgqlite.graph.attributes.AttributedGraph;

public class TaggedGraph extends AttributedGraph {

	/**
	 * A convenience method for nodes(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodes(String... tags){
		return nodesTaggedWithAny(tags);
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAny(String... tags){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes){
			if(node instanceof Tagged) {
				Tagged tagged = (Tagged) node;
				for(String tag : tags){
					if(tagged.tags().contains(tag)){
						result.add(node);
						break;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the set of nodes from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Node> nodesTaggedWithAll(String... tags){
		GraphElementSet<Node> result = new GraphElementHashSet<Node>();
		for(Node node : nodes){
			if(node instanceof Tagged) {
				Tagged tagged = (Tagged) node;
				boolean add = true;
				for(String tag : tags){
					if(!tagged.tags().contains(tag)){
						add = false;
						break;
					}
				}
				if(add){
					result.add(node);
				}
			}
		}
		return result;
	}
	
	/**
	 * A convenience method for edges(String... tags)
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edges(String... tags){
		return edgesTaggedWithAny(tags);
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with any of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAny(String... tags){
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			if(edge instanceof Tagged) {
				Tagged tagged = (Tagged) edge;
				for(String tag : tags){
					if(tagged.tags().contains(tag)){
						result.add(edge);
						break;
					}
				}
			}			
		}
		return result;
	}
	
	/**
	 * Returns the set of edges from this graph that are tagged with all of the
	 * given tags
	 * 
	 * @param tags
	 * @return
	 */
	public GraphElementSet<Edge> edgesTaggedWithAll(String... tags){
		GraphElementSet<Edge> result = new GraphElementHashSet<Edge>();
		for(Edge edge : edges){
			if(edge instanceof Tagged) {
				Tagged tagged = (Tagged) edge;
				boolean add = true;
				for(String tag : tags){
					if(!tagged.tags().contains(tag)){
						add = false;
						break;
					}
				}
				if(add){
					result.add(edge);
				}
			}
		}
		return result;
	}
	
}
