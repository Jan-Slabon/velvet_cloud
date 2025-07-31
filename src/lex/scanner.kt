package lex
import `interface`.*
class Scanner(val source : String)
{
    var start : Int = 0
    var line : Int = 1
    var token : Token? = null
    var err : Boolean = false
    var char_count = 0
    val reserved_identifiers = mapOf(
        "true" to {line: Int, char: Int -> Token(Identifier.BOOLEAN, "true", true, line, char)},
        "false" to {line: Int, char: Int -> Token(Identifier.BOOLEAN, "false", false, line, char)}
    )
    fun find_token(): Scanner
    {
        consume_whitespaces()
        return this
    }
    fun match(pattern : String, identifier : Identifier): Scanner
    {
        if (token != null) return this
        else
        {
            var current_char = start
            var line_char = char_count
            for (char in pattern)
            {
                if (current_char >= source.length) return this
                if (source[current_char] != char) return this
                current_char++
                line_char++
            }
            // Token accepted
            start = current_char
            char_count = line_char
            token = Token(identifier, pattern, null, line, char_count)
            return this
        }
    }
    fun match_identifier(): Scanner
    {
        if (token != null) return this
        else
        {
            if (source.length > start && is_alpha(source[start]))
            {
                var current_char = start + 1
                val token_begining_char = char_count
                while (current_char < source.length && is_alphanumeric(source[current_char]))
                {
                    current_char++
                    char_count++
                }
                val identifier = source.slice(start..current_char-1)
                start = current_char
                val token_creator =
                    reserved_identifiers.get(identifier) ?:
                    {_:Int, _:Int ->Token(Identifier.IDENTIFIER, identifier, null, line, token_begining_char)}
                token = token_creator(line, token_begining_char)
            }
        }
        return this
    }
    fun match_string(): Scanner
    {
        if(token != null) return this
        else
        {
            if(source.length > start && source[start] == '\"')
            {
                var current_char = start + 1
                var proposed_line_char_count = char_count
                var proposed_line_count = line
                while (source.length > current_char && !is_quote(current_char))
                {
                    // We allow multi line strings
                    if (source[current_char] == '\n')
                    {
                        proposed_line_count++
                        proposed_line_char_count = 0
                    }
                    current_char++
                }
                // Quotation not closed
                if(current_char >= source.length)
                {
                    println("Found string without an end at $line:$char_count, please add \" at the end of a string.")
                    err = true
                    token = Token(Identifier.ERR, "", null, line, char_count)
                    start++
                }
                else
                {
                    val string = source.slice(start+1..current_char-2)
                    token = Token(Identifier.STRING, '"' + string + '"', string, line, char_count)
                    char_count = proposed_line_char_count
                    line = proposed_line_count
                    start = current_char + 1
                }
            }
            return this
        }
    }
    fun match_number(): Scanner
    {
        if (token != null) return this
        else
        {
            var current_char = start
            var dot_encountered = false
            while (current_char < source.length && (is_numeric(source[current_char]) || source[current_char] == '.'))
            {
                if(source[current_char] == '.' && !dot_encountered) dot_encountered = true
                // Multiple dots find in number
                else if (source[current_char] == '.' && dot_encountered) return this
                current_char++
            }
            // Some number was found
            if (current_char > start)
            {
                val number = source.slice((start.. current_char-1))
                if (dot_encountered)
                {
                    val value = number.toDouble()
                    token = Token(Identifier.DOUBLE, number, value, line, char_count)
                }
                else
                {
                    val value = number.toInt()
                    token = Token(Identifier.INT, number, value, line, char_count)
                }
                char_count += (current_char - start)
                start = current_char
            }
            return this
        }
    }
    fun emit(): Token
    {
        val emited_token =
            token ?:
            // We reached the end of file
            if (start >= source.length)
                Token(Identifier.EOF, "", null, line, char_count)
            else
            {
                // Move to the next token
                var token_end = start
                val token_begin_char = char_count
                while (token_end < source.length && !white_space(source[token_end]))
                {
                    token_end++
                    char_count++
                }
                // Register invalid Token
                val invalid_lexme = source.slice((start..token_end-1))
                err = true
                start = token_end
                println("Invalid Token: \"$invalid_lexme\" at line $line and character $token_begin_char")
                Token(Identifier.ERR, invalid_lexme, null, line, token_begin_char)
            }
        token = null
        return emited_token
    }
    private fun is_quote(count : Int): Boolean
    {
        if (source[count] == '\"' && source[count-1] != '\\')
        {
            return true
        }
        else
        {
            return false
        }
    }
    private fun white_space(char : Char): Boolean
    {
        return !(char >= '!' && char <= '~')
    }
    private fun handle_newline()
    {
        line++
        char_count = 0
    }
    private fun consume_whitespaces()
    {
        while (start < source.length && white_space(source[start]))
        {
            if (source[start] == '\n') handle_newline()
            start++
            char_count++
        }
    }
    private fun is_numeric(char : Char): Boolean
    {
        return char >= '0' && char <= '9'
    }
    private fun is_alpha(char : Char): Boolean
    {
        // '_' added to make things easier
        return (char >= 'a' && char <= 'z') || (char >= 'A' && char <= 'Z') || char == '_'
    }
    private fun is_alphanumeric(char: Char): Boolean
    {
        return is_alpha(char) || is_numeric(char)
    }
}