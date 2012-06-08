/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vonhof.compiler;

import com.vonhof.compiler.exceptions.UnexpectedCharException;
import com.vonhof.compiler.exceptions.UnexpectedEOFException;
import com.vonhof.compiler.exceptions.UnexpectedTokenException;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class LexerTest extends TestCase {
    
    public LexerTest(String testName) {
        super(testName);
    }
    

    /**
     * Test of tokenize method, of class Lexer.
     */
    public void test_declare_and_assign() {
        Lexer lexer = new Lexer();
        
        List<Token> tokens = lexer.lex("var foo = 1;");
        
        assertEquals("All tokens found",5,tokens.size());
        assertEquals(TokenType.KEYWORD,tokens.get(0).getType());
        assertEquals(TokenType.IDENT,tokens.get(1).getType());
        assertEquals(TokenType.ASSIGN,tokens.get(2).getType());
        assertEquals(TokenType.NUMBER,tokens.get(3).getType());
        assertEquals(TokenType.END_STMT,tokens.get(4).getType());
        
    }
    
    public void test_unexpected_eof() {
        Lexer lexer = new Lexer();
        boolean thrown = false;
        try {
            lexer.lex("var foo = \"1;");
        } catch(UnexpectedEOFException ex) {
            thrown = true;
        }
        assertTrue("Throws unexpected EOF", thrown);
    }
    
    public void test_unexpected_char() {
        Lexer lexer = new Lexer();
        boolean thrown = false;
        try {
            lexer.lex("var foo = @;");
        } catch(UnexpectedCharException ex) {
            thrown = true;
        }
        assertTrue("Throws unexpected EOF", thrown);
    }
    
    public void test_operators() {
        Lexer lexer = new Lexer();
        
        List<Token> tokens = lexer.lex("1 * 4.3 + 2 / 54;");
        
        assertEquals("All tokens found",8,tokens.size());
        assertEquals(TokenType.NUMBER,tokens.get(0).getType());
        assertEquals(TokenType.OPERATOR,tokens.get(1).getType());
        assertEquals(TokenType.NUMBER,tokens.get(2).getType());
        assertEquals(TokenType.OPERATOR,tokens.get(3).getType());
        assertEquals(TokenType.NUMBER,tokens.get(4).getType());
        assertEquals(TokenType.OPERATOR,tokens.get(5).getType());
        assertEquals(TokenType.NUMBER,tokens.get(6).getType());
        assertEquals(TokenType.END_STMT,tokens.get(7).getType());   
    }
    
    public void test_keywords() {
        Lexer lexer = new Lexer();
        
        List<Token> tokens = lexer.lex("class Test { public var foo = 1;}");
        
        assertEquals("All tokens found",10,tokens.size());
        assertEquals(TokenType.KEYWORD,tokens.get(0).getType());
        assertEquals(TokenType.IDENT,tokens.get(1).getType());
        assertEquals(TokenType.CLOSURE_START,tokens.get(2).getType());
        assertEquals(TokenType.KEYWORD,tokens.get(3).getType());
        assertEquals(TokenType.KEYWORD,tokens.get(4).getType());
        assertEquals(TokenType.IDENT,tokens.get(5).getType());
        assertEquals(TokenType.ASSIGN,tokens.get(6).getType());
        assertEquals(TokenType.NUMBER,tokens.get(7).getType());   
        assertEquals(TokenType.END_STMT,tokens.get(8).getType());
        assertEquals(TokenType.CLOSURE_END,tokens.get(9).getType());
    }
}
