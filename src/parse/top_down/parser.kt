package parse.top_down

import `interface`.Binary
import `interface`.Expr
import `interface`.Grouping
import `interface`.Identifier
import `interface`.Literal
import `interface`.SupportedLiteral
import `interface`.Ternary
import `interface`.Token
import `interface`.Unary

class Parser(val tokens: List<Token>)
{
    var counter : Int = 0
    fun parse(): Expr
    {
        return expression()
    }
    fun expression(): Expr
    {
        var left = ternary()
        while (checkTokenType(Identifier.COMMA))
        {
            val operator = consume()
            val right = ternary()
            left = Binary(left, right, operator)
        }
        return left
    }
    fun ternary(): Expr
    {
        val condition = equality()

        if (checkTokenType(Identifier.QUESTION))
        {
            val operator = consume()
            val left = ternary()
            consume_conditional(Identifier.COLON, "Missing : after ? in ternary expression")
            val right = ternary()
            return Ternary(condition, left, right, operator)
        }
        return condition
    }
    fun equality() : Expr
    {
        var left : Expr = comparison()

        while (checkTokenType(Identifier.EQ, Identifier.NEQ))
        {
            val operator : Token = consume()
            val right : Expr = comparison()
            left = Binary(left, right, operator)
        }
        return left
    }
    fun comparison() : Expr
    {
        var left : Expr = term()

        while (checkTokenType(Identifier.GR, Identifier.LE, Identifier.GREQ, Identifier.LEQ))
        {
            val operator = consume()
            val right = term()
            left = Binary(left, right, operator)
        }
        return left
    }
    fun term(): Expr
    {
        var left = factor()

        while (checkTokenType(Identifier.SUB, Identifier.ADD))
        {
            val operator = consume()
            val right = factor()
            left = Binary(left, right, operator)
        }
        return left
    }
    fun factor(): Expr
    {
        var left = unary()

        while (checkTokenType(Identifier.MULT, Identifier.DIV))
        {
            val operator = consume()
            val right = unary()
            left = Binary(left, right, operator)
        }
        return left
    }
    fun unary(): Expr
    {
        if (checkTokenType(Identifier.NOT, Identifier.SUB))
        {
            val operator = consume()
            val right = unary()
            return Unary(right, operator)
        }
        return primary()
    }
    fun primary(): Expr
    {
        if(checkTokenType(Identifier.BOOLEAN)) return Literal(SupportedLiteral.Bool(consume().literal as Boolean))
        if(checkTokenType(Identifier.INT)) return Literal(SupportedLiteral.Integer(consume().literal as Int))
        if(checkTokenType(Identifier.DOUBLE)) return Literal(SupportedLiteral.Double(consume().literal as Double))
        if(checkTokenType(Identifier.STRING)) return Literal(SupportedLiteral.String(consume().literal as String))

        if(checkTokenType(Identifier.L_PAREN))
        {
            consume()
            val expr = expression()
            consume_conditional(Identifier.R_PAREN, "Expected ) after expression")
            return Grouping(expr)
        }
        throw Exception("Unexpected token ${peek().lexme} found")
    }


    private fun consume() : Token
    {
        counter++
        return tokens[counter - 1]
    }
    private fun consume_conditional(id : Identifier, message : String): Token
    {
        if(checkTokenType(id))
        {
            return consume()
        }
        else
        {
            throw Exception(message)
        }
    }
    private fun peek() : Token
    {
        return tokens[counter]
    }
    private fun checkTokenType(vararg ids : Identifier): Boolean
    {
        var acc = false
        for (id in ids)
        {
            acc = acc || peek().type == id
        }
        return acc
    }

}