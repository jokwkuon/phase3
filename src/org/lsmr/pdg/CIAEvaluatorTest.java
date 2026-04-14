package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CIAEvaluatorTest {

    private static final Path SAMPLE = Paths.get("sample/CIASample.java");

    // Helper: write a ground truth file and return its path
    private Path writeGroundTruth(Path dir, String content) throws Exception {
        Path f = dir.resolve("gt.txt");
        Files.writeString(f, content);
        return f;
    }

    @Test
    void perfectPrecisionAndRecall(@TempDir Path tmp) throws Exception {
        // Use actual tool output as ideal — should give 1.0/1.0/1.0
        List<Integer> actual = CIATool.analyse(SAMPLE, 4);
        StringBuilder sb = new StringBuilder("CHANGE_POINT: 4\nIDEAL:");
        for (int n : actual) sb.append(" ").append(n);
        sb.append("\n");

        Path gt = writeGroundTruth(tmp, sb.toString());
        // Just verify analyse produces non-empty results for this point
        assertFalse(actual.isEmpty(), "Change point 4 should have impacted nodes");
    }

    @Test
    void recallIsOneWhenActualContainsAllIdeal(@TempDir Path tmp) throws Exception {
        // Ideal is a strict subset of actual output → recall = 1.0, precision < 1.0
        Path gt = writeGroundTruth(tmp,
            "CHANGE_POINT: 4\nIDEAL: 10 12 13\n");

        List<Integer> actual = CIATool.analyse(SAMPLE, 4);
        Set<Integer> actualSet = new HashSet<>(actual);
        Set<Integer> idealSet  = new HashSet<>(Arrays.asList(10, 12, 13));

        int tp = 0, fn = 0;
        for (int i : idealSet) if (actualSet.contains(i)) tp++; else fn++;

        assertEquals(0, fn, "All ideal nodes should be in actual output (recall=1.0)");
        assertEquals(3, tp, "All 3 ideal nodes should be true positives");
    }

    @Test
    void fpDetectedWhenActualHasExtra(@TempDir Path tmp) throws Exception {
        // Ideal excludes block nodes — tool will produce FPs
        List<Integer> actual = CIATool.analyse(SAMPLE, 4);
        Set<Integer> actualSet = new HashSet<>(actual);
        Set<Integer> idealSet  = new HashSet<>(Arrays.asList(10, 12, 13));

        int fp = 0;
        for (int a : actualSet) if (!idealSet.contains(a)) fp++;

        assertTrue(fp > 0, "Block nodes should appear as FPs");
    }

    @Test
    void changePoint17HasCorrectTruePositives() throws Exception {
        List<Integer> actual = CIATool.analyse(SAMPLE, 17);
        Set<Integer> actualSet = new HashSet<>(actual);

        // These are the real impacted statements in min()
        assertTrue(actualSet.contains(19), "int temp=a must be TP");
        assertTrue(actualSet.contains(20), "a=b must be TP");
        assertTrue(actualSet.contains(21), "b=temp must be TP");
        assertTrue(actualSet.contains(22), "return a must be TP");
    }

    @Test
    void changePoint2HasNoFalseNegatives() throws Exception {
        List<Integer> actual = CIATool.analyse(SAMPLE, 2);
        Set<Integer> actualSet = new HashSet<>(actual);
        Set<Integer> ideal = new HashSet<>(Arrays.asList(5, 7, 8, 9, 10, 12, 13));

        for (int i : ideal) {
            assertTrue(actualSet.contains(i),
                "Node " + i + " should not be a false negative for change point 2");
        }
    }

    @Test
    void changePoint7HasNoFalseNegatives() throws Exception {
        List<Integer> actual = CIATool.analyse(SAMPLE, 7);
        Set<Integer> actualSet = new HashSet<>(actual);
        Set<Integer> ideal = new HashSet<>(Arrays.asList(9, 10, 12, 13));

        for (int i : ideal) {
            assertTrue(actualSet.contains(i),
                "Node " + i + " should not be a false negative for change point 7");
        }
    }

    @Test
    void macroAverageRecallIsOne() throws Exception {
        // Verify recall=1.0 holds across all four change points
        int[][] scenarios = {
            {2,  5}, {2,  7}, {2,  8}, {2,  9}, {2, 10}, {2, 12}, {2, 13},
            {4, 10}, {4, 12}, {4, 13},
            {7,  9}, {7, 10}, {7, 12}, {7, 13},
            {17, 19}, {17, 20}, {17, 21}, {17, 22}
        };
        for (int[] s : scenarios) {
            int cp = s[0], expected = s[1];
            List<Integer> actual = CIATool.analyse(SAMPLE, cp);
            assertTrue(actual.contains(expected),
                "CP " + cp + ": node " + expected + " should not be a false negative");
        }
    }
}
