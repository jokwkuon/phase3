package org.lsmr.pdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lsmr.cfg.ControlFlowGraph;
import org.lsmr.cfg.Edge;
import org.lsmr.cfg.Node;

public class DataDependenceBuilder {

    private static final String ENTRY  = "*ENTRY*";
    private static final String EXIT   = "*EXIT*";
    private static final String THROWN = "*THROWN*";

    public void addDataDependences(ControlFlowGraph cfg,
                                   ProgramDependenceGraph pdg,
                                   Map<Node, PDGNode> nodeMap) {

        List<Node> allNodes = cfg.nodes();

        Map<Node, Set<String>> genVar    = new HashMap<>();
        Map<Node, Set<Node>> killDefs    = new HashMap<>();
        Map<String, List<Node>> allDefsOf = new HashMap<>();

        for (Node n : allNodes) {
            if (isBookkeeping(n)) continue;
            String var = getDefinedVariable(n.label());
            if (var != null) {
                allDefsOf.computeIfAbsent(var, k -> new ArrayList<>()).add(n);
            }
        }

        for (Node n : allNodes) {
            genVar.put(n, new HashSet<>());
            killDefs.put(n, new HashSet<>());
            if (isBookkeeping(n)) continue;

            String var = getDefinedVariable(n.label());
            if (var != null) {
                genVar.get(n).add(var);
                List<Node> others = allDefsOf.get(var);
                if (others != null) {
                    for (Node other : others) {
                        if (!other.equals(n)) {
                            killDefs.get(n).add(other);
                        }
                    }
                }
            }
        }

        Map<Node, Set<Node>> inDefs = new HashMap<>();
        Map<Node, Set<Node>> outDefs = new HashMap<>();
        for (Node n : allNodes) {
            inDefs.put(n, new HashSet<>());
            outDefs.put(n, new HashSet<>());
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Node n : allNodes) {
                Set<Node> newIn = new HashSet<>();
                for (Edge e : n.inEdges()) {
                    Set<Node> predOut = outDefs.get(e.source());
                    if (predOut != null) {
                        newIn.addAll(predOut);
                    }
                }

                Set<Node> newOut = new HashSet<>(newIn);
                newOut.removeAll(killDefs.get(n));
                if (!genVar.get(n).isEmpty()) {
                    newOut.add(n);
                }

                inDefs.put(n, newIn);
                if (!newOut.equals(outDefs.get(n))) {
                    outDefs.put(n, newOut);
                    changed = true;
                }
            }
        }

