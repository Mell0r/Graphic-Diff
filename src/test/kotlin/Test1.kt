import java.io.File
import kotlin.test.*

val text1 = File("src/test/text1.txt").readLines().toTypedArray()
val text2 = File("src/test/text2.txt").readLines().toTypedArray()
val text3 = File("src/test/text3.txt").readLines().toTypedArray()
val text4 = File("src/test/text4.txt").readLines().toTypedArray()
val text5 = File("src/test/text5.txt").readLines().toTypedArray()
val text6 = File("src/test/text6.txt").readLines().toTypedArray()
val text7 = File("src/test/text7.txt").readLines().toTypedArray()
val textShuffledOriginal = File("src/test/shuffledTextOriginal.txt").readLines().toTypedArray()
val textShuffledRedacted = File("src/test/shuffledTextRedacted.txt").readLines().toTypedArray()

fun modifyTextByDiff(text: Array<String>, diff: Array<TextChange>): Array<String> {
    val modifiedText = arrayListOf<String>()
    var indexInDiff = 0
    for (indexInText in text.indices) {
        if (indexInDiff >= diff.size || diff[indexInDiff].index - 1 != indexInText) {
            modifiedText += text[indexInText]
        } else {
            while (indexInDiff < diff.size && diff[indexInDiff].index - 1 == indexInText && diff[indexInDiff].type == ChangeType.INSERT)
                modifiedText += diff[indexInDiff++].edit
            if (indexInDiff < diff.size && diff[indexInDiff].type == ChangeType.REMOVE && diff[indexInDiff].index - 1 == indexInText)
                indexInDiff++
            else
                modifiedText += text[indexInText]
        }
    }
    for (i in indexInDiff until diff.size)
        modifiedText += diff[i].edit
    return modifiedText.toTypedArray()
}

internal class TestLCP {
    @Test
    fun test1() {
        assertEquals(listOf(0, 1, 3, 5), getLongestCommonSubSeq(text1, text4).toList())
    }

    @Test
    fun test2() {
        assertEquals(listOf(2, 4, 5), getLongestCommonSubSeq(text1, text2).toList())
    }

    @Test
    fun test3() {
        assertEquals(listOf(0, 2), getLongestCommonSubSeq(text2, text3).toList())
    }

    @Test
    fun test4() {
        assertEquals(listOf(0, 2), getLongestCommonSubSeq(text3, text4).toList())
    }

    @Test
    fun test5() {
        assertEquals(listOf(), getLongestCommonSubSeq(text1, text5).toList())
    }

    @Test
    fun test6() {
        assertEquals(listOf(0), getLongestCommonSubSeq(text6, text5).toList())
    }

    @Test
    fun test7() {
        assertEquals(listOf(), getLongestCommonSubSeq(text3, text7).toList())
    }
}

internal class TestDiff {
    @Test
    fun test1() {
        assertEquals(text4.toList(), modifyTextByDiff(text1, calcDiff(text1, text4)).toList())
    }

    @Test
    fun test2() {
        assertEquals(text2.toList(), modifyTextByDiff(text1, calcDiff(text1, text2)).toList())
    }

    @Test
    fun test3() {
        assertEquals(text3.toList(), modifyTextByDiff(text2, calcDiff(text2, text3)).toList())
    }

    @Test
    fun test4() {
        assertEquals(text4.toList(), modifyTextByDiff(text3, calcDiff(text3, text4)).toList())
    }

    @Test
    fun test5() {
        assertEquals(text5.toList(), modifyTextByDiff(text1, calcDiff(text1, text5)).toList())
    }

    @Test
    fun test6() {
        assertEquals(text5.toList(), modifyTextByDiff(text6, calcDiff(text6, text5)).toList())
    }

    @Test
    fun test7() {
        assertEquals(text7.toList(), modifyTextByDiff(text3, calcDiff(text3, text7)).toList())
    }

    @Test
    fun testShuffled() {
        assertEquals(textShuffledRedacted.toList(), modifyTextByDiff(textShuffledOriginal, calcDiff(textShuffledOriginal, textShuffledRedacted)).toList())
    }
}