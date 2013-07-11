/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;// $ANTLR 3.4 GrammarProcessor.g 2012-10-01 17:25:14

import gw.internal.ext.org.antlr.runtime.BaseRecognizer;
import gw.internal.ext.org.antlr.runtime.BitSet;
import gw.internal.ext.org.antlr.runtime.DFA;
import gw.internal.ext.org.antlr.runtime.EarlyExitException;
import gw.internal.ext.org.antlr.runtime.MismatchedSetException;
import gw.internal.ext.org.antlr.runtime.NoViableAltException;
import gw.internal.ext.org.antlr.runtime.Parser;
import gw.internal.ext.org.antlr.runtime.ParserRuleReturnScope;
import gw.internal.ext.org.antlr.runtime.RecognitionException;
import gw.internal.ext.org.antlr.runtime.RecognizerSharedState;
import gw.internal.ext.org.antlr.runtime.Token;
import gw.internal.ext.org.antlr.runtime.TokenRewriteStream;
import gw.internal.ext.org.antlr.runtime.TokenStream;
import gw.internal.ext.org.antlr.stringtemplate.StringTemplate;
import gw.internal.ext.org.antlr.stringtemplate.StringTemplateGroup;
import gw.internal.ext.org.antlr.stringtemplate.language.AngleBracketTemplateLexer;

import java.util.HashMap;
/** ANTLR v3 grammar written in ANTLR v3 with AST construction */
@SuppressWarnings({"all", "warnings", "unchecked"})
public class GrammarProcessorParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ACTION", "ACTION_CHAR_LITERAL", "ACTION_ESC", "ACTION_STRING_LITERAL", "ALT", "ARG", "ARGLIST", "ARG_ACTION", "BACKTRACK_SEMPRED", "BANG", "BLOCK", "CHAR_LITERAL", "CHAR_RANGE", "CLOSURE", "COMBINED_GRAMMAR", "DOC_COMMENT", "DOUBLE_ANGLE_STRING_LITERAL", "DOUBLE_QUOTE_STRING_LITERAL", "EOA", "EOB", "EOR", "EPSILON", "ESC", "FRAGMENT", "GATED_SEMPRED", "ID", "INITACTION", "INT", "LABEL", "LEXER", "LEXER_GRAMMAR", "LITERAL_CHAR", "ML_COMMENT", "NESTED_ACTION", "NESTED_ARG_ACTION", "OPTIONAL", "OPTIONS", "PARSER", "PARSER_GRAMMAR", "POSITIVE_CLOSURE", "RANGE", "RET", "REWRITE", "ROOT", "RULE", "RULE_REF", "SCOPE", "SEMPRED", "SL_COMMENT", "SRC", "STRING_LITERAL", "SYNPRED", "SYN_SEMPRED", "TEMPLATE", "TOKENS", "TOKEN_REF", "TREE_BEGIN", "TREE_GRAMMAR", "WS", "WS_LOOP", "XDIGIT", "'$'", "'('", "')'", "'*'", "'+'", "'+='", "','", "'.'", "':'", "'::'", "';'", "'='", "'=>'", "'?'", "'@'", "'catch'", "'finally'", "'grammar'", "'lexer'", "'parser'", "'private'", "'protected'", "'public'", "'returns'", "'throws'", "'tree'", "'|'", "'}'", "'~'"
    };

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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public GrammarProcessorParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public GrammarProcessorParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected StringTemplateGroup templateLib =
  new StringTemplateGroup("GrammarProcessorParserTemplates", AngleBracketTemplateLexer.class);

public void setTemplateLib(StringTemplateGroup templateLib) {
  this.templateLib = templateLib;
}
public StringTemplateGroup getTemplateLib() {
  return templateLib;
}
/** allows convenient multi-value initialization:
 *  "new STAttrMap().put(...).put(...)"
 */
