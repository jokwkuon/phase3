package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.lsmr.cfg.ControlFlowGraph;
import org.lsmr.cfg.Edge;
import org.lsmr.cfg.Edge.EdgeLabel;
import org.lsmr.cfg.Node;


public class CFGStructureTest {

    @Test
    void ifTestHasNodes() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfTest.java");
        assertFalse(cfg.nodes().isEmpty());
    }

    @Test
    void ifTestHasTrueAndFalseEdges() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfTest.java");
        boolean foundTrue  = false;
        boolean foundFalse = false;
        for (Edge edge : cfg.edges()) {
            if (edge.label() == EdgeLabel.TRUE)  foundTrue  = true;
            if (edge.label() == EdgeLabel.FALSE) foundFalse = true;
        }
        assertTrue(foundTrue,  "Expected a TRUE edge in the if-test CFG");
        assertTrue(foundFalse, "Expected a FALSE edge in the if-test CFG");
    }

    @Test
    void ifElseTestHasTrueAndFalseEdges() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfElseTest.java");
        boolean foundTrue  = false;
        boolean foundFalse = false;
        for (Edge edge : cfg.edges()) {
            if (edge.label() == EdgeLabel.TRUE)  foundTrue  = true;
            if (edge.label() == EdgeLabel.FALSE) foundFalse = true;
        }
        assertTrue(foundTrue,  "Expected a TRUE edge in the if-else CFG");
        assertTrue(foundFalse, "Expected a FALSE edge in the if-else CFG");
    }

    @Test
    void whileTestHasTrueAndFalseEdges() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/WhileTest.java");
        boolean foundTrue  = false;
        boolean foundFalse = false;
        for (Edge edge : cfg.edges()) {
            if (edge.label() == EdgeLabel.TRUE)  foundTrue  = true;
            if (edge.label() == EdgeLabel.FALSE) foundFalse = true;
        }
        assertTrue(foundTrue,  "Expected a TRUE (loop-back) edge in the while CFG");
        assertTrue(foundFalse, "Expected a FALSE (exit) edge in the while CFG");
    }

    @Test
    void defUseTestHasNoConditionEdges() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/DefUseTest.java");
        for (Edge edge : cfg.edges()) {
            assertTrue(edge.label() != EdgeLabel.TRUE && edge.label() != EdgeLabel.FALSE,
                       "Straight-line code should have no branch edges");
        }
    }

    @Test
    void allEdgesHaveNonNullSource() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfTest.java");
        for (Edge edge : cfg.edges()) {
            assertTrue(edge.source() != null, "Every edge must have a non-null source");
        }
    }

    @Test
    void allNodesHaveLabelInIfTest() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfTest.java");
        for (Node node : cfg.nodes()) {
            assertFalse(node.label() == null || node.label().isEmpty(),
                        "Every node must have a non-empty label");
        }
    }
}