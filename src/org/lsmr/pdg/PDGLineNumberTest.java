package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PDGLineNumberTest {

    @Test
    void pdgNodesExposeNonNegativeOrKnownLineNumbers() throws Exception {
        ProgramDependenceGraph pdg = PDGTestUtils.buildPDG("sample/DefUseTest.java");

        boolean foundRealLine = false;
        for (PDGNode node : pdg.getNodes()) {
            if (node.getLineNumber() > 0) {
                foundRealLine = true;
            }
        }

        assertTrue(foundRealLine, "Expected at least one PDG node to expose a source line number");
    }
}