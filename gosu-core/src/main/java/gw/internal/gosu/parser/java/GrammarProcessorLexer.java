/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;// $ANTLR 3.4 GrammarProcessor.g 2012-10-01 17:25:14

import gw.internal.ext.org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class GrammarProcessorLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int ACTION=4;
    public static final int ACTION_CHAR_LITERAL=5;
    public static final int ACTION_ESC=6;
    public static final int ACTION_STRING_LITERAL=7;
    public static final int ALT=8;
    public static final int ARG=9;
    public static final int ARGLIST=10;
    public static final int ARG_ACTION=11;
    public static final int BACKTRACK_SEMPRED=12;
    public static final int BANG=13;
    public static final int BLOCK=14;
    public static final int CHAR_LITERAL=15;
    public static final int CHAR_RANGE=16;
    public static final int CLOSURE=17;
    public static final int COMBINED_GRAMMAR=18;
    public static final int DOC_COMMENT=19;
    public static final int DOUBLE_ANGLE_STRING_LITERAL=20;
    public static final int DOUBLE_QUOTE_STRING_LITERAL=21;
    public static final int EOA=22;
    public static final int EOB=23;
    public static final int EOR=24;
    public static final int EPSILON=25;
    public static final int ESC=26;
    public static final int FRAGMENT=27;
    public static final int GATED_SEMPRED=28;
    public static final int ID=29;
    public static final int INITACTION=30;
    public static final int INT=31;
    public static final int LABEL=32;
    public static final int LEXER=33;
    public static final int LEXER_GRAMMAR=34;
    public static final int LITERAL_CHAR=35;
    public static final int ML_COMMENT=36;
    public static final int NESTED_ACTION=37;
    public static final int NESTED_ARG_ACTION=38;
    public static final int OPTIONAL=39;
    public static final int OPTIONS=40;
    public static final int PARSER=41;
    public static final int PARSER_GRAMMAR=42;
    public static final int POSITIVE_CLOSURE=43;
    public static final int RANGE=44;
    public static final int RET=45;
    public static final int REWRITE=46;
    public static final int ROOT=47;
    public static final int RULE=48;
    public static final int RULE_REF=49;
    public static final int SCOPE=50;
    public static final int SEMPRED=51;
    public static final int SL_COMMENT=52;
    public static final int SRC=53;
    public static final int STRING_LITERAL=54;
    public static final int SYNPRED=55;
    public static final int SYN_SEMPRED=56;
    public static final int TEMPLATE=57;
    public static final int TOKENS=58;
    public static final int TOKEN_REF=59;
    public static final int TREE_BEGIN=60;
    public static final int TREE_GRAMMAR=61;
    public static final int WS=62;
    public static final int WS_LOOP=63;
    public static final int XDIGIT=64;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public GrammarProcessorLexer() {} 
    public GrammarProcessorLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public GrammarProcessorLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "GrammarProcessor.g"; }

    // $ANTLR start "BANG"
    public final void mBANG() throws RecognitionException {
        try {
            int _type = BANG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:2:6: ( '!' )
            // GrammarProcessor.g:2:8: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "BANG"

    // $ANTLR start "FRAGMENT"
    public final void mFRAGMENT() throws RecognitionException {
        try {
            int _type = FRAGMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:3:10: ( 'fragment' )
            // GrammarProcessor.g:3:12: 'fragment'
            {
            match("fragment"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FRAGMENT"

    // $ANTLR start "RANGE"
    public final void mRANGE() throws RecognitionException {
        try {
            int _type = RANGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:4:7: ( '..' )
            // GrammarProcessor.g:4:9: '..'
            {
            match(".."); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RANGE"

    // $ANTLR start "REWRITE"
    public final void mREWRITE() throws RecognitionException {
        try {
            int _type = REWRITE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:5:9: ( '->' )
            // GrammarProcessor.g:5:11: '->'
            {
            match("->"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "REWRITE"

    // $ANTLR start "ROOT"
    public final void mROOT() throws RecognitionException {
        try {
            int _type = ROOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:6:6: ( '^' )
            // GrammarProcessor.g:6:8: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ROOT"

    // $ANTLR start "SCOPE"
    public final void mSCOPE() throws RecognitionException {
        try {
            int _type = SCOPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:7:7: ( 'scope' )
            // GrammarProcessor.g:7:9: 'scope'
            {
            match("scope"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SCOPE"

    // $ANTLR start "TREE_BEGIN"
    public final void mTREE_BEGIN() throws RecognitionException {
        try {
            int _type = TREE_BEGIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:8:12: ( '^(' )
            // GrammarProcessor.g:8:14: '^('
            {
            match("^("); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TREE_BEGIN"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:9:7: ( '$' )
            // GrammarProcessor.g:9:9: '$'
            {
            match('$'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:10:7: ( '(' )
            // GrammarProcessor.g:10:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:11:7: ( ')' )
            // GrammarProcessor.g:11:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:12:7: ( '*' )
            // GrammarProcessor.g:12:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:13:7: ( '+' )
            // GrammarProcessor.g:13:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:14:7: ( '+=' )
            // GrammarProcessor.g:14:9: '+='
            {
            match("+="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:15:7: ( ',' )
            // GrammarProcessor.g:15:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:16:7: ( '.' )
            // GrammarProcessor.g:16:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:17:7: ( ':' )
            // GrammarProcessor.g:17:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:18:7: ( '::' )
            // GrammarProcessor.g:18:9: '::'
            {
            match("::"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:19:7: ( ';' )
            // GrammarProcessor.g:19:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "T__76"
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:20:7: ( '=' )
            // GrammarProcessor.g:20:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__76"

    // $ANTLR start "T__77"
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:21:7: ( '=>' )
            // GrammarProcessor.g:21:9: '=>'
            {
            match("=>"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__77"

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:22:7: ( '?' )
            // GrammarProcessor.g:22:9: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:23:7: ( '@' )
            // GrammarProcessor.g:23:9: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:24:7: ( 'catch' )
            // GrammarProcessor.g:24:9: 'catch'
            {
            match("catch"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:25:7: ( 'finally' )
            // GrammarProcessor.g:25:9: 'finally'
            {
            match("finally"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:26:7: ( 'grammar' )
            // GrammarProcessor.g:26:9: 'grammar'
            {
            match("grammar"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:27:7: ( 'lexer' )
            // GrammarProcessor.g:27:9: 'lexer'
            {
            match("lexer"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:28:7: ( 'parser' )
            // GrammarProcessor.g:28:9: 'parser'
            {
            match("parser"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:29:7: ( 'private' )
            // GrammarProcessor.g:29:9: 'private'
            {
            match("private"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:30:7: ( 'protected' )
            // GrammarProcessor.g:30:9: 'protected'
            {
            match("protected"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:31:7: ( 'public' )
            // GrammarProcessor.g:31:9: 'public'
            {
            match("public"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:32:7: ( 'returns' )
            // GrammarProcessor.g:32:9: 'returns'
            {
            match("returns"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "T__89"
    public final void mT__89() throws RecognitionException {
        try {
            int _type = T__89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:33:7: ( 'throws' )
            // GrammarProcessor.g:33:9: 'throws'
            {
            match("throws"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__89"

    // $ANTLR start "T__90"
    public final void mT__90() throws RecognitionException {
        try {
            int _type = T__90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:34:7: ( 'tree' )
            // GrammarProcessor.g:34:9: 'tree'
            {
            match("tree"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__90"

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:35:7: ( '|' )
            // GrammarProcessor.g:35:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:36:7: ( '}' )
            // GrammarProcessor.g:36:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:37:7: ( '~' )
            // GrammarProcessor.g:37:9: '~'
            {
            match('~'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "SL_COMMENT"
    public final void mSL_COMMENT() throws RecognitionException {
        try {
            int _type = SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:426:3: ( '//' ( ' $ANTLR ' SRC | (~ ( '\\r' | '\\n' ) )* ) ( '\\r' )? '\\n' )
            // GrammarProcessor.g:426:5: '//' ( ' $ANTLR ' SRC | (~ ( '\\r' | '\\n' ) )* ) ( '\\r' )? '\\n'
            {
            match("//"); 



            // GrammarProcessor.g:427:5: ( ' $ANTLR ' SRC | (~ ( '\\r' | '\\n' ) )* )
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // GrammarProcessor.g:427:7: ' $ANTLR ' SRC
                    {
                    match(" $ANTLR "); 



                    mSRC(); 


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:428:6: (~ ( '\\r' | '\\n' ) )*
                    {
                    // GrammarProcessor.g:428:6: (~ ( '\\r' | '\\n' ) )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( ((LA1_0 >= '\u0000' && LA1_0 <= '\t')||(LA1_0 >= '\u000B' && LA1_0 <= '\f')||(LA1_0 >= '\u000E' && LA1_0 <= '\uFFFF')) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // GrammarProcessor.g:
                    	    {
                    	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);


                    }
                    break;

            }


            // GrammarProcessor.g:430:3: ( '\\r' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\r') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // GrammarProcessor.g:430:3: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }


            match('\n'); 

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SL_COMMENT"

    // $ANTLR start "ML_COMMENT"
    public final void mML_COMMENT() throws RecognitionException {
        try {
            int _type = ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:435:2: ( '/*' ( . )* '*/' )
            // GrammarProcessor.g:435:4: '/*' ( . )* '*/'
            {
            match("/*"); 



            if (input.LA(1)=='*') _type=DOC_COMMENT; else _channel=HIDDEN;

            // GrammarProcessor.g:435:74: ( . )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0=='*') ) {
                    int LA4_1 = input.LA(2);

                    if ( (LA4_1=='/') ) {
                        alt4=2;
                    }
                    else if ( ((LA4_1 >= '\u0000' && LA4_1 <= '.')||(LA4_1 >= '0' && LA4_1 <= '\uFFFF')) ) {
                        alt4=1;
                    }


                }
                else if ( ((LA4_0 >= '\u0000' && LA4_0 <= ')')||(LA4_0 >= '+' && LA4_0 <= '\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // GrammarProcessor.g:435:74: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            match("*/"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ML_COMMENT"

    // $ANTLR start "CHAR_LITERAL"
    public final void mCHAR_LITERAL() throws RecognitionException {
        try {
            int _type = CHAR_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:439:2: ( '\\'' LITERAL_CHAR '\\'' )
            // GrammarProcessor.g:439:4: '\\'' LITERAL_CHAR '\\''
            {
            match('\''); 

            mLITERAL_CHAR(); 


            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CHAR_LITERAL"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:443:2: ( '\\'' LITERAL_CHAR ( LITERAL_CHAR )* '\\'' )
            // GrammarProcessor.g:443:4: '\\'' LITERAL_CHAR ( LITERAL_CHAR )* '\\''
            {
            match('\''); 

            mLITERAL_CHAR(); 


            // GrammarProcessor.g:443:22: ( LITERAL_CHAR )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '\u0000' && LA5_0 <= '&')||(LA5_0 >= '(' && LA5_0 <= '\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // GrammarProcessor.g:443:22: LITERAL_CHAR
            	    {
            	    mLITERAL_CHAR(); 


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "LITERAL_CHAR"
    public final void mLITERAL_CHAR() throws RecognitionException {
        try {
            // GrammarProcessor.g:449:2: ( ESC |~ ( '\\'' | '\\\\' ) )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\\') ) {
                alt6=1;
            }
            else if ( ((LA6_0 >= '\u0000' && LA6_0 <= '&')||(LA6_0 >= '(' && LA6_0 <= '[')||(LA6_0 >= ']' && LA6_0 <= '\uFFFF')) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // GrammarProcessor.g:449:4: ESC
                    {
                    mESC(); 


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:450:4: ~ ( '\\'' | '\\\\' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LITERAL_CHAR"

    // $ANTLR start "DOUBLE_QUOTE_STRING_LITERAL"
    public final void mDOUBLE_QUOTE_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = DOUBLE_QUOTE_STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:453:2: ( '\"' ( ESC |~ ( '\\\\' | '\"' ) )* '\"' )
            // GrammarProcessor.g:453:4: '\"' ( ESC |~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 

            // GrammarProcessor.g:453:8: ( ESC |~ ( '\\\\' | '\"' ) )*
            loop7:
            do {
                int alt7=3;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\\') ) {
                    alt7=1;
                }
                else if ( ((LA7_0 >= '\u0000' && LA7_0 <= '!')||(LA7_0 >= '#' && LA7_0 <= '[')||(LA7_0 >= ']' && LA7_0 <= '\uFFFF')) ) {
                    alt7=2;
                }


                switch (alt7) {
            	case 1 :
            	    // GrammarProcessor.g:453:9: ESC
            	    {
            	    mESC(); 


            	    }
            	    break;
            	case 2 :
            	    // GrammarProcessor.g:453:15: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DOUBLE_QUOTE_STRING_LITERAL"

    // $ANTLR start "DOUBLE_ANGLE_STRING_LITERAL"
    public final void mDOUBLE_ANGLE_STRING_LITERAL() throws RecognitionException {
        try {
            int _type = DOUBLE_ANGLE_STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:457:2: ( '<<' ( . )* '>>' )
            // GrammarProcessor.g:457:4: '<<' ( . )* '>>'
            {
            match("<<"); 



            // GrammarProcessor.g:457:9: ( . )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='>') ) {
                    int LA8_1 = input.LA(2);

                    if ( (LA8_1=='>') ) {
                        alt8=2;
                    }
                    else if ( ((LA8_1 >= '\u0000' && LA8_1 <= '=')||(LA8_1 >= '?' && LA8_1 <= '\uFFFF')) ) {
                        alt8=1;
                    }


                }
                else if ( ((LA8_0 >= '\u0000' && LA8_0 <= '=')||(LA8_0 >= '?' && LA8_0 <= '\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // GrammarProcessor.g:457:9: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            match(">>"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DOUBLE_ANGLE_STRING_LITERAL"

    // $ANTLR start "ESC"
    public final void mESC() throws RecognitionException {
        try {
            // GrammarProcessor.g:462:5: ( '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | '>' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT | . ) )
            // GrammarProcessor.g:462:7: '\\\\' ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | '>' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT | . )
            {
            match('\\'); 

            // GrammarProcessor.g:463:3: ( 'n' | 'r' | 't' | 'b' | 'f' | '\"' | '\\'' | '\\\\' | '>' | 'u' XDIGIT XDIGIT XDIGIT XDIGIT | . )
            int alt9=11;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='n') ) {
                alt9=1;
            }
            else if ( (LA9_0=='r') ) {
                alt9=2;
            }
            else if ( (LA9_0=='t') ) {
                alt9=3;
            }
            else if ( (LA9_0=='b') ) {
                alt9=4;
            }
            else if ( (LA9_0=='f') ) {
                alt9=5;
            }
            else if ( (LA9_0=='\"') ) {
                alt9=6;
            }
            else if ( (LA9_0=='\'') ) {
                alt9=7;
            }
            else if ( (LA9_0=='\\') ) {
                alt9=8;
            }
            else if ( (LA9_0=='>') ) {
                alt9=9;
            }
            else if ( (LA9_0=='u') ) {
                int LA9_10 = input.LA(2);

                if ( ((LA9_10 >= '0' && LA9_10 <= '9')||(LA9_10 >= 'A' && LA9_10 <= 'F')||(LA9_10 >= 'a' && LA9_10 <= 'f')) ) {
                    alt9=10;
                }
                else {
                    alt9=11;
                }
            }
            else if ( ((LA9_0 >= '\u0000' && LA9_0 <= '!')||(LA9_0 >= '#' && LA9_0 <= '&')||(LA9_0 >= '(' && LA9_0 <= '=')||(LA9_0 >= '?' && LA9_0 <= '[')||(LA9_0 >= ']' && LA9_0 <= 'a')||(LA9_0 >= 'c' && LA9_0 <= 'e')||(LA9_0 >= 'g' && LA9_0 <= 'm')||(LA9_0 >= 'o' && LA9_0 <= 'q')||LA9_0=='s'||(LA9_0 >= 'v' && LA9_0 <= '\uFFFF')) ) {
                alt9=11;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // GrammarProcessor.g:463:5: 'n'
                    {
                    match('n'); 

                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:464:5: 'r'
                    {
                    match('r'); 

                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:465:5: 't'
                    {
                    match('t'); 

                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:466:5: 'b'
                    {
                    match('b'); 

                    }
                    break;
                case 5 :
                    // GrammarProcessor.g:467:5: 'f'
                    {
                    match('f'); 

                    }
                    break;
                case 6 :
                    // GrammarProcessor.g:468:5: '\"'
                    {
                    match('\"'); 

                    }
                    break;
                case 7 :
                    // GrammarProcessor.g:469:5: '\\''
                    {
                    match('\''); 

                    }
                    break;
                case 8 :
                    // GrammarProcessor.g:470:5: '\\\\'
                    {
                    match('\\'); 

                    }
                    break;
                case 9 :
                    // GrammarProcessor.g:471:5: '>'
                    {
                    match('>'); 

                    }
                    break;
                case 10 :
                    // GrammarProcessor.g:472:5: 'u' XDIGIT XDIGIT XDIGIT XDIGIT
                    {
                    match('u'); 

                    mXDIGIT(); 


                    mXDIGIT(); 


                    mXDIGIT(); 


                    mXDIGIT(); 


                    }
                    break;
                case 11 :
                    // GrammarProcessor.g:473:5: .
                    {
                    matchAny(); 

                    }
                    break;

            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC"

    // $ANTLR start "XDIGIT"
    public final void mXDIGIT() throws RecognitionException {
        try {
            // GrammarProcessor.g:478:8: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            // GrammarProcessor.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "XDIGIT"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:483:5: ( ( '0' .. '9' )+ )
            // GrammarProcessor.g:483:7: ( '0' .. '9' )+
            {
            // GrammarProcessor.g:483:7: ( '0' .. '9' )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0 >= '0' && LA10_0 <= '9')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // GrammarProcessor.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "ARG_ACTION"
    public final void mARG_ACTION() throws RecognitionException {
        try {
            int _type = ARG_ACTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:487:2: ( NESTED_ARG_ACTION )
            // GrammarProcessor.g:487:4: NESTED_ARG_ACTION
            {
            mNESTED_ARG_ACTION(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ARG_ACTION"

    // $ANTLR start "NESTED_ARG_ACTION"
    public final void mNESTED_ARG_ACTION() throws RecognitionException {
        try {
            // GrammarProcessor.g:492:19: ( '[' ( options {greedy=false; k=1; } : NESTED_ARG_ACTION | ACTION_STRING_LITERAL | ACTION_CHAR_LITERAL | . )* ']' )
            // GrammarProcessor.g:493:2: '[' ( options {greedy=false; k=1; } : NESTED_ARG_ACTION | ACTION_STRING_LITERAL | ACTION_CHAR_LITERAL | . )* ']'
            {
            match('['); 

            // GrammarProcessor.g:494:2: ( options {greedy=false; k=1; } : NESTED_ARG_ACTION | ACTION_STRING_LITERAL | ACTION_CHAR_LITERAL | . )*
            loop11:
            do {
                int alt11=5;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==']') ) {
                    alt11=5;
                }
                else if ( (LA11_0=='[') ) {
                    alt11=1;
                }
                else if ( (LA11_0=='\"') ) {
                    alt11=2;
                }
                else if ( (LA11_0=='\'') ) {
                    alt11=3;
                }
                else if ( ((LA11_0 >= '\u0000' && LA11_0 <= '!')||(LA11_0 >= '#' && LA11_0 <= '&')||(LA11_0 >= '(' && LA11_0 <= 'Z')||LA11_0=='\\'||(LA11_0 >= '^' && LA11_0 <= '\uFFFF')) ) {
                    alt11=4;
                }


                switch (alt11) {
            	case 1 :
            	    // GrammarProcessor.g:495:4: NESTED_ARG_ACTION
            	    {
            	    mNESTED_ARG_ACTION(); 


            	    }
            	    break;
            	case 2 :
            	    // GrammarProcessor.g:496:4: ACTION_STRING_LITERAL
            	    {
            	    mACTION_STRING_LITERAL(); 


            	    }
            	    break;
            	case 3 :
            	    // GrammarProcessor.g:497:4: ACTION_CHAR_LITERAL
            	    {
            	    mACTION_CHAR_LITERAL(); 


            	    }
            	    break;
            	case 4 :
            	    // GrammarProcessor.g:498:4: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            match(']'); 

            setText(getText().substring(1, getText().length()-1));

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NESTED_ARG_ACTION"

    // $ANTLR start "ACTION"
    public final void mACTION() throws RecognitionException {
        try {
            int _type = ACTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:504:2: ( NESTED_ACTION ( '?' )? )
            // GrammarProcessor.g:504:4: NESTED_ACTION ( '?' )?
            {
            mNESTED_ACTION(); 


            // GrammarProcessor.g:504:18: ( '?' )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='?') ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // GrammarProcessor.g:504:20: '?'
                    {
                    match('?'); 

                    _type = SEMPRED;

                    }
                    break;

            }


            setText(getText());

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ACTION"

    // $ANTLR start "NESTED_ACTION"
    public final void mNESTED_ACTION() throws RecognitionException {
        try {
            // GrammarProcessor.g:509:15: ( '{' ( options {greedy=false; k=2; } : NESTED_ACTION | SL_COMMENT | ML_COMMENT | ACTION_STRING_LITERAL | ACTION_CHAR_LITERAL | . )* '}' )
            // GrammarProcessor.g:510:2: '{' ( options {greedy=false; k=2; } : NESTED_ACTION | SL_COMMENT | ML_COMMENT | ACTION_STRING_LITERAL | ACTION_CHAR_LITERAL | . )* '}'
            {
            match('{'); 

            // GrammarProcessor.g:511:2: ( options {greedy=false; k=2; } : NESTED_ACTION | SL_COMMENT | ML_COMMENT | ACTION_STRING_LITERAL | ACTION_CHAR_LITERAL | . )*
            loop13:
            do {
                int alt13=7;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='}') ) {
                    alt13=7;
                }
                else if ( (LA13_0=='{') ) {
                    alt13=1;
                }
                else if ( (LA13_0=='/') ) {
                    int LA13_3 = input.LA(2);

                    if ( (LA13_3=='/') ) {
                        alt13=2;
                    }
                    else if ( (LA13_3=='*') ) {
                        alt13=3;
                    }
                    else if ( ((LA13_3 >= '\u0000' && LA13_3 <= ')')||(LA13_3 >= '+' && LA13_3 <= '.')||(LA13_3 >= '0' && LA13_3 <= '\uFFFF')) ) {
                        alt13=6;
                    }


                }
                else if ( (LA13_0=='\"') ) {
                    int LA13_4 = input.LA(2);

                    if ( (LA13_4=='\\') ) {
                        alt13=4;
                    }
                    else if ( (LA13_4=='}') ) {
                        alt13=4;
                    }
                    else if ( (LA13_4=='\"') ) {
                        alt13=4;
                    }
                    else if ( (LA13_4=='{') ) {
                        alt13=4;
                    }
                    else if ( (LA13_4=='/') ) {
                        alt13=4;
                    }
                    else if ( (LA13_4=='\'') ) {
                        alt13=4;
                    }
                    else if ( ((LA13_4 >= '\u0000' && LA13_4 <= '!')||(LA13_4 >= '#' && LA13_4 <= '&')||(LA13_4 >= '(' && LA13_4 <= '.')||(LA13_4 >= '0' && LA13_4 <= '[')||(LA13_4 >= ']' && LA13_4 <= 'z')||LA13_4=='|'||(LA13_4 >= '~' && LA13_4 <= '\uFFFF')) ) {
                        alt13=4;
                    }


                }
                else if ( (LA13_0=='\'') ) {
                    int LA13_5 = input.LA(2);

                    if ( (LA13_5=='\\') ) {
                        alt13=5;
                    }
                    else if ( (LA13_5=='}') ) {
                        alt13=5;
                    }
                    else if ( (LA13_5=='{') ) {
                        alt13=5;
                    }
                    else if ( (LA13_5=='/') ) {
                        alt13=5;
                    }
                    else if ( (LA13_5=='\"') ) {
                        alt13=5;
                    }
                    else if ( ((LA13_5 >= '\u0000' && LA13_5 <= '!')||(LA13_5 >= '#' && LA13_5 <= '&')||(LA13_5 >= '(' && LA13_5 <= '.')||(LA13_5 >= '0' && LA13_5 <= '[')||(LA13_5 >= ']' && LA13_5 <= 'z')||LA13_5=='|'||(LA13_5 >= '~' && LA13_5 <= '\uFFFF')) ) {
                        alt13=5;
                    }
                    else if ( (LA13_5=='\'') ) {
                        alt13=6;
                    }


                }
                else if ( ((LA13_0 >= '\u0000' && LA13_0 <= '!')||(LA13_0 >= '#' && LA13_0 <= '&')||(LA13_0 >= '(' && LA13_0 <= '.')||(LA13_0 >= '0' && LA13_0 <= 'z')||LA13_0=='|'||(LA13_0 >= '~' && LA13_0 <= '\uFFFF')) ) {
                    alt13=6;
                }


                switch (alt13) {
            	case 1 :
            	    // GrammarProcessor.g:512:4: NESTED_ACTION
            	    {
            	    mNESTED_ACTION(); 


            	    }
            	    break;
            	case 2 :
            	    // GrammarProcessor.g:513:4: SL_COMMENT
            	    {
            	    mSL_COMMENT(); 


            	    }
            	    break;
            	case 3 :
            	    // GrammarProcessor.g:514:4: ML_COMMENT
            	    {
            	    mML_COMMENT(); 


            	    }
            	    break;
            	case 4 :
            	    // GrammarProcessor.g:515:4: ACTION_STRING_LITERAL
            	    {
            	    mACTION_STRING_LITERAL(); 


            	    }
            	    break;
            	case 5 :
            	    // GrammarProcessor.g:516:4: ACTION_CHAR_LITERAL
            	    {
            	    mACTION_CHAR_LITERAL(); 


            	    }
            	    break;
            	case 6 :
            	    // GrammarProcessor.g:517:4: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            match('}'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NESTED_ACTION"

    // $ANTLR start "ACTION_CHAR_LITERAL"
    public final void mACTION_CHAR_LITERAL() throws RecognitionException {
        try {
            // GrammarProcessor.g:524:2: ( '\\'' ( ACTION_ESC |~ ( '\\\\' | '\\'' ) ) '\\'' )
            // GrammarProcessor.g:524:4: '\\'' ( ACTION_ESC |~ ( '\\\\' | '\\'' ) ) '\\''
            {
            match('\''); 

            // GrammarProcessor.g:524:9: ( ACTION_ESC |~ ( '\\\\' | '\\'' ) )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0=='\\') ) {
                alt14=1;
            }
            else if ( ((LA14_0 >= '\u0000' && LA14_0 <= '&')||(LA14_0 >= '(' && LA14_0 <= '[')||(LA14_0 >= ']' && LA14_0 <= '\uFFFF')) ) {
                alt14=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }
            switch (alt14) {
                case 1 :
                    // GrammarProcessor.g:524:10: ACTION_ESC
                    {
                    mACTION_ESC(); 


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:524:21: ~ ( '\\\\' | '\\'' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            match('\''); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ACTION_CHAR_LITERAL"

    // $ANTLR start "ACTION_STRING_LITERAL"
    public final void mACTION_STRING_LITERAL() throws RecognitionException {
        try {
            // GrammarProcessor.g:529:2: ( '\"' ( ACTION_ESC |~ ( '\\\\' | '\"' ) )* '\"' )
            // GrammarProcessor.g:529:4: '\"' ( ACTION_ESC |~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 

            // GrammarProcessor.g:529:8: ( ACTION_ESC |~ ( '\\\\' | '\"' ) )*
            loop15:
            do {
                int alt15=3;
                int LA15_0 = input.LA(1);

                if ( (LA15_0=='\\') ) {
                    alt15=1;
                }
                else if ( ((LA15_0 >= '\u0000' && LA15_0 <= '!')||(LA15_0 >= '#' && LA15_0 <= '[')||(LA15_0 >= ']' && LA15_0 <= '\uFFFF')) ) {
                    alt15=2;
                }


                switch (alt15) {
            	case 1 :
            	    // GrammarProcessor.g:529:9: ACTION_ESC
            	    {
            	    mACTION_ESC(); 


            	    }
            	    break;
            	case 2 :
            	    // GrammarProcessor.g:529:20: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            match('\"'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ACTION_STRING_LITERAL"

    // $ANTLR start "ACTION_ESC"
    public final void mACTION_ESC() throws RecognitionException {
        try {
            // GrammarProcessor.g:534:2: ( '\\\\\\'' | '\\\\' '\"' | '\\\\' ~ ( '\\'' | '\"' ) )
            int alt16=3;
            int LA16_0 = input.LA(1);

            if ( (LA16_0=='\\') ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1=='\'') ) {
                    alt16=1;
                }
                else if ( (LA16_1=='\"') ) {
                    alt16=2;
                }
                else if ( ((LA16_1 >= '\u0000' && LA16_1 <= '!')||(LA16_1 >= '#' && LA16_1 <= '&')||(LA16_1 >= '(' && LA16_1 <= '\uFFFF')) ) {
                    alt16=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
                case 1 :
                    // GrammarProcessor.g:534:4: '\\\\\\''
                    {
                    match("\\'"); 



                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:535:4: '\\\\' '\"'
                    {
                    match('\\'); 

                    match('\"'); 

                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:536:4: '\\\\' ~ ( '\\'' | '\"' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ACTION_ESC"

    // $ANTLR start "TOKEN_REF"
    public final void mTOKEN_REF() throws RecognitionException {
        try {
            int _type = TOKEN_REF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:539:2: ( 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // GrammarProcessor.g:539:4: 'A' .. 'Z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            matchRange('A','Z'); 

            // GrammarProcessor.g:539:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( ((LA17_0 >= '0' && LA17_0 <= '9')||(LA17_0 >= 'A' && LA17_0 <= 'Z')||LA17_0=='_'||(LA17_0 >= 'a' && LA17_0 <= 'z')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // GrammarProcessor.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TOKEN_REF"

    // $ANTLR start "RULE_REF"
    public final void mRULE_REF() throws RecognitionException {
        try {
            int _type = RULE_REF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:543:2: ( 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // GrammarProcessor.g:543:4: 'a' .. 'z' ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            matchRange('a','z'); 

            // GrammarProcessor.g:543:13: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( ((LA18_0 >= '0' && LA18_0 <= '9')||(LA18_0 >= 'A' && LA18_0 <= 'Z')||LA18_0=='_'||(LA18_0 >= 'a' && LA18_0 <= 'z')) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // GrammarProcessor.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RULE_REF"

    // $ANTLR start "OPTIONS"
    public final void mOPTIONS() throws RecognitionException {
        try {
            int _type = OPTIONS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:553:2: ( 'options' WS_LOOP '{' )
            // GrammarProcessor.g:553:4: 'options' WS_LOOP '{'
            {
            match("options"); 



            mWS_LOOP(); 


            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OPTIONS"

    // $ANTLR start "TOKENS"
    public final void mTOKENS() throws RecognitionException {
        try {
            int _type = TOKENS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:554:2: ( 'tokens' WS_LOOP '{' )
            // GrammarProcessor.g:554:4: 'tokens' WS_LOOP '{'
            {
            match("tokens"); 



            mWS_LOOP(); 


            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TOKENS"

    // $ANTLR start "SRC"
    public final void mSRC() throws RecognitionException {
        try {
            // GrammarProcessor.g:567:5: ( 'src' ' ' ACTION_STRING_LITERAL ' ' INT )
            // GrammarProcessor.g:567:7: 'src' ' ' ACTION_STRING_LITERAL ' ' INT
            {
            match("src"); 



            match(' '); 

            mACTION_STRING_LITERAL(); 


            match(' '); 

            mINT(); 


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SRC"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // GrammarProcessor.g:565:4: ( ( ' ' | '\\t' | ( '\\r' )? '\\n' )+ )
            // GrammarProcessor.g:565:6: ( ' ' | '\\t' | ( '\\r' )? '\\n' )+
            {
            // GrammarProcessor.g:565:6: ( ' ' | '\\t' | ( '\\r' )? '\\n' )+
            int cnt20=0;
            loop20:
            do {
                int alt20=4;
                switch ( input.LA(1) ) {
                case ' ':
                    {
                    alt20=1;
                    }
                    break;
                case '\t':
                    {
                    alt20=2;
                    }
                    break;
                case '\n':
                case '\r':
                    {
                    alt20=3;
                    }
                    break;

                }

                switch (alt20) {
            	case 1 :
            	    // GrammarProcessor.g:565:8: ' '
            	    {
            	    match(' '); 

            	    }
            	    break;
            	case 2 :
            	    // GrammarProcessor.g:566:5: '\\t'
            	    {
            	    match('\t'); 

            	    }
            	    break;
            	case 3 :
            	    // GrammarProcessor.g:567:5: ( '\\r' )? '\\n'
            	    {
            	    // GrammarProcessor.g:567:5: ( '\\r' )?
            	    int alt19=2;
            	    int LA19_0 = input.LA(1);

            	    if ( (LA19_0=='\r') ) {
            	        alt19=1;
            	    }
            	    switch (alt19) {
            	        case 1 :
            	            // GrammarProcessor.g:567:5: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }


            	    match('\n'); 

            	    }
            	    break;

            	default :
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
            } while (true);


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "WS_LOOP"
    public final void mWS_LOOP() throws RecognitionException {
        try {
            // GrammarProcessor.g:575:2: ( ( WS | SL_COMMENT | ML_COMMENT )* )
            // GrammarProcessor.g:575:4: ( WS | SL_COMMENT | ML_COMMENT )*
            {
            // GrammarProcessor.g:575:4: ( WS | SL_COMMENT | ML_COMMENT )*
            loop21:
            do {
                int alt21=4;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0 >= '\t' && LA21_0 <= '\n')||LA21_0=='\r'||LA21_0==' ') ) {
                    alt21=1;
                }
                else if ( (LA21_0=='/') ) {
                    int LA21_3 = input.LA(2);

                    if ( (LA21_3=='/') ) {
                        alt21=2;
                    }
                    else if ( (LA21_3=='*') ) {
                        alt21=3;
                    }


                }


                switch (alt21) {
            	case 1 :
            	    // GrammarProcessor.g:575:6: WS
            	    {
            	    mWS(); 


            	    }
            	    break;
            	case 2 :
            	    // GrammarProcessor.g:576:5: SL_COMMENT
            	    {
            	    mSL_COMMENT(); 


            	    }
            	    break;
            	case 3 :
            	    // GrammarProcessor.g:577:5: ML_COMMENT
            	    {
            	    mML_COMMENT(); 


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS_LOOP"

    public void mTokens() throws RecognitionException {
        // GrammarProcessor.g:1:8: ( BANG | FRAGMENT | RANGE | REWRITE | ROOT | SCOPE | TREE_BEGIN | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | SL_COMMENT | ML_COMMENT | CHAR_LITERAL | STRING_LITERAL | DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL | INT | ARG_ACTION | ACTION | TOKEN_REF | RULE_REF | OPTIONS | TOKENS | WS )
        int alt22=50;
        alt22 = dfa22.predict(input);
        switch (alt22) {
            case 1 :
                // GrammarProcessor.g:1:10: BANG
                {
                mBANG(); 


                }
                break;
            case 2 :
                // GrammarProcessor.g:1:15: FRAGMENT
                {
                mFRAGMENT(); 


                }
                break;
            case 3 :
                // GrammarProcessor.g:1:24: RANGE
                {
                mRANGE(); 


                }
                break;
            case 4 :
                // GrammarProcessor.g:1:30: REWRITE
                {
                mREWRITE(); 


                }
                break;
            case 5 :
                // GrammarProcessor.g:1:38: ROOT
                {
                mROOT(); 


                }
                break;
            case 6 :
                // GrammarProcessor.g:1:43: SCOPE
                {
                mSCOPE(); 


                }
                break;
            case 7 :
                // GrammarProcessor.g:1:49: TREE_BEGIN
                {
                mTREE_BEGIN(); 


                }
                break;
            case 8 :
                // GrammarProcessor.g:1:60: T__65
                {
                mT__65(); 


                }
                break;
            case 9 :
                // GrammarProcessor.g:1:66: T__66
                {
                mT__66(); 


                }
                break;
            case 10 :
                // GrammarProcessor.g:1:72: T__67
                {
                mT__67(); 


                }
                break;
            case 11 :
                // GrammarProcessor.g:1:78: T__68
                {
                mT__68(); 


                }
                break;
            case 12 :
                // GrammarProcessor.g:1:84: T__69
                {
                mT__69(); 


                }
                break;
            case 13 :
                // GrammarProcessor.g:1:90: T__70
                {
                mT__70(); 


                }
                break;
            case 14 :
                // GrammarProcessor.g:1:96: T__71
                {
                mT__71(); 


                }
                break;
            case 15 :
                // GrammarProcessor.g:1:102: T__72
                {
                mT__72(); 


                }
                break;
            case 16 :
                // GrammarProcessor.g:1:108: T__73
                {
                mT__73(); 


                }
                break;
            case 17 :
                // GrammarProcessor.g:1:114: T__74
                {
                mT__74(); 


                }
                break;
            case 18 :
                // GrammarProcessor.g:1:120: T__75
                {
                mT__75(); 


                }
                break;
            case 19 :
                // GrammarProcessor.g:1:126: T__76
                {
                mT__76(); 


                }
                break;
            case 20 :
                // GrammarProcessor.g:1:132: T__77
                {
                mT__77(); 


                }
                break;
            case 21 :
                // GrammarProcessor.g:1:138: T__78
                {
                mT__78(); 


                }
                break;
            case 22 :
                // GrammarProcessor.g:1:144: T__79
                {
                mT__79(); 


                }
                break;
            case 23 :
                // GrammarProcessor.g:1:150: T__80
                {
                mT__80(); 


                }
                break;
            case 24 :
                // GrammarProcessor.g:1:156: T__81
                {
                mT__81(); 


                }
                break;
            case 25 :
                // GrammarProcessor.g:1:162: T__82
                {
                mT__82(); 


                }
                break;
            case 26 :
                // GrammarProcessor.g:1:168: T__83
                {
                mT__83(); 


                }
                break;
            case 27 :
                // GrammarProcessor.g:1:174: T__84
                {
                mT__84(); 


                }
                break;
            case 28 :
                // GrammarProcessor.g:1:180: T__85
                {
                mT__85(); 


                }
                break;
            case 29 :
                // GrammarProcessor.g:1:186: T__86
                {
                mT__86(); 


                }
                break;
            case 30 :
                // GrammarProcessor.g:1:192: T__87
                {
                mT__87(); 


                }
                break;
            case 31 :
                // GrammarProcessor.g:1:198: T__88
                {
                mT__88(); 


                }
                break;
            case 32 :
                // GrammarProcessor.g:1:204: T__89
                {
                mT__89(); 


                }
                break;
            case 33 :
                // GrammarProcessor.g:1:210: T__90
                {
                mT__90(); 


                }
                break;
            case 34 :
                // GrammarProcessor.g:1:216: T__91
                {
                mT__91(); 


                }
                break;
            case 35 :
                // GrammarProcessor.g:1:222: T__92
                {
                mT__92(); 


                }
                break;
            case 36 :
                // GrammarProcessor.g:1:228: T__93
                {
                mT__93(); 


                }
                break;
            case 37 :
                // GrammarProcessor.g:1:234: SL_COMMENT
                {
                mSL_COMMENT(); 


                }
                break;
            case 38 :
                // GrammarProcessor.g:1:245: ML_COMMENT
                {
                mML_COMMENT(); 


                }
                break;
            case 39 :
                // GrammarProcessor.g:1:256: CHAR_LITERAL
                {
                mCHAR_LITERAL(); 


                }
                break;
            case 40 :
                // GrammarProcessor.g:1:269: STRING_LITERAL
                {
                mSTRING_LITERAL(); 


                }
                break;
            case 41 :
                // GrammarProcessor.g:1:284: DOUBLE_QUOTE_STRING_LITERAL
                {
                mDOUBLE_QUOTE_STRING_LITERAL(); 


                }
                break;
            case 42 :
                // GrammarProcessor.g:1:312: DOUBLE_ANGLE_STRING_LITERAL
                {
                mDOUBLE_ANGLE_STRING_LITERAL(); 


                }
                break;
            case 43 :
                // GrammarProcessor.g:1:340: INT
                {
                mINT(); 


                }
                break;
            case 44 :
                // GrammarProcessor.g:1:344: ARG_ACTION
                {
                mARG_ACTION(); 


                }
                break;
            case 45 :
                // GrammarProcessor.g:1:355: ACTION
                {
                mACTION(); 


                }
                break;
            case 46 :
                // GrammarProcessor.g:1:362: TOKEN_REF
                {
                mTOKEN_REF(); 


                }
                break;
            case 47 :
                // GrammarProcessor.g:1:372: RULE_REF
                {
                mRULE_REF(); 


                }
                break;
            case 48 :
                // GrammarProcessor.g:1:381: OPTIONS
                {
                mOPTIONS(); 


                }
                break;
            case 49 :
                // GrammarProcessor.g:1:389: TOKENS
                {
                mTOKENS(); 


                }
                break;
            case 50 :
                // GrammarProcessor.g:1:396: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA2 dfa2 = new DFA2(this);
    protected DFA22 dfa22 = new DFA22(this);
    static final String DFA2_eotS =
        "\22\uffff\1\2\4\uffff\1\2\4\uffff";
    static final String DFA2_eofS =
        "\34\uffff";
    static final String DFA2_minS =
        "\2\0\1\uffff\26\0\1\uffff\1\0\1\uffff";
    static final String DFA2_maxS =
        "\2\uffff\1\uffff\26\uffff\1\uffff\1\uffff\1\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\2\26\uffff\1\1\1\uffff\1\1";
    static final String DFA2_specialS =
        "\1\20\1\15\1\uffff\1\14\1\17\1\16\1\5\1\6\1\7\1\24\1\25\1\26\1\27"+
        "\1\2\1\22\1\3\1\21\1\4\1\1\1\30\1\10\1\11\1\13\1\23\1\12\1\uffff"+
        "\1\0\1\uffff}>";
    static final String[] DFA2_transitionS = {
            "\40\2\1\1\uffdf\2",
            "\44\2\1\3\uffdb\2",
            "",
            "\101\2\1\4\uffbe\2",
            "\116\2\1\5\uffb1\2",
            "\124\2\1\6\uffab\2",
            "\114\2\1\7\uffb3\2",
            "\122\2\1\10\uffad\2",
            "\40\2\1\11\uffdf\2",
            "\163\2\1\12\uff8c\2",
            "\162\2\1\13\uff8d\2",
            "\143\2\1\14\uff9c\2",
            "\40\2\1\15\uffdf\2",
            "\42\2\1\16\uffdd\2",
            "\12\23\1\22\2\23\1\20\24\23\1\21\71\23\1\17\uffa3\23",
            "\12\30\1\27\2\30\1\26\24\30\1\25\4\30\1\24\uffd8\30",
            "\12\31\1\22\ufff5\31",
            "\40\2\1\32\uffdf\2",
            "\0\31",
            "\12\23\1\22\2\23\1\20\24\23\1\21\71\23\1\17\uffa3\23",
            "\12\23\1\22\2\23\1\20\24\23\1\21\71\23\1\17\uffa3\23",
            "\12\23\1\22\2\23\1\20\24\23\1\21\71\23\1\17\uffa3\23",
            "\12\31\1\22\ufff5\31",
            "\0\31",
            "\12\23\1\22\2\23\1\20\24\23\1\21\71\23\1\17\uffa3\23",
            "",
            "\60\2\12\33\uffc6\2",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "427:5: ( ' $ANTLR ' SRC | (~ ( '\\r' | '\\n' ) )* )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA2_26 = input.LA(1);

                        s = -1;
                        if ( ((LA2_26 >= '0' && LA2_26 <= '9')) ) {s = 27;}

                        else if ( ((LA2_26 >= '\u0000' && LA2_26 <= '/')||(LA2_26 >= ':' && LA2_26 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA2_18 = input.LA(1);

                        s = -1;
                        if ( ((LA2_18 >= '\u0000' && LA2_18 <= '\uFFFF')) ) {s = 25;}

                        else s = 2;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA2_13 = input.LA(1);

                        s = -1;
                        if ( (LA2_13=='\"') ) {s = 14;}

                        else if ( ((LA2_13 >= '\u0000' && LA2_13 <= '!')||(LA2_13 >= '#' && LA2_13 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA2_15 = input.LA(1);

                        s = -1;
                        if ( (LA2_15=='\'') ) {s = 20;}

                        else if ( (LA2_15=='\"') ) {s = 21;}

                        else if ( (LA2_15=='\r') ) {s = 22;}

                        else if ( (LA2_15=='\n') ) {s = 23;}

                        else if ( ((LA2_15 >= '\u0000' && LA2_15 <= '\t')||(LA2_15 >= '\u000B' && LA2_15 <= '\f')||(LA2_15 >= '\u000E' && LA2_15 <= '!')||(LA2_15 >= '#' && LA2_15 <= '&')||(LA2_15 >= '(' && LA2_15 <= '\uFFFF')) ) {s = 24;}

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA2_17 = input.LA(1);

                        s = -1;
                        if ( (LA2_17==' ') ) {s = 26;}

                        else if ( ((LA2_17 >= '\u0000' && LA2_17 <= '\u001F')||(LA2_17 >= '!' && LA2_17 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA2_6 = input.LA(1);

                        s = -1;
                        if ( (LA2_6=='L') ) {s = 7;}

                        else if ( ((LA2_6 >= '\u0000' && LA2_6 <= 'K')||(LA2_6 >= 'M' && LA2_6 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA2_7 = input.LA(1);

                        s = -1;
                        if ( (LA2_7=='R') ) {s = 8;}

                        else if ( ((LA2_7 >= '\u0000' && LA2_7 <= 'Q')||(LA2_7 >= 'S' && LA2_7 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA2_8 = input.LA(1);

                        s = -1;
                        if ( (LA2_8==' ') ) {s = 9;}

                        else if ( ((LA2_8 >= '\u0000' && LA2_8 <= '\u001F')||(LA2_8 >= '!' && LA2_8 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA2_20 = input.LA(1);

                        s = -1;
                        if ( (LA2_20=='\"') ) {s = 17;}

                        else if ( (LA2_20=='\\') ) {s = 15;}

                        else if ( (LA2_20=='\r') ) {s = 16;}

                        else if ( (LA2_20=='\n') ) {s = 18;}

                        else if ( ((LA2_20 >= '\u0000' && LA2_20 <= '\t')||(LA2_20 >= '\u000B' && LA2_20 <= '\f')||(LA2_20 >= '\u000E' && LA2_20 <= '!')||(LA2_20 >= '#' && LA2_20 <= '[')||(LA2_20 >= ']' && LA2_20 <= '\uFFFF')) ) {s = 19;}

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA2_21 = input.LA(1);

                        s = -1;
                        if ( (LA2_21=='\"') ) {s = 17;}

                        else if ( (LA2_21=='\\') ) {s = 15;}

                        else if ( (LA2_21=='\r') ) {s = 16;}

                        else if ( (LA2_21=='\n') ) {s = 18;}

                        else if ( ((LA2_21 >= '\u0000' && LA2_21 <= '\t')||(LA2_21 >= '\u000B' && LA2_21 <= '\f')||(LA2_21 >= '\u000E' && LA2_21 <= '!')||(LA2_21 >= '#' && LA2_21 <= '[')||(LA2_21 >= ']' && LA2_21 <= '\uFFFF')) ) {s = 19;}

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA2_24 = input.LA(1);

                        s = -1;
                        if ( (LA2_24=='\"') ) {s = 17;}

                        else if ( (LA2_24=='\\') ) {s = 15;}

                        else if ( (LA2_24=='\r') ) {s = 16;}

                        else if ( (LA2_24=='\n') ) {s = 18;}

                        else if ( ((LA2_24 >= '\u0000' && LA2_24 <= '\t')||(LA2_24 >= '\u000B' && LA2_24 <= '\f')||(LA2_24 >= '\u000E' && LA2_24 <= '!')||(LA2_24 >= '#' && LA2_24 <= '[')||(LA2_24 >= ']' && LA2_24 <= '\uFFFF')) ) {s = 19;}

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA2_22 = input.LA(1);

                        s = -1;
                        if ( ((LA2_22 >= '\u0000' && LA2_22 <= '\t')||(LA2_22 >= '\u000B' && LA2_22 <= '\uFFFF')) ) {s = 25;}

                        else if ( (LA2_22=='\n') ) {s = 18;}

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA2_3 = input.LA(1);

                        s = -1;
                        if ( (LA2_3=='A') ) {s = 4;}

                        else if ( ((LA2_3 >= '\u0000' && LA2_3 <= '@')||(LA2_3 >= 'B' && LA2_3 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA2_1 = input.LA(1);

                        s = -1;
                        if ( (LA2_1=='$') ) {s = 3;}

                        else if ( ((LA2_1 >= '\u0000' && LA2_1 <= '#')||(LA2_1 >= '%' && LA2_1 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA2_5 = input.LA(1);

                        s = -1;
                        if ( (LA2_5=='T') ) {s = 6;}

                        else if ( ((LA2_5 >= '\u0000' && LA2_5 <= 'S')||(LA2_5 >= 'U' && LA2_5 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA2_4 = input.LA(1);

                        s = -1;
                        if ( (LA2_4=='N') ) {s = 5;}

                        else if ( ((LA2_4 >= '\u0000' && LA2_4 <= 'M')||(LA2_4 >= 'O' && LA2_4 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA2_0 = input.LA(1);

                        s = -1;
                        if ( (LA2_0==' ') ) {s = 1;}

                        else if ( ((LA2_0 >= '\u0000' && LA2_0 <= '\u001F')||(LA2_0 >= '!' && LA2_0 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA2_16 = input.LA(1);

                        s = -1;
                        if ( ((LA2_16 >= '\u0000' && LA2_16 <= '\t')||(LA2_16 >= '\u000B' && LA2_16 <= '\uFFFF')) ) {s = 25;}

                        else if ( (LA2_16=='\n') ) {s = 18;}

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA2_14 = input.LA(1);

                        s = -1;
                        if ( (LA2_14=='\\') ) {s = 15;}

                        else if ( (LA2_14=='\r') ) {s = 16;}

                        else if ( (LA2_14=='\"') ) {s = 17;}

                        else if ( (LA2_14=='\n') ) {s = 18;}

                        else if ( ((LA2_14 >= '\u0000' && LA2_14 <= '\t')||(LA2_14 >= '\u000B' && LA2_14 <= '\f')||(LA2_14 >= '\u000E' && LA2_14 <= '!')||(LA2_14 >= '#' && LA2_14 <= '[')||(LA2_14 >= ']' && LA2_14 <= '\uFFFF')) ) {s = 19;}

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA2_23 = input.LA(1);

                        s = -1;
                        if ( ((LA2_23 >= '\u0000' && LA2_23 <= '\uFFFF')) ) {s = 25;}

                        else s = 2;

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA2_9 = input.LA(1);

                        s = -1;
                        if ( (LA2_9=='s') ) {s = 10;}

                        else if ( ((LA2_9 >= '\u0000' && LA2_9 <= 'r')||(LA2_9 >= 't' && LA2_9 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA2_10 = input.LA(1);

                        s = -1;
                        if ( (LA2_10=='r') ) {s = 11;}

                        else if ( ((LA2_10 >= '\u0000' && LA2_10 <= 'q')||(LA2_10 >= 's' && LA2_10 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA2_11 = input.LA(1);

                        s = -1;
                        if ( (LA2_11=='c') ) {s = 12;}

                        else if ( ((LA2_11 >= '\u0000' && LA2_11 <= 'b')||(LA2_11 >= 'd' && LA2_11 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 23 : 
                        int LA2_12 = input.LA(1);

                        s = -1;
                        if ( (LA2_12==' ') ) {s = 13;}

                        else if ( ((LA2_12 >= '\u0000' && LA2_12 <= '\u001F')||(LA2_12 >= '!' && LA2_12 <= '\uFFFF')) ) {s = 2;}

                        if ( s>=0 ) return s;
                        break;

                    case 24 : 
                        int LA2_19 = input.LA(1);

                        s = -1;
                        if ( (LA2_19=='\"') ) {s = 17;}

                        else if ( (LA2_19=='\\') ) {s = 15;}

                        else if ( (LA2_19=='\r') ) {s = 16;}

                        else if ( (LA2_19=='\n') ) {s = 18;}

                        else if ( ((LA2_19 >= '\u0000' && LA2_19 <= '\t')||(LA2_19 >= '\u000B' && LA2_19 <= '\f')||(LA2_19 >= '\u000E' && LA2_19 <= '!')||(LA2_19 >= '#' && LA2_19 <= '[')||(LA2_19 >= ']' && LA2_19 <= '\uFFFF')) ) {s = 19;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 2, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA22_eotS =
        "\2\uffff\1\44\1\51\1\uffff\1\53\1\44\4\uffff\1\56\1\uffff\1\60\1"+
        "\uffff\1\62\2\uffff\6\44\13\uffff\1\44\2\uffff\2\44\4\uffff\1\44"+
        "\6\uffff\12\44\4\uffff\17\44\15\uffff\15\44\1\173\1\44\2\uffff\3"+
        "\44\1\u0081\1\u0082\1\44\1\u0084\6\44\1\uffff\1\44\1\uffff\3\44"+
        "\2\uffff\1\44\1\uffff\1\u0091\2\44\1\u0094\1\44\1\u0096\1\44\1\uffff"+
        "\2\44\1\u009b\1\u009c\1\uffff\1\u009d\1\44\1\uffff\1\u009f\3\uffff"+
        "\1\44\1\u00a1\3\uffff\1\44\3\uffff\1\u00a3\1\uffff";
    static final String DFA22_eofS =
        "\u00a4\uffff";
    static final String DFA22_minS =
        "\1\11\1\uffff\1\151\1\56\1\uffff\1\50\1\143\4\uffff\1\75\1\uffff"+
        "\1\72\1\uffff\1\76\2\uffff\1\141\1\162\1\145\1\141\1\145\1\150\3"+
        "\uffff\1\52\1\0\6\uffff\1\160\2\uffff\1\141\1\156\4\uffff\1\157"+
        "\6\uffff\1\164\1\141\1\170\1\162\1\151\1\142\1\164\1\162\1\145\1"+
        "\153\2\uffff\2\0\1\164\1\147\1\141\1\160\1\143\1\155\1\145\1\163"+
        "\1\166\1\164\1\154\1\165\1\157\2\145\13\0\2\uffff\1\151\1\155\1"+
        "\154\1\145\1\150\1\155\1\162\1\145\1\141\1\145\1\151\1\162\1\167"+
        "\1\60\1\156\1\0\1\uffff\1\157\1\145\1\154\2\60\1\141\1\60\1\162"+
        "\1\164\2\143\1\156\1\163\1\uffff\1\163\1\0\2\156\1\171\2\uffff\1"+
        "\162\1\uffff\1\60\1\145\1\164\1\60\1\163\1\60\1\11\1\0\1\163\1\164"+
        "\2\60\1\uffff\1\60\1\145\1\uffff\1\60\2\uffff\1\0\1\11\1\60\3\uffff"+
        "\1\144\3\uffff\1\60\1\uffff";
    static final String DFA22_maxS =
        "\1\176\1\uffff\1\162\1\56\1\uffff\1\50\1\143\4\uffff\1\75\1\uffff"+
        "\1\72\1\uffff\1\76\2\uffff\1\141\1\162\1\145\1\165\1\145\1\162\3"+
        "\uffff\1\57\1\uffff\6\uffff\1\160\2\uffff\1\141\1\156\4\uffff\1"+
        "\157\6\uffff\1\164\1\141\1\170\1\162\1\157\1\142\1\164\1\162\1\145"+
        "\1\153\2\uffff\2\uffff\1\164\1\147\1\141\1\160\1\143\1\155\1\145"+
        "\1\163\1\166\1\164\1\154\1\165\1\157\2\145\13\uffff\2\uffff\1\151"+
        "\1\155\1\154\1\145\1\150\1\155\1\162\1\145\1\141\1\145\1\151\1\162"+
        "\1\167\1\172\1\156\1\uffff\1\uffff\1\157\1\145\1\154\2\172\1\141"+
        "\1\172\1\162\1\164\2\143\1\156\1\163\1\uffff\1\163\1\uffff\2\156"+
        "\1\171\2\uffff\1\162\1\uffff\1\172\1\145\1\164\1\172\1\163\1\172"+
        "\1\173\1\uffff\1\163\1\164\2\172\1\uffff\1\172\1\145\1\uffff\1\172"+
        "\2\uffff\1\uffff\1\173\1\172\3\uffff\1\144\3\uffff\1\172\1\uffff";
    static final String DFA22_acceptS =
        "\1\uffff\1\1\2\uffff\1\4\2\uffff\1\10\1\11\1\12\1\13\1\uffff\1\16"+
        "\1\uffff\1\22\1\uffff\1\25\1\26\6\uffff\1\42\1\43\1\44\2\uffff\1"+
        "\51\1\52\1\53\1\54\1\55\1\56\1\uffff\1\57\1\62\2\uffff\1\3\1\17"+
        "\1\7\1\5\1\uffff\1\15\1\14\1\21\1\20\1\24\1\23\12\uffff\1\45\1\46"+
        "\34\uffff\1\47\1\50\20\uffff\1\47\15\uffff\1\41\5\uffff\1\6\1\27"+
        "\1\uffff\1\32\14\uffff\1\33\2\uffff\1\36\1\uffff\1\40\1\61\3\uffff"+
        "\1\30\1\31\1\34\1\uffff\1\37\1\60\1\2\1\uffff\1\35";
    static final String DFA22_specialS =
        "\34\uffff\1\4\42\uffff\1\0\1\5\17\uffff\1\3\1\2\1\1\1\16\1\14\1"+
        "\21\1\20\1\12\1\11\1\7\1\6\21\uffff\1\13\20\uffff\1\15\16\uffff"+
        "\1\17\13\uffff\1\10\13\uffff}>";
    static final String[] DFA22_transitionS = {
            "\2\45\2\uffff\1\45\22\uffff\1\45\1\1\1\35\1\uffff\1\7\2\uffff"+
            "\1\34\1\10\1\11\1\12\1\13\1\14\1\4\1\3\1\33\12\37\1\15\1\16"+
            "\1\36\1\17\1\uffff\1\20\1\21\32\42\1\40\2\uffff\1\5\2\uffff"+
            "\2\44\1\22\2\44\1\2\1\23\4\44\1\24\2\44\1\43\1\25\1\44\1\26"+
            "\1\6\1\27\6\44\1\41\1\30\1\31\1\32",
            "",
            "\1\47\10\uffff\1\46",
            "\1\50",
            "",
            "\1\52",
            "\1\54",
            "",
            "",
            "",
            "",
            "\1\55",
            "",
            "\1\57",
            "",
            "\1\61",
            "",
            "",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\66\20\uffff\1\67\2\uffff\1\70",
            "\1\71",
            "\1\72\6\uffff\1\74\2\uffff\1\73",
            "",
            "",
            "",
            "\1\76\4\uffff\1\75",
            "\47\100\1\uffff\64\100\1\77\uffa3\100",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\101",
            "",
            "",
            "\1\102",
            "\1\103",
            "",
            "",
            "",
            "",
            "\1\104",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111\5\uffff\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\1\116",
            "\1\117",
            "",
            "",
            "\42\132\1\125\4\132\1\126\26\132\1\130\35\132\1\127\5\132\1"+
            "\123\3\132\1\124\7\132\1\120\3\132\1\121\1\132\1\122\1\131\uff8a"+
            "\132",
            "\47\134\1\133\uffd8\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143",
            "\1\144",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\uffd8\134",
            "\47\134\1\133\10\134\12\154\7\134\6\154\32\134\6\154\uff99"+
            "\134",
            "\47\134\1\133\uffd8\134",
            "",
            "",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "\1\171",
            "\1\172",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\174",
            "\60\134\12\175\7\134\6\175\32\134\6\175\uff99\134",
            "",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0083",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "",
            "\1\u008b",
            "\60\134\12\u008c\7\134\6\u008c\32\134\6\u008c\uff99\134",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "",
            "",
            "\1\u0090",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0092",
            "\1\u0093",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u0095",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\2\u0097\2\uffff\1\u0097\22\uffff\1\u0097\16\uffff\1\u0097"+
            "\113\uffff\1\u0097",
            "\60\134\12\u0098\7\134\6\u0098\32\134\6\u0098\uff99\134",
            "\1\u0099",
            "\1\u009a",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u009e",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\47\134\1\133\uffd8\134",
            "\2\u00a0\2\uffff\1\u00a0\22\uffff\1\u00a0\16\uffff\1\u00a0"+
            "\113\uffff\1\u00a0",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "",
            "\1\u00a2",
            "",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( BANG | FRAGMENT | RANGE | REWRITE | ROOT | SCOPE | TREE_BEGIN | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | SL_COMMENT | ML_COMMENT | CHAR_LITERAL | STRING_LITERAL | DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL | INT | ARG_ACTION | ACTION | TOKEN_REF | RULE_REF | OPTIONS | TOKENS | WS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA22_63 = input.LA(1);

                        s = -1;
                        if ( (LA22_63=='n') ) {s = 80;}

                        else if ( (LA22_63=='r') ) {s = 81;}

                        else if ( (LA22_63=='t') ) {s = 82;}

                        else if ( (LA22_63=='b') ) {s = 83;}

                        else if ( (LA22_63=='f') ) {s = 84;}

                        else if ( (LA22_63=='\"') ) {s = 85;}

                        else if ( (LA22_63=='\'') ) {s = 86;}

                        else if ( (LA22_63=='\\') ) {s = 87;}

                        else if ( (LA22_63=='>') ) {s = 88;}

                        else if ( (LA22_63=='u') ) {s = 89;}

                        else if ( ((LA22_63 >= '\u0000' && LA22_63 <= '!')||(LA22_63 >= '#' && LA22_63 <= '&')||(LA22_63 >= '(' && LA22_63 <= '=')||(LA22_63 >= '?' && LA22_63 <= '[')||(LA22_63 >= ']' && LA22_63 <= 'a')||(LA22_63 >= 'c' && LA22_63 <= 'e')||(LA22_63 >= 'g' && LA22_63 <= 'm')||(LA22_63 >= 'o' && LA22_63 <= 'q')||LA22_63=='s'||(LA22_63 >= 'v' && LA22_63 <= '\uFFFF')) ) {s = 90;}

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA22_82 = input.LA(1);

                        s = -1;
                        if ( (LA22_82=='\'') ) {s = 91;}

                        else if ( ((LA22_82 >= '\u0000' && LA22_82 <= '&')||(LA22_82 >= '(' && LA22_82 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA22_81 = input.LA(1);

                        s = -1;
                        if ( (LA22_81=='\'') ) {s = 91;}

                        else if ( ((LA22_81 >= '\u0000' && LA22_81 <= '&')||(LA22_81 >= '(' && LA22_81 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA22_80 = input.LA(1);

                        s = -1;
                        if ( (LA22_80=='\'') ) {s = 91;}

                        else if ( ((LA22_80 >= '\u0000' && LA22_80 <= '&')||(LA22_80 >= '(' && LA22_80 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA22_28 = input.LA(1);

                        s = -1;
                        if ( (LA22_28=='\\') ) {s = 63;}

                        else if ( ((LA22_28 >= '\u0000' && LA22_28 <= '&')||(LA22_28 >= '(' && LA22_28 <= '[')||(LA22_28 >= ']' && LA22_28 <= '\uFFFF')) ) {s = 64;}

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA22_64 = input.LA(1);

                        s = -1;
                        if ( (LA22_64=='\'') ) {s = 91;}

                        else if ( ((LA22_64 >= '\u0000' && LA22_64 <= '&')||(LA22_64 >= '(' && LA22_64 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA22_90 = input.LA(1);

                        s = -1;
                        if ( (LA22_90=='\'') ) {s = 91;}

                        else if ( ((LA22_90 >= '\u0000' && LA22_90 <= '&')||(LA22_90 >= '(' && LA22_90 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA22_89 = input.LA(1);

                        s = -1;
                        if ( ((LA22_89 >= '0' && LA22_89 <= '9')||(LA22_89 >= 'A' && LA22_89 <= 'F')||(LA22_89 >= 'a' && LA22_89 <= 'f')) ) {s = 108;}

                        else if ( (LA22_89=='\'') ) {s = 91;}

                        else if ( ((LA22_89 >= '\u0000' && LA22_89 <= '&')||(LA22_89 >= '(' && LA22_89 <= '/')||(LA22_89 >= ':' && LA22_89 <= '@')||(LA22_89 >= 'G' && LA22_89 <= '`')||(LA22_89 >= 'g' && LA22_89 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA22_152 = input.LA(1);

                        s = -1;
                        if ( (LA22_152=='\'') ) {s = 91;}

                        else if ( ((LA22_152 >= '\u0000' && LA22_152 <= '&')||(LA22_152 >= '(' && LA22_152 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA22_88 = input.LA(1);

                        s = -1;
                        if ( (LA22_88=='\'') ) {s = 91;}

                        else if ( ((LA22_88 >= '\u0000' && LA22_88 <= '&')||(LA22_88 >= '(' && LA22_88 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA22_87 = input.LA(1);

                        s = -1;
                        if ( (LA22_87=='\'') ) {s = 91;}

                        else if ( ((LA22_87 >= '\u0000' && LA22_87 <= '&')||(LA22_87 >= '(' && LA22_87 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA22_108 = input.LA(1);

                        s = -1;
                        if ( ((LA22_108 >= '0' && LA22_108 <= '9')||(LA22_108 >= 'A' && LA22_108 <= 'F')||(LA22_108 >= 'a' && LA22_108 <= 'f')) ) {s = 125;}

                        else if ( ((LA22_108 >= '\u0000' && LA22_108 <= '/')||(LA22_108 >= ':' && LA22_108 <= '@')||(LA22_108 >= 'G' && LA22_108 <= '`')||(LA22_108 >= 'g' && LA22_108 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA22_84 = input.LA(1);

                        s = -1;
                        if ( (LA22_84=='\'') ) {s = 91;}

                        else if ( ((LA22_84 >= '\u0000' && LA22_84 <= '&')||(LA22_84 >= '(' && LA22_84 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA22_125 = input.LA(1);

                        s = -1;
                        if ( ((LA22_125 >= '0' && LA22_125 <= '9')||(LA22_125 >= 'A' && LA22_125 <= 'F')||(LA22_125 >= 'a' && LA22_125 <= 'f')) ) {s = 140;}

                        else if ( ((LA22_125 >= '\u0000' && LA22_125 <= '/')||(LA22_125 >= ':' && LA22_125 <= '@')||(LA22_125 >= 'G' && LA22_125 <= '`')||(LA22_125 >= 'g' && LA22_125 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA22_83 = input.LA(1);

                        s = -1;
                        if ( (LA22_83=='\'') ) {s = 91;}

                        else if ( ((LA22_83 >= '\u0000' && LA22_83 <= '&')||(LA22_83 >= '(' && LA22_83 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA22_140 = input.LA(1);

                        s = -1;
                        if ( ((LA22_140 >= '0' && LA22_140 <= '9')||(LA22_140 >= 'A' && LA22_140 <= 'F')||(LA22_140 >= 'a' && LA22_140 <= 'f')) ) {s = 152;}

                        else if ( ((LA22_140 >= '\u0000' && LA22_140 <= '/')||(LA22_140 >= ':' && LA22_140 <= '@')||(LA22_140 >= 'G' && LA22_140 <= '`')||(LA22_140 >= 'g' && LA22_140 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA22_86 = input.LA(1);

                        s = -1;
                        if ( (LA22_86=='\'') ) {s = 91;}

                        else if ( ((LA22_86 >= '\u0000' && LA22_86 <= '&')||(LA22_86 >= '(' && LA22_86 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA22_85 = input.LA(1);

                        s = -1;
                        if ( (LA22_85=='\'') ) {s = 91;}

                        else if ( ((LA22_85 >= '\u0000' && LA22_85 <= '&')||(LA22_85 >= '(' && LA22_85 <= '\uFFFF')) ) {s = 92;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 22, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}