        for (Node useNode : allNodes) {
            if (isBookkeeping(useNode)) continue;

            PDGNode pdgUseNode = nodeMap.get(useNode);
            if (pdgUseNode == null) continue;

            Set<String> usedVars = getUsedVariables(useNode.label());
            Set<Node> reaching = inDefs.get(useNode);
            if (reaching == null) continue;

            for (String var : usedVars) {
                for (Node defNode : reaching) {
                    if (isBookkeeping(defNode)) continue;
                    Set<String> defVars = genVar.get(defNode);
                    if (defVars != null && defVars.contains(var)) {
                        PDGNode pdgDefNode = nodeMap.get(defNode);
                        if (pdgDefNode != null) {
                            pdg.addEdge(new PDGEdge(pdgDefNode, pdgUseNode, PDGEdge.Type.DATA, var));
                        }
                    }
                }
            }
        }
    }

    private String getDefinedVariable(String label) {
        String s = stripLinePrefix(label).trim();
        s = stripCommentsAndLiterals(s).trim();

        if (s.startsWith("for")) {
            String inside = contentsInsideOuterParens(s);
            if (inside != null) {
                int firstSemi = firstTopLevelChar(inside, ';');
                if (firstSemi >= 0) {
                    return getDefinedVariable(inside.substring(0, firstSemi));
                }
            }
        }

        if (s.endsWith("++ ;") || s.endsWith("++;") || s.endsWith("++")) {
            String c = s.replaceAll("\\+\\+\\s*;?\\s*$", "").trim();
            if (isSimpleIdentifier(c)) return c;
        }
        if (s.endsWith("-- ;") || s.endsWith("--;") || s.endsWith("--")) {
            String c = s.replaceAll("--\\s*;?\\s*$", "").trim();
            if (isSimpleIdentifier(c)) return c;
        }
        if (s.startsWith("++") || s.startsWith("--")) {
            String c = s.substring(2).replaceAll(";$", "").trim();
            if (isSimpleIdentifier(c)) return c;
        }

        String stripped = stripTypePrefix(s);
        int eq = indexOfTopLevelAssignmentOp(stripped);
        if (eq > 0) {
            String left = stripped.substring(0, eq).trim();
            left = stripEnclosingParens(left);

            int bracket = left.indexOf('[');
            if (bracket > 0) {
                left = left.substring(0, bracket).trim();
            }

            int dot = left.lastIndexOf('.');
            if (dot >= 0 && dot + 1 < left.length()) {
                left = left.substring(dot + 1).trim();
            }

            if (isSimpleIdentifier(left)) return left;
        }
        return null;
    }

    private Set<String> getUsedVariables(String label) {
        Set<String> used = new LinkedHashSet<>();
        String s = stripLinePrefix(label).trim();
        s = stripCommentsAndLiterals(s).trim();
        if (s.isEmpty()) return used;

        if (s.startsWith("for")) {
            collectForUses(s, used);
            return used;
        }
        if (s.startsWith("if")) {
            collectDelimitedExpressionUses(s, used);
            return used;
        }
        if (s.startsWith("while")) {
            collectDelimitedExpressionUses(s, used);
            return used;
        }
        if (s.startsWith("switch")) {
            collectDelimitedExpressionUses(s, used);
            return used;
        }
        if (s.startsWith("return ")) {
            collectIdentifiersFromExpression(s.substring("return".length()), used);
            return used;
        }
        if (s.startsWith("throw ")) {
            collectIdentifiersFromExpression(s.substring("throw".length()), used);
            return used;
        }
        if (s.startsWith("case ")) {
            collectIdentifiersFromExpression(s.substring("case".length()), used);
            return used;
        }
        if (s.startsWith("catch")) {
            String inside = contentsInsideOuterParens(s);
            if (inside != null) {
                int lastSpace = inside.lastIndexOf(' ');
                if (lastSpace > 0) {
                    collectIdentifiersFromExpression(inside.substring(0, lastSpace), used);
                } else {
                    collectIdentifiersFromExpression(inside, used);
                }
            }
            return used;
        }

        if (isPostIncDec(s) || isPreIncDec(s)) {
            String c = extractIncDecIdentifier(s);
            if (isSimpleIdentifier(c)) used.add(c);
            return used;
        }

        String stripped = stripTypePrefix(s);
        int eq = indexOfTopLevelAssignmentOp(stripped);
        if (eq > 0) {
            String lhs = stripped.substring(0, eq).trim();
            String rhs = stripped.substring(eq + 1).trim();

            collectIdentifiersFromExpression(rhs, used);
            collectArrayIndexUses(lhs, used);

            if (eq > 0 && "+-*/%&|^".indexOf(stripped.charAt(eq - 1)) >= 0) {
                String lhsVar = lhs;
                int bracket = lhsVar.indexOf('[');
                if (bracket > 0) lhsVar = lhsVar.substring(0, bracket).trim();
                int dot = lhsVar.lastIndexOf('.');
                if (dot >= 0 && dot + 1 < lhsVar.length()) lhsVar = lhsVar.substring(dot + 1).trim();
                lhsVar = stripEnclosingParens(lhsVar);
                if (isSimpleIdentifier(lhsVar)) used.add(lhsVar);
            }
            return used;
        }

        collectIdentifiersFromExpression(s, used);
        return used;
    }

    private void collectForUses(String statement, Set<String> used) {
        String inside = contentsInsideOuterParens(statement);
        if (inside == null) {
            collectIdentifiersFromExpression(statement, used);
            return;
        }

        int firstSemi = firstTopLevelChar(inside, ';');
        int secondSemi = firstSemi < 0 ? -1 : firstTopLevelChar(inside, ';', firstSemi + 1);

        if (firstSemi < 0 || secondSemi < 0) {
            collectIdentifiersFromExpression(inside, used);
            return;
        }

        String init = inside.substring(0, firstSemi);
        String condition = inside.substring(firstSemi + 1, secondSemi);
        String update = inside.substring(secondSemi + 1);

        int initAssign = indexOfTopLevelAssignmentOp(stripTypePrefix(init));
        if (initAssign >= 0) {
            String initRhs = stripTypePrefix(init).substring(initAssign + 1);
            collectIdentifiersFromExpression(initRhs, used);
        } else {
            collectIdentifiersFromExpression(init, used);
        }

        collectIdentifiersFromExpression(condition, used);
        collectIdentifiersFromExpression(update, used);
    }

    private void collectDelimitedExpressionUses(String statement, Set<String> used) {
        String inside = contentsInsideOuterParens(statement);
        if (inside != null) {
            collectIdentifiersFromExpression(inside, used);
        }
    }

    private void collectArrayIndexUses(String lhs, Set<String> used) {
        int open = lhs.indexOf('[');
        while (open >= 0) {
            int close = findMatchingBracket(lhs, open, '[', ']');
            if (close < 0) break;
            String idx = lhs.substring(open + 1, close);
            collectIdentifiersFromExpression(idx, used);
            open = lhs.indexOf('[', close + 1);
        }
    }

    private void collectIdentifiersFromExpression(String expr, Set<String> result) {
        if (expr == null) return;
        String text = stripCommentsAndLiterals(expr);
        int n = text.length();

        for (int i = 0; i < n; i++) {
            char ch = text.charAt(i);
            if (!Character.isJavaIdentifierStart(ch)) continue;

            int start = i;
            i++;
            while (i < n && Character.isJavaIdentifierPart(text.charAt(i))) {
                i++;
            }
            String token = text.substring(start, i);
            int prev = previousNonWhitespace(text, start - 1);
            int next = nextNonWhitespace(text, i);

            boolean precededByDot = prev >= 0 && text.charAt(prev) == '.';
            boolean methodName = next < n && text.charAt(next) == '(';
            boolean classLiteral = next + 5 < n && text.startsWith(".class", i);

            if (!isKeyword(token)
                    && !precededByDot
                    && !methodName
                    && !classLiteral) {
                result.add(token);
            }

            i--;
        }
    }

    private String stripTypePrefix(String s) {
        String trimmed = s.trim();
        String[] primitives = {"byte ", "short ", "char ", "int ", "long ",
                               "float ", "double ", "boolean "};
        for (String p : primitives) {
            if (trimmed.startsWith(p)) return trimmed.substring(p.length()).trim();
        }

        int genericBoundary = firstTypeNameBoundary(trimmed);
        if (genericBoundary > 0) {
            String first = trimmed.substring(0, genericBoundary).trim();
            String rest = trimmed.substring(genericBoundary).trim();
            if (looksLikeReferenceType(first) && indexOfTopLevelAssignmentOp(rest) >= 0) {
                return rest;
            }
        }
        return trimmed;
    }

    private int indexOfTopLevelAssignmentOp(String s) {
        int parenDepth = 0;
        int bracketDepth = 0;
        int braceDepth = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '(':
                    parenDepth++;
                    continue;
                case ')':
                    parenDepth--;
                    continue;
                case '[':
                    bracketDepth++;
                    continue;
                case ']':
                    bracketDepth--;
                    continue;
                case '{':
                    braceDepth++;
                    continue;
                case '}':
                    braceDepth--;
                    continue;
                default:
                    break;
            }

            if (parenDepth != 0 || bracketDepth != 0 || braceDepth != 0) {
                continue;
            }

            if (c == '=') {
                if (i + 1 < s.length() && s.charAt(i + 1) == '=') { i++; continue; }
                if (i > 0 && "!<>".indexOf(s.charAt(i - 1)) >= 0) continue;
                return i;
            }
            if ("+-*/%&|^".indexOf(c) >= 0 && i + 1 < s.length() && s.charAt(i + 1) == '=') {
                return i + 1;
            }
        }
        return -1;
    }

    private String stripCommentsAndLiterals(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        boolean inString = false;
        boolean inChar = false;
        boolean escaping = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (inString) {
                if (escaping) {
                    escaping = false;
                } else if (c == '\\') {
                    escaping = true;
                } else if (c == '"') {
                    inString = false;
                }
                sb.append(' ');
                continue;
            }

            if (inChar) {
                if (escaping) {
                    escaping = false;
                } else if (c == '\\') {
                    escaping = true;
                } else if (c == '\'') {
                    inChar = false;
                }
                sb.append(' ');
                continue;
            }

            if (c == '"') {
                inString = true;
                sb.append(' ');
                continue;
            }
            if (c == '\'') {
                inChar = true;
                sb.append(' ');
                continue;
            }
            if (c == '/' && i + 1 < text.length() && text.charAt(i + 1) == '/') {
                break;
            }

            sb.append(c);
        }
        return sb.toString();
    }

    private String contentsInsideOuterParens(String text) {
        int open = text.indexOf('(');
        if (open < 0) return null;
        int close = findMatchingBracket(text, open, '(', ')');
        if (close < 0) return null;
        return text.substring(open + 1, close);
    }

    private int findMatchingBracket(String text, int openIndex, char open, char close) {
        int depth = 0;
        for (int i = openIndex; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == open) depth++;
            else if (c == close) {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private int firstTopLevelChar(String text, char target) {
        return firstTopLevelChar(text, target, 0);
    }

    private int firstTopLevelChar(String text, char target, int start) {
        int parenDepth = 0;
        int bracketDepth = 0;
        int braceDepth = 0;
        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '(':
                    parenDepth++;
                    break;
                case ')':
                    parenDepth--;
                    break;
                case '[':
                    bracketDepth++;
                    break;
                case ']':
                    bracketDepth--;
                    break;
                case '{':
                    braceDepth++;
                    break;
                case '}':
                    braceDepth--;
                    break;
                default:
                    break;
            }
            if (c == target && parenDepth == 0 && bracketDepth == 0 && braceDepth == 0) {
                return i;
            }
        }
        return -1;
    }

    private int previousNonWhitespace(String text, int index) {
        while (index >= 0 && Character.isWhitespace(text.charAt(index))) {
            index--;
        }
        return index;
    }

    private int nextNonWhitespace(String text, int index) {
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
        return index;
    }

    private int firstTypeNameBoundary(String text) {
        int depth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '<') depth++;
            else if (c == '>') depth--;
            else if (depth == 0 && Character.isWhitespace(c)) return i;
        }
        return -1;
    }

    private boolean looksLikeReferenceType(String token) {
        if (token.isEmpty()) return false;
        String cleaned = token.replace("[]", "");
        int lastDot = cleaned.lastIndexOf('.');
        if (lastDot >= 0 && lastDot + 1 < cleaned.length()) {
            cleaned = cleaned.substring(lastDot + 1);
        }
        return !cleaned.isEmpty() && Character.isUpperCase(cleaned.charAt(0));
    }

    private boolean isPostIncDec(String s) {
        return s.matches(".*[A-Za-z_][A-Za-z0-9_]*\\s*(\\+\\+|--)\\s*;?");
    }

    private boolean isPreIncDec(String s) {
        return s.startsWith("++") || s.startsWith("--");
    }

    private String extractIncDecIdentifier(String s) {
        String cleaned = s.replaceAll("^\\s*(\\+\\+|--)\\s*", "")
                          .replaceAll("\\s*(\\+\\+|--)\\s*;?\\s*$", "")
                          .trim();
        return cleaned;
    }

    private String stripEnclosingParens(String text) {
        String result = text.trim();
        while (result.startsWith("(") && result.endsWith(")")) {
            int close = findMatchingBracket(result, 0, '(', ')');
            if (close != result.length() - 1) break;
            result = result.substring(1, result.length() - 1).trim();
        }
        return result;
    }

    private String stripLinePrefix(String label) {
        int colon = label.indexOf(':');
        if (colon >= 0 && colon + 1 < label.length())
            return label.substring(colon + 1).trim();
        return label.trim();
    }

    private boolean isSimpleIdentifier(String text) {
        return text != null && text.matches("[A-Za-z_][A-Za-z0-9_]*");
    }

    private boolean isBookkeeping(Node n) {
        String label = n.label();
        return label.equals(ENTRY) || label.equals(EXIT) || label.equals(THROWN);
    }

    private boolean isKeyword(String token) {
        switch (token) {
            case "if": case "else": case "while": case "for": case "do":
            case "return": case "break": case "continue": case "throw":
            case "try": case "catch": case "finally": case "switch": case "case":
            case "new": case "this": case "super": case "instanceof":
            case "true": case "false": case "null":
            case "int": case "long": case "double": case "float": case "boolean":
            case "byte": case "short": case "char": case "void":
            case "class": case "interface": case "extends": case "implements":
            case "static": case "final": case "public": case "private":
            case "protected": case "abstract": case "synchronized":
                return true;
            default:
                return false;
        }
    }
}
