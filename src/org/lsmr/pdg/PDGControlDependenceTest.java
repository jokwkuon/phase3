package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PDGControlDependenceTest {

    @Test
    void ifBodyIsControlDependentOnIfCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "if", "x = 1"),
                   "Expected control edge: if -> x = 1 (true branch body)");
    }

    @Test
    void ifTestProducesAtLeastOneControlEdge() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL).count();
        assertTrue(count >= 1, "Expected at least one control edge");
    }

    @Test
    void ifElseTrueBranchIsControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfElseTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "if", "y = 1"),
                   "Expected control edge: if -> y = 1 (true branch)");
    }

    @Test
    void ifElseFalseBranchIsControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfElseTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "if", "y = 2"),
                   "Expected control edge: if -> y = 2 (false branch)");
    }

    @Test
    void ifElseProducesAtLeastTwoControlEdges() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfElseTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL).count();
        assertTrue(count >= 2, "Expected at least two control edges for if-else");
    }

    @Test
    void whileBodyIsControlDependentOnCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/WhileTest.java");
        assertTrue(PDGTestUtils.hasControlEdge(pdg, "while", "x = x + 1"),
                   "Expected control edge: while -> x = x + 1");
    }

    @Test
    void whileTestProducesAtLeastOneControlEdge() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/WhileTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL).count();
        assertTrue(count >= 1, "Expected at least one control edge for while loop");
    }

    @Test
    void straightLineCodeHasNoControlEdges() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DefUseTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.CONTROL).count();
        assertTrue(count == 0, "Straight-line code should produce no control edges");
    }

    @Test
    void noBookkeepingNodesInPDG() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfTest.java");
        for (PDGNode node : pdg.getNodes()) {
            String lbl = node.getLabel();
            assertFalse(lbl.equals("*ENTRY*") || lbl.equals("*EXIT*") || lbl.equals("*THROWN*"),
                        "Bookkeeping node must not appear in the PDG: " + lbl);
        }
    }

    @Test
    void nodeAfterIfElseIsNotControlDependent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfElseTest.java");
        assertFalse(PDGTestUtils.hasControlEdge(pdg, "if", "int x = 0"),
                    "int x = 0 should not be control dependent on the if");
        assertFalse(PDGTestUtils.hasControlEdge(pdg, "if", "int y = 0"),
                    "int y = 0 should not be control dependent on the if");
    }

    @Test
    void initNotControlDependentOnWhile() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/WhileTest.java");
        assertFalse(PDGTestUtils.hasControlEdge(pdg, "while", "int x = 0"),
                    "int x = 0 should not be control dependent on while");
    }
}