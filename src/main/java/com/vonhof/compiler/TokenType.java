/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vonhof.compiler;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public enum TokenType {
    ASSIGN,
    REGEX,
    STRING,
    NUMBER,
    KEYWORD,
    IDENT,
    PARENTHESES_START, 
    PARENTHESES_END, 
    CLOSURE_START, 
    CLOSURE_END, 
    END_STMT, 
    OPERATOR, 
    PRIMITIVE, 
    SQ_BRACKET_START, 
    SQ_BRACKET_END,
    COMMENT
}
