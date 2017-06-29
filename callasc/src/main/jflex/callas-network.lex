package org.callas.parse;

import java.io.IOException;

import java.util.List;
import java.util.Stack;

import java_cup.runtime.Symbol;

import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.ErrorMessage;

import org.tyco.common.errorMsg.msgs.SyntacticError;

/**
 * The lexicon of Callas.
 *
 * @author Tiago Cogumbreiro
 * @version $Id: callas-network.lex,v 1.1 2011/01/21 17:08:11 cogumbreiro Exp $
 */
@SuppressWarnings("all") // Uncomment to disable warnings on generated files!
%% 

%class PreNetworkLexer
%unicode
%cupsym NetworkSym
%cup
%type java_cup.runtime.Symbol
%line
%column
%{
    private int indentLen = 0;
    private int returnTo = YYINITIAL;
    Stack<Integer> levels = new Stack<Integer>();
    List<ErrorMessage> errors = null;
    StringBuffer buffer = null;
    boolean hasEscape = false;
    
    {
        levels.push(new Integer(0));
    }
    
    public void setErrors(List<ErrorMessage> errors) {
        this.errors = errors;
    }
    
    private int pos(){
        return yychar;
    }

    protected java_cup.runtime.Symbol token (int kind, Object value) {
        return new java_cup.runtime.Symbol (kind, yyline, yycolumn, value);
    }

    protected java_cup.runtime.Symbol token (int kind) {
        return token (kind, yytext ());
    }

    private int getIndentLen() {
        return levels.peek().intValue();
    }
    
    private Symbol indentDedent() {
        yypushback(yytext().length());
        final int newState;
        final Symbol result;
        if (getIndentLen() < indentLen) {
            levels.push(indentLen);
            newState = STATE_NORMAL;
            result = token(NetworkSym.INDENT);
        } else if (getIndentLen() > indentLen) {
            levels.pop();
            newState = STATE_DEDENT;
            result = token(NetworkSym.DEDENT);
        } else {
            newState = STATE_NORMAL;
            result = null;
        }
        yybegin(newState);
        return result;
    }
    
    private void useEscape() {
        if (hasEscape) {return;}
        hasEscape = true;
        buffer = new StringBuffer(buffer.toString().trim()); 
    }            
%}

%state STATE_NORMAL
%state STATE_DEDENT
%state STATE_VALUE
%state STATE_VALUE_FINISH

nl       = \r?\n
blanks   = [\ \t]+
comment  = "#".*
letter   = [a-zA-Z_]
digit    = [0-9]
key      = {letter}({digit}|{letter})*
value    = [^\n\r]+{nl}

%%

<YYINITIAL>{
{comment}?{nl}   { indentLen = 0;}
" "+ { indentLen += yytext().length(); }
.    { Symbol t = indentDedent(); if (t != null) return t; }
}

<STATE_DEDENT>{
. { Symbol t = indentDedent(); if (t != null) return t; }
}

<STATE_NORMAL>{
\\{nl}          { }
{comment}?{nl}  { indentLen = 0; yybegin(YYINITIAL); return token(NetworkSym.NL);}
"="             { buffer = new StringBuffer(); hasEscape = false; yybegin(STATE_VALUE); return token(NetworkSym.EQ); }
":"             { return token(NetworkSym.COLON); }
"sensor"        { return token(NetworkSym.SENSOR); }
"pass"          { return token(NetworkSym.PASSKW); }
{key}           { return token(NetworkSym.KEY, yytext().trim()); }
{blanks}*       { }
.               { return token(NetworkSym.error, yytext()); }
}

<STATE_VALUE> {
"\\\\" { useEscape(); buffer.append("\\"); }
"\\t"  { useEscape(); buffer.append("\t"); }
"\\n"  { useEscape(); buffer.append("\n"); }
"\\#"  { useEscape(); buffer.append("#");  }
"\\ "  { useEscape(); buffer.append(" ");  }
{comment}?{nl}
    {
        // push back the '\n'
        yypushback(1);
        yybegin(STATE_NORMAL);
        String result = buffer.toString();
        if (!hasEscape) {
            result = result.trim();
        }
        return token(NetworkSym.VALUE, result);
    }
    
.      { buffer.append(yytext()); }
}
