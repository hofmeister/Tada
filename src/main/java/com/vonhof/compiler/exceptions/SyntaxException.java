package com.vonhof.compiler.exceptions;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class SyntaxException extends RuntimeException {
    private final int line;
    private final int chr;
    private final String msg;

    protected SyntaxException(String message, int line, int chr) {
        this.line = line;
        this.chr = chr;
        this.msg = message;
    }

    @Override
    public String toString() {
        return String.format("%s: [line %s, chr %s]", msg, line, chr);
    }

}
