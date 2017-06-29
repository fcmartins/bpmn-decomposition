package org.callas.parse;

import java.io.IOException;

import java.util.List;
import java.util.Stack;

import java_cup.runtime.Symbol;

import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.ErrorMessage;
import org.callas.absyn.processes.BinaryOperator;
import org.callas.absyn.processes.UnaryOperator;

import org.tyco.common.errorMsg.msgs.SyntacticError;

/**
 * The lexicon of Callas.
 *
 * @author Tiago Cogumbreiro
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @version $Id: callas-proc.lex,v 1.11 2012/04/17 16:37:33 ruimendes Exp $
 */
@SuppressWarnings("all") // Uncomment to disable warnings on generated files!
%% 

%class PreProcessLexer
%unicode
%cupsym ProcessSym
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
            result = token(ProcessSym.INDENT);
        } else if (getIndentLen() > indentLen) {
            levels.pop();
            newState = STATE_DEDENT;
            result = token(ProcessSym.DEDENT);
        } else {
            newState = STATE_NORMAL;
            result = null;
        }
        yybegin(newState);
        return result;
    }
            
%}

%state STATE_STRING
%state STATE_MSTRING
%state STATE_NORMAL
%state STATE_DEDENT

nl       = \r?\n
blanks   = [\ \t]+
comment  = "#".*
letter   = [a-zA-Z]
digit    = [0-9]
positive = {digit}+
integer  = {positive}

/* DoubleLiteral regex taken from JFlex Java's example */
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?
FLit1    = [0-9]+ \. [0-9]* 
FLit2    = \. [0-9]+ 
FLit3    = [0-9]+ 
Exponent = [eE] [+-]? [0-9]+

