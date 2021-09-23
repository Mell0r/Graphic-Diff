import kotlin.test.*

/** Модифицирует текст по изменениям, записанным в результате исполнения diff */
fun modifyTextByDiff(text: Text, diff: List<TextChange>): List<String> {
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
    return modifiedText.toList()
}

internal class TestDiff {
    @Test
    fun testDifferenceInMiddle() {
        assertEquals(text4.toList(), modifyTextByDiff(text1, calcDiff(text1, text4)).toList())
        assertEquals(text4.toList(), modifyTextByDiff(text3, calcDiff(text3, text4)).toList())
    }

    @Test
    fun testDifferenceInEnd() {
        assertEquals(text2.toList(), modifyTextByDiff(text1, calcDiff(text1, text2)).toList())
    }

    @Test
    fun testDifferenceInBegin() {
        assertEquals(text3.toList(), modifyTextByDiff(text2, calcDiff(text2, text3)).toList())
    }

    @Test
    fun testCompleteDifferent() {
        assertEquals(text5.toList(), modifyTextByDiff(text1, calcDiff(text1, text5)).toList())
        assertEquals(text5.toList(), modifyTextByDiff(text6, calcDiff(text6, text5)).toList()) //text6 with empty lines
        assertEquals(text7.toList(), modifyTextByDiff(text3, calcDiff(text3, text7)).toList()) //equal with different tabulation
    }

    @Test
    fun testShuffled() {
        assertEquals(textShuffledRedacted.toList(), modifyTextByDiff(textShuffledOriginal, calcDiff(textShuffledOriginal, textShuffledRedacted)).toList())
    }
}