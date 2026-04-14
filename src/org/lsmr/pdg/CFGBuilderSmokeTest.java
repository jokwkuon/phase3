package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.lsmr.cfg.ControlFlowGraph;


public class CFGBuilderSmokeTest {

    @Test
    void buildsAtLeastOneCFGForIfTest() throws Exception {
        List<ControlFlowGraph> cfgs = PDGTestUtils.buildCFGs("sample/IfTest.java");
        assertNotNull(cfgs);
        assertFalse(cfgs.isEmpty());
    }

    @Test
    void buildsAtLeastOneCFGForWhileTest() throws Exception {
        List<ControlFlowGraph> cfgs = PDGTestUtils.buildCFGs("sample/WhileTest.java");
        assertNotNull(cfgs);
        assertFalse(cfgs.isEmpty());
    }

    @Test
    void buildsAtLeastOneCFGForDefUseTest() throws Exception {
        List<ControlFlowGraph> cfgs = PDGTestUtils.buildCFGs("sample/DefUseTest.java");
        assertNotNull(cfgs);
        assertFalse(cfgs.isEmpty());
    }

    @Test
    void cfgHasEntryAndExitNodes() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfTest.java");
        assertNotNull(cfg.entry);
        assertNotNull(cfg.normalExit);
        assertNotNull(cfg.abruptExit);
    }

    @Test
    void cfgEntryHasOutEdge() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfTest.java");
        assertFalse(cfg.entry.outEdges().isEmpty());
    }

    @Test
    void cfgNormalExitHasNoOutEdges() throws Exception {
        ControlFlowGraph cfg = PDGTestUtils.firstCFG("sample/IfTest.java");
        assertTrue(cfg.normalExit.outEdges().isEmpty());
    }
}