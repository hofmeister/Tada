package com.vonhof.compiler;

import java.util.List;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class Parser {

    
    public Node parse(String input,List<Token> tokens) {
        Node baseNode = new Node(NodeType.PROGRAM);
        Node current = null;
        for(Token token:tokens) {
            if (current == null) {
                switch(token.getType()) {
                    case KEYWORD:
                        break;
                    case ASSIGN:
                        break;
                    case CLOSURE_START:
                        break;
                    case CLOSURE_END:
                        break;
                    case PARENTHESES_START:
                        break;
                    case PARENTHESES_END:
                        break;
                }
            }
            
            switch(token.getType()) {
                case OPERATOR:
                    break;
                case ASSIGN:
                    break;
                case CLOSURE_START:
                    break;
                case CLOSURE_END:
                    break;
                case PARENTHESES_START:
                    break;
                case PARENTHESES_END:
                    break;
            }
        }
        return baseNode;
    }
}
