package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CIAEvaluatorTest {

    private static final Path SAMPLE = Paths.get("sample/CIASample.java");

    private Path writeGT(Path dir, String content) throws Exception {
        Path f = dir.resolve("gt.txt");
        Files.writeString(f, content);
        return f;
    }

    // --- parseGroundTruth tests ---

    @Test
    void parsesSingleScenario(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp, "CHANGE_POINT: 4\nIDEAL: 10 12 13\n");
        List<CIAEvaluator.Scenario> scenarios = CIAEvaluator.parseGroundTruth(gt);
        assertEquals(1, scenarios.size());
        assertEquals(4, scenarios.get(0).changePoint);
        assertEquals(Arrays.asList(10, 12, 13), scenarios.get(0).idealImpacted);
    }

    @Test
    void parsesMultipleScenarios(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp,
            "CHANGE_POINT: 2\nIDEAL: 5 7 8\n\nCHANGE_POINT: 17\nIDEAL: 19 20\n");
        List<CIAEvaluator.Scenario> scenarios = CIAEvaluator.parseGroundTruth(gt);
        assertEquals(2, scenarios.size());
        assertEquals(2,  scenarios.get(0).changePoint);
        assertEquals(17, scenarios.get(1).changePoint);
    }

    @Test
    void ignoresCommentLines(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp,
            "# This is a comment\nCHANGE_POINT: 4\nIDEAL: 10 12 13\n");
        List<CIAEvaluator.Scenario> scenarios = CIAEvaluator.parseGroundTruth(gt);
        assertEquals(1, scenarios.size());
        assertEquals(4, scenarios.get(0).changePoint);
    }

    @Test
    void throwsOnEmptyFile(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp, "");
        assertThrows(IllegalArgumentException.class,
            () -> CIAEvaluator.parseGroundTruth(gt));
    }

    @Test
    void throwsOnOnlyComments(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp, "# only comments\n");
        assertThrows(IllegalArgumentException.class,
            () -> CIAEvaluator.parseGroundTruth(gt));
    }

    // --- Scenario class tests ---

    @Test
    void scenarioStoresValues() {
        CIAEvaluator.Scenario s = new CIAEvaluator.Scenario(7, Arrays.asList(9, 10, 13));
        assertEquals(7, s.changePoint);
        assertEquals(Arrays.asList(9, 10, 13), s.idealImpacted);
    }

    @Test
    void scenarioIdealIsUnmodifiable() {
        CIAEvaluator.Scenario s = new CIAEvaluator.Scenario(7, Arrays.asList(9, 10));
        assertThrows(UnsupportedOperationException.class,
            () -> s.idealImpacted.add(99));
    }

    // --- main() / full pipeline tests ---

    @Test
    void mainProducesOutputForValidInputs(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp, "CHANGE_POINT: 4\nIDEAL: 10 12 13\n");

        PrintStream oldOut = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
        try {
            CIAEvaluator.main(new String[]{SAMPLE.toString(), gt.toString()});
        } finally {
            System.setOut(oldOut);
        }

        String output = buf.toString();
        assertTrue(output.contains("MACRO-AVG"), "Output should contain MACRO-AVG line");
        assertTrue(output.contains("ChangePoint"), "Output should contain header");
    }

    @Test
    void mainReportsFPsForBlockNodes(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp, "CHANGE_POINT: 4\nIDEAL: 10 12 13\n");

        PrintStream oldOut = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
        try {
            CIAEvaluator.main(new String[]{SAMPLE.toString(), gt.toString()});
        } finally {
            System.setOut(oldOut);
        }

        assertTrue(buf.toString().contains("FP"), "Should report false positives for block nodes");
    }

    @Test
    void mainNoFalseNegativesAcrossAllChangePoints(@TempDir Path tmp) throws Exception {
        Path gt = writeGT(tmp,
            "CHANGE_POINT: 2\nIDEAL: 5 7 8 9 10 12 13\n\n" +
            "CHANGE_POINT: 4\nIDEAL: 10 12 13\n\n" +
            "CHANGE_POINT: 7\nIDEAL: 9 10 12 13\n\n" +
            "CHANGE_POINT: 17\nIDEAL: 19 20 21 22\n");

        PrintStream oldOut = System.out;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
        try {
            CIAEvaluator.main(new String[]{SAMPLE.toString(), gt.toString()});
        } finally {
            System.setOut(oldOut);
        }

        assertFalse(buf.toString().contains("FN (missed)"),
            "Should have no false negatives (recall=1.0)");
    }
}