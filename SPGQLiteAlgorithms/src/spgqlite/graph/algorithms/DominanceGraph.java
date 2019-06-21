package spgqlite.graph.algorithms;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 *  This code is adapted from: https://svn.apache.org/repos/asf/flex/falcon/trunk/compiler/src/org/apache/flex/abc/graph/algorithms/DominatorTree.java (Revision 1840868)
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import spgqlite.graph.Edge;
import spgqlite.graph.Graph;
import spgqlite.graph.GraphElementHashSet;
import spgqlite.graph.GraphElementSet;
import spgqlite.graph.Node;


/**
 * An implementation of the O(n log n) Lengauer-Tarjan algorithm for building
 * the <a href="http://en.wikipedia.org/wiki/Dominator_%28graph_theory%29">dominator
 * tree</a> of a graph.
 */
public class DominanceGraph extends Graph {
	
	/**
	 * The immediate dominator or idom of a node n is the unique node that strictly
	 * dominates n but does not strictly dominate any other node that strictly
	 * dominates n. Every node, except the entry node, has an immediate dominator.
	 * Because the immediate dominator is unique, it is a tree. The start node is
	 * the root of the tree.
	 */
	public static final String DOMINATOR_TREE_EDGE = "idom";
	
	/**
	 * Used to tag the edges from a node that post-dominate a node.
	 * 
	 * Wikipedia: Analogous to the definition of dominance above, a node z is said
	 * to post-dominate a node n if all paths to the exit node of the graph starting
	 * at n must go through z. Similarly, the immediate post-dominator of a node n
	 * is the postdominator of n that doesn't strictly postdominate any other strict
	 * postdominators of n.
	 */
	public static final String POST_DOMINATOR_TREE_EDGE = "ipdom";
	
	/**
	 * Used to tag the edges from a node that identify the node's dominance
	 * frontier.
	 * 
	 * Wikipedia: The dominance frontier of a node d is the set of all nodes n
	 * such that d dominates an immediate predecessor of n, but d does not
	 * strictly dominate n. It is the set of nodes where d's dominance stops.
	 */
	public static final String DOMINANCE_FRONTIER_EDGE = "dom-frontier";
	
	/**
	 * Used to tag the edges from a node that identify the node's post-dominance
	 * frontier.
	 * 
	 * Wikipedia: The dominance frontier of a node d is the set of all nodes n
	 * such that d dominates an immediate predecessor of n, but d does not
	 * strictly dominate n. It is the set of nodes where d's dominance stops.
	 * Note that analogous to the definition of dominance above, a node z is said
	 * to post-dominate a node n if all paths to the exit node of the graph starting
	 * at n must go through z. Similarly, the immediate post-dominator of a node n
	 * is the postdominator of n that doesn't strictly postdominate any other strict
	 * postdominators of n.
	 */
	public static final String POST_DOMINANCE_FRONTIER_EDGE = "pdom-frontier";

	/**
	 * Unique Entry/Exit Graph to operate on
	 */
	private UniqueEntryExitGraph uniqueEntryExitGraph;
	
	/**
	 * If true edges in the graph should be interpreted as being in the reverse direction, 
	 * this is useful for computing post-dominance (https://en.wikipedia.org/wiki/Dominator_(graph_theory)#Postdominance)
	 */
	private boolean invertEdges = false;
	
	/**
	 * Semidominator numbers by block.
	 */
	private Map<Node, Integer> semi = new HashMap<Node, Integer>();

	/**
	 * Parents by block.
	 */
	private Map<Node, Node> parent = new HashMap<Node, Node>();

	/**
	 * Predecessors by block.
	 */
	private Multimap<Node> pred = new Multimap<Node>();

	/**
	 * Blocks in DFS order; used to look up a block from its semidominator
	 * numbering.
	 */
	private ArrayList<Node> vertex = new ArrayList<Node>();

	/**
	 * Blocks by semidominator block.
	 */
	private Multimap<Node> bucket = new Multimap<Node>();

