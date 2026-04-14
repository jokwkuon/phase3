package org.lsmr.pdg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Evaluator for the CIA Tool.
 *
 * <p>Reads a ground-truth file (plain text, one scenario per block) and
 * compares the tool's actual output against the ideal (manually-derived)
 * output.  Computes precision, recall, and F1 for each change point and
 * a macro-average across all change points.
 *
 * <p><b>Ground-truth file format</b> (one or more blocks separated by blank
 * lines):
 * <pre>
 * CHANGE_POINT: 3
 * IDEAL: 5 7 9 12
 * </pre>
 * Lines beginning with '#' are treated as comments and ignored.
 *
 * <p>Usage:
 * <pre>
 *   java org.lsmr.pdg.CIAEvaluator &lt;source-file&gt; &lt;ground-truth-file&gt;
 * </pre>
 */
public class CIAEvaluator {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java org.lsmr.pdg.CIAEvaluator <source-file> <ground-truth-file>");
            System.exit(1);
        }

        Path sourceFile    = Paths.get(args[0]);
        Path groundTruth   = Paths.get(args[1]);

        List<Scenario> scenarios = parseGroundTruth(groundTruth);

        System.out.printf("%-14s %-12s %-12s %-12s %-12s %-12s %-8s%n",
                "ChangePoint", "TP", "FP", "FN", "Precision", "Recall", "F1");
        System.out.println("-".repeat(82));

        double sumP = 0, sumR = 0, sumF = 0;

        for (Scenario s : scenarios) {
            List<Integer> actual = CIATool.analyse(sourceFile, s.changePoint);
            Set<Integer> actualSet = new HashSet<>(actual);
            Set<Integer> idealSet  = new HashSet<>(s.idealImpacted);

            int tp = 0, fp = 0, fn = 0;
            for (int a : actualSet) if (idealSet.contains(a)) tp++; else fp++;
            for (int i : idealSet)  if (!actualSet.contains(i)) fn++;

            double precision = (tp + fp == 0) ? 1.0 : (double) tp / (tp + fp);
            double recall    = (tp + fn == 0) ? 1.0 : (double) tp / (tp + fn);
            double f1        = (precision + recall == 0) ? 0.0
                                : 2 * precision * recall / (precision + recall);

            sumP += precision; sumR += recall; sumF += f1;

            System.out.printf("%-14d %-12d %-12d %-12d %-12.3f %-12.3f %-8.3f%n",
                    s.changePoint, tp, fp, fn, precision, recall, f1);

            // Detail: false positives and false negatives
            Set<Integer> fpSet = new HashSet<>(actualSet); fpSet.removeAll(idealSet);
            Set<Integer> fnSet = new HashSet<>(idealSet);  fnSet.removeAll(actualSet);
            if (!fpSet.isEmpty()) System.out.println("    FP (spurious): " + sorted(fpSet));
            if (!fnSet.isEmpty()) System.out.println("    FN (missed):   " + sorted(fnSet));
        }

        System.out.println("-".repeat(82));
        int n = scenarios.size();
        System.out.printf("%-14s %-36s %-12.3f %-12.3f %-8.3f%n",
                "MACRO-AVG", "",
                sumP / n, sumR / n, sumF / n);
    }

    // -----------------------------------------------------------------------

    private static List<Scenario> parseGroundTruth(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        List<Scenario> result = new ArrayList<>();

        Integer changePoint = null;
        List<Integer> ideal = new ArrayList<>();

        for (String raw : lines) {
            String line = raw.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                if (changePoint != null && !ideal.isEmpty()) {
                    result.add(new Scenario(changePoint, ideal));
                    changePoint = null;
                    ideal = new ArrayList<>();
                }
                continue;
            }
            if (line.startsWith("CHANGE_POINT:")) {
                if (changePoint != null && !ideal.isEmpty()) {
                    result.add(new Scenario(changePoint, ideal));
                    ideal = new ArrayList<>();
                }
                changePoint = Integer.parseInt(line.substring("CHANGE_POINT:".length()).trim());
            } else if (line.startsWith("IDEAL:")) {
                String rest = line.substring("IDEAL:".length()).trim();
                for (String tok : rest.split("\\s+")) {
                    if (!tok.isEmpty()) ideal.add(Integer.parseInt(tok));
                }
            }
        }
        if (changePoint != null && !ideal.isEmpty()) {
            result.add(new Scenario(changePoint, ideal));
        }

        if (result.isEmpty()) throw new IllegalArgumentException("No scenarios found in " + file);
        return result;
    }

    private static List<Integer> sorted(Set<Integer> s) {
        List<Integer> l = new ArrayList<>(s);
        Collections.sort(l);
        return l;
    }

    static class Scenario {
        final int changePoint;
        final List<Integer> idealImpacted;
        Scenario(int cp, List<Integer> ideal) {
            this.changePoint    = cp;
            this.idealImpacted  = Collections.unmodifiableList(new ArrayList<>(ideal));
        }
    }
}