package com.vonhof.compiler;

import com.vonhof.compiler.exceptions.UnexpectedEOFException;
import com.vonhof.compiler.exceptions.UnexpectedCharException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class Lexer {

    private final char ESCAPE_CHR = '\\';
    private final char STMT_END = ';';
    private final Pattern PTRN_ASSIGN = Pattern.compile("[\\!\\*\\%\\/\\+\\-]?=");
    private final Pattern PTRN_OPERATOR = Pattern.compile("[\\!\\*\\%\\/\\+\\-]");
    private final Pattern PTRN_NUMBER = Pattern.compile("(?:[1-9][0-9]*(?:\\.[0-9]+)?|0?\\.[0-9]+)\\b");
    private final Pattern PTRN_IDENT = Pattern.compile("(?uis)[A-Z][A-Z0-9_]+\\b");
    private final Pattern PTRN_ALPHANUM = Pattern.compile("[A-Z0-9]");
    private final Pattern PTRN_COMMENT_LINE = Pattern.compile("(?!<\\\\)//[^\n]*");
    private final Pattern PTRN_COMMENT_BLOCK = Pattern.compile("(?uis)/\\*.*\\*/");
    
    
    private final String[] KEYWORDS = {
        "new",
        "var",
        "static",
        "class",
        "abstract",
        "virtual",
        "package",
        "import",
        "public",
        "protected",
        "private",
        "function",
        "for",
        "struct",
        "annotation",
        "while",
        "final",
        "inline",
        "delete",
        "interface",
        "extends",
        "implements"
    };
    
    private final String[] PRIMITIVES = {
        "int",
        "float",
        "double",
        "boolean",
        "short",
        "uint",
        "ufloat",
        "udouble",
        "ushort",
        "char",
        "byte"
    };
    
    public List<Token> lex(String input) {
        LinkedList<Token> out = new LinkedList<Token>();
        char skipUntill = 0;
        Token current = null;
        
        int line = 0;
        int column = 0;
        int lastLinebreak = 0;
        
        final Matcher assignMatcher = PTRN_ASSIGN.matcher(input);
        final Matcher operatorMatcher = PTRN_OPERATOR.matcher(input);
        final Matcher numberMatcher = PTRN_NUMBER.matcher(input);
        final Matcher identMatcher = PTRN_IDENT.matcher(input);
        final Matcher commentBlockMatcher = PTRN_COMMENT_BLOCK.matcher(input);
        final Matcher commentLineMatcher = PTRN_COMMENT_LINE.matcher(input);
                
        for(int i = 0; i < input.length();i++) {
            if (!out.isEmpty()) {
                Token last = out.get(out.size()-1);
                if (last.getLimit() > i)
                    i = last.getLimit();
                if (i >= input.length())
                    break;
            }
            
            final char chr = input.charAt(i);
            
            column = i-lastLinebreak;
            
            if (chr == ESCAPE_CHR)
                continue;
            if (chr == ' ') 
                continue;
            if (skipUntill != 0 && skipUntill != chr) {
                continue;
            }
            if (chr == '\n') {
                line++;
                lastLinebreak = i;
                continue;
            }
            
            boolean foundValidChr = true;
            
            //First try to identify token by char alone (to avoid regex matching when possible)
            switch(chr) {
                case '\'':
                case '"':
                    //String
                    if (skipUntill == chr) {
                        current.setLimit(i+1);
                        out.add(current);
                        current = null;
                        skipUntill = 0;
                    } else {
                        skipUntill = chr;
                        current = new Token(i,line,column,TokenType.STRING);
                    }
                    break;
                case STMT_END:
                    out.add(new Token(i,i+1,line,column,TokenType.END_STMT));
                    break;
                case '(':
                    out.add(new Token(i,i+1,line,column,TokenType.PARENTHESES_START));
                    break;
                case ')':
                    out.add(new Token(i,i+1,line,column,TokenType.PARENTHESES_END));
                    break;
                case '{':
                    out.add(new Token(i,i+1,line,column,TokenType.CLOSURE_START));
                    break;
                case '}':
                    out.add(new Token(i,i+1,line,column,TokenType.CLOSURE_END));
                    break;
                case '[':
                    out.add(new Token(i,i+1,line,column,TokenType.SQ_BRACKET_START));
                    break;
                case ']':
                    out.add(new Token(i,i+1,line,column,TokenType.SQ_BRACKET_END));
                    break;
                default:
                    foundValidChr = false;
                    break;
            }
            //If we found char token - continue
            if (foundValidChr) 
                continue;
            
            //Next - attempt to find tokens by regex and lookup tables
            Token next = next(assignMatcher,i,line,column,input,TokenType.ASSIGN);
            
            if (next == null)
                next = next(operatorMatcher,i,line,column,input,TokenType.OPERATOR);
            
            if (next == null)
                next = next(numberMatcher,i,line,column,input,TokenType.NUMBER);
            
            if (next == null) 
                next = getKeyword(i,line,column, input);
            
            if (next == null) 
                next = getPrimitive(i,line,column, input);
            
            if (next == null)
                next = next(identMatcher,i,line,column,input,TokenType.IDENT);
            
            if (next == null)
                next = next(commentBlockMatcher,i,line,column,input,TokenType.COMMENT);
            
            if (next == null)
                next = next(commentLineMatcher,i,line,column,input,TokenType.COMMENT);
            
            if (next != null) {
                out.add(next);
                continue;
            }
            
            //No valid token found
            throw new UnexpectedCharException(input, line, i);
        }
        
        if (skipUntill != 0)
            throw new UnexpectedEOFException(line, column);
        
        return out;
    }
    
    private Token next(Matcher m,int offset,int line,int column,String input,TokenType type) {
        if (m.region(offset,input.length()).lookingAt()) {
            return new Token(offset, m.end(),line,column, type);
        }
        return null;
    }
    
    private Token getKeyword(int offset,int line,int column,String input) {
        return lookup(TokenType.KEYWORD, KEYWORDS, offset,line,column, input);
    }
    
    private Token getPrimitive(int offset,int line,int column,String input) {
        return lookup(TokenType.PRIMITIVE, PRIMITIVES, offset,line,column, input);
    }
    
    private Token lookup(TokenType type,String[] words,int offset,int line,int column,String input) {
        final String subStr = input.substring(offset);
        for(String word:words) {
            if (subStr.startsWith(word)) {
                Matcher matcher = PTRN_ALPHANUM.matcher(subStr);
                if (!matcher.lookingAt()) {
                    return new Token(offset, offset+word.length(),line,column, type);
                }
            }
        }
        return null;
    }
}