package com.vonhof.compiler.exceptions;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class UnexpectedCharException extends SyntaxException {

    public UnexpectedCharException(String message, int line, int chr) {
        super(message, line, chr);
    }
}
