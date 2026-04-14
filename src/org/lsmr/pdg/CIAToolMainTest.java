package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class CIAToolMainTest {

    @Test
    void runRejectsWrongArgumentCount() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

        int code = CIATool.run(
            new String[]{},
            new PrintStream(outBuf),
            new PrintStream(errBuf)
        );

        assertEquals(1, code);
        assertTrue(errBuf.toString().contains("Usage:"));
    }

    @Test
    void runRejectsNonIntegerChangePoint() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

        int code = CIATool.run(
            new String[]{"sample/CIASample.java", "abc"},
            new PrintStream(outBuf),
            new PrintStream(errBuf)
        );

        assertEquals(1, code);
        assertTrue(errBuf.toString().contains("Line number must be an integer"));
    }

    @Test
    void runReportsImpactedLinesForValidInput() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

        int code = CIATool.run(
            new String[]{"sample/CIASample.java", "4"},
            new PrintStream(outBuf),
            new PrintStream(errBuf)
        );

        String output = outBuf.toString();

        assertEquals(0, code);
        assertTrue(output.contains("CHANGE_POINT: 4"));
        assertTrue(output.contains("IMPACTED:"));
        assertTrue(output.contains("IMPACTED_COUNT:"));
        assertTrue(errBuf.toString().isEmpty());
    }

    @Test
    void runReportsNoneForValidButNonexistentChangePoint() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

        int code = CIATool.run(
            new String[]{"sample/CIASample.java", "999"},
            new PrintStream(outBuf),
            new PrintStream(errBuf)
        );

        String output = outBuf.toString();

        assertEquals(0, code);
        assertTrue(output.contains("CHANGE_POINT: 999"));
        assertTrue(output.contains("IMPACTED: (none)"));
        assertTrue(output.contains("IMPACTED_COUNT: 0"));
    }

    @Test
    void runHandlesMissingFile() {
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        ByteArrayOutputStream errBuf = new ByteArrayOutputStream();

        int code = CIATool.run(
            new String[]{"sample/DOES_NOT_EXIST.java", "4"},
            new PrintStream(outBuf),
            new PrintStream(errBuf)
        );

        assertEquals(2, code);
        assertTrue(errBuf.toString().contains("Could not read file:"));
    }

    @Test
    void mainReturnsNormallyOnSuccess() {
        assertDoesNotThrow(() ->
            CIATool.main(new String[]{"sample/CIASample.java", "4"})
        );
    }
}