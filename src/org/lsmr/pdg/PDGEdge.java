package org.lsmr.pdg;

public class PDGEdge {

    public enum Type {
        CONTROL,
        DATA
    }

    private final PDGNode from;
    private final PDGNode to;
    private final Type type;
    private final String variable;

    public PDGEdge(PDGNode from, PDGNode to, Type type, String variable) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.variable = variable;
    }

    public PDGNode getFrom() {
        return from;
    }

    public PDGNode getTo() {
        return to;
    }

    public Type getType() {
        return type;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PDGEdge)) return false;
        PDGEdge other = (PDGEdge) obj;
        if (!from.equals(other.from)) return false;
        if (!to.equals(other.to)) return false;
        if (type != other.type) return false;
        if (variable == null) return other.variable == null;
        return variable.equals(other.variable);
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (variable != null ? variable.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (type == Type.CONTROL) {
            return "CONTROL: " + from + " -> " + to;
        }
        return "DATA(" + variable + "): " + from + " -> " + to;
    }
}