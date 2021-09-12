import java.io.File
import kotlin.math.max

const val ANSI_GREEN = "\u001B[32m"
const val ANSI_RESET = "\u001B[0m"
const val ANSI_RED = "\u001B[31m"

fun readTextPath(args: Array<String>, textName :String, ind : Int) = if(args.isNotEmpty()) {
        args[ind]
    } else {
        println("Please, enter name or path of the $textName text:")
        var inputSuccess = false
        var path = ""
        while (!inputSuccess) {
            var input = readLine()
            inputSuccess = if (input.isNullOrBlank()) {
                false
            } else {
                if (input[0] != '.')
                    input = "./$input"
                File(input).exists()
            }
            if (!inputSuccess)
                println("Wrong input, please, try again:")
            else
                path = input!! //if input was null, inputSuccess
        }
        path
    }

fun getLongestCommonSubSeq(text1 : Array<String>, text2 : Array<String>) : Array<Int> {
    val lcs = Array<Array<Int>>(text1.size + 1) { Array(text2.size + 1) { 0 } }
    for ((i1, s1) in text1.withIndex()) {
        for ((i2, s2) in text2.withIndex()) {
            lcs[i1 + 1][i2 + 1] = max(lcs[i1][i2 + 1], lcs[i1 + 1][i2])
            if (s1 == s2)
                lcs[i1 + 1][i2 + 1] = max(lcs[i1 + 1][i2 + 1], lcs[i1][i2] + 1)
        }
    }
    val commonIndices = ArrayList<Int>()
    var i1 = text1.size
    var i2 = text2.size
    while (i1 > 0 && i2 > 0) {
        when (lcs[i1][i2]) {
            lcs[i1 - 1][i2] -> i1--
            lcs[i1][i2 - 1] -> i2--
            else -> {
                commonIndices.add(i1 - 1)
                i1--
                i2--
            }
        }
    }
    return commonIndices.asReversed().toTypedArray()
}

fun main(args: Array<String>) {
    val originText = File(readTextPath(args, "origin", 0)).readLines().toTypedArray()
    val redactedText = File(readTextPath(args, "redacted", 1)).readLines().toTypedArray()
    val commonIndices = getLongestCommonSubSeq(originText, redactedText)
    var indexInRedacted = 0
    var prevCommonIndex = -1
    for (commonIndex in commonIndices) {
        for (i in (prevCommonIndex + 1) until commonIndex) {
            print("Deleted line ${ i + 1 }: ")
            println(ANSI_RED + originText[i] + ANSI_RESET)
        }
        if (redactedText[indexInRedacted] != originText[commonIndex])
            println("Inserted before line ${ commonIndex + 1 }:")
        while (redactedText[indexInRedacted] != originText[commonIndex]) {
            println(ANSI_GREEN + redactedText[indexInRedacted++] + ANSI_RESET)
        }
        indexInRedacted++
        prevCommonIndex = commonIndex
    }
    for (i in prevCommonIndex + 1 until originText.size) {
        println(ANSI_RED + "Deleted line ${ i + 1 }: ${ originText[i] }" + ANSI_RESET)
    }
    if (indexInRedacted < redactedText.size)
        println("Inserted after the end of origin text:")
    for (i in indexInRedacted until redactedText.size)
        println(ANSI_GREEN + redactedText[i] + ANSI_RESET)
}