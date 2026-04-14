package org.lsmr.pdg;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.lsmr.cfg.ControlFlowGraph;

public class CIATool {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java org.lsmr.pdg.CIATool <source-file> <line-number>");
            System.exit(1);
        }

        Path input = Paths.get(args[0]);
        int changeNode;
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

    public static List<Integer> analyse(Path sourceFile, int changePoint) throws IOException {
        List<ControlFlowGraph> cfgs = Main.buildCFGs(sourceFile);
        ProgramDependenceGraphBuilder builder = new ProgramDependenceGraphBuilder();

        for (ControlFlowGraph cfg : cfgs) {
            ProgramDependenceGraph pdg = builder.build(cfg);

            List<PDGNode> seeds = new ArrayList<>();
            for (PDGNode node : pdg.getNodes()) {
                if (extractCounter(node.getLabel()) == changePoint) {
                    seeds.add(node);
                }
            }

            if (seeds.isEmpty()) continue;

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

            Set<Integer> impactedSet = new TreeSet<>();
            for (PDGNode node : visited) {
                int counter = extractCounter(node.getLabel());
                if (counter >= 0 && counter != changePoint) {
                    impactedSet.add(counter);
                }
            }

            // CRITICAL FIX: return immediately after finding the method
            // containing the change point — do not bleed into other methods
            return new ArrayList<>(impactedSet);
        }

        return Collections.emptyList();
    }

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