	/**
	 * idominator map, built iteratively.
	 */
	private Map<Node, Node> idom = new HashMap<Node, Node>();

	/**
	 * Dominance frontiers of this dominator tree, built on demand.
	 */
	private Multimap<Node> dominanceFrontiers = null;

	/**
	 * Dominator tree, built on demand from the idominator map.
	 */
	private Multimap<Node> dominatorTree = null;

	/**
	 * Auxiliary data structure used by the O(m log n) eval/link implementation:
	 * ancestor relationships in the forest (the processed tree as it's built back
	 * up).
	 */
	private Map<Node, Node> ancestor = new HashMap<Node, Node>();

	/**
	 * Auxiliary data structure used by the O(m log n) eval/link implementation:
	 * node with least semidominator seen during traversal of a path from node to
	 * subtree root in the forest.
	 */
	private Map<Node, Node> label = new HashMap<Node, Node>();

	/**
	 * A topological traversal of the dominator tree, built on demand.
	 */
	private LinkedList<Node> topologicalTraversalImpl = null;
	
	/**
	 * Constructs a dominance graph from the given graph's unique root
	 * 
	 * @param root the root of the graph.
	 */
	public DominanceGraph(UniqueEntryExitGraph graph, boolean invertEdges) {
		this(graph, new GraphElementHashSet<Node>(), invertEdges);
	}

	/**
	 * Perform dominance analysis on the given graph with the collection of explicit "roots."
	 * 
	 * @param explicitRoots
	 *            the collection of roots; one of these is the true root of the
	 *            flowgraph, the others could be exception handlers that would otherwise
	 *            be unreachable.
	 * 
	 * @param invertEdges true if edges in the given graph should be interpreted as being in the reverse direction, 
	 *                    this is useful for computing post-dominance (https://en.wikipedia.org/wiki/Dominator_(graph_theory)#Postdominance)
	 */
	public DominanceGraph(UniqueEntryExitGraph uniqueEntryExitGraph, GraphElementSet<Node> explicitRoots, boolean invertEdges) {
		super(uniqueEntryExitGraph.nodes());
		this.uniqueEntryExitGraph = uniqueEntryExitGraph;
		this.invertEdges = invertEdges;
		GraphElementSet<Node> roots = new GraphElementHashSet<Node>();
		roots.add(uniqueEntryExitGraph.getEntryNode());
		roots.addAll(explicitRoots);
		if(invertEdges) {
			this.dfs(new GraphElementHashSet<Node>(uniqueEntryExitGraph.getExitNode()));
		} else {
			this.dfs(roots);
		}
		this.computeDominators();
		// create edges for the immediate dominator tree (idom) or immediate post dominator tree (ipdom)
		for(Entry<Node,Node> entry : getIdoms().entrySet()){
			Node fromNode = entry.getKey();
			Node toNode = entry.getValue();
			Edge idomEdge = new Edge(fromNode, toNode);
			if(invertEdges) {
				idomEdge.tags().add(POST_DOMINATOR_TREE_EDGE);
			} else {
				idomEdge.tags().add(DOMINATOR_TREE_EDGE);
			}
			this.edges().add(idomEdge);
			
		}
		// create edges for the dominance frontier (dom-frontier) or post dominance frontier (pdom-frontier)
		for(Entry<Node, Set<Node>> entry : getDominanceFrontiers().entrySet()){
			Node fromNode = entry.getKey();
			for(Node toNode : entry.getValue()){
				Edge domFrontierEdge = new Edge(fromNode, toNode);
				if(invertEdges) {
					domFrontierEdge.tags().add(POST_DOMINANCE_FRONTIER_EDGE);
				} else {
					domFrontierEdge.tags().add(DOMINANCE_FRONTIER_EDGE);
				}
				this.edges().add(domFrontierEdge);
			}
		}
	}
	
	public UniqueEntryExitGraph getUniqueEntryExitGraph() {
		return uniqueEntryExitGraph;
	}

	/**
	 * Returns the map of immediate dominators
	 * 
	 * @return the map from each block to its immediate dominator (if it has one).
	 */
	public Map<Node, Node> getIdoms() {
		return this.idom;
	}
	
