package com.vonhof.compiler.exceptions;

import com.vonhof.compiler.Token;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class UnexpectedTokenException extends SyntaxException {

    public UnexpectedTokenException(int line, int chr) {
        super("Unexpected Token", line, chr);
    }
    
    public UnexpectedTokenException(Token token) {
        super(String.format("Unexpected Token: %s",token.getType()), token.getLine(), token.getColumn());
    }

}
