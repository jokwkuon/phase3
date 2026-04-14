package org.lsmr.pdg;

import java.util.HashMap;
import java.util.Map;

import org.lsmr.cfg.ControlFlowGraph;
import org.lsmr.cfg.Node;

public class ProgramDependenceGraphBuilder {

    private static final String ENTRY  = "*ENTRY*";
    private static final String EXIT   = "*EXIT*";
    private static final String THROWN = "*THROWN*";

    public ProgramDependenceGraph build(ControlFlowGraph cfg) {
        if (cfg == null)
            throw new IllegalArgumentException("CFG must not be null.");

        ProgramDependenceGraph pdg = new ProgramDependenceGraph();
        Map<Node, PDGNode> nodeMap = new HashMap<>();

        for (Node cfgNode : cfg.nodes()) {
            if (isBookkeeping(cfgNode)) continue;
            PDGNode pdgNode = new PDGNode(cfgNode);
            pdg.addNode(pdgNode);
            nodeMap.put(cfgNode, pdgNode);
        }

        new ControlDependenceBuilder().addControlDependences(cfg, pdg, nodeMap);
        new DataDependenceBuilder().addDataDependences(cfg, pdg, nodeMap);

        return pdg;
    }

    private boolean isBookkeeping(Node n) {
        String label = n.label();
        return label.equals(ENTRY) || label.equals(EXIT) || label.equals(THROWN);
    }
}