package org.lsmr.pdg;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.lsmr.cfg.ControlFlowGraph;

public class CIATool {

    public static void main(String[] args) {
        int exit = run(args, System.out, System.err);
        if (exit != 0) {
            System.exit(exit);
        }
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        if (args.length != 2) {
            err.println("Usage: java org.lsmr.pdg.CIATool <source-file> <line-number>");
            return 1;
        }

        Path input = Paths.get(args[0]);
        int changeNode;
        try {
            changeNode = Integer.parseInt(args[1].trim());
        } catch (NumberFormatException e) {
            err.println("Line number must be an integer: " + args[1]);
            return 1;
        }

        try {
            List<Integer> impacted = analyse(input, changeNode);

            out.println("CHANGE_POINT: " + changeNode);
            if (impacted.isEmpty()) {
                out.println("IMPACTED: (none)");
            } else {
                StringBuilder sb = new StringBuilder("IMPACTED:");
                for (int ln : impacted) sb.append(" ").append(ln);
                out.println(sb);
            }
            out.println("IMPACTED_COUNT: " + impacted.size());
            return 0;

        } catch (IOException e) {
            err.println("Could not read file: " + input);
            e.printStackTrace(err);
            return 2;
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
