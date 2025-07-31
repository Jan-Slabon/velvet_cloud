import `interface`.PrintVisitor

import java.io.BufferedReader
import lex.*
import java.io.File
import parse.top_down.Parser
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun process_file(path : String)
{
    val bufferedReader: BufferedReader = File(path).bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    val lexer : Lexer = Lexer()
    val tokens = lexer.scan(inputString)
    val parser : Parser = Parser(tokens)
    val expr = parser.parse()
    PrintVisitor().print(expr)
    println()
    for (token in tokens)
    {
        println("Token: ${token.type}, ")
    }
}
fun process_line(plain_text : String)
{

}
fun main(args : Array<String>) {
    if (args.size > 1)
    {
        println("Error: Too many arguments")
    }
    else if (args.size == 1)
    {
      process_file(args[0])
    }
    else
    {
        println("Running in interactive mode")
        val input = readln()
        process_line(input)
    }
}