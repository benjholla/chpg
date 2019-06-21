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
 *  This code is adapted from: https://svn.apache.org/repos/asf/flex/falcon/trunk/compiler/src/org/apache/flex/abc/graph/algorithms/DepthFirstPreorderIterator.java
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import spgqlite.graph.GraphElementSet;
import spgqlite.graph.Node;

/**
 * DepthFirstPreorderIterator yields a depth-first pre-order traversal of a
 * graph.
 */
public class DepthFirstPreorderIterator implements Iterator<Node> {

	/**
	 * Unique Entry/Exit Graph to operate on
	 */
	private UniqueEntryExitGraph graph;
	
	/**
	 * Indicates if the edge direction in the given graph should be interpreted as being reversed
	 */
	private boolean invertEdges = false; 

	/**
	 * @param roots the caller's root(s) of the flowgraph. There should be only one
	 *              start block, but multiple roots are tolerated to work around
	 *              fuzzy successor logic to exception handlers.
	 */
	public DepthFirstPreorderIterator(UniqueEntryExitGraph graph, GraphElementSet<Node> roots, boolean invertEdges) {
		this.graph = graph;
		this.invertEdges = invertEdges;
		for (Node root : roots) {
			this.stack.add(root);
		}
	}

	public DepthFirstPreorderIterator(UniqueEntryExitGraph graph, Node root, boolean invertEdges) {
		this.graph = graph;
		this.invertEdges = invertEdges;
		this.stack.add(root);
	}

	/**
	 * The to-be-visited stack of blocks.
	 */
	private Stack<Node> stack = new Stack<Node>();

	/**
	 * The set of edges already traversed.
	 */
	private Set<Edge> visitedEdges = new HashSet<Edge>();

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
	public Node next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		Node next = stack.pop();
		pushSuccessors(next);
		return next;
	}

	/**
	 * Traverse any previously-untraversed edges by adding the destination block to
	 * the to-do stack.
	 * 
	 * @param b the current block.
	 */
	private void pushSuccessors(Node b) {
		GraphElementSet<Node> successors;
		if(invertEdges) {
			successors = this.graph.predecessors(b);
		} else {
			successors = this.graph.successors(b);
		}
		for (Node successor : successors) {
			if (visitedEdges.add(new Edge(b, successor))) {
				stack.push(successor);
			}
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Edge is used to detect edges previously traversed. It implements composite
	 * hash and equality operations so it can be used as a key in a hashed
	 * collection.
	 */
	private static class Edge {
		private Integer fromAddress;
		private Integer toAddress;

		public Edge(Node from, Node to) {
			this.fromAddress = from.getAddress();
			this.toAddress = to.getAddress();
		}

		@Override
		public int hashCode() {
			return Objects.hash(fromAddress, toAddress);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Edge)) {
				return false;
			}
			Edge other = (Edge) obj;
			return Objects.equals(fromAddress, other.fromAddress) && Objects.equals(toAddress, other.toAddress);
		}

	}
	
}
