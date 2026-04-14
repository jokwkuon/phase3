package org.lsmr.pdg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgramDependenceGraph {

    private final List<PDGNode> nodes = new ArrayList<>();
    private final List<PDGEdge> edges = new ArrayList<>();

    public void addNode(PDGNode node) {
        if (node != null && !nodes.contains(node))
            nodes.add(node);
    }

    public void addEdge(PDGEdge edge) {
        if (edge != null && !edges.contains(edge))
            edges.add(edge);
    }

    public List<PDGNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<PDGEdge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public List<PDGEdge> getEdgesFrom(PDGNode node) {
        List<PDGEdge> result = new ArrayList<>();
        for (PDGEdge edge : edges)
            if (edge.getFrom().equals(node)) result.add(edge);
        return result;
    }

    public List<PDGEdge> getEdgesTo(PDGNode node) {
        List<PDGEdge> result = new ArrayList<>();
        for (PDGEdge edge : edges)
            if (edge.getTo().equals(node)) result.add(edge);
        return result;
    }

    public boolean containsNode(PDGNode node) {
        return nodes.contains(node);
    }
}