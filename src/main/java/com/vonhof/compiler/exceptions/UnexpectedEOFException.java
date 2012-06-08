package com.vonhof.compiler.exceptions;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class UnexpectedEOFException extends SyntaxException {

    public UnexpectedEOFException(int line, int chr) {
        super("Unexpected EOF", line, chr);
    }

}
