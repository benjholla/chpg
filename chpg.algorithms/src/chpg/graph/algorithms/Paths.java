package chpg.graph.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import chpg.graph.Edge;
import chpg.graph.Graph;
import chpg.graph.GraphElementHashSet;
import chpg.graph.GraphElementSet;
import chpg.graph.Node;

public class Paths {

	/**
	 * Helper method to enumerate all non-cyclic paths in a given graph from the given from and to nodes
	 * @param graph
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<List<Edge>> enumeratePaths(Graph graph, Node from, Node to, boolean allowRevisits){
		return enumeratePaths(graph, from, new GraphElementHashSet<Node>(to), allowRevisits);
	}
	
	/**
	 * Helper method to enumerate all non-cyclic paths in a given graph from the given from and to nodes
	 * @param graph
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<List<Edge>> enumeratePaths(Graph graph, Node from, GraphElementSet<Node> toSet, boolean allowRevisits){
		// DFS enumerate all paths
		List<List<Edge>> paths = new ArrayList<List<Edge>>();
		// check if there exists at least one path between the nodes
		// this also serves to efficiently restrict the scope of the path enumeration
		graph = graph.between(new GraphElementHashSet<Node>(from), toSet);
		
		if(!graph.isEmpty()) {
			// stack for depth first search (DFS)
			Stack<SearchEdge> stack = new Stack<SearchEdge>();
			
			// network is not a DAG so we will need to prevent cycles
			Set<Edge> visited = new HashSet<Edge>();
			
			// start searching from the from node
			for(Edge edge : graph.forwardStep(from).edges()) {
				stack.push(new SearchEdge(edge, 0));
			}
			
			// keep track of the path as we walk it
			// the history is also used to store the prefixes of the next paths
			// so that we do not need to recompute the prefixes of the subsequent paths
			List<Edge> history = new ArrayList<Edge>();
			
			// enumerate all paths to the to node from the from node
			while (!stack.isEmpty()) {
				SearchEdge current = stack.pop();
				visited.add(current.getEdge());
				
				// trim the nodes from the history that are not part of the next path
				history = history.subList(0, current.getDepth());
				
				// add the current node to our path history
				history.add(current.getEdge());
				
				if(toSet.contains(current.getEdge().to())) {
					// since we the to node we know we have a new path
					// update the history
					List<Edge> path = new ArrayList<Edge>(history);
					
					// add the complete path
					paths.add(path);
				} else {
					for(Edge outgoingEdge : graph.forwardStep(current.getEdge().to()).edges()) {
						if(allowRevisits || !visited.contains(outgoingEdge)) {
							// push the child node on the stack to be processed
							stack.push(new SearchEdge(outgoingEdge, current.getDepth()+1));
						}
					}
				}
			}
		}
		return paths;
	}
	
	/**
	 * Helper class to keep track of the node and where the node was in the graph
	 */
	private static class SearchEdge {
		private int depth;
		private Edge edge;
		
		public SearchEdge(Edge edge, int depth) {
			this.edge = edge;
			this.depth = depth;
		}
		
		public int getDepth() {
			return depth;
		}
		
		public Edge getEdge() {
			return edge;
		}

		@Override
		public String toString() {
			return "SearchEdge [depth=" + depth + ", edge=" + edge + "]";
		}
	}
	
}
