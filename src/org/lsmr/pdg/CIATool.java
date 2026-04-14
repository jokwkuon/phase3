package org.lsmr.pdg;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.lsmr.cfg.ControlFlowGraph;

/**
 * Change Impact Analysis (CIA) Tool.
 *
 * <p>Usage:
 * <pre>
 *   java org.lsmr.pdg.CIATool &lt;source-file&gt; &lt;line-number&gt;
 * </pre>
 *
 * <p>The tool builds the PDG for every method in the source file, locates
 * the PDG node(s) whose label starts with the given line-number prefix
 * ("N: ..."), and then performs a <em>forward</em> reachability traversal
 * following both DATA and CONTROL edges.  Every reachable node is considered
 * "impacted" by the change at that line.
 *
 * <p>Output format (one record per line, easy to parse):
 * <pre>
 * CHANGE_POINT: &lt;line&gt;
 * IMPACTED: &lt;line1&gt; &lt;line2&gt; ...
 * IMPACTED_COUNT: &lt;n&gt;
 * </pre>
 * Line numbers are extracted from the "N: ..." prefix that
 * {@link org.lsmr.cfg.StatementNodeBuilder} embeds in every node label.
 */
public class CIATool {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java org.lsmr.pdg.CIATool <source-file> <line-number>");
            System.exit(1);
        }

        Path input      = Paths.get(args[0]);
        int  changeNode;
        try {
            changeNode = Integer.parseInt(args[1].trim());
        } catch (NumberFormatException e) {
            System.err.println("Line number must be an integer: " + args[1]);
            System.exit(1);
            return;
        }

        try {
            List<Integer> impacted = analyse(input, changeNode);

            System.out.println("CHANGE_POINT: " + changeNode);
            if (impacted.isEmpty()) {
                System.out.println("IMPACTED: (none)");
            } else {
                StringBuilder sb = new StringBuilder("IMPACTED:");
                for (int ln : impacted) sb.append(" ").append(ln);
                System.out.println(sb);
            }
            System.out.println("IMPACTED_COUNT: " + impacted.size());

        } catch (IOException e) {
            System.err.println("Could not read file: " + input);
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Core API: returns a sorted list of line numbers (PDG node counters)
     * that are reachable from the change-point node via forward PDG traversal.
     *
     * @param sourceFile the Java source file to analyse
     * @param changePoint the node-counter (line/statement index) used as the
     *                    initial change point
     * @return sorted list of impacted node counters (excluding the change-point
     *         itself)
     */
    public static List<Integer> analyse(Path sourceFile, int changePoint) throws IOException {
        List<ControlFlowGraph> cfgs = Main.buildCFGs(sourceFile);
        ProgramDependenceGraphBuilder builder = new ProgramDependenceGraphBuilder();

        Set<Integer> impactedSet = new TreeSet<>();

        for (ControlFlowGraph cfg : cfgs) {
            ProgramDependenceGraph pdg = builder.build(cfg);

            // Find the seed node(s) whose counter matches the change point
            List<PDGNode> seeds = new ArrayList<>();
            for (PDGNode node : pdg.getNodes()) {
                if (extractCounter(node.getLabel()) == changePoint) {
                    seeds.add(node);
                }
            }

            if (seeds.isEmpty()) continue;   // change point not in this method

            // Forward BFS over PDG edges (both DATA and CONTROL)
            Set<PDGNode> visited = new HashSet<>(seeds);
            Queue<PDGNode> worklist = new LinkedList<>(seeds);

            while (!worklist.isEmpty()) {
                PDGNode current = worklist.poll();

                for (PDGEdge edge : pdg.getEdges()) {
                    if (edge.getFrom().equals(current)) {
                        PDGNode target = edge.getTo();
                        if (visited.add(target)) {
                            worklist.add(target);
                        }
                    }
                }
            }

            // Collect impacted counters (exclude the seed itself)
            for (PDGNode node : visited) {
                int counter = extractCounter(node.getLabel());
                if (counter >= 0 && counter != changePoint) {
                    impactedSet.add(counter);
                }
            }
        }

        return new ArrayList<>(impactedSet);   // already sorted (TreeSet)
    }

    /**
     * Extracts the integer counter prefix from a node label of the form
     * "N: statement text".  Returns -1 if the label does not start with
     * a numeric prefix.
     */
    public static int extractCounter(String label) {
        if (label == null) return -1;
        int colon = label.indexOf(':');
        if (colon <= 0) return -1;
        String prefix = label.substring(0, colon).trim();
        try {
            return Integer.parseInt(prefix);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}