	/**
	 * Returns the map of semi-dominators
	 */
	public Map<Node, Integer> getSdoms() {
		return this.semi;
	}

	/**
	 * Compute and/or fetch the dominator tree as a Multimap.
	 * 
	 * @return the dominator tree.
	 */
	public Multimap<Node> getDominatorTree() {
		if (this.dominatorTree == null) {
			this.dominatorTree = new Multimap<Node>();
			for (Node node : this.idom.keySet()) {
				dominatorTree.get(this.idom.get(node)).add(node);
			}
		}
		return this.dominatorTree;
	}

	/**
	 * Compute and/or fetch the dominance frontiers as a Multimap.
	 * 
	 * @return a Multimap where the set of nodes mapped to each key node is the set
	 *         of nodes in the key node's dominance frontier.
	 */
	public Multimap<Node> getDominanceFrontiers() {
		if (this.dominanceFrontiers == null) {
			this.dominanceFrontiers = new Multimap<Node>();

			getDominatorTree(); // touch the dominator tree

			for (Node x : reverseTopologicalTraversal()) {
				Set<Node> dfx = this.dominanceFrontiers.get(x);

				GraphElementSet<Node> successors;
				if(invertEdges) {
					successors = uniqueEntryExitGraph.predecessors(x);
				} else {
					successors = uniqueEntryExitGraph.successors(x);
				}
				
				// compute DF(local)
				for (Node y : successors) {
					if (idom.get(y) != x) {
						dfx.add(y);
					}
				}

				// compute DF(up)
				for (Node z : this.dominatorTree.get(x)) {
					for (Node y : this.dominanceFrontiers.get(z)) {
						if (idom.get(y) != x) {
							dfx.add(y);
						}
					}
				}
			}
		}

		return this.dominanceFrontiers;
	}

	/**
	 * Create and/or fetch a topological traversal of the dominator tree, such that
	 * for every node, idom(node) appears before node.
	 * 
	 * @return the topological traversal of the dominator tree, as an immutable List.
	 */
	public List<Node> topologicalTraversal() {
		return Collections.unmodifiableList(getToplogicalTraversalImplementation());
	}

	/**
	 * Create and/or fetch a reverse topological traversal of the dominator tree,
	 * such that for every node, node appears before idom(node).
	 * 
	 * @return a reverse topological traversal of the dominator tree, as an
	 *         immutable List.
	 */
	public Iterable<Node> reverseTopologicalTraversal() {
		return new Iterable<Node>() {
			@Override
			public Iterator<Node> iterator() {
				return getToplogicalTraversalImplementation().descendingIterator();
			}
		};
	}

