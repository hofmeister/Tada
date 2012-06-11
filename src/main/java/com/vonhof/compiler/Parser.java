package com.vonhof.compiler;

import java.util.*;

/**
 *
 * @author Henrik Hofmeister <@vonhofdk>
 */
public class Parser {
    private static final List<Sequence> sequences = new ArrayList<Sequence>();
    static {
        
        final Sequence modifiers = new Sequence().add(new RandomOrderFinder()
                                            .add(true,TokenType.ABSTRACT,TokenType.STATIC,TokenType.FINAL)
                                            .add(new OneOfFinder(true)
                                                .add(TokenType.PRIVATE,TokenType.PROTECTED,TokenType.PUBLIC)));
        
        //Basic values
        final OneOfFinder value = new OneOfFinder().add(TokenType.IDENT,TokenType.NUMBER,TokenType.STRING,
                                                    TokenType.TRUE,TokenType.FALSE)
                                                .add(NodeType.EXPRESSION);
        //Add inline JS-like structures
        final Sequence inlineObject = new Sequence().add(TokenType.CLOSURE_START)
                                                    .add(TokenType.COMMA,new Sequence()
                                                        .add(TokenType.IDENT,TokenType.COLON).add(NodeType.EXPRESSION))
                                                    .add(TokenType.CLOSURE_END);
        value.add(inlineObject);
        
        final Sequence inlineArray = new Sequence().add(TokenType.SQ_BRACKET_START)
                                                    .add(false,TokenType.COMMA,new Sequence().add(NodeType.EXPRESSION))
                                                    .add(TokenType.SQ_BRACKET_END);
        value.add(inlineArray);
        
        //Var type
        Finder type = new OneOfFinder()
                            .add(TokenType.IDENT,TokenType.PRIMITIVE);
        
        //Expression
        OneOfFinder expression =
            new OneOfFinder()
                    .add(value)
                    .add(TokenType.LAMBDA)
                    .add(new Sequence()
                            .add(NodeType.EXPRESSION)
                                .add(TokenType.OPERATOR)
                            .add(NodeType.EXPRESSION))
                    .add(new Sequence()
                            .add(TokenType.PARENTHESES_START)
                                    .add(NodeType.EXPRESSION)
                            .add(TokenType.PARENTHESES_END));
        
        //Add expressions and typecasting
        sequences.add(new Sequence(NodeType.EXPRESSION)
                    .add(true,new Sequence().add(TokenType.PARENTHESES_START).add(type).add(TokenType.PARENTHESES_END))
                    .add(expression));

        //Assign statement
        sequences.add(new Sequence(NodeType.STATEMENT)
                            .add(true,new Sequence().add(type))
                            .add(TokenType.IDENT,TokenType.ASSIGN)
                            .add(NodeType.EXPRESSION)
                            .add(TokenType.SEMICOLON));
        
        
    }
    
    public Node parse(String input,List<Token> tokens) {
        Node baseNode = new Node(NodeType.PROGRAM);
        Node current = null;
        for(Token token:tokens) {
            
        }
        return baseNode;
    }
    
    
    private static class Sequence {
        private final List<Finder> finders = new LinkedList<Finder>();
        private final NodeType type;

        public Sequence(NodeType type) {
            this.type = type;
        }
        public Sequence() {
            this(NodeType.FRAGMENT);
        }
        
        
        protected Sequence add(Finder ... finders) {
            for(Finder finder:finders) {
                this.finders.add(finder);
            }
            return this;
        }
        
        //Add sequence
        protected Sequence add(Sequence sequence) {
            return add(false, sequence);
        }
        
        protected Sequence add(boolean optional,Sequence sequence) {
            return add(optional, null, sequence);
        }
        protected Sequence add(TokenType sep,Sequence sequence) {
            return add(false, sep,sequence);
        }
        
        protected Sequence add(boolean optional,TokenType sep,Sequence sequence) {
            return add(optional, sep != null,sep,sequence);
        }
        
        protected Sequence add(boolean optional,boolean multi,TokenType sep,Sequence sequence) {
            finders.add(new SequenceFinder(sequence, optional, multi,sep));
            return this;
        }
        
        //Add token types
        protected Sequence add(TokenType ... types) {
            return add(false, types);
        }
        
        protected Sequence add(boolean optional,TokenType ... types) {
            return add(optional, null, types);
        }
        protected Sequence addMulti(TokenType sep,TokenType type) {
            return add(false,sep,new TokenType[] {type});
        }
        
