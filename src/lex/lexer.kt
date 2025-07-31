package lex

import javax.naming.directory.InvalidAttributesException
import `interface`.*

class Lexer
{
    var tokens : MutableList<Token> = mutableListOf()
    fun scan(plain_text : String): List<Token>
    {
        val scanner : Scanner = Scanner(plain_text)
        do {
            scanner.find_token()
                .match("if", Identifier.IF)
                .match("else", Identifier.ELSE)
                .match("type", Identifier.TYPE)
                .match("fun", Identifier.FUNCTION)
                .match("let", Identifier.LET)
                .match("and", Identifier.AND)
                .match("or", Identifier.OR)
                .match("not", Identifier.NOT)
                .match("xor", Identifier.XOR)
                .match("|>", Identifier.PIPE)
                .match("->", Identifier.ARROW)
                .match("!=", Identifier.NEQ)
                .match("==", Identifier.EQ)
                .match(">=", Identifier.GREQ)
                .match("<=", Identifier.LEQ)
                .match("<", Identifier.LE)
                .match(">", Identifier.GR)
                .match("=", Identifier.ASSIGN)
                .match("+", Identifier.ADD)
                .match("*", Identifier.MULT)
                .match("/", Identifier.DIV)
                .match("%", Identifier.MOD)
                .match("{", Identifier.L_BRACE)
                .match("}", Identifier.R_BRACE)
                .match("(", Identifier.L_PAREN)
                .match(")", Identifier.R_PAREN)
                .match(",", Identifier.COMMA)
                .match(";", Identifier.SEMICOLON)
                .match("-", Identifier.SUB)
                .match(":", Identifier.COLON)
                .match("?", Identifier.QUESTION)
                .match_number()
                .match_string()
                .match_identifier()

            val token = scanner.emit()
            tokens.add(token)
        }while (token.type != Identifier.EOF)
        if (scanner.err)
        {
            println("Error(s) occured during lexical analysis")
            throw InvalidAttributesException()
        }
        return tokens
    }
};