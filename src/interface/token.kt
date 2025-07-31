package `interface`

enum class Identifier
{
    // Logical Operators
    AND, OR, XOR, NOT,
    // Sets
    INT, BOOLEAN, DOUBLE, VOID, STRING,
    // Ord + PartialOrd
    EQ, NEQ, LE, LEQ, GR, GREQ,
    // Algebraic Ops
    ASSIGN, ADD, SUB, MULT, DIV, MOD,
    // Control
    IF, ELSE,
    // Definitions
    LET, FUNCTION, TYPE, IDENTIFIER,
    // Operators (|> instead of "." and -> instead of "for")
    PIPE, ARROW, QUESTION, COLON,
    // Formating
    L_BRACE, R_BRACE, L_PAREN, R_PAREN, SEMICOLON, EOF, COMMA,
    // Error
    ERR
}
class Token(val type : Identifier, val lexme : String, val literal : Any?, val line : Int, val char : Int)