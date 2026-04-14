package org.lsmr.pdg;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRLexer;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.CompilationUnitContext;
import org.lsmr.cfg.ControlFlowGraph;
import org.lsmr.cfg.Edge;
import org.lsmr.cfg.Node;
import org.lsmr.cfg.StatementNodeBuilder;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java org.lsmr.pdg.Main <Java-source-file>");
            System.exit(1);
        }

        Path input = Paths.get(args[0]);

        try {
            List<ControlFlowGraph> cfgs = buildCFGs(input);

            System.out.println("Built " + cfgs.size() + " CFG(s) from: " + input);
            System.out.println();

            ProgramDependenceGraphBuilder pdgBuilder = new ProgramDependenceGraphBuilder();

            for (ControlFlowGraph cfg : cfgs) {
                printCFG(cfg);

                ProgramDependenceGraph pdg = pdgBuilder.build(cfg);
                printPDG(pdg);

                // Write DOT file next to the input file
                String safeName = cfg.name().replaceAll("[^A-Za-z0-9_]", "_");
                Path outDir = input.toAbsolutePath().getParent();
                Path dotFile = outDir.resolve(safeName + "_pdg.dot");
                Files.write(dotFile, toDot(pdg, cfg.name()).getBytes(StandardCharsets.UTF_8));

                System.out.println("Wrote DOT file: " + dotFile);
                System.out.println("--------------------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Could not read file: " + input);
            e.printStackTrace();
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Failed to parse/build CFG or PDG for: " + input);
            e.printStackTrace();
            System.exit(3);
        }
    }

    // parses a Java 1.2 source file and returns one CFG per method
    public static List<ControlFlowGraph> buildCFGs(Path inputFile) throws IOException {
        Java1_2ANTLRLexer lexer =
            new Java1_2ANTLRLexer(CharStreams.fromPath(inputFile));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java1_2ANTLRParser parser = new Java1_2ANTLRParser(tokens);

        CompilationUnitContext root = parser.compilationUnit();

        StatementNodeBuilder builder = new StatementNodeBuilder();
        root.accept(builder);

        return builder.getCFGs();
    }

    private static void printCFG(ControlFlowGraph cfg) {
        System.out.println("=== CFG: " + cfg.name() + " ===");
        System.out.println("Nodes:");
        for (Node node : cfg.nodes()) {
            System.out.println("  " + node.label());
        }
        System.out.println("Edges:");
        for (Edge edge : cfg.edges()) {
            String lbl   = edge.label().toString();
            String extra = edge.extendedLabel();
            String text  = "  " + edge.source().label()
                         + " -> "
                         + (edge.target() == null ? "null" : edge.target().label());
            if (!lbl.isEmpty()) {
                text += " [" + lbl;
                if (extra != null && !extra.isEmpty()) text += ": " + extra;
                text += "]";
            }
            System.out.println(text);
        }
        System.out.println();
    }

    private static void printPDG(ProgramDependenceGraph pdg) {
        System.out.println("PDG Edges:");
        for (PDGEdge edge : pdg.getEdges()) {
            System.out.println("  " + edge);
        }
        System.out.println();
    }

    // writes a Graphviz DOT file for the given PDG
    private static String toDot(ProgramDependenceGraph pdg, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ").append(name.replaceAll("[^A-Za-z0-9_]", "_")).append(" {\n");
        sb.append("  rankdir=TB;\n");

        java.util.Map<PDGNode, String> ids = new java.util.HashMap<>();
        int i = 0;
        for (PDGNode node : pdg.getNodes()) {
            String id = "n" + i++;
            ids.put(node, id);
            sb.append("  ").append(id)
              .append(" [label=\"").append(node.getLabel().replace("\"", "\\\"")).append("\"];\n");
        }

        for (PDGEdge edge : pdg.getEdges()) {
            String from = ids.get(edge.getFrom());
            String to   = ids.get(edge.getTo());
            if (from == null || to == null) continue;
            String lbl = edge.getType() == PDGEdge.Type.DATA
                       ? "DATA(" + edge.getVariable() + ")"
                       : "CONTROL";
            sb.append("  ").append(from).append(" -> ").append(to)
              .append(" [label=\"").append(lbl).append("\"];\n");
        }

        sb.append("}\n");
        return sb.toString();
    }
}