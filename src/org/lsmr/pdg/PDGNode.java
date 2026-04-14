 package org.lsmr.pdg;

import org.lsmr.cfg.Node;

public class PDGNode {

    private final Node cfgNode;

    public PDGNode(Node cfgNode) {
        this.cfgNode = cfgNode;
    }

    public Node getCfgNode() {
        return cfgNode;
    }

    public String getLabel() {
        return cfgNode.label();
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