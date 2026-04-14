package org.lsmr.cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lsmr.cfg.Edge.EdgeLabel;

class WorkingGraph {
	public List<Edge> edges = new ArrayList<>();
	public Node node;
	public Map<String, List<Edge>> breakEdges = new HashMap<>();
	public Map<String, List<Edge>> continueEdges = new HashMap<>();
	public boolean throws_ = false;

	public void connect(WorkingGraph s) {
		if(s == null)
			return;

		for(Edge edge : edges) {
			if(edge.label() != EdgeLabel.THROWN && edge.target() == null)
				edge.setTarget(s.node);
		}

		edges.addAll(s.edges);

		for(String label : s.breakEdges.keySet()) {
			List<Edge> sEdges = s.breakEdges.get(label);
			List<Edge> thisEdges = breakEdges.get(label);

			if(thisEdges == null)
				breakEdges.put(label, sEdges);
			else
				thisEdges.addAll(sEdges);
		}

		for(String label : s.continueEdges.keySet()) {
			List<Edge> sEdges = s.continueEdges.get(label);
			List<Edge> thisEdges = continueEdges.get(label);

			if(thisEdges == null)
				continueEdges.put(label, sEdges);
			else
				thisEdges.addAll(sEdges);
		}
	}
}