id       = _*[a-z]({digit}|{letter}|_)*
typeName = _*[A-Z]({digit}|{letter}|_)*

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
{comment}?{nl}  { indentLen = 0; yybegin(YYINITIAL); return token(ProcessSym.NL);}
"("             { return token(ProcessSym.LPAREN); }
")"             { return token(ProcessSym.RPAREN); }
"."             { return token(ProcessSym.DOT); }
","             { return token(ProcessSym.COMMA); }
"="             { return token(ProcessSym.EQUAL); }
":"             { return token(ProcessSym.COLON); }
";"             { return token(ProcessSym.SEMICOLON); }
/*"**"            { return token(ProcessSym.POWER, Operator.DOUBLE_STAR); }*/
"*"             { return token(ProcessSym.LONG_MULTIPLICATION, BinaryOperator.LONG_MULTIPLICATION); }
"*."            { return token(ProcessSym.DOUBLE_MULTIPLICATION, BinaryOperator.DOUBLE_MULTIPLICATION); }
"-"             { return token(ProcessSym.LONG_SUBTRACTION, BinaryOperator.LONG_SUBTRACTION); }
"-."            { return token(ProcessSym.DOUBLE_SUBTRACTION, BinaryOperator.DOUBLE_SUBTRACTION); }
"/"             { return token(ProcessSym.DOUBLE_DIVISION, BinaryOperator.DOUBLE_DIVISION); }
"//"            { return token(ProcessSym.LONG_DIVISION, BinaryOperator.LONG_DIVISION); }
">>"            { return token(ProcessSym.LONG_SHIFT_RIGHT, BinaryOperator.LONG_SHIFT_RIGHT); }
"<<"            { return token(ProcessSym.LONG_SHIFT_LEFT, BinaryOperator.LONG_SHIFT_LEFT); }
"&"             { return token(ProcessSym.LONG_AND, BinaryOperator.LONG_AND); }
"|"             { return token(ProcessSym.LONG_OR, BinaryOperator.LONG_OR); }
"^"             { return token(ProcessSym.LONG_EXCLUSIVE_OR, BinaryOperator.LONG_EXCLUSIVE_OR); }
"%"             { return token(ProcessSym.LONG_MODULUS, BinaryOperator.LONG_MODULUS); }
"+"             { return token(ProcessSym.LONG_ADDITION, BinaryOperator.LONG_ADDITION); }
"+."            { return token(ProcessSym.DOUBLE_ADDITION, BinaryOperator.DOUBLE_ADDITION); }
"<"             { return token(ProcessSym.LONG_LESS_THAN, BinaryOperator.LONG_LESS_THAN); }
"<."            { return token(ProcessSym.DOUBLE_LESS_THAN, BinaryOperator.DOUBLE_LESS_THAN); }
">"             { return token(ProcessSym.LONG_GREATER_THAN, BinaryOperator.LONG_GREATER_THAN); }
">."            { return token(ProcessSym.DOUBLE_GREATER_THAN, BinaryOperator.DOUBLE_GREATER_THAN); }
"=="            { return token(ProcessSym.LONG_EQUALS, BinaryOperator.LONG_EQUALS); }
"!="            { return token(ProcessSym.LONG_DIFFERENT, BinaryOperator.LONG_DIFFERENT); }
"<="            { return token(ProcessSym.LONG_LESS_THAN_EQUALS, BinaryOperator.LONG_LESS_THAN_EQUALS); }
">="            { return token(ProcessSym.LONG_GREATER_THAN_EQUALS, BinaryOperator.LONG_GREATER_THAN_EQUALS); }
"and"           { return token(ProcessSym.BOOL_AND, BinaryOperator.BOOL_AND); }
"or"            { return token(ProcessSym.BOOL_OR, BinaryOperator.BOOL_OR); }
"xor"           { return token(ProcessSym.BOOL_EXCLUSIVE_OR, BinaryOperator.BOOL_EXCLUSIVE_OR); }
"~"             { return token(ProcessSym.LONG_NOT, UnaryOperator.LONG_NOT); }
"not"           { return token(ProcessSym.BOOL_NOT, UnaryOperator.BOOL_NOT); }
"||"            { return token(ProcessSym.MERGE); }
"||="           { return token(ProcessSym.MERGE_ASSIGN); }
"as"            { return token(ProcessSym.AS); }
"if"            { return token(ProcessSym.IF); }
"else"          { return token(ProcessSym.ELSE); }
"elif"          { return token(ProcessSym.ELIF); }
"long"          { return token(ProcessSym.LONG_TYPE); }
"bool"          { return token(ProcessSym.BOOL_TYPE); }
"of"            { return token(ProcessSym.OF); }
"string"        { return token(ProcessSym.STRING_TYPE); }
"double"        { return token(ProcessSym.DOUBLE_TYPE); }
"select"        { return token(ProcessSym.SELECT); }
"receive"       { return token(ProcessSym.RECEIVE); }
"send"          { return token(ProcessSym.SEND); }
"True"          { return token(ProcessSym.TRUE); }
"False"         { return token(ProcessSym.FALSE); }
"extern"        { return token(ProcessSym.EXTERN); }
"every"         { return token(ProcessSym.EVERY); }
"def"           { return token(ProcessSym.DEF); }
"load"          { return token(ProcessSym.LOAD); }
"store"         { return token(ProcessSym.STORE); }
"install"       { return token(ProcessSym.INSTALL); }
"defmodule"     { return token(ProcessSym.DEFMOD); }
"module"        { return token(ProcessSym.MODULE); }
"pass"          { return token(ProcessSym.PASSKW); }
"from"          { return token(ProcessSym.FROM); }
"import"        { return token(ProcessSym.IMPORT); }
"kill"          { return token(ProcessSym.KILL); }
"open"          { return token(ProcessSym.OPEN); }
"close"         { return token(ProcessSym.CLOSE); }
{integer}       { return token(ProcessSym.LONG, new Long(yytext ())); }
{DoubleLiteral} { return token(ProcessSym.DOUBLE, new Double(yytext ())); }
{typeName}      { return token(ProcessSym.TYPE_NAME, org.tyco.common.symbol.Symbol.symbol(yytext())); }
{id}            { return token(ProcessSym.ID, org.tyco.common.symbol.Symbol.symbol(yytext())); }
{blanks}*       { }
\"\"\"          { buffer = new StringBuffer(); yybegin (STATE_MSTRING); }
\"              { buffer = new StringBuffer(); yybegin (STATE_STRING); }
.               { return token(ProcessSym.error, yytext()); }
}

<STATE_STRING>{
\\\\         { buffer.append("\\"); }
\\\"         { buffer.append("\""); }
\\n          { buffer.append("\n"); }
\\t          { buffer.append("\t"); }
\"           { yybegin(YYINITIAL); return token(ProcessSym.STRING, buffer.toString());}
{nl}         { return token(ProcessSym.error, yytext()); }
.            { buffer.append(yytext()); }
}
<STATE_MSTRING>{
\"\"\"      { yybegin(STATE_NORMAL); return token (ProcessSym.STRING,buffer.toString());}
[^\"]|\"[^\"]|\"\"[^\"]  { buffer.append(yytext()); }
}
