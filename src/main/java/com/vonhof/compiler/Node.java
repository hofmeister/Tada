package com.vonhof.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class Node {
    private final NodeType type;
    private final List<Node> children = new ArrayList<Node>();
    private final List<Token> tokens = new ArrayList<Token>();

    public Node(NodeType type) {
        this.type = type;
    }

    public List<Node> children() {
        return children;
    }

    public List<Token> tokens() {
        return tokens;
    }
    
    public void add(Token token) {
        this.tokens.add(token);
    }

    public void add(Node child) {
        this.children.add(child);
    }

    public NodeType getType() {
        return type;
    }
}
