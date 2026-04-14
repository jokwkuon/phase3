package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PDGEdgeCaseTest {


    @Test
    void forLoopProducesAtLeastOneControlEdge() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/ForTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL)
                .count();
        assertTrue(count >= 1, "for loop should produce at least one control edge");
    }

    @Test
    void forLoopBodyIsControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/ForTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "for", "sum = sum + i"),
                   "Expected control edge: for -> sum = sum + i");
    }

    @Test
    void forLoopInitReachesBody() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/ForTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "sum", "int sum = 0", "sum = sum + i"),
                   "Expected data edge: int sum = 0 --sum--> sum = sum + i");
    }

    @Test
    void forLoopBodySumReachesItself() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/ForTest.java");
        boolean found = PDGTestUtils.hasDataEdge(pdg, "sum", "sum = sum + i", "sum = sum + i")
                     || PDGTestUtils.hasDataEdge(pdg, "sum", "int sum = 0", "sum = sum + i");
        assertTrue(found, "Expected a data edge for sum into the loop body");
    }

    @Test
    void forLoopHasTrueAndFalseEdges() throws Exception {
        org.lsmr.cfg.ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/ForTest.java");
        boolean foundTrue  = false;
        boolean foundFalse = false;
        for (org.lsmr.cfg.Edge edge : cfg.edges()) {
            if (edge.label() == org.lsmr.cfg.Edge.EdgeLabel.TRUE)  foundTrue  = true;
            if (edge.label() == org.lsmr.cfg.Edge.EdgeLabel.FALSE) foundFalse = true;
        }
        assertTrue(foundTrue,  "for loop CFG must have a TRUE edge");
        assertTrue(foundFalse, "for loop CFG must have a FALSE edge");
    }


    @Test
    void doWhileProducesAtLeastOneControlEdge() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DoWhileTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL)
                .count();
        assertTrue(count >= 1, "do-while should produce at least one control edge");
    }

    @Test
    void doWhileBodyIsControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DoWhileTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "while", "x = x + 1"),
                   "Expected control edge: while -> x = x + 1");
    }


    @Test
    void doWhileBodyXReachesCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DoWhileTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "x = x + 1", "while ( x < 3 )"),
                   "Expected loop-carried data edge: x = x + 1 --x--> while condition");
    }

    @Test
    void doWhileBodyXUsesX() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DoWhileTest.java");
        boolean found = PDGTestUtils.hasDataEdge(pdg, "x", "int x = 0", "x = x + 1")
                     || PDGTestUtils.hasDataEdge(pdg, "x", "x = x + 1", "x = x + 1");
        assertTrue(found, "Expected a data edge for x into do-while body");
    }

    @Test
    void doWhileInitXReachesBody() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DoWhileTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 0", "x = x + 1"),
                   "Expected data edge: int x = 0 --x--> x = x + 1 (first iteration)");
    }

    @Test
    void switchProducesAtLeastOneControlEdge() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/SwitchTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL)
                .count();
        assertTrue(count >= 1, "switch should produce at least one control edge");
    }

    @Test
    void switchCase1BodyIsControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/SwitchTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "switch", "y = 10"),
                   "Expected control edge: switch -> y = 10 (case 1 body)");
    }

    @Test
    void switchCase2BodyIsControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/SwitchTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "switch", "y = 20"),
                   "Expected control edge: switch -> y = 20 (case 2 body)");
    }

    @Test
    void switchXDefReachesSwitchCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/SwitchTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 1", "switch"),
                   "Expected data edge: int x = 1 --x--> switch (x)");
    }

    @Test
    void switchHasCaseEdgesInCFG() throws Exception {
        org.lsmr.cfg.ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/SwitchTest.java");
        boolean foundCase = false;
        for (org.lsmr.cfg.Edge edge : cfg.edges()) {
            if (edge.label() == org.lsmr.cfg.Edge.EdgeLabel.CASE) foundCase = true;
        }
        assertTrue(foundCase, "switch CFG must have CASE edges");
    }



    @Test
    void tryCatchProducesNodes() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/TryCatchTest.java");
        assertFalse(pdg.getNodes().isEmpty(), "try-catch PDG should have nodes");
    }

    @Test
    void tryCatchXDefReachesTryBody() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/TryCatchTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 1", "x = x + 1"),
                   "Expected data edge: int x = 1 --x--> x = x + 1 (try body)");
    }

    @Test
    void tryCatchHasThrownOrCaughtEdgeInCFG() throws Exception {
        org.lsmr.cfg.ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/TryCatchTest.java");
        boolean found = false;
        for (org.lsmr.cfg.Edge edge : cfg.edges()) {
            if (edge.label() == org.lsmr.cfg.Edge.EdgeLabel.THROWN
                    || edge.label() == org.lsmr.cfg.Edge.EdgeLabel.CAUGHT) {
                found = true;
            }
        }
        assertTrue(found, "try-catch CFG must have THROWN or CAUGHT edges");
    }

    @Test
    void tryCatchNoBookkeepingNodesInPDG() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/TryCatchTest.java");
        for (PDGNode node : pdg.getNodes()) {
            String lbl = node.getLabel();
            assertFalse(lbl.equals("*ENTRY*") || lbl.equals("*EXIT*") || lbl.equals("*THROWN*"),
                        "Bookkeeping node should not appear in PDG: " + lbl);
        }
    }

 
    @Test
    void nestedIfProducesAtLeastTwoControlEdges() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/NestedIfTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL)
                .count();
        assertTrue(count >= 2, "Nested if should produce at least two control edges");
    }

    @Test
    void nestedIfInnerBodyControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/NestedIfTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "if", "x = 1"),
                   "Expected control edge: if -> x = 1");
    }

    @Test
    void nestedIfXDefReachesOuterCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/NestedIfTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 0", "if"),
                   "Expected data edge: int x = 0 --x--> if (x == 0)");
    }

    @Test
    void nestedIfYDefReachesInnerCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/NestedIfTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "y", "int y = 0", "if"),
                   "Expected data edge: int y = 0 --y--> if (y == 0)");
    }

    @Test
    void nestedIfInitNotControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/NestedIfTest.java");
        assertFalse(PDGTestUtils.hasControlEdge(pdg, "if", "int x = 0"),
                    "int x = 0 should not be control dependent on any if");
    }
}