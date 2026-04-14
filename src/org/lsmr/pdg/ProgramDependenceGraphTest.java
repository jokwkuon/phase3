package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProgramDependenceGraphTest {

    private ProgramDependenceGraph pdg;
    private PDGNode nodeA;
    private PDGNode nodeB;
    private PDGNode nodeC;

    @BeforeEach
    void setUp() throws Exception {
        // Build a real PDG from a simple source so PDGNode wraps a real CFG node
        pdg = PDGTestUtils.buildPDGFromSource("Simple",
            "class Simple {\n" +
            "  void m() {\n" +
            "    int x = 1;\n" +
            "    int y = x + 2;\n" +
            "    int z = y + 3;\n" +
            "  }\n" +
            "}\n", ".m(");

        // Also create standalone nodes for unit-level tests
        // We need real CFG nodes — build a tiny one
        ProgramDependenceGraph tiny = PDGTestUtils.buildPDGFromSource("Tiny",
            "class Tiny {\n" +
            "  void m() {\n" +
            "    int a = 1;\n" +
            "    int b = 2;\n" +
            "    int c = 3;\n" +
            "  }\n" +
            "}\n", ".m(");
        var nodes = tiny.getNodes();
        nodeA = nodes.get(0);
        nodeB = nodes.get(1);
        nodeC = nodes.get(2);
    }

    // -----------------------------------------------------------------------
    // ProgramDependenceGraph tests
    // -----------------------------------------------------------------------

    @Test
    void newGraphHasNoNodes() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        assertTrue(g.getNodes().isEmpty());
    }

    @Test
    void newGraphHasNoEdges() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        assertTrue(g.getEdges().isEmpty());
    }

    @Test
    void addNodeIncreasesCount() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        assertEquals(1, g.getNodes().size());
    }

    @Test
    void addNullNodeIsIgnored() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(null);
        assertTrue(g.getNodes().isEmpty());
    }

    @Test
    void addDuplicateNodeIsIgnored() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        g.addNode(nodeA);
        assertEquals(1, g.getNodes().size());
    }

    @Test
    void addEdgeIncreasesCount() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        g.addNode(nodeB);
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        g.addEdge(edge);
        assertEquals(1, g.getEdges().size());
    }

    @Test
    void addNullEdgeIsIgnored() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addEdge(null);
        assertTrue(g.getEdges().isEmpty());
    }

    @Test
    void addDuplicateEdgeIsIgnored() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        g.addNode(nodeB);
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        g.addEdge(edge);
        g.addEdge(edge);
        assertEquals(1, g.getEdges().size());
    }

    @Test
    void containsNodeReturnsTrueForAddedNode() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        assertTrue(g.containsNode(nodeA));
    }

    @Test
    void containsNodeReturnsFalseForMissingNode() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        assertFalse(g.containsNode(nodeA));
    }

    @Test
    void getEdgesFromReturnsCorrectEdges() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        g.addNode(nodeB);
        g.addNode(nodeC);
        PDGEdge edgeAB = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        PDGEdge edgeAC = new PDGEdge(nodeA, nodeC, PDGEdge.Type.CONTROL, null);
        PDGEdge edgeBC = new PDGEdge(nodeB, nodeC, PDGEdge.Type.DATA, "x");
        g.addEdge(edgeAB);
        g.addEdge(edgeAC);
        g.addEdge(edgeBC);

        var fromA = g.getEdgesFrom(nodeA);
        assertEquals(2, fromA.size());
        assertTrue(fromA.contains(edgeAB));
        assertTrue(fromA.contains(edgeAC));
    }

    @Test
    void getEdgesToReturnsCorrectEdges() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        g.addNode(nodeB);
        g.addNode(nodeC);
        PDGEdge edgeAC = new PDGEdge(nodeA, nodeC, PDGEdge.Type.CONTROL, null);
        PDGEdge edgeBC = new PDGEdge(nodeB, nodeC, PDGEdge.Type.DATA, "x");
        g.addEdge(edgeAC);
        g.addEdge(edgeBC);

        var toC = g.getEdgesTo(nodeC);
        assertEquals(2, toC.size());
        assertTrue(toC.contains(edgeAC));
        assertTrue(toC.contains(edgeBC));
    }

    @Test
    void getNodesReturnsUnmodifiableList() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        g.addNode(nodeA);
        assertThrows(UnsupportedOperationException.class,
            () -> g.getNodes().add(nodeB));
    }

    @Test
    void getEdgesReturnsUnmodifiableList() {
        ProgramDependenceGraph g = new ProgramDependenceGraph();
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertThrows(UnsupportedOperationException.class,
            () -> g.getEdges().add(edge));
    }

    @Test
    void realPdgHasNodes() {
        assertFalse(pdg.getNodes().isEmpty(), "Built PDG should have nodes");
    }

    @Test
    void realPdgHasEdges() {
        assertFalse(pdg.getEdges().isEmpty(), "Built PDG should have edges");
    }

    // -----------------------------------------------------------------------
    // PDGNode tests
    // -----------------------------------------------------------------------

    @Test
    void pdgNodeGetLabelIsNotNull() {
        assertNotNull(nodeA.getLabel());
    }

    @Test
    void pdgNodeGetLabelIsNotEmpty() {
        assertFalse(nodeA.getLabel().isEmpty());
    }

    @Test
    void pdgNodeToStringContainsLabel() {
        assertTrue(nodeA.toString().contains(nodeA.getLabel()));
    }

    @Test
    void pdgNodeEqualsItself() {
        assertEquals(nodeA, nodeA);
    }

    @Test
    void pdgNodeNotEqualToNull() {
        assertNotEquals(nodeA, null);
    }

    @Test
    void pdgNodeNotEqualToDifferentNode() {
        assertNotEquals(nodeA, nodeB);
    }

    @Test
    void pdgNodeHashCodeConsistent() {
        assertEquals(nodeA.hashCode(), nodeA.hashCode());
    }

    // -----------------------------------------------------------------------
    // PDGEdge tests
    // -----------------------------------------------------------------------

    @Test
    void controlEdgeGetFrom() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertEquals(nodeA, edge.getFrom());
    }

    @Test
    void controlEdgeGetTo() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertEquals(nodeB, edge.getTo());
    }

    @Test
    void controlEdgeGetType() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertEquals(PDGEdge.Type.CONTROL, edge.getType());
    }

    @Test
    void controlEdgeGetVariableIsNull() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertNull(edge.getVariable());
    }

    @Test
    void dataEdgeGetVariable() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        assertEquals("x", edge.getVariable());
    }

    @Test
    void dataEdgeGetType() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        assertEquals(PDGEdge.Type.DATA, edge.getType());
    }

    @Test
    void edgeEqualsItself() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertEquals(edge, edge);
    }

    @Test
    void edgeNotEqualToNull() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertNotEquals(edge, null);
    }

    @Test
    void edgeNotEqualToNonEdge() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertNotEquals(edge, "not an edge");
    }

    @Test
    void sameEdgesAreEqual() {
        PDGEdge e1 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        PDGEdge e2 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        assertEquals(e1, e2);
    }

    @Test
    void differentVariableMeansNotEqual() {
        PDGEdge e1 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        PDGEdge e2 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "y");
        assertNotEquals(e1, e2);
    }

    @Test
    void differentTypeMeansNotEqual() {
        PDGEdge e1 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        PDGEdge e2 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, null);
        assertNotEquals(e1, e2);
    }

    @Test
    void edgeHashCodeConsistent() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        assertEquals(edge.hashCode(), edge.hashCode());
    }

    @Test
    void equalEdgesHaveSameHashCode() {
        PDGEdge e1 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        PDGEdge e2 = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void controlEdgeToStringContainsControl() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.CONTROL, null);
        assertTrue(edge.toString().contains("CONTROL"));
    }

    @Test
    void dataEdgeToStringContainsVariable() {
        PDGEdge edge = new PDGEdge(nodeA, nodeB, PDGEdge.Type.DATA, "x");
        assertTrue(edge.toString().contains("x"));
    }

    // -----------------------------------------------------------------------
    // ProgramDependenceGraphBuilder tests
    // -----------------------------------------------------------------------

    @Test
    void builderThrowsOnNullCFG() {
        assertThrows(IllegalArgumentException.class,
            () -> new ProgramDependenceGraphBuilder().build(null));
    }

    @Test
    void builderProducesNonEmptyPDGForIfTest() throws Exception {
        ProgramDependenceGraph g = PDGTestUtils.buildPDG("sample/IfTest.java");
        assertFalse(g.getNodes().isEmpty());
        assertFalse(g.getEdges().isEmpty());
    }
}