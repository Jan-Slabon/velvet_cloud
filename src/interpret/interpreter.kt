package interpret

import `interface`.Binary
import `interface`.Expr
import `interface`.Grouping
import `interface`.Identifier
import `interface`.Literal
import `interface`.SupportedLiteral
import `interface`.Ternary
import `interface`.Unary
import `interface`.Visitor
import kotlin.math.exp


class Interpreter : Visitor<SupportedLiteral>()
{
    fun evaluate(expr: Expr) : SupportedLiteral
    {
        try {
            val eval = expr.accept(this)
            return eval
        }catch (exception: Exception)
        {
            when(expr)
            {
                is Unary -> println("Runtime error: " + exception.message + " at line: ${expr.operator.line}:${expr.operator.char}.")
                is Binary -> println("Runtime error: " + exception.message + " at line: ${expr.operator.line}:${expr.operator.char}.")
                is Ternary -> println("Runtime error: " + exception.message + " at line: ${expr.operator.line}:${expr.operator.char}.")
                else -> println("Unexpected Runtime error.")
            }
            return SupportedLiteral.String("Runtime error")
        }
    }
    override fun visit(expr: Expr): SupportedLiteral {
        when(expr)
        {
            is Literal ->
            {
                return expr.value
            }
            is Binary ->
            {
                val left = evaluate(expr.left)
                val right = evaluate(expr.right)
                return when(expr.operator.type) {
                    Identifier.ADD -> left.add(right)
                    Identifier.MULT -> left.mult(right)
                    Identifier.SUB -> left.sub(right)
                    Identifier.EQ -> left.compare(right)
                    Identifier.NEQ -> left.compare(right).not()
                    Identifier.GR -> left.less(right).not().and(left.compare(right).not())
                    Identifier.LE -> left.less(right)
                    Identifier.GREQ -> left.less(right).not()
                    Identifier.LEQ -> left.less(right).or(left.compare(right))
                    Identifier.COMMA -> right
                    else -> throw Exception("Unsupported operator ${expr.operator.lexme}")

                }
            }
            is Grouping -> {
                return evaluate(expr)
            }
            is Ternary ->
            {
                val condition = evaluate(expr.cond)
                return when(condition)
                {
                    is SupportedLiteral.Bool ->
                    {
                        when(condition.bool)
                        {
                            true -> evaluate(expr.left)
                            false -> evaluate(expr.right)
                        }
                    }
                    else -> throw Exception("Unsupported type: ${condition::class} as condition in ternary operator")
                }
            }
            is Unary -> {
                val value = evaluate(expr.expr)
                return when(expr.operator.type)
                    {
                        Identifier.SUB ->
                        {
                            when(value)
                            {
                                is SupportedLiteral.Double -> SupportedLiteral.Double(-value.double)
                                is SupportedLiteral.Integer -> SupportedLiteral.Integer(-value.int)
                                else -> throw Exception("Unsupported type ${value::class} for argument " + expr.operator.lexme)
                            }
                        }
                        Identifier.NOT ->
                        {
                            when(value)
                            {
                                is SupportedLiteral.Bool -> SupportedLiteral.Bool(!value.bool)
                                else -> throw Exception("Unsupported type ${value::class} for argument " + expr.operator.lexme)
                            }
                        }
                        else -> throw Exception("Bad operator at unary expression")
                    }
            }
        }
    }

    private fun checkType(argLexme : String, value : SupportedLiteral, vararg types : SupportedLiteral) : SupportedLiteral
    {
        var acc = false
        for (type in types)
        {
            acc = acc || value::class == type::class
        }
        if (acc) return value
        else throw Exception("Unsupported type ${value::class} for argument " + argLexme)
    }

}