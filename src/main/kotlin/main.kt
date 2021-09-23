import java.io.File
import java.util.Objects.hash
import kotlin.math.max

typealias Text = List<String>

enum class ChangeType {
    REMOVE, INSERT
}

/**
 * Класс, хранящий информацию об одном изменении текста
 */
class TextChange(val type: ChangeType, val index: Int, val edit: String)

/**
* Возвращает список строк из файла по пути к нему
 */
fun getTextFormFile(file: File) = file.readLines().toList()

/**
* Возвращает индексы строк первого текста, для всех строк, которые оказались в наибольшей общей подпоследовательности двух текстов
 */
fun getLongestCommonSubSeq(text1: Text, text2: Text): List<Int> {
    //calculation lcs by dynamic programming
    val hashedText1 = text1.map {it -> hash(it)}
    val hashedText2 = text2.map {it -> hash(it)}
    val lcs = Array(hashedText1.size + 1) { Array(hashedText2.size + 1) { 0 } }
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
    return commonIndices.asReversed().toList()
}

/**
 * Считает diff в виде списка единичных изменений в тексте originText, по сравнению с текстом redactedText
 */
fun calcDiff(originText: Text, redactedText: Text): List<TextChange> {
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
    return diff.toList()
}

fun main() {
    MainFrame()
}