	/**
	 * Depth-first search the graph and initialize data structures.
	 * 
	 * @param roots
	 *            the root(s) of the flowgraph. One of these is the start block, the
	 *            others are exception handlers.
	 */
	private void dfs(GraphElementSet<Node> roots) {
		Iterator<Node> iterator = new DepthFirstPreorderIterator(uniqueEntryExitGraph, roots, invertEdges);
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (!semi.containsKey(node)) {
				vertex.add(node);

				// initial assumption: the node's semidominator is itself
				semi.put(node, semi.size());
				label.put(node, node);

				GraphElementSet<Node> successors;
				if(invertEdges) {
					successors = uniqueEntryExitGraph.predecessors(node);
				} else {
					successors = uniqueEntryExitGraph.successors(node);
				}
				
				for (Node child : successors) {
					pred.get(child).add(node);
					if (!semi.containsKey(child)) {
						parent.put(child, node);
					}
				}
			}
		}
	}

	/**
	 * Steps 2, 3, and 4 of Lengauer-Tarjan.
	 */
	private void computeDominators() {
		int lastSemiNumber = semi.size() - 1;

		for (int i = lastSemiNumber; i > 0; i--) {
			Node w = vertex.get(i);
			Node p = this.parent.get(w);

			// step 2: compute semidominators
			// for each v in pred(w)...
			int semidominator = semi.get(w);
			for (Node v : pred.get(w)) {
				semidominator = Math.min(semidominator, semi.get(eval(v)));
			}

			semi.put(w, semidominator);
			bucket.get(vertex.get(semidominator)).add(w);

			// Link w into the forest via its parent, p
			link(p, w);

			// step 3: implicitly compute idominators
			// for each v in bucket(parent(w)) ...
			for (Node v : bucket.get(p)) {
				Node u = eval(v);
				if (semi.get(u) < semi.get(v)) {
					idom.put(v, u);
				} else {
					idom.put(v, p);
				}
			}

			bucket.get(p).clear();
		}

		// step 4: explicitly compute idominators
		for (int i = 1; i <= lastSemiNumber; i++) {
			Node w = vertex.get(i);
			if (idom.get(w) != vertex.get((semi.get(w)))) {
				idom.put(w, idom.get(idom.get(w)));
			}
		}
	}

	/**
	 * Extract the node with the least-numbered semidominator in the (processed)
	 * ancestors of the given node.
	 * 
	 * @param v the node of interest.
	 * @return "If v is the root of a tree in the forest, return v. Otherwise, let r
	 *         be the root of the tree which contains v. Return any vertex u != r of
	 *         miniumum semi(u) on the path r-*v."
	 */
	private Node eval(Node v) {
		// this version of Lengauer-Tarjan implements eval(v) as a path-compression procedure
		compress(v);
		return label.get(v);
	}

	/**
	 * Traverse ancestor pointers back to a subtree root, then propagate the least
	 * semidominator seen along this path through the "label" map.
	 */
	private void compress(Node v) {
		Stack<Node> worklist = new Stack<Node>();
		worklist.add(v);

		Node a = this.ancestor.get(v);

		// traverse back to the subtree root.
		while (this.ancestor.containsKey(a)) {
			worklist.push(a);
			a = this.ancestor.get(a);
		}

		// propagate semidominator information forward.
		Node ancestor = worklist.pop();
		int leastSemi = semi.get(label.get(ancestor));

		while (!worklist.empty()) {
			Node descendent = worklist.pop();
			int currentSemi = semi.get(label.get(descendent));

			if (currentSemi > leastSemi) {
				label.put(descendent, label.get(ancestor));
			} else {
				leastSemi = currentSemi;
			}

			// prepare to process the next iteration.
			ancestor = descendent;
		}
	}

	/**
	 * Simple version of link(parent,child) simply links the child into the parent's
	 * forest, with no attempt to balance the subtrees or otherwise optimize
	 * searching.
	 */
	private void link(Node parent, Node child) {
		this.ancestor.put(child, parent);
	}

	/**
	 * Multimap maps a key to a set of values
	 */
	@SuppressWarnings("serial")
	public static class Multimap<T> extends HashMap<T, Set<T>> {
		/**
		 * Fetch the set for a given key, creating it if necessary.
		 * 
		 * @param key the multimap key
		 * @return the set of values mapped to the key.
		 */
		@SuppressWarnings("unchecked")
		@Override
		public Set<T> get(Object key) {
			if (!this.containsKey(key)) {
				this.put((T) key, new HashSet<T>());
			}
			return super.get(key);
		}
	}

	/**
	 * Create/fetch the topological traversal of the dominator tree.
	 * 
	 * @return {@link this.topologicalTraversal}, the traversal of the dominator
	 *         tree such that for any node n with a dominator, n appears before
	 *         idom(n).
	 */
	private LinkedList<Node> getToplogicalTraversalImplementation() {
		if (this.topologicalTraversalImpl == null) {
			this.topologicalTraversalImpl = new LinkedList<Node>();
			for (Node node : this.vertex) {
				int idx = this.topologicalTraversalImpl.indexOf(this.idom.get(node));
				if (idx != -1) {
					this.topologicalTraversalImpl.add(idx + 1, node);
				} else {
					this.topologicalTraversalImpl.add(node);
				}
			}
		}
		return this.topologicalTraversalImpl;
	}
	
}
