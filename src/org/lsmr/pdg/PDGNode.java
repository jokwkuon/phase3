package org.lsmr.pdg;

import org.lsmr.cfg.Node;

public class PDGNode {

    private final Node cfgNode;
    private final int lineNumber;

    public PDGNode(Node cfgNode) {
        this.cfgNode = cfgNode;
        this.lineNumber = extractLineNumber(cfgNode.label());
    }

    public Node getCfgNode() {
        return cfgNode;
    }

    public String getLabel() {
        return cfgNode.label();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    private int extractLineNumber(String label) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PDGNode)) return false;
        PDGNode other = (PDGNode) obj;
        return cfgNode.equals(other.cfgNode);
    }

    @Override
    public int hashCode() {
        return cfgNode.hashCode();
    }

    @Override
    public String toString() {
        return cfgNode.label();
    }
}