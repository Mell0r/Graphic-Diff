import java.io.File
import kotlin.test.*

val text1 = File("src/test/text1.txt").readLines().toList()
val text2 = File("src/test/text2.txt").readLines().toList()
val text3 = File("src/test/text3.txt").readLines().toList()
val text4 = File("src/test/text4.txt").readLines().toList()
val text5 = File("src/test/text5.txt").readLines().toList()
val text6 = File("src/test/text6.txt").readLines().toList()
val text7 = File("src/test/text7.txt").readLines().toList()
val textShuffledOriginal = File("src/test/shuffledTextOriginal.txt").readLines().toList()
val textShuffledRedacted = File("src/test/shuffledTextRedacted.txt").readLines().toList()

internal class TestLCP {
    @Test
    fun testDifferenceInMiddle() {
        assertEquals(listOf(0, 1, 3, 5), getLongestCommonSubSeq(text1, text4).toList())
        assertEquals(listOf(0, 2), getLongestCommonSubSeq(text3, text4).toList())
    }

    @Test
    fun testDifferenceInEnd() {
        assertEquals(listOf(2, 4, 5), getLongestCommonSubSeq(text1, text2).toList())
    }

    @Test
    fun testDifferenceInBegin() {
        assertEquals(listOf(0, 2), getLongestCommonSubSeq(text2, text3).toList())
    }

    @Test
    fun testCompleteDifferent() {
        assertEquals(listOf(), getLongestCommonSubSeq(text1, text5).toList())
        assertEquals(listOf(0), getLongestCommonSubSeq(text6, text5).toList()) //text6 with empty lines
        assertEquals(listOf(), getLongestCommonSubSeq(text3, text7).toList()) //equal with different tabulation
    }
}