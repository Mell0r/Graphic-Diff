import java.io.File
import kotlin.test.*

internal class Test1 {
    @Test
    fun testLCP1() {
        assertEquals(arrayListOf(0, 1, 3, 5), getLongestCommonSubSeq(File("./text1.txt").readLines().toTypedArray(), File("./text4.txt").readLines().toTypedArray()))
    }
    @Test
    fun testLCP2() {
        assertEquals(arrayListOf(2, 4, 5), getLongestCommonSubSeq(File("./text1.txt").readLines().toTypedArray(), File("./text2.txt").readLines().toTypedArray()))
    }
    @Test
    fun testLCP3() {
        assertEquals(arrayListOf(0, 2), getLongestCommonSubSeq(File("./text2.txt").readLines().toTypedArray(), File("./text3.txt").readLines().toTypedArray()))
    }
    @Test
    fun testLCP4() {
        assertEquals(arrayListOf(0, 2), getLongestCommonSubSeq(File("./text3.txt").readLines().toTypedArray(), File("./text4.txt").readLines().toTypedArray()))
    }
    @Test
    fun testLCP5() {
        assertEquals(arrayListOf(), getLongestCommonSubSeq(File("./text1.txt").readLines().toTypedArray(), File("./text5.txt").readLines().toTypedArray()))
    }
    @Test
    fun testLCP6() {
        assertEquals(arrayListOf(0), getLongestCommonSubSeq(File("./text6.txt").readLines().toTypedArray(), File("./text5.txt").readLines().toTypedArray()))
    }
    @Test
    fun testLCP7() {
        assertEquals(arrayListOf(), getLongestCommonSubSeq(File("./text3.txt").readLines().toTypedArray(), File("./text7.txt").readLines().toTypedArray()))
    }
}
