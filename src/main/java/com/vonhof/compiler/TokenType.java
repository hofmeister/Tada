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
    IDENT,
    PARENTHESES_START, 
    PARENTHESES_END, 
    CLOSURE_START, 
    CLOSURE_END, 
    SEMICOLON, COLON,COMMA,
    OPERATOR, 
    PRIMITIVE, 
    SQ_BRACKET_START, 
    SQ_BRACKET_END,
    COMMENT,
    TRUE,FALSE,
    DELETE,IMPORT,NEW,PACKAGE,NAMESPACE,
    FINAL,VOLATILE,SYNCRONIZED,VIRTUAL,
    PRIVATE,PUBLIC,PROTECTED,
    IF,ELSE,FOR,WHILE,DO,
    CLASS,INTERFACE,ANNOTATION,INLINE,ABSTRACT,STATIC,STRUCT,
    IMPLEMENTS,EXTENDS,
    LAMBDA}
