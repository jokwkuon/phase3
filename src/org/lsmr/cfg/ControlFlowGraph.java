package org.lsmr.cfg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lsmr.cfg.Edge.EdgeLabel;

/**
 * Represents mathematical graphs. Each graph contains a set of nodes, each of
 * which will refer to this graph as its container, and a set of edges, each of
 * which will refer to this graph as its container and to two nodes that must be
 * in this graph. To guarantee that these constraints be met, nodes and edges
 * are read-only and have to be created indirectly from the graph in which they
 * exist.
 */
public class ControlFlowGraph {
	/**
	 * Represents the entry point of this CFG. This will have no in-edges, but at
	 * least one out-edge.
	 */
	public Node entry;

	/**
	 * Represents the exit point of this CFG for normal returns. This will have no
	 * out-edges, but at least one in-edge.
	 */
	public Node normalExit;

	/**
	 * Represents the exit point of this CFG for exception throws. This will have no
	 * out-edges, but at least one in-edge.
	 */
	public Node abruptExit;

	private Map<String, List<Node>> nodes = new HashMap<>();
	private List<Edge> edges = new ArrayList<>();
	private String name;

	/**
	 * Accesses the name of this graph.
	 * 
	 * @return The name of this graph.
	 */
	public String name() {
		return name;
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of the graph. Cannot be null or empty.
	 * @throws IllegalArgumentException
	 *             If the name is null or empty.
	 */
	public ControlFlowGraph(String name) {
		if(name == null || name.length() < 1)
			throw new IllegalArgumentException();

		this.name = name;

		String entryLabel = "*ENTRY*";
		entry = new Node(entryLabel);
		entry.setGraph(this);
		nodes.put(entryLabel, new ArrayList<>(Arrays.asList(entry)));

		String exitLabel = "*EXIT*";
		normalExit = new Node(exitLabel);
		normalExit.setGraph(this);
		nodes.put(exitLabel, new ArrayList<>(Arrays.asList(normalExit)));

		String abruptExitLabel = "*THROWN*";
		abruptExit = new Node(abruptExitLabel);
		abruptExit.setGraph(this);
		nodes.put(abruptExitLabel, new ArrayList<>(Arrays.asList(abruptExit)));
	}

	/**
	 * Accesses the set of nodes defined on this graph.
	 * 
	 * @return An immutable set of all nodes defined on this graph.
	 */
	public List<Node> nodes() {
		ArrayList<Node> result = new ArrayList<>();

		for(List<Node> list : nodes.values())
			result.addAll(list);

		return Collections.unmodifiableList(result);
	}

	/**
	 * Accesses the set of edges defined on this graph.
	 * 
	 * @return An immutable set of all edges defined on this graph.
	 */
	public List<Edge> edges() {
		return Collections.unmodifiableList(edges);
	}

	/**
	 * Builds a node on this graph with the indicated label.
	 * 
	 * @param label
	 *            A label for the new node that may be used in identifying it.
	 *            Cannot be null or empty.
	 * @return The newly created node.
	 * @throws IllegalArgumentException
	 *             If the label is null or empty.
	 */
	public Node buildNode(String label) {
		if(label == null || label.length() < 1)
			throw new IllegalArgumentException();

		Node node = new Node(label);
		List<Node> list;

		if(nodes.containsKey(label))
			list = nodes.get(label);
		else {
			list = new ArrayList<>();
			nodes.put(label, list);
		}

		list.add(node);
		node.setGraph(this);

		return node;
	}

	/**
	 * Builds an edge on this graph with the indicated label.
	 * 
	 * @param source
	 *            The source node. Cannot be null.
	 * @param target
	 *            The target node. Cannot be null.
	 * @param label
	 *            A label for the new edge that may be used in identifying it.
	 *            Cannot be null or empty.
	 * @return The newly created edge.
	 * @throws IllegalArgumentException
	 *             If the label is null or empty or either of the indicated nodes is
	 *             null.
	 */
	public Edge buildEdge(Node source, Node target, EdgeLabel label) {
		return buildEdge(source, target, label, null);
	}

	/**
	 * Builds an edge on this graph with the indicated label.
	 * 
	 * @param source
	 *            The source node. Cannot be null.
	 * @param target
	 *            The target node. Cannot be null.
	 * @param label
	 *            A label for the new edge that may be used in identifying it.
	 *            Cannot be null or empty.
	 * @param extendedLabel
	 *            An extension to the label for the new edge that may be used in
	 *            identifying it.
	 * @return The newly created edge.
	 * @throws IllegalArgumentException
	 *             If the label is null or empty or either of the indicated nodes is
	 *             null.
	 */
	public Edge buildEdge(Node source, Node target, EdgeLabel label, String extendedLabel) {
		if(source == null || label == null)
			throw new IllegalArgumentException();

		Edge edge = new Edge(source, target, label, extendedLabel);

		edges.add(edge);
		edge.setGraph(this);
		source.addOutEdge(edge);

		if(target != null)
			target.addInEdge(edge);

		return edge;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ControlFlowGraph))
			return false;

		ControlFlowGraph other = (ControlFlowGraph)obj;

		return areEqual(nodes, other.nodes) && areEqual(edges, other.edges);
	}

	private static boolean areEqual(Map<String, List<Node>> nodes1, Map<String, List<Node>> nodes2) {
		List<Node> list1 = new ArrayList<>();
		List<Node> list2 = new ArrayList<>();

		for(List<Node> nodeList : nodes1.values())
			list1.addAll(nodeList);

		for(List<Node> nodeList : nodes2.values())
			list2.addAll(nodeList);

		if(list1.size() != list2.size()) {
			// System.err.println("Node sets differ in size: " + list1.size() + ", " +
			// list2.size());
			return false;
		}

		for(Node n : list1)
			if(!list2.contains(n)) {
				// System.err.println("Second node set does not contain: " + n);
				return false;
			}

		return true;
	}

	private static boolean areEqual(List<Edge> edges1, List<Edge> edges2) {
		if(edges1.size() != edges2.size())
			return false;

		for(Edge edge : edges1) {
			if(!edges2.contains(edge)) {
				// System.err.println("Second edge set does not contain: " + edge);
				return false;
			}
		}

		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean start = true;

		sb.append("graph ");
		sb.append(name);
		sb.append(": nodes [");
		for(List<Node> nodeList : nodes.values())
			for(Node node : nodeList) {
				if(start)
					start = false;
				else
					sb.append(", ");
				sb.append(node.label());
			}
		sb.append("], edges ");
		sb.append(edges.toString());
		sb.append("");

		return sb.toString();
	}
}
