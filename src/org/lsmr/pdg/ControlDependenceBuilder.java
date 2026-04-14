package org.lsmr.pdg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.lsmr.cfg.ControlFlowGraph;
import org.lsmr.cfg.Edge;
import org.lsmr.cfg.Edge.EdgeLabel;
import org.lsmr.cfg.Node;

public class ControlDependenceBuilder {

    private static final String ENTRY  = "*ENTRY*";
    private static final String EXIT   = "*EXIT*";
    private static final String THROWN = "*THROWN*";

    public void addControlDependences(ControlFlowGraph cfg,
                                      ProgramDependenceGraph pdg,
                                      Map<Node, PDGNode> nodeMap) {

        List<Node> allNodes = cfg.nodes();
        Map<Node, Set<Node>> postDoms = computePostDominators(cfg, allNodes);

        for (Node x : allNodes) {
            if (isBookkeeping(x)) continue;

            boolean isBranch = false;
            for (Edge e : x.outEdges()) {
                if (e.label() == EdgeLabel.TRUE
                        || e.label() == EdgeLabel.FALSE
                        || e.label() == EdgeLabel.CASE) {
                    isBranch = true;
                    break;
                }
            }
            if (!isBranch) continue;

            Set<Node> xPostDoms = postDoms.get(x);
            if (xPostDoms == null) xPostDoms = new HashSet<>();

            PDGNode pdgX = nodeMap.get(x);
            if (pdgX == null) continue;

            for (Edge e : x.outEdges()) {
                Node start = e.target();
                if (start == null || isBookkeeping(start)) continue;

                Queue<Node> worklist = new LinkedList<>();
                Set<Node> visited = new HashSet<>();
                worklist.add(start);
                visited.add(start);

                while (!worklist.isEmpty()) {
                    Node n = worklist.poll();
                    if (xPostDoms.contains(n)) continue;

                    PDGNode pdgN = nodeMap.get(n);
                    if (pdgN != null)
                        pdg.addEdge(new PDGEdge(pdgX, pdgN, PDGEdge.Type.CONTROL, null));

                    for (Edge out : n.outEdges()) {
                        Node succ = out.target();
                        if (succ != null && !isBookkeeping(succ) && visited.add(succ))
                            worklist.add(succ);
                    }
                }
            }
        }
    }

    private Map<Node, Set<Node>> computePostDominators(ControlFlowGraph cfg,
                                                        List<Node> allNodes) {
        Set<Node> exits = new HashSet<>();
        exits.add(cfg.normalExit);
        exits.add(cfg.abruptExit);

        Map<Node, Set<Node>> postDoms = new HashMap<>();
        Set<Node> allSet = new HashSet<>(allNodes);

        for (Node n : allNodes) {
            if (exits.contains(n)) {
                Set<Node> s = new HashSet<>();
                s.add(n);
                postDoms.put(n, s);
            } else {
                postDoms.put(n, new HashSet<>(allSet));
            }
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Node n : allNodes) {
                if (exits.contains(n)) continue;
                Set<Node> newPD = null;
                for (Edge e : n.outEdges()) {
                    Node succ = e.target();
                    if (succ == null) continue;
                    Set<Node> succPD = postDoms.get(succ);
                    if (succPD == null) continue;
                    if (newPD == null) newPD = new HashSet<>(succPD);
                    else newPD.retainAll(succPD);
                }
                if (newPD == null) newPD = new HashSet<>();
                newPD.add(n);
                if (!newPD.equals(postDoms.get(n))) {
                    postDoms.put(n, newPD);
                    changed = true;
                }
            }
        }
        return postDoms;
    }

    private boolean isBookkeeping(Node n) {
        String label = n.label();
        return label.equals(ENTRY) || label.equals(EXIT) || label.equals(THROWN);
    }
}