public static class STAttrMap extends HashMap {
  public STAttrMap put(String attrName, Object value) {
    super.put(attrName, value);
    return this;
  }
  public STAttrMap put(String attrName, int value) {
    super.put(attrName, new Integer(value));
    return this;
  }
}
    public String[] getTokenNames() { return GrammarProcessorParser.tokenNames; }
    public String getGrammarFileName() { return "GrammarProcessor.g"; }


        int count = 0;


    public static class grammarDef_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "grammarDef"
    // GrammarProcessor.g:92:1: grammarDef : ( DOC_COMMENT )? ( 'lexer' | 'parser' | 'tree' |) 'grammar' id ';' ( optionsSpec )? ( tokensSpec )? ( attrScope )* ( action )* ( rule )+ EOF ;
    public final grammarDef_return grammarDef() throws RecognitionException {
        grammarDef_return retval = new grammarDef_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:93:5: ( ( DOC_COMMENT )? ( 'lexer' | 'parser' | 'tree' |) 'grammar' id ';' ( optionsSpec )? ( tokensSpec )? ( attrScope )* ( action )* ( rule )+ EOF )
            // GrammarProcessor.g:93:9: ( DOC_COMMENT )? ( 'lexer' | 'parser' | 'tree' |) 'grammar' id ';' ( optionsSpec )? ( tokensSpec )? ( attrScope )* ( action )* ( rule )+ EOF
            {
            // GrammarProcessor.g:93:9: ( DOC_COMMENT )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==DOC_COMMENT) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // GrammarProcessor.g:93:9: DOC_COMMENT
                    {
                    match(input,DOC_COMMENT,FOLLOW_DOC_COMMENT_in_grammarDef335); 

                    }
                    break;

            }


            // GrammarProcessor.g:94:6: ( 'lexer' | 'parser' | 'tree' |)
            int alt2=4;
            switch ( input.LA(1) ) {
            case 83:
                {
                alt2=1;
                }
                break;
            case 84:
                {
                alt2=2;
                }
                break;
            case 90:
                {
                alt2=3;
                }
                break;
            case 82:
                {
                alt2=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // GrammarProcessor.g:94:8: 'lexer'
                    {
                    match(input,83,FOLLOW_83_in_grammarDef345); 

                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:95:10: 'parser'
                    {
                    match(input,84,FOLLOW_84_in_grammarDef361); 

                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:96:10: 'tree'
                    {
                    match(input,90,FOLLOW_90_in_grammarDef376); 

                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:98:6: 
                    {
                    }
                    break;

            }


            match(input,82,FOLLOW_82_in_grammarDef400); 

            pushFollow(FOLLOW_id_in_grammarDef402);
            id();

            state._fsp--;


            match(input,75,FOLLOW_75_in_grammarDef404); 

            // GrammarProcessor.g:98:25: ( optionsSpec )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==OPTIONS) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // GrammarProcessor.g:98:25: optionsSpec
                    {
                    pushFollow(FOLLOW_optionsSpec_in_grammarDef406);
                    optionsSpec();

                    state._fsp--;


                    }
                    break;

            }


            // GrammarProcessor.g:98:38: ( tokensSpec )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==TOKENS) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // GrammarProcessor.g:98:38: tokensSpec
                    {
                    pushFollow(FOLLOW_tokensSpec_in_grammarDef409);
                    tokensSpec();

                    state._fsp--;


                    }
                    break;

            }


            // GrammarProcessor.g:98:50: ( attrScope )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==SCOPE) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // GrammarProcessor.g:98:50: attrScope
            	    {
            	    pushFollow(FOLLOW_attrScope_in_grammarDef412);
            	    attrScope();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            // GrammarProcessor.g:98:61: ( action )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==79) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // GrammarProcessor.g:98:61: action
            	    {
            	    pushFollow(FOLLOW_action_in_grammarDef415);
            	    action();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            // GrammarProcessor.g:99:6: ( rule )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==DOC_COMMENT||LA7_0==FRAGMENT||LA7_0==RULE_REF||LA7_0==TOKEN_REF||(LA7_0 >= 85 && LA7_0 <= 87)) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // GrammarProcessor.g:99:6: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_grammarDef423);
            	    rule();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            match(input,EOF,FOLLOW_EOF_in_grammarDef431); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "grammarDef"


    public static class tokensSpec_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "tokensSpec"
    // GrammarProcessor.g:103:1: tokensSpec : TOKENS ( tokenSpec )+ '}' ;
    public final tokensSpec_return tokensSpec() throws RecognitionException {
        tokensSpec_return retval = new tokensSpec_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:104:2: ( TOKENS ( tokenSpec )+ '}' )
            // GrammarProcessor.g:104:4: TOKENS ( tokenSpec )+ '}'
            {
            match(input,TOKENS,FOLLOW_TOKENS_in_tokensSpec445); 

            // GrammarProcessor.g:104:11: ( tokenSpec )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==TOKEN_REF) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // GrammarProcessor.g:104:11: tokenSpec
            	    {
            	    pushFollow(FOLLOW_tokenSpec_in_tokensSpec447);
            	    tokenSpec();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            match(input,92,FOLLOW_92_in_tokensSpec450); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "tokensSpec"


    public static class tokenSpec_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "tokenSpec"
    // GrammarProcessor.g:107:1: tokenSpec : TOKEN_REF ( '=' ( STRING_LITERAL | CHAR_LITERAL ) |) ';' ;
    public final tokenSpec_return tokenSpec() throws RecognitionException {
        tokenSpec_return retval = new tokenSpec_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:108:2: ( TOKEN_REF ( '=' ( STRING_LITERAL | CHAR_LITERAL ) |) ';' )
            // GrammarProcessor.g:108:4: TOKEN_REF ( '=' ( STRING_LITERAL | CHAR_LITERAL ) |) ';'
            {
            match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_tokenSpec461); 

            // GrammarProcessor.g:109:3: ( '=' ( STRING_LITERAL | CHAR_LITERAL ) |)
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==76) ) {
                alt9=1;
            }
            else if ( (LA9_0==75) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // GrammarProcessor.g:109:5: '=' ( STRING_LITERAL | CHAR_LITERAL )
                    {
                    match(input,76,FOLLOW_76_in_tokenSpec467); 

                    if ( input.LA(1)==CHAR_LITERAL||input.LA(1)==STRING_LITERAL ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:111:3: 
                    {
                    }
                    break;

            }


            match(input,75,FOLLOW_75_in_tokenSpec487); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "tokenSpec"


    public static class attrScope_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "attrScope"
    // GrammarProcessor.g:115:1: attrScope : 'scope' id ACTION ;
    public final attrScope_return attrScope() throws RecognitionException {
        attrScope_return retval = new attrScope_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:116:2: ( 'scope' id ACTION )
            // GrammarProcessor.g:116:4: 'scope' id ACTION
            {
            match(input,SCOPE,FOLLOW_SCOPE_in_attrScope498); 

            pushFollow(FOLLOW_id_in_attrScope500);
            id();

            state._fsp--;


            match(input,ACTION,FOLLOW_ACTION_in_attrScope502); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "attrScope"


    public static class action_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "action"
    // GrammarProcessor.g:120:1: action : '@' ( actionScopeName '::' )? id ACTION ;
    public final action_return action() throws RecognitionException {
        action_return retval = new action_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:121:2: ( '@' ( actionScopeName '::' )? id ACTION )
            // GrammarProcessor.g:121:4: '@' ( actionScopeName '::' )? id ACTION
            {
            match(input,79,FOLLOW_79_in_action515); 

            // GrammarProcessor.g:121:8: ( actionScopeName '::' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==RULE_REF||LA10_0==TOKEN_REF) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==74) ) {
                    alt10=1;
                }
            }
            else if ( ((LA10_0 >= 83 && LA10_0 <= 84)) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // GrammarProcessor.g:121:9: actionScopeName '::'
                    {
                    pushFollow(FOLLOW_actionScopeName_in_action518);
                    actionScopeName();

                    state._fsp--;


                    match(input,74,FOLLOW_74_in_action520); 

                    }
                    break;

            }


            pushFollow(FOLLOW_id_in_action524);
            id();

            state._fsp--;


            match(input,ACTION,FOLLOW_ACTION_in_action526); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "action"


    public static class actionScopeName_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "actionScopeName"
    // GrammarProcessor.g:127:1: actionScopeName : ( id | 'lexer' | 'parser' );
    public final actionScopeName_return actionScopeName() throws RecognitionException {
        actionScopeName_return retval = new actionScopeName_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:128:2: ( id | 'lexer' | 'parser' )
            int alt11=3;
            switch ( input.LA(1) ) {
            case RULE_REF:
            case TOKEN_REF:
                {
                alt11=1;
                }
                break;
            case 83:
                {
                alt11=2;
                }
                break;
            case 84:
                {
                alt11=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // GrammarProcessor.g:128:4: id
                    {
                    pushFollow(FOLLOW_id_in_actionScopeName539);
                    id();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:129:4: 'lexer'
                    {
                    match(input,83,FOLLOW_83_in_actionScopeName544); 

                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:130:7: 'parser'
                    {
                    match(input,84,FOLLOW_84_in_actionScopeName552); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "actionScopeName"


    public static class optionsSpec_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "optionsSpec"
    // GrammarProcessor.g:133:1: optionsSpec : OPTIONS ( option ';' )+ '}' ;
    public final optionsSpec_return optionsSpec() throws RecognitionException {
        optionsSpec_return retval = new optionsSpec_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:134:2: ( OPTIONS ( option ';' )+ '}' )
            // GrammarProcessor.g:134:4: OPTIONS ( option ';' )+ '}'
            {
            match(input,OPTIONS,FOLLOW_OPTIONS_in_optionsSpec563); 

            // GrammarProcessor.g:134:12: ( option ';' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( (LA12_0==RULE_REF||LA12_0==TOKEN_REF) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // GrammarProcessor.g:134:13: option ';'
            	    {
            	    pushFollow(FOLLOW_option_in_optionsSpec566);
            	    option();

            	    state._fsp--;


            	    match(input,75,FOLLOW_75_in_optionsSpec568); 

            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);


            match(input,92,FOLLOW_92_in_optionsSpec572); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "optionsSpec"


    public static class option_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "option"
    // GrammarProcessor.g:137:1: option : id '=' optionValue ;
    public final option_return option() throws RecognitionException {
        option_return retval = new option_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:138:5: ( id '=' optionValue )
            // GrammarProcessor.g:138:9: id '=' optionValue
            {
            pushFollow(FOLLOW_id_in_option588);
            id();

            state._fsp--;


            match(input,76,FOLLOW_76_in_option590); 

            pushFollow(FOLLOW_optionValue_in_option592);
            optionValue();

            state._fsp--;


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "option"


    public static class optionValue_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "optionValue"
    // GrammarProcessor.g:141:1: optionValue : ( id | STRING_LITERAL | CHAR_LITERAL | INT | '*' );
    public final optionValue_return optionValue() throws RecognitionException {
        optionValue_return retval = new optionValue_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:142:5: ( id | STRING_LITERAL | CHAR_LITERAL | INT | '*' )
            int alt13=5;
            switch ( input.LA(1) ) {
            case RULE_REF:
            case TOKEN_REF:
                {
                alt13=1;
                }
                break;
            case STRING_LITERAL:
                {
                alt13=2;
                }
                break;
            case CHAR_LITERAL:
                {
                alt13=3;
                }
                break;
            case INT:
                {
                alt13=4;
                }
                break;
            case 68:
                {
                alt13=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // GrammarProcessor.g:142:9: id
                    {
                    pushFollow(FOLLOW_id_in_optionValue611);
                    id();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:143:9: STRING_LITERAL
                    {
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_optionValue621); 

                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:144:9: CHAR_LITERAL
                    {
                    match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_optionValue631); 

                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:145:9: INT
                    {
                    match(input,INT,FOLLOW_INT_in_optionValue641); 

                    }
                    break;
                case 5 :
                    // GrammarProcessor.g:146:7: '*'
                    {
                    match(input,68,FOLLOW_68_in_optionValue649); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "optionValue"


    public static class rule_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rule"
    // GrammarProcessor.g:149:1: rule : ( DOC_COMMENT )? ( ( 'protected' | 'public' | 'private' | 'fragment' ) )? id ( '!' )? ( ARG_ACTION )? ( 'returns' ARG_ACTION )? ( throwsSpec )? ( optionsSpec )? ( ruleScopeSpec )? ( ruleAction )* ':' altList ';' ( exceptionGroup )? ;
    public final rule_return rule() throws RecognitionException {
        rule_return retval = new rule_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:150:2: ( ( DOC_COMMENT )? ( ( 'protected' | 'public' | 'private' | 'fragment' ) )? id ( '!' )? ( ARG_ACTION )? ( 'returns' ARG_ACTION )? ( throwsSpec )? ( optionsSpec )? ( ruleScopeSpec )? ( ruleAction )* ':' altList ';' ( exceptionGroup )? )
            // GrammarProcessor.g:150:4: ( DOC_COMMENT )? ( ( 'protected' | 'public' | 'private' | 'fragment' ) )? id ( '!' )? ( ARG_ACTION )? ( 'returns' ARG_ACTION )? ( throwsSpec )? ( optionsSpec )? ( ruleScopeSpec )? ( ruleAction )* ':' altList ';' ( exceptionGroup )?
            {
            // GrammarProcessor.g:150:4: ( DOC_COMMENT )?
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==DOC_COMMENT) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // GrammarProcessor.g:150:4: DOC_COMMENT
                    {
                    match(input,DOC_COMMENT,FOLLOW_DOC_COMMENT_in_rule665); 

                    }
                    break;

            }


            // GrammarProcessor.g:151:3: ( ( 'protected' | 'public' | 'private' | 'fragment' ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==FRAGMENT||(LA15_0 >= 85 && LA15_0 <= 87)) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // GrammarProcessor.g:
                    {
                    if ( input.LA(1)==FRAGMENT||(input.LA(1) >= 85 && input.LA(1) <= 87) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }


            pushFollow(FOLLOW_id_in_rule687);
            id();

            state._fsp--;


            // GrammarProcessor.g:153:3: ( '!' )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==BANG) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // GrammarProcessor.g:153:3: '!'
                    {
                    match(input,BANG,FOLLOW_BANG_in_rule693); 

                    }
                    break;

            }


            // GrammarProcessor.g:154:3: ( ARG_ACTION )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==ARG_ACTION) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // GrammarProcessor.g:154:5: ARG_ACTION
                    {
                    match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_rule700); 

                    }
                    break;

            }


            // GrammarProcessor.g:155:3: ( 'returns' ARG_ACTION )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==88) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // GrammarProcessor.g:155:5: 'returns' ARG_ACTION
                    {
                    match(input,88,FOLLOW_88_in_rule709); 

                    match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_rule711); 

                    }
                    break;

            }


            // GrammarProcessor.g:156:3: ( throwsSpec )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==89) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // GrammarProcessor.g:156:3: throwsSpec
                    {
                    pushFollow(FOLLOW_throwsSpec_in_rule719);
                    throwsSpec();

                    state._fsp--;


                    }
                    break;

            }


            // GrammarProcessor.g:156:15: ( optionsSpec )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==OPTIONS) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // GrammarProcessor.g:156:15: optionsSpec
                    {
                    pushFollow(FOLLOW_optionsSpec_in_rule722);
                    optionsSpec();

                    state._fsp--;


                    }
                    break;

            }


            // GrammarProcessor.g:156:28: ( ruleScopeSpec )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==SCOPE) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // GrammarProcessor.g:156:28: ruleScopeSpec
                    {
                    pushFollow(FOLLOW_ruleScopeSpec_in_rule725);
                    ruleScopeSpec();

                    state._fsp--;


                    }
                    break;

            }


            // GrammarProcessor.g:156:43: ( ruleAction )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==79) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // GrammarProcessor.g:156:43: ruleAction
            	    {
            	    pushFollow(FOLLOW_ruleAction_in_rule728);
            	    ruleAction();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            match(input,73,FOLLOW_73_in_rule733); 

            pushFollow(FOLLOW_altList_in_rule739);
            altList();

            state._fsp--;


            match(input,75,FOLLOW_75_in_rule741); 

            // GrammarProcessor.g:159:3: ( exceptionGroup )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( ((LA23_0 >= 80 && LA23_0 <= 81)) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // GrammarProcessor.g:159:3: exceptionGroup
                    {
                    pushFollow(FOLLOW_exceptionGroup_in_rule745);
                    exceptionGroup();

                    state._fsp--;


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rule"


    public static class ruleAction_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "ruleAction"
    // GrammarProcessor.g:163:1: ruleAction : '@' id ACTION ;
    public final ruleAction_return ruleAction() throws RecognitionException {
        ruleAction_return retval = new ruleAction_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:164:2: ( '@' id ACTION )
            // GrammarProcessor.g:164:4: '@' id ACTION
            {
            match(input,79,FOLLOW_79_in_ruleAction759); 

            pushFollow(FOLLOW_id_in_ruleAction761);
            id();

            state._fsp--;


            match(input,ACTION,FOLLOW_ACTION_in_ruleAction763); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ruleAction"


    public static class throwsSpec_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "throwsSpec"
    // GrammarProcessor.g:167:1: throwsSpec : 'throws' id ( ',' id )* ;
    public final throwsSpec_return throwsSpec() throws RecognitionException {
        throwsSpec_return retval = new throwsSpec_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:168:2: ( 'throws' id ( ',' id )* )
            // GrammarProcessor.g:168:4: 'throws' id ( ',' id )*
            {
            match(input,89,FOLLOW_89_in_throwsSpec774); 

            pushFollow(FOLLOW_id_in_throwsSpec776);
            id();

            state._fsp--;


            // GrammarProcessor.g:168:16: ( ',' id )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( (LA24_0==71) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // GrammarProcessor.g:168:18: ',' id
            	    {
            	    match(input,71,FOLLOW_71_in_throwsSpec780); 

            	    pushFollow(FOLLOW_id_in_throwsSpec782);
            	    id();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "throwsSpec"


    public static class ruleScopeSpec_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "ruleScopeSpec"
    // GrammarProcessor.g:171:1: ruleScopeSpec : ( 'scope' ACTION | 'scope' id ( ',' id )* ';' | 'scope' ACTION 'scope' id ( ',' id )* ';' );
    public final ruleScopeSpec_return ruleScopeSpec() throws RecognitionException {
        ruleScopeSpec_return retval = new ruleScopeSpec_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:172:2: ( 'scope' ACTION | 'scope' id ( ',' id )* ';' | 'scope' ACTION 'scope' id ( ',' id )* ';' )
            int alt27=3;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==SCOPE) ) {
                int LA27_1 = input.LA(2);

                if ( (LA27_1==ACTION) ) {
                    int LA27_2 = input.LA(3);

                    if ( (LA27_2==SCOPE) ) {
                        alt27=3;
                    }
                    else if ( (LA27_2==73||LA27_2==79) ) {
                        alt27=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 2, input);

                        throw nvae;

                    }
                }
                else if ( (LA27_1==RULE_REF||LA27_1==TOKEN_REF) ) {
                    alt27=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;

            }
            switch (alt27) {
                case 1 :
                    // GrammarProcessor.g:172:4: 'scope' ACTION
                    {
                    match(input,SCOPE,FOLLOW_SCOPE_in_ruleScopeSpec796); 

                    match(input,ACTION,FOLLOW_ACTION_in_ruleScopeSpec798); 

                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:173:4: 'scope' id ( ',' id )* ';'
                    {
                    match(input,SCOPE,FOLLOW_SCOPE_in_ruleScopeSpec803); 

                    pushFollow(FOLLOW_id_in_ruleScopeSpec805);
                    id();

                    state._fsp--;


                    // GrammarProcessor.g:173:15: ( ',' id )*
                    loop25:
                    do {
                        int alt25=2;
                        int LA25_0 = input.LA(1);

                        if ( (LA25_0==71) ) {
                            alt25=1;
                        }


                        switch (alt25) {
                    	case 1 :
                    	    // GrammarProcessor.g:173:16: ',' id
                    	    {
                    	    match(input,71,FOLLOW_71_in_ruleScopeSpec808); 

                    	    pushFollow(FOLLOW_id_in_ruleScopeSpec810);
                    	    id();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop25;
                        }
                    } while (true);


                    match(input,75,FOLLOW_75_in_ruleScopeSpec814); 

                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:174:4: 'scope' ACTION 'scope' id ( ',' id )* ';'
                    {
                    match(input,SCOPE,FOLLOW_SCOPE_in_ruleScopeSpec819); 

                    match(input,ACTION,FOLLOW_ACTION_in_ruleScopeSpec821); 

                    match(input,SCOPE,FOLLOW_SCOPE_in_ruleScopeSpec825); 

                    pushFollow(FOLLOW_id_in_ruleScopeSpec827);
                    id();

                    state._fsp--;


                    // GrammarProcessor.g:175:14: ( ',' id )*
                    loop26:
                    do {
                        int alt26=2;
                        int LA26_0 = input.LA(1);

                        if ( (LA26_0==71) ) {
                            alt26=1;
                        }


                        switch (alt26) {
                    	case 1 :
                    	    // GrammarProcessor.g:175:15: ',' id
                    	    {
                    	    match(input,71,FOLLOW_71_in_ruleScopeSpec830); 

                    	    pushFollow(FOLLOW_id_in_ruleScopeSpec832);
                    	    id();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop26;
                        }
                    } while (true);


                    match(input,75,FOLLOW_75_in_ruleScopeSpec836); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ruleScopeSpec"


    public static class block_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "block"
    // GrammarProcessor.g:178:1: block : '(' ( ( optionsSpec )? ':' )? alternative rewrite ( '|' alternative rewrite )* ')' ;
    public final block_return block() throws RecognitionException {
        block_return retval = new block_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:179:5: ( '(' ( ( optionsSpec )? ':' )? alternative rewrite ( '|' alternative rewrite )* ')' )
            // GrammarProcessor.g:179:7: '(' ( ( optionsSpec )? ':' )? alternative rewrite ( '|' alternative rewrite )* ')'
            {
            match(input,66,FOLLOW_66_in_block850); 

            // GrammarProcessor.g:180:3: ( ( optionsSpec )? ':' )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==OPTIONS||LA29_0==73) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // GrammarProcessor.g:180:5: ( optionsSpec )? ':'
                    {
                    // GrammarProcessor.g:180:5: ( optionsSpec )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==OPTIONS) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // GrammarProcessor.g:180:7: optionsSpec
                            {
                            pushFollow(FOLLOW_optionsSpec_in_block858);
                            optionsSpec();

                            state._fsp--;


                            }
                            break;

                    }


                    match(input,73,FOLLOW_73_in_block862); 

                    }
                    break;

            }


            pushFollow(FOLLOW_alternative_in_block867);
            alternative();

            state._fsp--;


            pushFollow(FOLLOW_rewrite_in_block869);
            rewrite();

            state._fsp--;


            // GrammarProcessor.g:180:48: ( '|' alternative rewrite )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==91) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // GrammarProcessor.g:180:50: '|' alternative rewrite
            	    {
            	    match(input,91,FOLLOW_91_in_block873); 

            	    pushFollow(FOLLOW_alternative_in_block875);
            	    alternative();

            	    state._fsp--;


            	    pushFollow(FOLLOW_rewrite_in_block877);
            	    rewrite();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);


            match(input,67,FOLLOW_67_in_block882); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "block"


    public static class altList_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "altList"
    // GrammarProcessor.g:183:1: altList : alternative rewrite ( '|' alternative rewrite )* ;
    public final altList_return altList() throws RecognitionException {
        altList_return retval = new altList_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:184:5: ( alternative rewrite ( '|' alternative rewrite )* )
            // GrammarProcessor.g:184:7: alternative rewrite ( '|' alternative rewrite )*
            {
            pushFollow(FOLLOW_alternative_in_altList899);
            alternative();

            state._fsp--;


            pushFollow(FOLLOW_rewrite_in_altList901);
            rewrite();

            state._fsp--;


            // GrammarProcessor.g:184:27: ( '|' alternative rewrite )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==91) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // GrammarProcessor.g:184:29: '|' alternative rewrite
            	    {
            	    match(input,91,FOLLOW_91_in_altList905); 

            	    pushFollow(FOLLOW_alternative_in_altList907);
            	    alternative();

            	    state._fsp--;


            	    pushFollow(FOLLOW_rewrite_in_altList909);
            	    rewrite();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "altList"


    public static class alternative_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "alternative"
    // GrammarProcessor.g:187:1: alternative : ( ( element )+ |);
    public final alternative_return alternative() throws RecognitionException {
        alternative_return retval = new alternative_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:188:5: ( ( element )+ |)
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==ACTION||LA33_0==CHAR_LITERAL||LA33_0==RULE_REF||LA33_0==SEMPRED||LA33_0==STRING_LITERAL||(LA33_0 >= TOKEN_REF && LA33_0 <= TREE_BEGIN)||LA33_0==66||LA33_0==72||LA33_0==93) ) {
                alt33=1;
            }
            else if ( (LA33_0==REWRITE||LA33_0==67||LA33_0==75||LA33_0==91) ) {
                alt33=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 33, 0, input);

                throw nvae;

            }
            switch (alt33) {
                case 1 :
                    // GrammarProcessor.g:188:9: ( element )+
                    {
                    // GrammarProcessor.g:188:9: ( element )+
                    int cnt32=0;
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( (LA32_0==ACTION||LA32_0==CHAR_LITERAL||LA32_0==RULE_REF||LA32_0==SEMPRED||LA32_0==STRING_LITERAL||(LA32_0 >= TOKEN_REF && LA32_0 <= TREE_BEGIN)||LA32_0==66||LA32_0==72||LA32_0==93) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // GrammarProcessor.g:188:9: element
                    	    {
                    	    pushFollow(FOLLOW_element_in_alternative931);
                    	    element();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt32 >= 1 ) break loop32;
                                EarlyExitException eee =
                                    new EarlyExitException(32, input);
                                throw eee;
                        }
                        cnt32++;
                    } while (true);


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:190:5: 
                    {
                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "alternative"


    public static class exceptionGroup_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "exceptionGroup"
    // GrammarProcessor.g:192:1: exceptionGroup : ( ( exceptionHandler )+ ( finallyClause )? | finallyClause );
    public final exceptionGroup_return exceptionGroup() throws RecognitionException {
        exceptionGroup_return retval = new exceptionGroup_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:193:2: ( ( exceptionHandler )+ ( finallyClause )? | finallyClause )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==80) ) {
                alt36=1;
            }
            else if ( (LA36_0==81) ) {
                alt36=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;

            }
            switch (alt36) {
                case 1 :
                    // GrammarProcessor.g:193:4: ( exceptionHandler )+ ( finallyClause )?
                    {
                    // GrammarProcessor.g:193:4: ( exceptionHandler )+
                    int cnt34=0;
                    loop34:
                    do {
                        int alt34=2;
                        int LA34_0 = input.LA(1);

                        if ( (LA34_0==80) ) {
                            alt34=1;
                        }


                        switch (alt34) {
                    	case 1 :
                    	    // GrammarProcessor.g:193:6: exceptionHandler
                    	    {
                    	    pushFollow(FOLLOW_exceptionHandler_in_exceptionGroup954);
                    	    exceptionHandler();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt34 >= 1 ) break loop34;
                                EarlyExitException eee =
                                    new EarlyExitException(34, input);
                                throw eee;
                        }
                        cnt34++;
                    } while (true);


                    // GrammarProcessor.g:193:26: ( finallyClause )?
                    int alt35=2;
                    int LA35_0 = input.LA(1);

                    if ( (LA35_0==81) ) {
                        alt35=1;
                    }
                    switch (alt35) {
                        case 1 :
                            // GrammarProcessor.g:193:28: finallyClause
                            {
                            pushFollow(FOLLOW_finallyClause_in_exceptionGroup961);
                            finallyClause();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:194:4: finallyClause
                    {
                    pushFollow(FOLLOW_finallyClause_in_exceptionGroup969);
                    finallyClause();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "exceptionGroup"


    public static class exceptionHandler_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "exceptionHandler"
    // GrammarProcessor.g:197:1: exceptionHandler : 'catch' ARG_ACTION ACTION ;
    public final exceptionHandler_return exceptionHandler() throws RecognitionException {
        exceptionHandler_return retval = new exceptionHandler_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:198:5: ( 'catch' ARG_ACTION ACTION )
            // GrammarProcessor.g:198:10: 'catch' ARG_ACTION ACTION
            {
            match(input,80,FOLLOW_80_in_exceptionHandler989); 

            match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_exceptionHandler991); 

            match(input,ACTION,FOLLOW_ACTION_in_exceptionHandler993); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "exceptionHandler"


    public static class finallyClause_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "finallyClause"
    // GrammarProcessor.g:201:1: finallyClause : 'finally' ACTION ;
    public final finallyClause_return finallyClause() throws RecognitionException {
        finallyClause_return retval = new finallyClause_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:202:5: ( 'finally' ACTION )
            // GrammarProcessor.g:202:10: 'finally' ACTION
            {
            match(input,81,FOLLOW_81_in_finallyClause1013); 

            match(input,ACTION,FOLLOW_ACTION_in_finallyClause1015); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "finallyClause"


    public static class element_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "element"
    // GrammarProcessor.g:205:1: element : elementNoOptionSpec ;
    public final element_return element() throws RecognitionException {
        element_return retval = new element_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:206:2: ( elementNoOptionSpec )
            // GrammarProcessor.g:206:4: elementNoOptionSpec
            {
            pushFollow(FOLLOW_elementNoOptionSpec_in_element1029);
            elementNoOptionSpec();

            state._fsp--;


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "element"


    public static class elementNoOptionSpec_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "elementNoOptionSpec"
    // GrammarProcessor.g:209:1: elementNoOptionSpec : ( id ( '=' | '+=' ) atom ( ebnfSuffix |) | id ( '=' | '+=' ) block ( ebnfSuffix |) | atom ( ebnfSuffix |) | ebnf | ACTION | SEMPRED ( '=>' |) | treeSpec ( ebnfSuffix |) );
    public final elementNoOptionSpec_return elementNoOptionSpec() throws RecognitionException {
        elementNoOptionSpec_return retval = new elementNoOptionSpec_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:210:2: ( id ( '=' | '+=' ) atom ( ebnfSuffix |) | id ( '=' | '+=' ) block ( ebnfSuffix |) | atom ( ebnfSuffix |) | ebnf | ACTION | SEMPRED ( '=>' |) | treeSpec ( ebnfSuffix |) )
            int alt42=7;
            switch ( input.LA(1) ) {
            case TOKEN_REF:
                {
                int LA42_1 = input.LA(2);

                if ( (LA42_1==70||LA42_1==76) ) {
                    int LA42_8 = input.LA(3);

                    if ( (LA42_8==CHAR_LITERAL||LA42_8==RULE_REF||LA42_8==STRING_LITERAL||LA42_8==TOKEN_REF||LA42_8==72||LA42_8==93) ) {
                        alt42=1;
                    }
                    else if ( (LA42_8==66) ) {
                        alt42=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 42, 8, input);

                        throw nvae;

                    }
                }
                else if ( (LA42_1==ACTION||LA42_1==ARG_ACTION||LA42_1==BANG||LA42_1==CHAR_LITERAL||(LA42_1 >= REWRITE && LA42_1 <= ROOT)||LA42_1==RULE_REF||LA42_1==SEMPRED||LA42_1==STRING_LITERAL||(LA42_1 >= TOKEN_REF && LA42_1 <= TREE_BEGIN)||(LA42_1 >= 66 && LA42_1 <= 69)||LA42_1==72||LA42_1==75||LA42_1==78||LA42_1==91||LA42_1==93) ) {
                    alt42=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 42, 1, input);

                    throw nvae;

                }
                }
                break;
            case CHAR_LITERAL:
            case STRING_LITERAL:
            case 72:
            case 93:
                {
                alt42=3;
                }
                break;
            case RULE_REF:
                {
                int LA42_3 = input.LA(2);

                if ( (LA42_3==70||LA42_3==76) ) {
                    int LA42_8 = input.LA(3);

                    if ( (LA42_8==CHAR_LITERAL||LA42_8==RULE_REF||LA42_8==STRING_LITERAL||LA42_8==TOKEN_REF||LA42_8==72||LA42_8==93) ) {
                        alt42=1;
                    }
                    else if ( (LA42_8==66) ) {
                        alt42=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 42, 8, input);

                        throw nvae;

                    }
                }
                else if ( (LA42_3==ACTION||LA42_3==ARG_ACTION||LA42_3==BANG||LA42_3==CHAR_LITERAL||(LA42_3 >= REWRITE && LA42_3 <= ROOT)||LA42_3==RULE_REF||LA42_3==SEMPRED||LA42_3==STRING_LITERAL||(LA42_3 >= TOKEN_REF && LA42_3 <= TREE_BEGIN)||(LA42_3 >= 66 && LA42_3 <= 69)||LA42_3==72||LA42_3==75||LA42_3==78||LA42_3==91||LA42_3==93) ) {
                    alt42=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 42, 3, input);

                    throw nvae;

                }
                }
                break;
            case 66:
                {
                alt42=4;
                }
                break;
            case ACTION:
                {
                alt42=5;
                }
                break;
            case SEMPRED:
                {
                alt42=6;
                }
                break;
            case TREE_BEGIN:
                {
                alt42=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;

            }

            switch (alt42) {
                case 1 :
                    // GrammarProcessor.g:210:4: id ( '=' | '+=' ) atom ( ebnfSuffix |)
                    {
                    pushFollow(FOLLOW_id_in_elementNoOptionSpec1040);
                    id();

                    state._fsp--;


                    if ( input.LA(1)==70||input.LA(1)==76 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    pushFollow(FOLLOW_atom_in_elementNoOptionSpec1050);
                    atom();

                    state._fsp--;


                    // GrammarProcessor.g:211:3: ( ebnfSuffix |)
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( ((LA37_0 >= 68 && LA37_0 <= 69)||LA37_0==78) ) {
                        alt37=1;
                    }
                    else if ( (LA37_0==ACTION||LA37_0==CHAR_LITERAL||LA37_0==REWRITE||LA37_0==RULE_REF||LA37_0==SEMPRED||LA37_0==STRING_LITERAL||(LA37_0 >= TOKEN_REF && LA37_0 <= TREE_BEGIN)||(LA37_0 >= 66 && LA37_0 <= 67)||LA37_0==72||LA37_0==75||LA37_0==91||LA37_0==93) ) {
                        alt37=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 37, 0, input);

                        throw nvae;

                    }
                    switch (alt37) {
                        case 1 :
                            // GrammarProcessor.g:211:5: ebnfSuffix
                            {
                            pushFollow(FOLLOW_ebnfSuffix_in_elementNoOptionSpec1056);
                            ebnfSuffix();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:213:3: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:214:4: id ( '=' | '+=' ) block ( ebnfSuffix |)
                    {
                    pushFollow(FOLLOW_id_in_elementNoOptionSpec1069);
                    id();

                    state._fsp--;


                    if ( input.LA(1)==70||input.LA(1)==76 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    pushFollow(FOLLOW_block_in_elementNoOptionSpec1079);
                    block();

                    state._fsp--;


                    // GrammarProcessor.g:215:3: ( ebnfSuffix |)
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( ((LA38_0 >= 68 && LA38_0 <= 69)||LA38_0==78) ) {
                        alt38=1;
                    }
                    else if ( (LA38_0==ACTION||LA38_0==CHAR_LITERAL||LA38_0==REWRITE||LA38_0==RULE_REF||LA38_0==SEMPRED||LA38_0==STRING_LITERAL||(LA38_0 >= TOKEN_REF && LA38_0 <= TREE_BEGIN)||(LA38_0 >= 66 && LA38_0 <= 67)||LA38_0==72||LA38_0==75||LA38_0==91||LA38_0==93) ) {
                        alt38=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 38, 0, input);

                        throw nvae;

                    }
                    switch (alt38) {
                        case 1 :
                            // GrammarProcessor.g:215:5: ebnfSuffix
                            {
                            pushFollow(FOLLOW_ebnfSuffix_in_elementNoOptionSpec1085);
                            ebnfSuffix();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:217:3: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:218:4: atom ( ebnfSuffix |)
                    {
                    pushFollow(FOLLOW_atom_in_elementNoOptionSpec1098);
                    atom();

                    state._fsp--;


                    // GrammarProcessor.g:219:3: ( ebnfSuffix |)
                    int alt39=2;
                    int LA39_0 = input.LA(1);

                    if ( ((LA39_0 >= 68 && LA39_0 <= 69)||LA39_0==78) ) {
                        alt39=1;
                    }
                    else if ( (LA39_0==ACTION||LA39_0==CHAR_LITERAL||LA39_0==REWRITE||LA39_0==RULE_REF||LA39_0==SEMPRED||LA39_0==STRING_LITERAL||(LA39_0 >= TOKEN_REF && LA39_0 <= TREE_BEGIN)||(LA39_0 >= 66 && LA39_0 <= 67)||LA39_0==72||LA39_0==75||LA39_0==91||LA39_0==93) ) {
                        alt39=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 39, 0, input);

                        throw nvae;

                    }
                    switch (alt39) {
                        case 1 :
                            // GrammarProcessor.g:219:5: ebnfSuffix
                            {
                            pushFollow(FOLLOW_ebnfSuffix_in_elementNoOptionSpec1104);
                            ebnfSuffix();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:221:3: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:222:4: ebnf
                    {
                    pushFollow(FOLLOW_ebnf_in_elementNoOptionSpec1117);
                    ebnf();

                    state._fsp--;


                    }
                    break;
                case 5 :
                    // GrammarProcessor.g:223:6: ACTION
                    {
                    match(input,ACTION,FOLLOW_ACTION_in_elementNoOptionSpec1124); 

                    }
                    break;
                case 6 :
                    // GrammarProcessor.g:224:6: SEMPRED ( '=>' |)
                    {
                    match(input,SEMPRED,FOLLOW_SEMPRED_in_elementNoOptionSpec1131); 

                    // GrammarProcessor.g:224:14: ( '=>' |)
                    int alt40=2;
                    int LA40_0 = input.LA(1);

                    if ( (LA40_0==77) ) {
                        alt40=1;
                    }
                    else if ( (LA40_0==ACTION||LA40_0==CHAR_LITERAL||LA40_0==REWRITE||LA40_0==RULE_REF||LA40_0==SEMPRED||LA40_0==STRING_LITERAL||(LA40_0 >= TOKEN_REF && LA40_0 <= TREE_BEGIN)||(LA40_0 >= 66 && LA40_0 <= 67)||LA40_0==72||LA40_0==75||LA40_0==91||LA40_0==93) ) {
                        alt40=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 40, 0, input);

                        throw nvae;

                    }
                    switch (alt40) {
                        case 1 :
                            // GrammarProcessor.g:224:16: '=>'
                            {
                            match(input,77,FOLLOW_77_in_elementNoOptionSpec1135); 

                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:224:23: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 7 :
                    // GrammarProcessor.g:225:6: treeSpec ( ebnfSuffix |)
                    {
                    pushFollow(FOLLOW_treeSpec_in_elementNoOptionSpec1146);
                    treeSpec();

                    state._fsp--;


                    // GrammarProcessor.g:226:3: ( ebnfSuffix |)
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( ((LA41_0 >= 68 && LA41_0 <= 69)||LA41_0==78) ) {
                        alt41=1;
                    }
                    else if ( (LA41_0==ACTION||LA41_0==CHAR_LITERAL||LA41_0==REWRITE||LA41_0==RULE_REF||LA41_0==SEMPRED||LA41_0==STRING_LITERAL||(LA41_0 >= TOKEN_REF && LA41_0 <= TREE_BEGIN)||(LA41_0 >= 66 && LA41_0 <= 67)||LA41_0==72||LA41_0==75||LA41_0==91||LA41_0==93) ) {
                        alt41=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 41, 0, input);

                        throw nvae;

                    }
                    switch (alt41) {
                        case 1 :
                            // GrammarProcessor.g:226:5: ebnfSuffix
                            {
                            pushFollow(FOLLOW_ebnfSuffix_in_elementNoOptionSpec1152);
                            ebnfSuffix();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:228:3: 
                            {
                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "elementNoOptionSpec"


    public static class atom_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "atom"
    // GrammarProcessor.g:231:1: atom : ( range ( ( '^' | '!' ) |) | terminal | notSet ( ( '^' | '!' ) |) | RULE_REF ( ARG_ACTION )? ( ( '^' | '!' ) )? );
    public final atom_return atom() throws RecognitionException {
        atom_return retval = new atom_return();
        retval.start = input.LT(1);


        Token RULE_REF1=null;

        try {
            // GrammarProcessor.g:231:5: ( range ( ( '^' | '!' ) |) | terminal | notSet ( ( '^' | '!' ) |) | RULE_REF ( ARG_ACTION )? ( ( '^' | '!' ) )? )
            int alt47=4;
            switch ( input.LA(1) ) {
            case CHAR_LITERAL:
                {
                int LA47_1 = input.LA(2);

                if ( (LA47_1==RANGE) ) {
                    alt47=1;
                }
                else if ( (LA47_1==ACTION||LA47_1==BANG||LA47_1==CHAR_LITERAL||(LA47_1 >= REWRITE && LA47_1 <= ROOT)||LA47_1==RULE_REF||LA47_1==SEMPRED||LA47_1==STRING_LITERAL||(LA47_1 >= TOKEN_REF && LA47_1 <= TREE_BEGIN)||(LA47_1 >= 66 && LA47_1 <= 69)||LA47_1==72||LA47_1==75||LA47_1==78||LA47_1==91||LA47_1==93) ) {
                    alt47=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 47, 1, input);

                    throw nvae;

                }
                }
                break;
            case STRING_LITERAL:
            case TOKEN_REF:
            case 72:
                {
                alt47=2;
                }
                break;
            case 93:
                {
                alt47=3;
                }
                break;
            case RULE_REF:
                {
                alt47=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;

            }

            switch (alt47) {
                case 1 :
                    // GrammarProcessor.g:231:9: range ( ( '^' | '!' ) |)
                    {
                    pushFollow(FOLLOW_range_in_atom1171);
                    range();

                    state._fsp--;


                    // GrammarProcessor.g:231:15: ( ( '^' | '!' ) |)
                    int alt43=2;
                    int LA43_0 = input.LA(1);

                    if ( (LA43_0==BANG||LA43_0==ROOT) ) {
                        alt43=1;
                    }
                    else if ( (LA43_0==ACTION||LA43_0==CHAR_LITERAL||LA43_0==REWRITE||LA43_0==RULE_REF||LA43_0==SEMPRED||LA43_0==STRING_LITERAL||(LA43_0 >= TOKEN_REF && LA43_0 <= TREE_BEGIN)||(LA43_0 >= 66 && LA43_0 <= 69)||LA43_0==72||LA43_0==75||LA43_0==78||LA43_0==91||LA43_0==93) ) {
                        alt43=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 43, 0, input);

                        throw nvae;

                    }
                    switch (alt43) {
                        case 1 :
                            // GrammarProcessor.g:231:17: ( '^' | '!' )
                            {
                            if ( input.LA(1)==BANG||input.LA(1)==ROOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:231:31: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:232:9: terminal
                    {
                    pushFollow(FOLLOW_terminal_in_atom1195);
                    terminal();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:233:7: notSet ( ( '^' | '!' ) |)
                    {
                    pushFollow(FOLLOW_notSet_in_atom1203);
                    notSet();

                    state._fsp--;


                    // GrammarProcessor.g:233:14: ( ( '^' | '!' ) |)
                    int alt44=2;
                    int LA44_0 = input.LA(1);

                    if ( (LA44_0==BANG||LA44_0==ROOT) ) {
                        alt44=1;
                    }
                    else if ( (LA44_0==ACTION||LA44_0==CHAR_LITERAL||LA44_0==REWRITE||LA44_0==RULE_REF||LA44_0==SEMPRED||LA44_0==STRING_LITERAL||(LA44_0 >= TOKEN_REF && LA44_0 <= TREE_BEGIN)||(LA44_0 >= 66 && LA44_0 <= 69)||LA44_0==72||LA44_0==75||LA44_0==78||LA44_0==91||LA44_0==93) ) {
                        alt44=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 44, 0, input);

                        throw nvae;

                    }
                    switch (alt44) {
                        case 1 :
                            // GrammarProcessor.g:233:16: ( '^' | '!' )
                            {
                            if ( input.LA(1)==BANG||input.LA(1)==ROOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:233:30: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:234:9: RULE_REF ( ARG_ACTION )? ( ( '^' | '!' ) )?
                    {
                    RULE_REF1=(Token)match(input,RULE_REF,FOLLOW_RULE_REF_in_atom1227); 


                                String tagName = "t"+count++;
                                String content =
                                "{T.pushTop();T.setCurrentParent(T.addNode(\""+(RULE_REF1!=null?RULE_REF1.getText():null)+"\"));} "+
                                tagName + "=" +(RULE_REF1!=null?RULE_REF1.getText():null)+" "+
                                "{T.popTop().setTextRange($"+tagName+".start, $"+tagName+".stop);}";
                                ((TokenRewriteStream) input).replace(RULE_REF1.getTokenIndex(), RULE_REF1.getTokenIndex(), new StringTemplate(templateLib, content));
                               

                    // GrammarProcessor.g:243:9: ( ARG_ACTION )?
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==ARG_ACTION) ) {
                        alt45=1;
                    }
                    switch (alt45) {
                        case 1 :
                            // GrammarProcessor.g:243:11: ARG_ACTION
                            {
                            match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_atom1267); 

                            }
                            break;

                    }


                    // GrammarProcessor.g:243:25: ( ( '^' | '!' ) )?
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==BANG||LA46_0==ROOT) ) {
                        alt46=1;
                    }
                    switch (alt46) {
                        case 1 :
                            // GrammarProcessor.g:
                            {
                            if ( input.LA(1)==BANG||input.LA(1)==ROOT ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "atom"


    public static class notSet_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "notSet"
    // GrammarProcessor.g:246:1: notSet : '~' ( notTerminal | block ) ;
    public final notSet_return notSet() throws RecognitionException {
        notSet_return retval = new notSet_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:247:2: ( '~' ( notTerminal | block ) )
            // GrammarProcessor.g:247:4: '~' ( notTerminal | block )
            {
            match(input,93,FOLLOW_93_in_notSet1297); 

            // GrammarProcessor.g:248:3: ( notTerminal | block )
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==CHAR_LITERAL||LA48_0==STRING_LITERAL||LA48_0==TOKEN_REF) ) {
                alt48=1;
            }
            else if ( (LA48_0==66) ) {
                alt48=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;

            }
            switch (alt48) {
                case 1 :
                    // GrammarProcessor.g:248:5: notTerminal
                    {
                    pushFollow(FOLLOW_notTerminal_in_notSet1303);
                    notTerminal();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:249:5: block
                    {
                    pushFollow(FOLLOW_block_in_notSet1309);
                    block();

                    state._fsp--;


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "notSet"


    public static class treeSpec_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "treeSpec"
    // GrammarProcessor.g:253:1: treeSpec : '^(' element ( element )+ ')' ;
    public final treeSpec_return treeSpec() throws RecognitionException {
        treeSpec_return retval = new treeSpec_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:254:2: ( '^(' element ( element )+ ')' )
            // GrammarProcessor.g:254:4: '^(' element ( element )+ ')'
            {
            match(input,TREE_BEGIN,FOLLOW_TREE_BEGIN_in_treeSpec1324); 

            pushFollow(FOLLOW_element_in_treeSpec1326);
            element();

            state._fsp--;


            // GrammarProcessor.g:254:17: ( element )+
            int cnt49=0;
            loop49:
            do {
                int alt49=2;
                int LA49_0 = input.LA(1);

                if ( (LA49_0==ACTION||LA49_0==CHAR_LITERAL||LA49_0==RULE_REF||LA49_0==SEMPRED||LA49_0==STRING_LITERAL||(LA49_0 >= TOKEN_REF && LA49_0 <= TREE_BEGIN)||LA49_0==66||LA49_0==72||LA49_0==93) ) {
                    alt49=1;
                }


                switch (alt49) {
            	case 1 :
            	    // GrammarProcessor.g:254:19: element
            	    {
            	    pushFollow(FOLLOW_element_in_treeSpec1330);
            	    element();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt49 >= 1 ) break loop49;
                        EarlyExitException eee =
                            new EarlyExitException(49, input);
                        throw eee;
                }
                cnt49++;
            } while (true);


            match(input,67,FOLLOW_67_in_treeSpec1335); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "treeSpec"


    public static class ebnf_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "ebnf"
    // GrammarProcessor.g:258:1: ebnf : block ( '?' | '*' | '+' | '=>' |) ;
    public final ebnf_return ebnf() throws RecognitionException {
        ebnf_return retval = new ebnf_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:259:2: ( block ( '?' | '*' | '+' | '=>' |) )
            // GrammarProcessor.g:259:4: block ( '?' | '*' | '+' | '=>' |)
            {
            pushFollow(FOLLOW_block_in_ebnf1348);
            block();

            state._fsp--;


            // GrammarProcessor.g:260:3: ( '?' | '*' | '+' | '=>' |)
            int alt50=5;
            switch ( input.LA(1) ) {
            case 78:
                {
                alt50=1;
                }
                break;
            case 68:
                {
                alt50=2;
                }
                break;
            case 69:
                {
                alt50=3;
                }
                break;
            case 77:
                {
                alt50=4;
                }
                break;
            case ACTION:
            case CHAR_LITERAL:
            case REWRITE:
            case RULE_REF:
            case SEMPRED:
            case STRING_LITERAL:
            case TOKEN_REF:
            case TREE_BEGIN:
            case 66:
            case 67:
            case 72:
            case 75:
            case 91:
            case 93:
                {
                alt50=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;

            }

            switch (alt50) {
                case 1 :
                    // GrammarProcessor.g:260:5: '?'
                    {
                    match(input,78,FOLLOW_78_in_ebnf1354); 

                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:261:5: '*'
                    {
                    match(input,68,FOLLOW_68_in_ebnf1360); 

                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:262:5: '+'
                    {
                    match(input,69,FOLLOW_69_in_ebnf1366); 

                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:263:7: '=>'
                    {
                    match(input,77,FOLLOW_77_in_ebnf1374); 

                    }
                    break;
                case 5 :
                    // GrammarProcessor.g:265:3: 
                    {
                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ebnf"


    public static class range_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "range"
    // GrammarProcessor.g:268:1: range : CHAR_LITERAL RANGE CHAR_LITERAL ;
    public final range_return range() throws RecognitionException {
        range_return retval = new range_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:269:2: ( CHAR_LITERAL RANGE CHAR_LITERAL )
            // GrammarProcessor.g:269:4: CHAR_LITERAL RANGE CHAR_LITERAL
            {
            match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_range1399); 

            match(input,RANGE,FOLLOW_RANGE_in_range1401); 

            match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_range1403); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "range"


    public static class terminal_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "terminal"
    // GrammarProcessor.g:272:1: terminal : ( CHAR_LITERAL | TOKEN_REF ( ARG_ACTION |) | STRING_LITERAL | '.' ) ( '^' | '!' )? ;
    public final terminal_return terminal() throws RecognitionException {
        terminal_return retval = new terminal_return();
        retval.start = input.LT(1);


        Token CHAR_LITERAL2=null;
        Token TOKEN_REF3=null;
        Token STRING_LITERAL4=null;

        try {
            // GrammarProcessor.g:273:5: ( ( CHAR_LITERAL | TOKEN_REF ( ARG_ACTION |) | STRING_LITERAL | '.' ) ( '^' | '!' )? )
            // GrammarProcessor.g:273:9: ( CHAR_LITERAL | TOKEN_REF ( ARG_ACTION |) | STRING_LITERAL | '.' ) ( '^' | '!' )?
            {
            // GrammarProcessor.g:273:9: ( CHAR_LITERAL | TOKEN_REF ( ARG_ACTION |) | STRING_LITERAL | '.' )
            int alt52=4;
            switch ( input.LA(1) ) {
            case CHAR_LITERAL:
                {
                alt52=1;
                }
                break;
            case TOKEN_REF:
                {
                alt52=2;
                }
                break;
            case STRING_LITERAL:
                {
                alt52=3;
                }
                break;
            case 72:
                {
                alt52=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 52, 0, input);

                throw nvae;

            }

            switch (alt52) {
                case 1 :
                    // GrammarProcessor.g:273:11: CHAR_LITERAL
                    {
                    CHAR_LITERAL2=(Token)match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_terminal1421); 

                                        
                                String s = (CHAR_LITERAL2!=null?CHAR_LITERAL2.getText():null);
                                s = s.substring(1,s.length());
                                s = s.substring(0,s.length()-1);
                                //s = s.replace("%",("\\"+"%"));
                                //s = s.replace("'","\\'");
                                s = s.replace("<", "\\<");
                                String escapedName = "'"+s+"'";
                                String content = 
                                "("+escapedName + "{T.addLeaf(\"'"+s.replace("%",("\\"+"%"))+"'\",input.LT(-1));})";
                                
                                ((TokenRewriteStream) input).replace(CHAR_LITERAL2.getTokenIndex(), CHAR_LITERAL2.getTokenIndex(), new StringTemplate(templateLib, content));
                               

                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:287:7: TOKEN_REF ( ARG_ACTION |)
                    {
                    TOKEN_REF3=(Token)match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_terminal1445); 


                                    String name = (TOKEN_REF3!=null?TOKEN_REF3.getText():null);
                                    String content =
                                    name + " {T.addLeaf(\""+name+"['\"+input.LT(-1).getText()+\"']\",input.LT(-1));}";
                                    ((TokenRewriteStream) input).replace(TOKEN_REF3.getTokenIndex(), TOKEN_REF3.getTokenIndex(), new StringTemplate(templateLib, content));
                                

                    // GrammarProcessor.g:294:4: ( ARG_ACTION |)
                    int alt51=2;
                    int LA51_0 = input.LA(1);

                    if ( (LA51_0==ARG_ACTION) ) {
                        alt51=1;
                    }
                    else if ( (LA51_0==ACTION||LA51_0==BANG||LA51_0==CHAR_LITERAL||(LA51_0 >= REWRITE && LA51_0 <= ROOT)||LA51_0==RULE_REF||LA51_0==SEMPRED||LA51_0==STRING_LITERAL||(LA51_0 >= TOKEN_REF && LA51_0 <= TREE_BEGIN)||(LA51_0 >= 66 && LA51_0 <= 69)||LA51_0==72||LA51_0==75||LA51_0==78||LA51_0==91||LA51_0==93) ) {
                        alt51=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 51, 0, input);

                        throw nvae;

                    }
                    switch (alt51) {
                        case 1 :
                            // GrammarProcessor.g:294:6: ARG_ACTION
                            {
                            match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_terminal1468); 

                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:296:4: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:297:7: STRING_LITERAL
                    {
                    STRING_LITERAL4=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_terminal1486); 


                                String s = (STRING_LITERAL4!=null?STRING_LITERAL4.getText():null);
                                s = s.replace("%",("\\"+"%"));
                                String content =
                                (STRING_LITERAL4!=null?STRING_LITERAL4.getText():null) + "{T.addLeaf(\""+s+"\",input.LT(-1));}";
                                ((TokenRewriteStream) input).replace(STRING_LITERAL4.getTokenIndex(), STRING_LITERAL4.getTokenIndex(), new StringTemplate(templateLib, content));
                               

                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:305:7: '.'
                    {
                    match(input,72,FOLLOW_72_in_terminal1511); 

                    }
                    break;

            }


            // GrammarProcessor.g:307:3: ( '^' | '!' )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==BANG||LA53_0==ROOT) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // GrammarProcessor.g:
                    {
                    if ( input.LA(1)==BANG||input.LA(1)==ROOT ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "terminal"


    public static class notTerminal_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "notTerminal"
    // GrammarProcessor.g:312:1: notTerminal : ( CHAR_LITERAL | TOKEN_REF | STRING_LITERAL );
    public final notTerminal_return notTerminal() throws RecognitionException {
        notTerminal_return retval = new notTerminal_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:313:2: ( CHAR_LITERAL | TOKEN_REF | STRING_LITERAL )
            // GrammarProcessor.g:
            {
            if ( input.LA(1)==CHAR_LITERAL||input.LA(1)==STRING_LITERAL||input.LA(1)==TOKEN_REF ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "notTerminal"


    public static class ebnfSuffix_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "ebnfSuffix"
    // GrammarProcessor.g:318:1: ebnfSuffix : ( '?' | '*' | '+' );
    public final ebnfSuffix_return ebnfSuffix() throws RecognitionException {
        ebnfSuffix_return retval = new ebnfSuffix_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:319:2: ( '?' | '*' | '+' )
            // GrammarProcessor.g:
            {
            if ( (input.LA(1) >= 68 && input.LA(1) <= 69)||input.LA(1)==78 ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "ebnfSuffix"


    public static class rewrite_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite"
    // GrammarProcessor.g:328:1: rewrite : ( ( '->' SEMPRED rewrite_alternative )* '->' rewrite_alternative |);
    public final rewrite_return rewrite() throws RecognitionException {
        rewrite_return retval = new rewrite_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:329:2: ( ( '->' SEMPRED rewrite_alternative )* '->' rewrite_alternative |)
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==REWRITE) ) {
                alt55=1;
            }
            else if ( (LA55_0==67||LA55_0==75||LA55_0==91) ) {
                alt55=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 55, 0, input);

                throw nvae;

            }
            switch (alt55) {
                case 1 :
                    // GrammarProcessor.g:329:4: ( '->' SEMPRED rewrite_alternative )* '->' rewrite_alternative
                    {
                    // GrammarProcessor.g:329:4: ( '->' SEMPRED rewrite_alternative )*
                    loop54:
                    do {
                        int alt54=2;
                        int LA54_0 = input.LA(1);

                        if ( (LA54_0==REWRITE) ) {
                            int LA54_1 = input.LA(2);

                            if ( (LA54_1==SEMPRED) ) {
                                alt54=1;
                            }


                        }


                        switch (alt54) {
                    	case 1 :
                    	    // GrammarProcessor.g:329:6: '->' SEMPRED rewrite_alternative
                    	    {
                    	    match(input,REWRITE,FOLLOW_REWRITE_in_rewrite1601); 

                    	    match(input,SEMPRED,FOLLOW_SEMPRED_in_rewrite1603); 

                    	    pushFollow(FOLLOW_rewrite_alternative_in_rewrite1605);
                    	    rewrite_alternative();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop54;
                        }
                    } while (true);


                    match(input,REWRITE,FOLLOW_REWRITE_in_rewrite1609); 

                    pushFollow(FOLLOW_rewrite_alternative_in_rewrite1611);
                    rewrite_alternative();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:331:2: 
                    {
                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite"


    public static class rewrite_alternative_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_alternative"
    // GrammarProcessor.g:333:1: rewrite_alternative : ( rewrite_template | rewrite_tree_alternative |);
    public final rewrite_alternative_return rewrite_alternative() throws RecognitionException {
        rewrite_alternative_return retval = new rewrite_alternative_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:334:2: ( rewrite_template | rewrite_tree_alternative |)
            int alt56=3;
            switch ( input.LA(1) ) {
            case TOKEN_REF:
                {
                int LA56_1 = input.LA(2);

                if ( (LA56_1==66) ) {
                    switch ( input.LA(3) ) {
                    case TOKEN_REF:
                        {
                        int LA56_9 = input.LA(4);

                        if ( (LA56_9==76) ) {
                            alt56=1;
                        }
                        else if ( (LA56_9==ACTION||LA56_9==ARG_ACTION||LA56_9==CHAR_LITERAL||LA56_9==RULE_REF||LA56_9==STRING_LITERAL||(LA56_9 >= TOKEN_REF && LA56_9 <= TREE_BEGIN)||(LA56_9 >= 65 && LA56_9 <= 69)||LA56_9==78) ) {
                            alt56=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 56, 9, input);

                            throw nvae;

                        }
                        }
                        break;
                    case 67:
                        {
                        alt56=1;
                        }
                        break;
                    case ACTION:
                    case CHAR_LITERAL:
                    case STRING_LITERAL:
                    case TREE_BEGIN:
                    case 65:
                    case 66:
                        {
                        alt56=2;
                        }
                        break;
                    case RULE_REF:
                        {
                        int LA56_10 = input.LA(4);

                        if ( (LA56_10==76) ) {
                            alt56=1;
                        }
                        else if ( (LA56_10==ACTION||LA56_10==CHAR_LITERAL||LA56_10==RULE_REF||LA56_10==STRING_LITERAL||(LA56_10 >= TOKEN_REF && LA56_10 <= TREE_BEGIN)||(LA56_10 >= 65 && LA56_10 <= 69)||LA56_10==78) ) {
                            alt56=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 56, 10, input);

                            throw nvae;

                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 56, 7, input);

                        throw nvae;

                    }

                }
                else if ( (LA56_1==ACTION||LA56_1==ARG_ACTION||LA56_1==CHAR_LITERAL||LA56_1==REWRITE||LA56_1==RULE_REF||LA56_1==STRING_LITERAL||(LA56_1 >= TOKEN_REF && LA56_1 <= TREE_BEGIN)||LA56_1==65||(LA56_1 >= 67 && LA56_1 <= 69)||LA56_1==75||LA56_1==78||LA56_1==91) ) {
                    alt56=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 56, 1, input);

                    throw nvae;

                }
                }
                break;
            case 66:
                {
                int LA56_2 = input.LA(2);

                if ( (LA56_2==ACTION) ) {
                    int LA56_8 = input.LA(3);

                    if ( (LA56_8==67) ) {
                        int LA56_11 = input.LA(4);

                        if ( (LA56_11==66) ) {
                            alt56=1;
                        }
                        else if ( ((LA56_11 >= 68 && LA56_11 <= 69)||LA56_11==78) ) {
                            alt56=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 56, 11, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA56_8==ACTION||LA56_8==CHAR_LITERAL||LA56_8==RULE_REF||LA56_8==STRING_LITERAL||(LA56_8 >= TOKEN_REF && LA56_8 <= TREE_BEGIN)||(LA56_8 >= 65 && LA56_8 <= 66)||(LA56_8 >= 68 && LA56_8 <= 69)||LA56_8==78) ) {
                        alt56=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 56, 8, input);

                        throw nvae;

                    }
                }
                else if ( (LA56_2==CHAR_LITERAL||LA56_2==RULE_REF||LA56_2==STRING_LITERAL||(LA56_2 >= TOKEN_REF && LA56_2 <= TREE_BEGIN)||(LA56_2 >= 65 && LA56_2 <= 66)) ) {
                    alt56=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 56, 2, input);

                    throw nvae;

                }
                }
                break;
            case ACTION:
                {
                alt56=1;
                }
                break;
            case CHAR_LITERAL:
            case STRING_LITERAL:
            case TREE_BEGIN:
            case 65:
                {
                alt56=2;
                }
                break;
            case RULE_REF:
                {
                int LA56_5 = input.LA(2);

                if ( (LA56_5==66) ) {
                    switch ( input.LA(3) ) {
                    case TOKEN_REF:
                        {
                        int LA56_9 = input.LA(4);

                        if ( (LA56_9==76) ) {
                            alt56=1;
                        }
                        else if ( (LA56_9==ACTION||LA56_9==ARG_ACTION||LA56_9==CHAR_LITERAL||LA56_9==RULE_REF||LA56_9==STRING_LITERAL||(LA56_9 >= TOKEN_REF && LA56_9 <= TREE_BEGIN)||(LA56_9 >= 65 && LA56_9 <= 69)||LA56_9==78) ) {
                            alt56=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 56, 9, input);

                            throw nvae;

                        }
                        }
                        break;
                    case 67:
                        {
                        alt56=1;
                        }
                        break;
                    case ACTION:
                    case CHAR_LITERAL:
                    case STRING_LITERAL:
                    case TREE_BEGIN:
                    case 65:
                    case 66:
                        {
                        alt56=2;
                        }
                        break;
                    case RULE_REF:
                        {
                        int LA56_10 = input.LA(4);

                        if ( (LA56_10==76) ) {
                            alt56=1;
                        }
                        else if ( (LA56_10==ACTION||LA56_10==CHAR_LITERAL||LA56_10==RULE_REF||LA56_10==STRING_LITERAL||(LA56_10 >= TOKEN_REF && LA56_10 <= TREE_BEGIN)||(LA56_10 >= 65 && LA56_10 <= 69)||LA56_10==78) ) {
                            alt56=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 56, 10, input);

                            throw nvae;

                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 56, 7, input);

                        throw nvae;

                    }

                }
                else if ( (LA56_5==ACTION||LA56_5==CHAR_LITERAL||LA56_5==REWRITE||LA56_5==RULE_REF||LA56_5==STRING_LITERAL||(LA56_5 >= TOKEN_REF && LA56_5 <= TREE_BEGIN)||LA56_5==65||(LA56_5 >= 67 && LA56_5 <= 69)||LA56_5==75||LA56_5==78||LA56_5==91) ) {
                    alt56=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 56, 5, input);

                    throw nvae;

                }
                }
                break;
            case REWRITE:
            case 67:
            case 75:
            case 91:
                {
                alt56=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;

            }

            switch (alt56) {
                case 1 :
                    // GrammarProcessor.g:334:4: rewrite_template
                    {
                    pushFollow(FOLLOW_rewrite_template_in_rewrite_alternative1625);
                    rewrite_template();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:335:4: rewrite_tree_alternative
                    {
                    pushFollow(FOLLOW_rewrite_tree_alternative_in_rewrite_alternative1630);
                    rewrite_tree_alternative();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:337:2: 
                    {
                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_alternative"


    public static class rewrite_tree_block_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_tree_block"
    // GrammarProcessor.g:339:1: rewrite_tree_block : '(' rewrite_tree_alternative ')' ;
    public final rewrite_tree_block_return rewrite_tree_block() throws RecognitionException {
        rewrite_tree_block_return retval = new rewrite_tree_block_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:340:5: ( '(' rewrite_tree_alternative ')' )
            // GrammarProcessor.g:340:7: '(' rewrite_tree_alternative ')'
            {
            match(input,66,FOLLOW_66_in_rewrite_tree_block1651); 

            pushFollow(FOLLOW_rewrite_tree_alternative_in_rewrite_tree_block1653);
            rewrite_tree_alternative();

            state._fsp--;


            match(input,67,FOLLOW_67_in_rewrite_tree_block1655); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_tree_block"


    public static class rewrite_tree_alternative_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_tree_alternative"
    // GrammarProcessor.g:343:1: rewrite_tree_alternative : ( rewrite_tree_element )+ ;
    public final rewrite_tree_alternative_return rewrite_tree_alternative() throws RecognitionException {
        rewrite_tree_alternative_return retval = new rewrite_tree_alternative_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:344:5: ( ( rewrite_tree_element )+ )
            // GrammarProcessor.g:344:7: ( rewrite_tree_element )+
            {
            // GrammarProcessor.g:344:7: ( rewrite_tree_element )+
            int cnt57=0;
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==ACTION||LA57_0==CHAR_LITERAL||LA57_0==RULE_REF||LA57_0==STRING_LITERAL||(LA57_0 >= TOKEN_REF && LA57_0 <= TREE_BEGIN)||(LA57_0 >= 65 && LA57_0 <= 66)) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // GrammarProcessor.g:344:7: rewrite_tree_element
            	    {
            	    pushFollow(FOLLOW_rewrite_tree_element_in_rewrite_tree_alternative1672);
            	    rewrite_tree_element();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt57 >= 1 ) break loop57;
                        EarlyExitException eee =
                            new EarlyExitException(57, input);
                        throw eee;
                }
                cnt57++;
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_tree_alternative"


    public static class rewrite_tree_element_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_tree_element"
    // GrammarProcessor.g:347:1: rewrite_tree_element : ( rewrite_tree_atom | rewrite_tree_atom ebnfSuffix | rewrite_tree ( ebnfSuffix |) | rewrite_tree_ebnf );
    public final rewrite_tree_element_return rewrite_tree_element() throws RecognitionException {
        rewrite_tree_element_return retval = new rewrite_tree_element_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:348:2: ( rewrite_tree_atom | rewrite_tree_atom ebnfSuffix | rewrite_tree ( ebnfSuffix |) | rewrite_tree_ebnf )
            int alt59=4;
            switch ( input.LA(1) ) {
            case CHAR_LITERAL:
                {
                int LA59_1 = input.LA(2);

                if ( (LA59_1==ACTION||LA59_1==CHAR_LITERAL||LA59_1==REWRITE||LA59_1==RULE_REF||LA59_1==STRING_LITERAL||(LA59_1 >= TOKEN_REF && LA59_1 <= TREE_BEGIN)||(LA59_1 >= 65 && LA59_1 <= 67)||LA59_1==75||LA59_1==91) ) {
                    alt59=1;
                }
                else if ( ((LA59_1 >= 68 && LA59_1 <= 69)||LA59_1==78) ) {
                    alt59=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 1, input);

                    throw nvae;

                }
                }
                break;
            case TOKEN_REF:
                {
                switch ( input.LA(2) ) {
                case ARG_ACTION:
                    {
                    int LA59_11 = input.LA(3);

                    if ( (LA59_11==ACTION||LA59_11==CHAR_LITERAL||LA59_11==REWRITE||LA59_11==RULE_REF||LA59_11==STRING_LITERAL||(LA59_11 >= TOKEN_REF && LA59_11 <= TREE_BEGIN)||(LA59_11 >= 65 && LA59_11 <= 67)||LA59_11==75||LA59_11==91) ) {
                        alt59=1;
                    }
                    else if ( ((LA59_11 >= 68 && LA59_11 <= 69)||LA59_11==78) ) {
                        alt59=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 59, 11, input);

                        throw nvae;

                    }
                    }
                    break;
                case ACTION:
                case CHAR_LITERAL:
                case REWRITE:
                case RULE_REF:
                case STRING_LITERAL:
                case TOKEN_REF:
                case TREE_BEGIN:
                case 65:
                case 66:
                case 67:
                case 75:
                case 91:
                    {
                    alt59=1;
                    }
                    break;
                case 68:
                case 69:
                case 78:
                    {
                    alt59=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 2, input);

                    throw nvae;

                }

                }
                break;
            case RULE_REF:
                {
                int LA59_3 = input.LA(2);

                if ( (LA59_3==ACTION||LA59_3==CHAR_LITERAL||LA59_3==REWRITE||LA59_3==RULE_REF||LA59_3==STRING_LITERAL||(LA59_3 >= TOKEN_REF && LA59_3 <= TREE_BEGIN)||(LA59_3 >= 65 && LA59_3 <= 67)||LA59_3==75||LA59_3==91) ) {
                    alt59=1;
                }
                else if ( ((LA59_3 >= 68 && LA59_3 <= 69)||LA59_3==78) ) {
                    alt59=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 3, input);

                    throw nvae;

                }
                }
                break;
            case STRING_LITERAL:
                {
                int LA59_4 = input.LA(2);

                if ( (LA59_4==ACTION||LA59_4==CHAR_LITERAL||LA59_4==REWRITE||LA59_4==RULE_REF||LA59_4==STRING_LITERAL||(LA59_4 >= TOKEN_REF && LA59_4 <= TREE_BEGIN)||(LA59_4 >= 65 && LA59_4 <= 67)||LA59_4==75||LA59_4==91) ) {
                    alt59=1;
                }
                else if ( ((LA59_4 >= 68 && LA59_4 <= 69)||LA59_4==78) ) {
                    alt59=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 4, input);

                    throw nvae;

                }
                }
                break;
            case 65:
                {
                int LA59_5 = input.LA(2);

                if ( (LA59_5==RULE_REF||LA59_5==TOKEN_REF) ) {
                    int LA59_12 = input.LA(3);

                    if ( (LA59_12==ACTION||LA59_12==CHAR_LITERAL||LA59_12==REWRITE||LA59_12==RULE_REF||LA59_12==STRING_LITERAL||(LA59_12 >= TOKEN_REF && LA59_12 <= TREE_BEGIN)||(LA59_12 >= 65 && LA59_12 <= 67)||LA59_12==75||LA59_12==91) ) {
                        alt59=1;
                    }
                    else if ( ((LA59_12 >= 68 && LA59_12 <= 69)||LA59_12==78) ) {
                        alt59=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 59, 12, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 5, input);

                    throw nvae;

                }
                }
                break;
            case ACTION:
                {
                int LA59_6 = input.LA(2);

                if ( (LA59_6==ACTION||LA59_6==CHAR_LITERAL||LA59_6==REWRITE||LA59_6==RULE_REF||LA59_6==STRING_LITERAL||(LA59_6 >= TOKEN_REF && LA59_6 <= TREE_BEGIN)||(LA59_6 >= 65 && LA59_6 <= 67)||LA59_6==75||LA59_6==91) ) {
                    alt59=1;
                }
                else if ( ((LA59_6 >= 68 && LA59_6 <= 69)||LA59_6==78) ) {
                    alt59=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 59, 6, input);

                    throw nvae;

                }
                }
                break;
            case TREE_BEGIN:
                {
                alt59=3;
                }
                break;
            case 66:
                {
                alt59=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;

            }

            switch (alt59) {
                case 1 :
                    // GrammarProcessor.g:348:4: rewrite_tree_atom
                    {
                    pushFollow(FOLLOW_rewrite_tree_atom_in_rewrite_tree_element1687);
                    rewrite_tree_atom();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:349:4: rewrite_tree_atom ebnfSuffix
                    {
                    pushFollow(FOLLOW_rewrite_tree_atom_in_rewrite_tree_element1692);
                    rewrite_tree_atom();

                    state._fsp--;


                    pushFollow(FOLLOW_ebnfSuffix_in_rewrite_tree_element1694);
                    ebnfSuffix();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:350:6: rewrite_tree ( ebnfSuffix |)
                    {
                    pushFollow(FOLLOW_rewrite_tree_in_rewrite_tree_element1701);
                    rewrite_tree();

                    state._fsp--;


                    // GrammarProcessor.g:351:3: ( ebnfSuffix |)
                    int alt58=2;
                    int LA58_0 = input.LA(1);

                    if ( ((LA58_0 >= 68 && LA58_0 <= 69)||LA58_0==78) ) {
                        alt58=1;
                    }
                    else if ( (LA58_0==ACTION||LA58_0==CHAR_LITERAL||LA58_0==REWRITE||LA58_0==RULE_REF||LA58_0==STRING_LITERAL||(LA58_0 >= TOKEN_REF && LA58_0 <= TREE_BEGIN)||(LA58_0 >= 65 && LA58_0 <= 67)||LA58_0==75||LA58_0==91) ) {
                        alt58=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 58, 0, input);

                        throw nvae;

                    }
                    switch (alt58) {
                        case 1 :
                            // GrammarProcessor.g:351:5: ebnfSuffix
                            {
                            pushFollow(FOLLOW_ebnfSuffix_in_rewrite_tree_element1707);
                            ebnfSuffix();

                            state._fsp--;


                            }
                            break;
                        case 2 :
                            // GrammarProcessor.g:353:3: 
                            {
                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:354:6: rewrite_tree_ebnf
                    {
                    pushFollow(FOLLOW_rewrite_tree_ebnf_in_rewrite_tree_element1722);
                    rewrite_tree_ebnf();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_tree_element"


    public static class rewrite_tree_atom_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_tree_atom"
    // GrammarProcessor.g:357:1: rewrite_tree_atom : ( CHAR_LITERAL | TOKEN_REF ( ARG_ACTION )? | RULE_REF | STRING_LITERAL | '$' id | ACTION );
    public final rewrite_tree_atom_return rewrite_tree_atom() throws RecognitionException {
        rewrite_tree_atom_return retval = new rewrite_tree_atom_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:358:5: ( CHAR_LITERAL | TOKEN_REF ( ARG_ACTION )? | RULE_REF | STRING_LITERAL | '$' id | ACTION )
            int alt61=6;
            switch ( input.LA(1) ) {
            case CHAR_LITERAL:
                {
                alt61=1;
                }
                break;
            case TOKEN_REF:
                {
                alt61=2;
                }
                break;
            case RULE_REF:
                {
                alt61=3;
                }
                break;
            case STRING_LITERAL:
                {
                alt61=4;
                }
                break;
            case 65:
                {
                alt61=5;
                }
                break;
            case ACTION:
                {
                alt61=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 61, 0, input);

                throw nvae;

            }

            switch (alt61) {
                case 1 :
                    // GrammarProcessor.g:358:9: CHAR_LITERAL
                    {
                    match(input,CHAR_LITERAL,FOLLOW_CHAR_LITERAL_in_rewrite_tree_atom1738); 

                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:359:6: TOKEN_REF ( ARG_ACTION )?
                    {
                    match(input,TOKEN_REF,FOLLOW_TOKEN_REF_in_rewrite_tree_atom1745); 

                    // GrammarProcessor.g:359:16: ( ARG_ACTION )?
                    int alt60=2;
                    int LA60_0 = input.LA(1);

                    if ( (LA60_0==ARG_ACTION) ) {
                        alt60=1;
                    }
                    switch (alt60) {
                        case 1 :
                            // GrammarProcessor.g:359:16: ARG_ACTION
                            {
                            match(input,ARG_ACTION,FOLLOW_ARG_ACTION_in_rewrite_tree_atom1747); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:360:9: RULE_REF
                    {
                    match(input,RULE_REF,FOLLOW_RULE_REF_in_rewrite_tree_atom1759); 

                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:361:6: STRING_LITERAL
                    {
                    match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_rewrite_tree_atom1766); 

                    }
                    break;
                case 5 :
                    // GrammarProcessor.g:362:4: '$' id
                    {
                    match(input,65,FOLLOW_65_in_rewrite_tree_atom1771); 

                    pushFollow(FOLLOW_id_in_rewrite_tree_atom1773);
                    id();

                    state._fsp--;


                    }
                    break;
                case 6 :
                    // GrammarProcessor.g:363:4: ACTION
                    {
                    match(input,ACTION,FOLLOW_ACTION_in_rewrite_tree_atom1779); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_tree_atom"


    public static class rewrite_tree_ebnf_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_tree_ebnf"
    // GrammarProcessor.g:366:1: rewrite_tree_ebnf : rewrite_tree_block ebnfSuffix ;
    public final rewrite_tree_ebnf_return rewrite_tree_ebnf() throws RecognitionException {
        rewrite_tree_ebnf_return retval = new rewrite_tree_ebnf_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:367:2: ( rewrite_tree_block ebnfSuffix )
            // GrammarProcessor.g:367:4: rewrite_tree_block ebnfSuffix
            {
            pushFollow(FOLLOW_rewrite_tree_block_in_rewrite_tree_ebnf1790);
            rewrite_tree_block();

            state._fsp--;


            pushFollow(FOLLOW_ebnfSuffix_in_rewrite_tree_ebnf1792);
            ebnfSuffix();

            state._fsp--;


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_tree_ebnf"


    public static class rewrite_tree_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_tree"
    // GrammarProcessor.g:370:1: rewrite_tree : '^(' rewrite_tree_atom ( rewrite_tree_element )* ')' ;
    public final rewrite_tree_return rewrite_tree() throws RecognitionException {
        rewrite_tree_return retval = new rewrite_tree_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:371:2: ( '^(' rewrite_tree_atom ( rewrite_tree_element )* ')' )
            // GrammarProcessor.g:371:4: '^(' rewrite_tree_atom ( rewrite_tree_element )* ')'
            {
            match(input,TREE_BEGIN,FOLLOW_TREE_BEGIN_in_rewrite_tree1804); 

            pushFollow(FOLLOW_rewrite_tree_atom_in_rewrite_tree1806);
            rewrite_tree_atom();

            state._fsp--;


            // GrammarProcessor.g:371:27: ( rewrite_tree_element )*
            loop62:
            do {
                int alt62=2;
                int LA62_0 = input.LA(1);

                if ( (LA62_0==ACTION||LA62_0==CHAR_LITERAL||LA62_0==RULE_REF||LA62_0==STRING_LITERAL||(LA62_0 >= TOKEN_REF && LA62_0 <= TREE_BEGIN)||(LA62_0 >= 65 && LA62_0 <= 66)) ) {
                    alt62=1;
                }


                switch (alt62) {
            	case 1 :
            	    // GrammarProcessor.g:371:27: rewrite_tree_element
            	    {
            	    pushFollow(FOLLOW_rewrite_tree_element_in_rewrite_tree1808);
            	    rewrite_tree_element();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop62;
                }
            } while (true);


            match(input,67,FOLLOW_67_in_rewrite_tree1811); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_tree"


    public static class rewrite_template_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_template"
    // GrammarProcessor.g:385:1: rewrite_template : ( id '(' rewrite_template_args ')' ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL ) | rewrite_template_ref | rewrite_indirect_template_head | ACTION );
    public final rewrite_template_return rewrite_template() throws RecognitionException {
        rewrite_template_return retval = new rewrite_template_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:386:2: ( id '(' rewrite_template_args ')' ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL ) | rewrite_template_ref | rewrite_indirect_template_head | ACTION )
            int alt63=4;
            alt63 = dfa63.predict(input);
            switch (alt63) {
                case 1 :
                    // GrammarProcessor.g:387:3: id '(' rewrite_template_args ')' ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL )
                    {
                    pushFollow(FOLLOW_id_in_rewrite_template1829);
                    id();

                    state._fsp--;


                    match(input,66,FOLLOW_66_in_rewrite_template1831); 

                    pushFollow(FOLLOW_rewrite_template_args_in_rewrite_template1833);
                    rewrite_template_args();

                    state._fsp--;


                    match(input,67,FOLLOW_67_in_rewrite_template1835); 

                    if ( (input.LA(1) >= DOUBLE_ANGLE_STRING_LITERAL && input.LA(1) <= DOUBLE_QUOTE_STRING_LITERAL) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:391:3: rewrite_template_ref
                    {
                    pushFollow(FOLLOW_rewrite_template_ref_in_rewrite_template1856);
                    rewrite_template_ref();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // GrammarProcessor.g:394:3: rewrite_indirect_template_head
                    {
                    pushFollow(FOLLOW_rewrite_indirect_template_head_in_rewrite_template1865);
                    rewrite_indirect_template_head();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // GrammarProcessor.g:397:3: ACTION
                    {
                    match(input,ACTION,FOLLOW_ACTION_in_rewrite_template1874); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_template"


    public static class rewrite_template_ref_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_template_ref"
    // GrammarProcessor.g:401:1: rewrite_template_ref : id '(' rewrite_template_args ')' ;
    public final rewrite_template_ref_return rewrite_template_ref() throws RecognitionException {
        rewrite_template_ref_return retval = new rewrite_template_ref_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:402:2: ( id '(' rewrite_template_args ')' )
            // GrammarProcessor.g:402:4: id '(' rewrite_template_args ')'
            {
            pushFollow(FOLLOW_id_in_rewrite_template_ref1887);
            id();

            state._fsp--;


            match(input,66,FOLLOW_66_in_rewrite_template_ref1889); 

            pushFollow(FOLLOW_rewrite_template_args_in_rewrite_template_ref1891);
            rewrite_template_args();

            state._fsp--;


            match(input,67,FOLLOW_67_in_rewrite_template_ref1893); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_template_ref"


    public static class rewrite_indirect_template_head_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_indirect_template_head"
    // GrammarProcessor.g:406:1: rewrite_indirect_template_head : '(' ACTION ')' '(' rewrite_template_args ')' ;
    public final rewrite_indirect_template_head_return rewrite_indirect_template_head() throws RecognitionException {
        rewrite_indirect_template_head_return retval = new rewrite_indirect_template_head_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:407:2: ( '(' ACTION ')' '(' rewrite_template_args ')' )
            // GrammarProcessor.g:407:4: '(' ACTION ')' '(' rewrite_template_args ')'
            {
            match(input,66,FOLLOW_66_in_rewrite_indirect_template_head1906); 

            match(input,ACTION,FOLLOW_ACTION_in_rewrite_indirect_template_head1908); 

            match(input,67,FOLLOW_67_in_rewrite_indirect_template_head1910); 

            match(input,66,FOLLOW_66_in_rewrite_indirect_template_head1912); 

            pushFollow(FOLLOW_rewrite_template_args_in_rewrite_indirect_template_head1914);
            rewrite_template_args();

            state._fsp--;


            match(input,67,FOLLOW_67_in_rewrite_indirect_template_head1916); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_indirect_template_head"


    public static class rewrite_template_args_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_template_args"
    // GrammarProcessor.g:410:1: rewrite_template_args : ( rewrite_template_arg ( ',' rewrite_template_arg )* |);
    public final rewrite_template_args_return rewrite_template_args() throws RecognitionException {
        rewrite_template_args_return retval = new rewrite_template_args_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:411:2: ( rewrite_template_arg ( ',' rewrite_template_arg )* |)
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==RULE_REF||LA65_0==TOKEN_REF) ) {
                alt65=1;
            }
            else if ( (LA65_0==67) ) {
                alt65=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;

            }
            switch (alt65) {
                case 1 :
                    // GrammarProcessor.g:411:4: rewrite_template_arg ( ',' rewrite_template_arg )*
                    {
                    pushFollow(FOLLOW_rewrite_template_arg_in_rewrite_template_args1927);
                    rewrite_template_arg();

                    state._fsp--;


                    // GrammarProcessor.g:411:25: ( ',' rewrite_template_arg )*
                    loop64:
                    do {
                        int alt64=2;
                        int LA64_0 = input.LA(1);

                        if ( (LA64_0==71) ) {
                            alt64=1;
                        }


                        switch (alt64) {
                    	case 1 :
                    	    // GrammarProcessor.g:411:26: ',' rewrite_template_arg
                    	    {
                    	    match(input,71,FOLLOW_71_in_rewrite_template_args1930); 

                    	    pushFollow(FOLLOW_rewrite_template_arg_in_rewrite_template_args1932);
                    	    rewrite_template_arg();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    break loop64;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // GrammarProcessor.g:413:2: 
                    {
                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_template_args"


    public static class rewrite_template_arg_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "rewrite_template_arg"
    // GrammarProcessor.g:415:1: rewrite_template_arg : id '=' ACTION ;
    public final rewrite_template_arg_return rewrite_template_arg() throws RecognitionException {
        rewrite_template_arg_return retval = new rewrite_template_arg_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:416:2: ( id '=' ACTION )
            // GrammarProcessor.g:416:6: id '=' ACTION
            {
            pushFollow(FOLLOW_id_in_rewrite_template_arg1950);
            id();

            state._fsp--;


            match(input,76,FOLLOW_76_in_rewrite_template_arg1952); 

            match(input,ACTION,FOLLOW_ACTION_in_rewrite_template_arg1954); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rewrite_template_arg"


    public static class id_return extends ParserRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };


    // $ANTLR start "id"
    // GrammarProcessor.g:419:1: id : ( TOKEN_REF | RULE_REF );
    public final id_return id() throws RecognitionException {
        id_return retval = new id_return();
        retval.start = input.LT(1);


        try {
            // GrammarProcessor.g:419:4: ( TOKEN_REF | RULE_REF )
            // GrammarProcessor.g:
            {
            if ( input.LA(1)==RULE_REF||input.LA(1)==TOKEN_REF ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "id"

    // Delegated rules


    protected DFA63 dfa63 = new DFA63(this);
    static final String DFA63_eotS =
        "\17\uffff";
    static final String DFA63_eofS =
        "\17\uffff";
    static final String DFA63_minS =
        "\1\4\1\102\2\uffff\1\61\1\114\1\24\1\4\2\uffff\1\103\1\61\1\114"+
        "\1\4\1\103";
    static final String DFA63_maxS =
        "\2\102\2\uffff\1\103\1\114\1\133\1\4\2\uffff\1\107\1\73\1\114\1"+
        "\4\1\107";
    static final String DFA63_acceptS =
        "\2\uffff\1\3\1\4\4\uffff\1\1\1\2\5\uffff";
    static final String DFA63_specialS =
        "\17\uffff}>";
    static final String[] DFA63_transitionS = {
            "\1\3\54\uffff\1\1\11\uffff\1\1\6\uffff\1\2",
            "\1\4",
            "",
            "",
            "\1\5\11\uffff\1\5\7\uffff\1\6",
            "\1\7",
            "\2\10\30\uffff\1\11\24\uffff\1\11\7\uffff\1\11\17\uffff\1\11",
            "\1\12",
            "",
            "",
            "\1\6\3\uffff\1\13",
            "\1\14\11\uffff\1\14",
            "\1\15",
            "\1\16",
            "\1\6\3\uffff\1\13"
    };

    static final short[] DFA63_eot = DFA.unpackEncodedString(DFA63_eotS);
    static final short[] DFA63_eof = DFA.unpackEncodedString(DFA63_eofS);
    static final char[] DFA63_min = DFA.unpackEncodedStringToUnsignedChars(DFA63_minS);
    static final char[] DFA63_max = DFA.unpackEncodedStringToUnsignedChars(DFA63_maxS);
    static final short[] DFA63_accept = DFA.unpackEncodedString(DFA63_acceptS);
    static final short[] DFA63_special = DFA.unpackEncodedString(DFA63_specialS);
    static final short[][] DFA63_transition;

    static {
        int numStates = DFA63_transitionS.length;
        DFA63_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA63_transition[i] = DFA.unpackEncodedString(DFA63_transitionS[i]);
        }
    }

    class DFA63 extends DFA {

        public DFA63(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 63;
            this.eot = DFA63_eot;
            this.eof = DFA63_eof;
            this.min = DFA63_min;
            this.max = DFA63_max;
            this.accept = DFA63_accept;
            this.special = DFA63_special;
            this.transition = DFA63_transition;
        }
        public String getDescription() {
            return "385:1: rewrite_template : ( id '(' rewrite_template_args ')' ( DOUBLE_QUOTE_STRING_LITERAL | DOUBLE_ANGLE_STRING_LITERAL ) | rewrite_template_ref | rewrite_indirect_template_head | ACTION );";
        }
    }
 

    public static final BitSet FOLLOW_DOC_COMMENT_in_grammarDef335 = new BitSet(new long[]{0x0000000000000000L,0x00000000041C0000L});
    public static final BitSet FOLLOW_83_in_grammarDef345 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_84_in_grammarDef361 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_90_in_grammarDef376 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_grammarDef400 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_grammarDef402 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_grammarDef404 = new BitSet(new long[]{0x0C06010008080000L,0x0000000000E08000L});
    public static final BitSet FOLLOW_optionsSpec_in_grammarDef406 = new BitSet(new long[]{0x0C06000008080000L,0x0000000000E08000L});
    public static final BitSet FOLLOW_tokensSpec_in_grammarDef409 = new BitSet(new long[]{0x0806000008080000L,0x0000000000E08000L});
    public static final BitSet FOLLOW_attrScope_in_grammarDef412 = new BitSet(new long[]{0x0806000008080000L,0x0000000000E08000L});
    public static final BitSet FOLLOW_action_in_grammarDef415 = new BitSet(new long[]{0x0802000008080000L,0x0000000000E08000L});
    public static final BitSet FOLLOW_rule_in_grammarDef423 = new BitSet(new long[]{0x0802000008080000L,0x0000000000E00000L});
    public static final BitSet FOLLOW_EOF_in_grammarDef431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKENS_in_tokensSpec445 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_tokenSpec_in_tokensSpec447 = new BitSet(new long[]{0x0800000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_92_in_tokensSpec450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_REF_in_tokenSpec461 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_76_in_tokenSpec467 = new BitSet(new long[]{0x0040000000008000L});
    public static final BitSet FOLLOW_set_in_tokenSpec469 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_tokenSpec487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SCOPE_in_attrScope498 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_attrScope500 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_attrScope502 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_action515 = new BitSet(new long[]{0x0802000000000000L,0x0000000000180000L});
    public static final BitSet FOLLOW_actionScopeName_in_action518 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
    public static final BitSet FOLLOW_74_in_action520 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_action524 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_action526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_id_in_actionScopeName539 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_actionScopeName544 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_actionScopeName552 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPTIONS_in_optionsSpec563 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_option_in_optionsSpec566 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_optionsSpec568 = new BitSet(new long[]{0x0802000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_92_in_optionsSpec572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_id_in_option588 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_option590 = new BitSet(new long[]{0x0842000080008000L,0x0000000000000010L});
    public static final BitSet FOLLOW_optionValue_in_option592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_id_in_optionValue611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_optionValue621 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_optionValue631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_optionValue641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_optionValue649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOC_COMMENT_in_rule665 = new BitSet(new long[]{0x0802000008000000L,0x0000000000E00000L});
    public static final BitSet FOLLOW_id_in_rule687 = new BitSet(new long[]{0x0004010000002800L,0x0000000003008200L});
    public static final BitSet FOLLOW_BANG_in_rule693 = new BitSet(new long[]{0x0004010000000800L,0x0000000003008200L});
    public static final BitSet FOLLOW_ARG_ACTION_in_rule700 = new BitSet(new long[]{0x0004010000000000L,0x0000000003008200L});
    public static final BitSet FOLLOW_88_in_rule709 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_ARG_ACTION_in_rule711 = new BitSet(new long[]{0x0004010000000000L,0x0000000002008200L});
    public static final BitSet FOLLOW_throwsSpec_in_rule719 = new BitSet(new long[]{0x0004010000000000L,0x0000000000008200L});
    public static final BitSet FOLLOW_optionsSpec_in_rule722 = new BitSet(new long[]{0x0004000000000000L,0x0000000000008200L});
    public static final BitSet FOLLOW_ruleScopeSpec_in_rule725 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008200L});
    public static final BitSet FOLLOW_ruleAction_in_rule728 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008200L});
    public static final BitSet FOLLOW_73_in_rule733 = new BitSet(new long[]{0x184A400000008010L,0x0000000028000104L});
    public static final BitSet FOLLOW_altList_in_rule739 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_rule741 = new BitSet(new long[]{0x0000000000000002L,0x0000000000030000L});
    public static final BitSet FOLLOW_exceptionGroup_in_rule745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_ruleAction759 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_ruleAction761 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_ruleAction763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_throwsSpec774 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_throwsSpec776 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_throwsSpec780 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_throwsSpec782 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_SCOPE_in_ruleScopeSpec796 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_ruleScopeSpec798 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SCOPE_in_ruleScopeSpec803 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_ruleScopeSpec805 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000880L});
    public static final BitSet FOLLOW_71_in_ruleScopeSpec808 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_ruleScopeSpec810 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000880L});
    public static final BitSet FOLLOW_75_in_ruleScopeSpec814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SCOPE_in_ruleScopeSpec819 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_ruleScopeSpec821 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_SCOPE_in_ruleScopeSpec825 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_ruleScopeSpec827 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000880L});
    public static final BitSet FOLLOW_71_in_ruleScopeSpec830 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_ruleScopeSpec832 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000880L});
    public static final BitSet FOLLOW_75_in_ruleScopeSpec836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_block850 = new BitSet(new long[]{0x184A410000008010L,0x000000002800030CL});
    public static final BitSet FOLLOW_optionsSpec_in_block858 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_73_in_block862 = new BitSet(new long[]{0x184A400000008010L,0x000000002800010CL});
    public static final BitSet FOLLOW_alternative_in_block867 = new BitSet(new long[]{0x0000400000000000L,0x0000000008000008L});
    public static final BitSet FOLLOW_rewrite_in_block869 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000008L});
    public static final BitSet FOLLOW_91_in_block873 = new BitSet(new long[]{0x184A400000008010L,0x000000002800010CL});
    public static final BitSet FOLLOW_alternative_in_block875 = new BitSet(new long[]{0x0000400000000000L,0x0000000008000008L});
    public static final BitSet FOLLOW_rewrite_in_block877 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000008L});
    public static final BitSet FOLLOW_67_in_block882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_alternative_in_altList899 = new BitSet(new long[]{0x0000400000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_rewrite_in_altList901 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_91_in_altList905 = new BitSet(new long[]{0x184A400000008010L,0x0000000028000104L});
    public static final BitSet FOLLOW_alternative_in_altList907 = new BitSet(new long[]{0x0000400000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_rewrite_in_altList909 = new BitSet(new long[]{0x0000000000000002L,0x0000000008000000L});
    public static final BitSet FOLLOW_element_in_alternative931 = new BitSet(new long[]{0x184A000000008012L,0x0000000020000104L});
    public static final BitSet FOLLOW_exceptionHandler_in_exceptionGroup954 = new BitSet(new long[]{0x0000000000000002L,0x0000000000030000L});
    public static final BitSet FOLLOW_finallyClause_in_exceptionGroup961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_finallyClause_in_exceptionGroup969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_exceptionHandler989 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_ARG_ACTION_in_exceptionHandler991 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_exceptionHandler993 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_81_in_finallyClause1013 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_finallyClause1015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementNoOptionSpec_in_element1029 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_id_in_elementNoOptionSpec1040 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001040L});
    public static final BitSet FOLLOW_set_in_elementNoOptionSpec1042 = new BitSet(new long[]{0x0842000000008000L,0x0000000020000100L});
    public static final BitSet FOLLOW_atom_in_elementNoOptionSpec1050 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004030L});
    public static final BitSet FOLLOW_ebnfSuffix_in_elementNoOptionSpec1056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_id_in_elementNoOptionSpec1069 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001040L});
    public static final BitSet FOLLOW_set_in_elementNoOptionSpec1071 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_elementNoOptionSpec1079 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004030L});
    public static final BitSet FOLLOW_ebnfSuffix_in_elementNoOptionSpec1085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_elementNoOptionSpec1098 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004030L});
    public static final BitSet FOLLOW_ebnfSuffix_in_elementNoOptionSpec1104 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ebnf_in_elementNoOptionSpec1117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_elementNoOptionSpec1124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMPRED_in_elementNoOptionSpec1131 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
    public static final BitSet FOLLOW_77_in_elementNoOptionSpec1135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_treeSpec_in_elementNoOptionSpec1146 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004030L});
    public static final BitSet FOLLOW_ebnfSuffix_in_elementNoOptionSpec1152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_range_in_atom1171 = new BitSet(new long[]{0x0000800000002002L});
    public static final BitSet FOLLOW_set_in_atom1175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_terminal_in_atom1195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_notSet_in_atom1203 = new BitSet(new long[]{0x0000800000002002L});
    public static final BitSet FOLLOW_set_in_atom1207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_REF_in_atom1227 = new BitSet(new long[]{0x0000800000002802L});
    public static final BitSet FOLLOW_ARG_ACTION_in_atom1267 = new BitSet(new long[]{0x0000800000002002L});
    public static final BitSet FOLLOW_93_in_notSet1297 = new BitSet(new long[]{0x0840000000008000L,0x0000000000000004L});
    public static final BitSet FOLLOW_notTerminal_in_notSet1303 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_notSet1309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TREE_BEGIN_in_treeSpec1324 = new BitSet(new long[]{0x184A000000008010L,0x0000000020000104L});
    public static final BitSet FOLLOW_element_in_treeSpec1326 = new BitSet(new long[]{0x184A000000008010L,0x0000000020000104L});
    public static final BitSet FOLLOW_element_in_treeSpec1330 = new BitSet(new long[]{0x184A000000008010L,0x000000002000010CL});
    public static final BitSet FOLLOW_67_in_treeSpec1335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_ebnf1348 = new BitSet(new long[]{0x0000000000000002L,0x0000000000006030L});
    public static final BitSet FOLLOW_78_in_ebnf1354 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_ebnf1360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_ebnf1366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_ebnf1374 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_range1399 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_RANGE_in_range1401 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_range1403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_terminal1421 = new BitSet(new long[]{0x0000800000002002L});
    public static final BitSet FOLLOW_TOKEN_REF_in_terminal1445 = new BitSet(new long[]{0x0000800000002802L});
    public static final BitSet FOLLOW_ARG_ACTION_in_terminal1468 = new BitSet(new long[]{0x0000800000002002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_terminal1486 = new BitSet(new long[]{0x0000800000002002L});
    public static final BitSet FOLLOW_72_in_terminal1511 = new BitSet(new long[]{0x0000800000002002L});
    public static final BitSet FOLLOW_REWRITE_in_rewrite1601 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_SEMPRED_in_rewrite1603 = new BitSet(new long[]{0x1842400000008010L,0x0000000000000006L});
    public static final BitSet FOLLOW_rewrite_alternative_in_rewrite1605 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_REWRITE_in_rewrite1609 = new BitSet(new long[]{0x1842000000008010L,0x0000000000000006L});
    public static final BitSet FOLLOW_rewrite_alternative_in_rewrite1611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_template_in_rewrite_alternative1625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_alternative_in_rewrite_alternative1630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_rewrite_tree_block1651 = new BitSet(new long[]{0x1842000000008010L,0x0000000000000006L});
    public static final BitSet FOLLOW_rewrite_tree_alternative_in_rewrite_tree_block1653 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_rewrite_tree_block1655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_element_in_rewrite_tree_alternative1672 = new BitSet(new long[]{0x1842000000008012L,0x0000000000000006L});
    public static final BitSet FOLLOW_rewrite_tree_atom_in_rewrite_tree_element1687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_atom_in_rewrite_tree_element1692 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004030L});
    public static final BitSet FOLLOW_ebnfSuffix_in_rewrite_tree_element1694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_in_rewrite_tree_element1701 = new BitSet(new long[]{0x0000000000000002L,0x0000000000004030L});
    public static final BitSet FOLLOW_ebnfSuffix_in_rewrite_tree_element1707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_ebnf_in_rewrite_tree_element1722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CHAR_LITERAL_in_rewrite_tree_atom1738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TOKEN_REF_in_rewrite_tree_atom1745 = new BitSet(new long[]{0x0000000000000802L});
    public static final BitSet FOLLOW_ARG_ACTION_in_rewrite_tree_atom1747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_REF_in_rewrite_tree_atom1759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_rewrite_tree_atom1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_rewrite_tree_atom1771 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_id_in_rewrite_tree_atom1773 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_tree_atom1779 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_block_in_rewrite_tree_ebnf1790 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004030L});
    public static final BitSet FOLLOW_ebnfSuffix_in_rewrite_tree_ebnf1792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TREE_BEGIN_in_rewrite_tree1804 = new BitSet(new long[]{0x0842000000008010L,0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_tree_atom_in_rewrite_tree1806 = new BitSet(new long[]{0x1842000000008010L,0x000000000000000EL});
    public static final BitSet FOLLOW_rewrite_tree_element_in_rewrite_tree1808 = new BitSet(new long[]{0x1842000000008010L,0x000000000000000EL});
    public static final BitSet FOLLOW_67_in_rewrite_tree1811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_id_in_rewrite_template1829 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_rewrite_template1831 = new BitSet(new long[]{0x0802000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_rewrite_template_args_in_rewrite_template1833 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_rewrite_template1835 = new BitSet(new long[]{0x0000000000300000L});
    public static final BitSet FOLLOW_set_in_rewrite_template1839 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_template_ref_in_rewrite_template1856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_indirect_template_head_in_rewrite_template1865 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_template1874 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_id_in_rewrite_template_ref1887 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_rewrite_template_ref1889 = new BitSet(new long[]{0x0802000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_rewrite_template_args_in_rewrite_template_ref1891 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_rewrite_template_ref1893 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_rewrite_indirect_template_head1906 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_indirect_template_head1908 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_rewrite_indirect_template_head1910 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_rewrite_indirect_template_head1912 = new BitSet(new long[]{0x0802000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_rewrite_template_args_in_rewrite_indirect_template_head1914 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_67_in_rewrite_indirect_template_head1916 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rewrite_template_arg_in_rewrite_template_args1927 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_rewrite_template_args1930 = new BitSet(new long[]{0x0802000000000000L});
    public static final BitSet FOLLOW_rewrite_template_arg_in_rewrite_template_args1932 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_id_in_rewrite_template_arg1950 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_rewrite_template_arg1952 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ACTION_in_rewrite_template_arg1954 = new BitSet(new long[]{0x0000000000000002L});

}