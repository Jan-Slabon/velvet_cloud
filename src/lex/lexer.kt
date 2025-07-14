package lex

import javax.naming.directory.InvalidAttributesException

enum class Identifier
{
    // Logical Operators
    AND, OR, XOR, NOT,
    // Sets
    INT, BOOLEAN, DOUBLE, VOID, STRING,
    // Ord + PartialOrd
    EQ, NEQ, LE, LEQ, GR, GREQ,
    // Algebraic Ops
    ASSIGN, ADD, MULT, DIV, MOD,
    // Control
    IF, ELSE,
    // Definitions
    LET, FUNCTION, TYPE, IDENTIFIER,
    // Operators (|> instead of "." and -> instead of "for")
    PIPE, ARROW,
    // Formating
    L_BRACE, R_BRACE, L_PAREN, R_PAREN, SEMICOLON, EOF, COMMA,
    // Error
    ERR
}
class Token(val type : Identifier, val lexme : String, val literal : Any?, val line : Int, val char : Int)

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