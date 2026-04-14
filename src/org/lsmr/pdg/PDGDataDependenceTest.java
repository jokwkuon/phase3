package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PDGDataDependenceTest {

    @Test
    void defUseProducesAtLeastOneDataEdge() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DefUseTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.DATA)
                .count();
        assertTrue(count >= 1, "Expected at least one data edge");
    }

    @Test
    void defUseXReachesY() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DefUseTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 1", "int y = x + 2"),
                   "Expected data edge: int x = 1 --x--> int y = x + 2");
    }

    @Test
    void redefinitionKillsFirstDef() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/RedefinitionTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "x = 5", "int y = x + 2"),
                   "Expected data edge: x = 5 --x--> int y = x + 2");
    }

    @Test
    void firstDefDoesNotReachAfterRedefinition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/RedefinitionTest.java");
        assertFalse(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 1", "int y = x + 2"),
                    "int x = 1 should NOT reach int y = x + 2 after redefinition by x = 5");
    }

    @Test
    void redefinitionProducesAtLeastOneDataEdge() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/RedefinitionTest.java");
        long count = pdg.getEdges().stream()
                .filter(e -> e.getType() == PDGEdge.Type.DATA)
                .count();
        assertTrue(count >= 1, "Expected at least one data edge in redefinition test");
    }

    @Test
    void ifElseBothBranchNodesPresent() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfElseTest.java");
        boolean foundY1 = false;
        boolean foundY2 = false;
        for (PDGNode node : pdg.getNodes()) {
            if (node.getLabel().contains("y = 1")) foundY1 = true;
            if (node.getLabel().contains("y = 2")) foundY2 = true;
        }
        assertTrue(foundY1, "Expected PDG node for 'y = 1'");
        assertTrue(foundY2, "Expected PDG node for 'y = 2'");
    }

    @Test
    void ifTestXDefReachesIfCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/IfTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 0", "if"),
                   "Expected data edge: int x = 0 --x--> if (x == 0)");
    }

    @Test
    void whileInitXReachesCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/WhileTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 0", "while"),
                   "Expected data edge: int x = 0 --x--> while (x < 3)");
    }

    @Test
    void whileBodyXReachesCondition() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/WhileTest.java");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "x = x + 1", "while"),
                   "Expected loop-carried data edge: x = x + 1 --x--> while");
    }

    @Test
    void whileBodyXUsesX() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/WhileTest.java");
        boolean found = PDGTestUtils.hasDataEdge(pdg, "x", "int x = 0", "x = x + 1")
                     || PDGTestUtils.hasDataEdge(pdg, "x", "x = x + 1", "x = x + 1");
        assertTrue(found, "Expected a data edge into the loop body for x");
    }

    @Test
    void nestedArithmeticUsesBothVariables() throws Exception {
        String src =
                "class NestedArithmetic {\n" +
                "  void m() {\n" +
                "    int x = 1;\n" +
                "    int y = 2;\n" +
                "    int z = (x + 3) * (y + 4);\n" +
                "  }\n" +
                "}\n";

        ProgramDependenceGraph pdg = PDGTestUtils.buildPDGFromSource("NestedArithmetic", src, ".m(");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 1", "int z ="),
                   "Expected x to reach the nested arithmetic expression");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "y", "int y = 2", "int z ="),
                   "Expected y to reach the nested arithmetic expression");
    }

    @Test
    void nestedMethodCallArgumentsAreCountedAsUses() throws Exception {
        String src =
                "class NestedCalls {\n" +
                "  int f(int a) { return a; }\n" +
                "  void m() {\n" +
                "    int x = 1;\n" +
                "    int y = 2;\n" +
                "    int z = f(x + (y * 2));\n" +
                "  }\n" +
                "}\n";

        ProgramDependenceGraph pdg = PDGTestUtils.buildPDGFromSource("NestedCalls", src, ".m(");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 1", "int z ="),
                   "Expected x to be used through the nested method-call argument");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "y", "int y = 2", "int z ="),
                   "Expected y to be used through the nested method-call argument");
    }

    @Test
    void nestedConditionUsesAllReferencedVariables() throws Exception {
        String src =
                "class NestedCondition {\n" +
                "  void m() {\n" +
                "    int x = 1;\n" +
                "    int y = 2;\n" +
                "    if ((x + 1) < (y * 3)) {\n" +
                "      y = x;\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        ProgramDependenceGraph pdg = PDGTestUtils.buildPDGFromSource("NestedCondition", src, ".m(");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "x", "int x = 1", "if"),
                   "Expected x to reach the nested if condition");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "y", "int y = 2", "if"),
                   "Expected y to reach the nested if condition");
    }

    @Test
    void arrayIndexExpressionCountsAsUse() throws Exception {
        String src =
                "class IndexedAccess {\n" +
                "  void m() {\n" +
                "    int i = 1;\n" +
                "    int j = 2;\n" +
                "    int a[];\n" +
                "    int z = a[i + j];\n" +
                "  }\n" +
                "}\n";

        ProgramDependenceGraph pdg = PDGTestUtils.buildPDGFromSource("IndexedAccess", src, ".m(");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "i", "int i = 1", "int z ="),
                   "Expected i to reach the nested array-index expression");
        assertTrue(PDGTestUtils.hasDataEdge(pdg, "j", "int j = 2", "int z ="),
                   "Expected j to reach the nested array-index expression");
    }

    @Test
    void noDataEdgeFromUnrelatedNodes() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DefUseTest.java");
        assertFalse(PDGTestUtils.hasDataEdge(pdg, "y", "int y = x + 2", "int x = 1"),
                    "Should not have a backwards data edge from y to x's definition");
    }
}
