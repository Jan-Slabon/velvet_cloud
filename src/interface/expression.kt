package `interface`

abstract class Visitor<R>
{
    abstract fun visit(expr : Expr): R
}

class PrintVisitor : Visitor<Unit>() {
    fun print(expr: Expr)
    {
        expr.accept(this)
    }
    override fun visit(expr: Expr) {
        when (expr) {
            is Unary -> {
                print(expr.operator.lexme + " ")
                visit(expr.expr)
            }
            is Binary -> {
                print(expr.operator.lexme + " ")
                visit(expr.left)
                visit(expr.right)
            }
            is Grouping -> {
                print("( ")
                visit(expr.expr)
                print(") ")
            }
            is Literal -> {
                when(expr.value)
                {
                    is SupportedLiteral.Double -> print("${expr.value.double} ")
                    is SupportedLiteral.Bool -> print("${expr.value.bool} ")
                    is SupportedLiteral.Integer -> print("${expr.value.int} ")
                    is SupportedLiteral.String -> print(expr.value.str + " ")
                }
            }

            is Ternary ->
            {
                print(expr.operator.lexme + " ")
                visit(expr.cond)
                visit(expr.left)
                visit(expr.right)
            }
        }
    }
}
sealed class SupportedLiteral
{
    class Integer(val int : Int) : SupportedLiteral()
    class Double(val double : kotlin.Double) : SupportedLiteral()
    class String(val str : kotlin.String) : SupportedLiteral()
    class Bool(val bool : Boolean): SupportedLiteral()
}
sealed class Expr
{
    fun <R>accept(visitor : Visitor<R>): R
    {
        return visitor.visit(this)
    }
}
class Binary(val left : Expr, val right : Expr, val operator : Token) : Expr()

class Unary(val expr : Expr, val operator : Token) : Expr()

class Grouping(val expr : Expr) : Expr()

class Literal(val value : SupportedLiteral) : Expr()

class Ternary(val cond : Expr, val left: Expr, val right: Expr, val operator: Token) : Expr()

