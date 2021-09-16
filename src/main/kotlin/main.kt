import java.io.File
import kotlin.math.max

enum class Color(val ansi: String) {
    GREEN("\u001B[32m"),
    RESET("\u001B[0m"),
    RED("\u001B[31m")
}

enum class ChangeType {
    REMOVE, INSERT
}

class TextChange(val type: ChangeType, val index: Int, val edit: String)

fun getTextFormFile(path: String) = File(path).readLines().toTypedArray()

fun readText(args: Array<String>, textName: String, indInArgs: Int) = if (args.isNotEmpty()) {
    getTextFormFile(args[indInArgs])
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
                input = "src/test/$input"
            File(input).exists()
        }
        if (!inputSuccess)
            println("Wrong input, please, try again:")
        else
            path = input!! //if input was null, inputSuccess
    }
    getTextFormFile(path)
}

fun getLongestCommonSubSeq(text1: Array<String>, text2: Array<String>): Array<Int> {
    //calculation lcs by dynamic programming
    val lcs = Array(text1.size + 1) { Array(text2.size + 1) { 0 } }
    for ((i1, s1) in text1.withIndex()) {
        for ((i2, s2) in text2.withIndex()) {
            lcs[i1 + 1][i2 + 1] = max(lcs[i1][i2 + 1], lcs[i1 + 1][i2])
            if (s1 == s2)
                lcs[i1 + 1][i2 + 1] = max(lcs[i1 + 1][i2 + 1], lcs[i1][i2] + 1)
        }
    }
    //build answer on lcs
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

fun printlnInColor(output: String, color: Color) {
    println(color.ansi + output + Color.RESET.ansi)
}

fun calcDiff(originText: Array<String>, redactedText: Array<String>): Array<TextChange> {
    val commonIndices = getLongestCommonSubSeq(originText, redactedText)
    val diff = arrayListOf<TextChange>()
    var indexInRedacted = 0
    var prevCommonIndex = -1
    for (commonIndex in commonIndices) {
        for (i in (prevCommonIndex + 1) until commonIndex)
            diff += TextChange(ChangeType.REMOVE, i + 1, originText[i])
        while (redactedText[indexInRedacted] != originText[commonIndex])
            diff += TextChange(ChangeType.INSERT, commonIndex + 1, redactedText[indexInRedacted++])
        indexInRedacted++
        prevCommonIndex = commonIndex
    }
    for (i in prevCommonIndex + 1 until originText.size)
        diff += TextChange(ChangeType.REMOVE, i + 1, originText[i])
    for (i in indexInRedacted until redactedText.size)
        diff += TextChange(ChangeType.INSERT, originText.size + 1, redactedText[i])
    return diff.toTypedArray()
}

fun main(args: Array<String>) {
    val originText = readText(args, "origin", 0)
    val redactedText = readText(args, "redacted", 1)
    val diff = calcDiff(originText, redactedText)
    var insertedPrev = false
    for (change in diff) {
        if (change.type == ChangeType.INSERT) {
            when {
                insertedPrev -> printlnInColor(change.edit, Color.GREEN)
                change.index <= originText.size -> {
                    println("Inserted before line ${change.index}:")
                    printlnInColor(change.edit, Color.GREEN)
                }
                else -> {
                    println("Inserted after the end of text:")
                    printlnInColor(change.edit, Color.GREEN)
                }
            }
            insertedPrev = true
        } else {
            print("Deleted line ${change.index}: ")
            printlnInColor(change.edit, Color.RED)
            insertedPrev = false
        }
    }
}