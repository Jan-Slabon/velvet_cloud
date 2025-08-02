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
    abstract fun sub(value: SupportedLiteral): SupportedLiteral
    abstract fun add(value : SupportedLiteral): SupportedLiteral
    abstract fun mult(value : SupportedLiteral): SupportedLiteral
    abstract fun div(value : SupportedLiteral): SupportedLiteral
    abstract fun compare(value : SupportedLiteral): SupportedLiteral.Bool
    abstract fun less(value : SupportedLiteral): SupportedLiteral.Bool
    class Integer(val int : Int) : SupportedLiteral()
    {
        override fun sub(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Integer(value.int - this.int)
                is SupportedLiteral.Double -> SupportedLiteral.Double(value.double - this.int)
                else -> {
                    throw Exception("Unsupported right side of Integer - operator: ${value::class}.")
                }
            }
        }

        override fun add(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Integer(value.int + this.int)
                is SupportedLiteral.Double -> SupportedLiteral.Double(value.double + this.int)
                else -> {
                    throw Exception("Unsupported right side of Integer + operator: ${value::class}.")
                }
            }
        }

        override fun mult(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Integer(value.int * this.int)
                is SupportedLiteral.Double -> SupportedLiteral.Double(value.double * this.int)
                else -> {
                    throw Exception("Unsupported right side of Integer * operator: ${value::class}.")
                }
            }
        }

        override fun div(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Integer(this.int / value.int)
                is SupportedLiteral.Double -> SupportedLiteral.Double(this.int / value.double)
                else -> {
                    throw Exception("Unsupported right side of Integer / operator: ${value::class}.")
                }
            }
        }

        override fun compare(value: SupportedLiteral): SupportedLiteral.Bool {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Bool(value.int == this.int)
                else -> {
                    throw Exception("Unsupported right side of Integer == operator: ${value::class}.")
                }
            }
        }

        override fun less(value: SupportedLiteral): Bool {
            return when(value)
            {
                is Double -> Bool(this.int < value.double)
                is Integer -> Bool(this.int < value.int)
                else -> throw Exception("Integer is not ordered with respect to ${value::class}")
            }
        }
    }
    class Double(val double : kotlin.Double) : SupportedLiteral() {
        override fun sub(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Double(value.int - this.double)
                is SupportedLiteral.Double -> SupportedLiteral.Double(value.double - this.double)
                else -> {
                    throw Exception("Unsupported right side of Integer + operator: ${value::class}.")
                }
            }
        }

        override fun add(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Double(value.int + this.double)
                is SupportedLiteral.Double -> SupportedLiteral.Double(value.double + this.double)
                else -> {
                    throw Exception("Unsupported right side of Double + operator: ${value::class}.")
                }
            }
        }

        override fun mult(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Double(value.int * this.double)
                is SupportedLiteral.Double -> SupportedLiteral.Double(value.double * this.double)
                else -> {
                    throw Exception("Unsupported right side of Double * operator: ${value::class}.")
                }
            }
        }

        override fun div(value: SupportedLiteral): SupportedLiteral {
            return when(value) {
                is SupportedLiteral.Integer -> SupportedLiteral.Double(this.double / value.int)
                is SupportedLiteral.Double -> SupportedLiteral.Double(this.double / value.double)
                else -> {
                    throw Exception("Unsupported right side of Double / operator: ${value::class}.")
                }
            }
        }

        override fun compare(value: SupportedLiteral): SupportedLiteral.Bool {
            return when(value)
            {
                is Double -> SupportedLiteral.Bool(value.double == this.double)
                else -> throw Exception("Unsupported right side of Double == operator: ${value::class}.")
            }
        }

        override fun less(value: SupportedLiteral): Bool {
            return when(value)
            {
                is Double -> Bool(this.double < value.double)
                is Integer -> Bool(this.double < value.int)
                else -> throw Exception("Double is not ordered with ${value::class}")
            }
        }
    }

    class String(val str : kotlin.String) : SupportedLiteral() {
        override fun sub(value: SupportedLiteral): SupportedLiteral {
            throw Exception("Strings can't be subtracted.")
        }

        override fun add(value: SupportedLiteral): SupportedLiteral {
            return when(value)
            {
                is String -> String(value.str + this.str)
                else -> throw Exception("Unsupported right side of String + operator: ${value::class}.")
            }
        }

        override fun mult(value: SupportedLiteral): SupportedLiteral {
            throw Exception("Strings can't be multiplied.")
        }

        override fun div(value: SupportedLiteral): SupportedLiteral {
            throw Exception("Strings can't be divided.")
        }

        override fun compare(value: SupportedLiteral): SupportedLiteral.Bool {
            return when(value)
            {
                is SupportedLiteral.String -> SupportedLiteral.Bool(value.str == this.str)
                else -> throw Exception("Unsupported right side of String == operator: ${value::class}.")
            }
        }

        override fun less(value: SupportedLiteral): Bool {
            throw Exception("Strings are not ordered set.")
        }
    }

    class Bool(val bool : Boolean): SupportedLiteral()
    {
        fun not(): SupportedLiteral.Bool
        {
            return Bool(!this.bool)
        }
        fun or(right: SupportedLiteral.Bool): SupportedLiteral.Bool
        {
            return Bool(this.bool || right.bool)
        }
        fun and(right: SupportedLiteral.Bool): SupportedLiteral.Bool
        {
            return Bool(this.bool && right.bool)
        }

        override fun sub(value: SupportedLiteral): SupportedLiteral {
            throw Exception("Booleans can't be subtracted")
        }

        override fun add(value: SupportedLiteral): SupportedLiteral {
            throw Exception("Booleans can't be added.")
        }

        override fun mult(value: SupportedLiteral): SupportedLiteral {
            throw Exception("Booleans can't be multiplied.")
        }

        override fun div(value: SupportedLiteral): SupportedLiteral {
            throw Exception("Strings can't be divided.")
        }

        override fun compare(value: SupportedLiteral): SupportedLiteral.Bool {
            when(value)
            {
                is SupportedLiteral.Bool -> return Bool(value.bool == this.bool)
                else -> throw Exception("Can't compare none Boolean type to Boolean")
            }
        }

        override fun less(value: SupportedLiteral): Bool {
            throw Exception("Booleans are not ordered set.")
        }
    }

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

