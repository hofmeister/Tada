package com.vonhof.compiler;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class Token {
    private final int offset;
    private int limit;
    private final int column;
    private final int line;
    private final TokenType type;

    public Token(int offset, int limit, int column, int line, TokenType type) {
        this.offset = offset;
        this.limit = limit;
        this.column = column;
        this.line = line;
        this.type = type;
    }

    public Token(int offset, int column, int line, TokenType type) {
        this.offset = offset;
        this.column = column;
        this.line = line;
        this.type = type;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public TokenType getType() {
        return type;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
