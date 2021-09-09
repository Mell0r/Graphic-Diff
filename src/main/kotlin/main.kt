import java.io.File
import kotlin.math.max

fun readTextPath(args: Array<String>, textName :String, ind : Int) = if(args.isNotEmpty()) {
        args[ind]
    } else {
        println("Please, enter name or path of the $textName text:")
        var path = readLine()
        var flag = if (path == null || path.isEmpty()) {
            true
        } else {
            if (path[0] != '.')
                path = "./$path"
            !File(path).exists()
        }
        while (flag) {
            println("Wrong input, please, try again:")
            path = readLine()
            flag = if (path == null || path.isEmpty()) {
                true
            } else {
                if (path[0] != '.')
                    path = "./$path"
                !File(path).exists()
            }
        }
        path
    }

fun getLongestCommonSubSeq(text1 : Array<String>, text2 : Array<String>) : ArrayList<Int> {
    val lcs = Array<Array<Int>>(text1.size + 1) { Array(text2.size + 1) { 0 } }
    for ((i1, s1) in text1.withIndex()) {
        for ((i2, s2) in text2.withIndex()) {
            lcs[i1 + 1][i2 + 1] = max(lcs[i1][i2 + 1], lcs[i1 + 1][i2])
            if (s1 == s2)
                lcs[i1 + 1][i2 + 1] = max(lcs[i1 + 1][i2 + 1], lcs[i1][i2] + 1)
        }
    }
    val commonIndexes = ArrayList<Int>()
    var i1 = text1.size
    var i2 = text2.size
    while (i1 > 0 && i2 > 0) {
        when (lcs[i1][i2]) {
            lcs[i1 - 1][i2] -> i1--
            lcs[i1][i2 - 1] -> i2--
            else -> {
                commonIndexes.add(i1 - 1)
                i1--
                i2--
            }
        }
    }
    commonIndexes.reverse()
    return commonIndexes
}

fun main(args: Array<String>) {
    val originText = File(readTextPath(args, "origin", 0)).readLines().toTypedArray()
    val redactedText = File(readTextPath(args, "redacted", 1)).readLines().toTypedArray()
//    val commonIndexes = getLongestCommonSubSeq(originText, redactedText)
//    var compareIndex = 0
//    for ((curIndex, curLine) in originText.withIndex()) {
//        if (curLine != redactedText[compareIndex])
//            println("<$curIndex th line $curLine")
//    }
}