        protected Sequence add(boolean optional,TokenType sep,TokenType ... types) {
            return add(optional, sep != null, sep, types);
        }
        
        protected Sequence add(boolean optional,boolean multi,TokenType sep,TokenType ... types) {
            for(TokenType type:types) {
                this.finders.add(new TokenFinder(type, optional,multi));
            }
            return this;
        }
        
        //Add node types
        
        protected Sequence add(NodeType ... types) {
            return add(false, types);
        }
        
        protected Sequence add(boolean optional,NodeType ... types) {
            return add(optional, false, types);
        }
        
        protected Sequence add(boolean optional,boolean multi,NodeType ... types) {
            for(NodeType type:types) {
                this.finders.add(new NodeFinder(type, optional,multi));
            }
            return this;
        }
        
        public NodeType getType() {
            return type;
        }
    }
    
    private abstract static class Finder {
        private final boolean optional;
        private final boolean multi;
        private final TokenType sep;

        public Finder(boolean optional, boolean multi, TokenType sep) {
            this.optional = optional;
            this.multi = multi;
            this.sep = sep;
        }
        
        

        public Finder(boolean optional, boolean multi) {
            this(optional, multi, null);
        }

        public boolean isMulti() {
            return multi;
        }

        public boolean isOptional() {
            return optional;
        }
    }
    
    private static class TokenFinder extends Finder {
        private final TokenType token;

        public TokenFinder(TokenType token, boolean optional, boolean multi) {
            super(optional, multi);
            this.token = token;
        }
    }
    
    private static class NodeFinder extends Finder {
        private final NodeType token;

        public NodeFinder(NodeType token, boolean optional, boolean multi) {
            super(optional, multi);
            this.token = token;
        }
    }
    
    private static class SequenceFinder extends Finder {
        private final Sequence sequence;

        public SequenceFinder(Sequence sequence, boolean optional, boolean multi) {
            super(optional, multi);
            this.sequence = sequence;
        }

        public SequenceFinder(Sequence sequence, boolean optional, boolean multi, TokenType sep) {
            super(optional, multi, sep);
            this.sequence = sequence;
        }
        
    }
    
    private abstract static class GroupFinder<T extends GroupFinder> extends Finder {
        private final List<Finder> finders = new LinkedList<Finder>();

        public GroupFinder(boolean optional, boolean multi) {
            super(optional, multi);
        }
        
        public GroupFinder(boolean optional) {
            this(optional, false);
        }
        
        public GroupFinder() {
            this(false, false);
        }
        
        protected T add(Finder ... finders) {
            for(Finder finder:finders) {
                this.finders.add(finder);
            }
            return (T) this;
        }
        protected T add(Sequence sequence) {
            return add(false, false, sequence);
        }
        
        protected T add(boolean optional,Sequence sequence) {
            return add(optional, false, sequence);
        }
        
        protected T add(boolean optional,boolean multi,Sequence sequence) {
            finders.add(new SequenceFinder(sequence, optional, multi));
            return (T) this;
        }
        
        protected T add(TokenType ... finders) {
            return add(false, finders);
        }
        
        protected T add(boolean optional,TokenType ... finders) {
            return add(optional, false, finders);
        }
        
        protected T add(boolean optional,boolean multi,TokenType ... finders) {
            for(TokenType type:finders) {
                this.finders.add(new TokenFinder(type, optional,multi));
            }
            return (T) this;
        }
        
        protected T add(NodeType ... finders) {
            return add(false, finders);
        }
        
        protected T add(boolean optional,NodeType ... finders) {
            return add(optional, false, finders);
        }
        
        protected T add(boolean optional,boolean multi,NodeType ... finders) {
            for(NodeType type:finders) {
                this.finders.add(new NodeFinder(type, optional,multi));
            }
            return (T) this;
        }
    }
    
    public static class RandomOrderFinder extends GroupFinder<RandomOrderFinder> {

        public RandomOrderFinder() {
        }

        public RandomOrderFinder(boolean optional) {
            super(optional);
        }

        public RandomOrderFinder(boolean optional, boolean multi) {
            super(optional, multi);
        }
        
    }
    
    public static class OneOfFinder extends GroupFinder<OneOfFinder> {

        public OneOfFinder(boolean optional) {
            super(optional);
        }

        public OneOfFinder() {
        }

        public OneOfFinder(boolean optional, boolean multi) {
            super(optional, multi);
        }
        
    }
}
