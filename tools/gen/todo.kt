package tools

import com.squareup.kotlinpoet.*
abstract class Visitor
{
    abstract fun visit(expr : Expr)
}
open class Expr
{
    fun apply(visitor : Visitor)
    {
        visitor.visit(this)
    }
}
class Binary(val left : Expr, val right : Expr, val operator : (Expr, Expr) -> Expr) : Expr()
{
//    override fun apply(visitor : Visitor)
//    {
//        visitor.visit(this)
//    }
}
class Unary(val value : Expr, val operator : (Expr) -> Expr) : Expr()
{
//    override fun apply(visitor : Visitor)
//    {
//        visitor.visit(this)
//    }
}
class PrintVisitor : Visitor()
{
    override fun visit(expr: Expr) {
        when(expr)
        {
            is Unary -> println("Unary")
            is Binary -> println("Binary")
            else -> println("Undefined")
        }
    }
}

fun main(args : Array<String>)
{
//    val file = FileSpec.builder("gen", "exprresionsGenerated")
//    file.addCode(
//        "abstract class Expr\n" +
//                "{\n" +
//                "    abstract fun apply(visitor : Visitor)\n" +
//                "}"
//    )
//    val type = gen_class("Binary", arrayOf("first", "second", "plus"))
//    file.addType(type)
//    val gen_code = file.build()
//    gen_code.writeTo(System.out)
    val bin = Binary(Expr(), Expr(), {a, b -> a})
    val print = PrintVisitor()
    bin.apply(print)
}
fun gen_class(name : String, fields : Array<String>): TypeSpec
{
    val exprClass = ClassName("gen", "Greeter")
    val file = FileSpec.builder("gen", "exprresionGen")
    val type = TypeSpec.classBuilder(name)
    val constructorBuilder = FunSpec.constructorBuilder()
    for(field in fields)
    {
        type.addProperty(
            PropertySpec.builder(field, Expr::class)
                .initializer(field)
                .build()
        )
        constructorBuilder.addParameter(field, String::class)
    }
    val constructor = constructorBuilder.build()
    return type.primaryConstructor(constructor).build()
//        file.addType(
//            TypeSpec.classBuilder("Expr")
//                .primaryConstructor(
//                    FunSpec.constructorBuilder()
//                        .addParameter("name", String::class)
//                        .build()
//                )
//                .addProperty(
//                    PropertySpec.builder("name", String::class)
//                        .initializer("name")
//                        .build()
//                )
//                .addFunction(
//                    FunSpec.builder("greet")
//                        .addStatement("println(%P)", "Hello, \$name")
//                        .build()
//                )
//                .build()
//        )
//        .addFunction(
//            FunSpec.builder("main")
//                .addParameter("args", String::class, KModifier.VARARG)
//                .addStatement("%T(args[0]).greet()", exprClass)
//                .build()
//        )
//        .build()

    //ready_file.writeTo(System.out)
}