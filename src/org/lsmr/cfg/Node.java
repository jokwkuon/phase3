package org.lsmr.cfg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents nodes in a CFG. Each node is expected to have a non-null,
 * non-empty label. A node is tightly connected to a specific graph, so the
 * creation of nodes is permitted only via
 * {@link ControlFlowGraph#buildNode(String)}.
 */
public class Node {
	private ControlFlowGraph graph;
	private Set<Edge> inEdges = new HashSet<>();
	private Set<Edge> outEdges = new HashSet<>();
	private String label;

	Node(String label) {
		this.label = label;
	}

	/**
	 * Accesses the label for this node.
	 * 
	 * @return The label for this node. Should not be null; should not be empty.
	 */
	public String label() {
		return label;
	}

	/**
	 * Accesses the parent graph for this node.
	 * 
	 * @return The parent graph for this node. Should not be null.
	 */
	public ControlFlowGraph graph() {
		return graph;
	}

	/**
	 * Accesses the out-edges of this node as an unmodifiable set.
	 * 
	 * @return An unmodifiable set of the out-edges of this node.
	 */
	public Set<Edge> outEdges() {
		return Collections.unmodifiableSet(outEdges);
	}

	/**
	 * Accesses the in-edges of this node as an unmodifiable set.
	 * 
	 * @return An unmodifiable set of the in-edges of this node.
	 */
	public Set<Edge> inEdges() {
		return Collections.unmodifiableSet(inEdges);
	}

	/**
	 * Adds the indicated edge to this node as an in-edge.
	 * 
	 * @param edge
	 *            The edge to be added as an in-edge
	 * @throws IllegalArgumentException
	 *             If the edge is null.
	 * @throws IllegalStateException
	 *             If the target node of the edge is not identical to this node.
	 */
	public void addInEdge(Edge edge) {
		inEdges.add(edge);
	}

	/**
	 * Adds the indicated edge to this node as an out-edge.
	 * 
	 * @param edge
	 *            The edge to be added as an out-edge
	 * @throws IllegalArgumentException
	 *             If the edge is null.
	 * @throws IllegalStateException
	 *             If the source node of the edge is not identical to this node.
	 */
	public void addOutEdge(Edge edge) {
		outEdges.add(edge);
	}

	void setGraph(ControlFlowGraph graph) {
		this.graph = graph;
	}

	@Override
	public int hashCode() {
		return label.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node) {
			Node other = (Node)obj;

			return label.equals(other.label);
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("node ");
		sb.append(label);
		sb.append(": in-edges [");

		boolean start = true;

		for(Edge edge : inEdges) {
			if(start)
				start = false;
			else
				sb.append(", ");

			sb.append(edge.toString());
		}

		sb.append("]; out-edges [");

		start = true;

		for(Edge edge : outEdges) {
			if(start)
				start = false;
			else
				sb.append(", ");

			sb.append(edge.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}
