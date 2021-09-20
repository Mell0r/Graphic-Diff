import java.awt.Component
import java.awt.Font
import java.io.File
import javax.swing.*
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

class MainFrame: JFrame("Difference calculator") {
    private fun showErrorDialog(message : String) {
        JOptionPane.showMessageDialog( this, message, "Error", JOptionPane.ERROR_MESSAGE)
    }

    private fun showStartFrame() { // Создание панели и вывод содержимого на экран
        val contents = JPanel()
        contents.layout = BoxLayout(contents, BoxLayout.PAGE_AXIS)
        contents.add(originLabel)
        contents.add(originTextField)
        contents.add(redactedLabel)
        contents.add(redactedTextField)
        contents.add(calculateButton)
        contentPane = contents
        setSize(500, 170)
        isVisible = true
    }
    // Текстовые поля
    private val originTextField: JTextField
    private val redactedTextField: JTextField
    // Подписи
    private val originLabel: JLabel
    private val redactedLabel: JLabel
    // Кнопки
    private val calculateButton = JButton("Calculate!")
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        // Инициализация текстовых полей
        originTextField = JTextField(45)
        redactedTextField = JTextField(45)
        // Инициализация подписей
        originLabel = JLabel("Write name of origin text here:")
        redactedLabel = JLabel("Write name of redacted text here:")
        // Настройка шрифта
        originTextField.font = Font("Dialog", Font.PLAIN, 14)
        originLabel.font = Font("Dialog", Font.PLAIN, 15)
        redactedTextField.font = Font("Dialog", Font.PLAIN, 14)
        redactedLabel.font = Font("Dialog", Font.PLAIN, 15)
        calculateButton.font = Font("Dialog", Font.PLAIN, 20)
        //Настройка расположения
        originTextField.alignmentX = Component.CENTER_ALIGNMENT
        originLabel.alignmentX = Component.CENTER_ALIGNMENT
        redactedTextField.alignmentX = Component.CENTER_ALIGNMENT
        redactedLabel.alignmentX = Component.CENTER_ALIGNMENT
        calculateButton.alignmentX = Component.CENTER_ALIGNMENT
        // Слушатель окончания ввода
        calculateButton.addActionListener {
            val originPath = originTextField.text
            val redactedPath = redactedTextField.text
            if (!File(originPath).exists())
            {
                if (!File(redactedPath).exists())
                    showErrorDialog("Wrong name of origin and redacted files. Please, fix it and try again.")
                else
                    showErrorDialog("Wrong name of origin text. Please, fix it and try again.")
            }
            else if (!File(redactedPath).exists()) {
                showErrorDialog("Wrong name of redacted text. Please, fix it and try again.")
            }
            else {
                val originText = getTextFormFile(originPath)
                val redactedText = getTextFormFile(redactedPath)
                val diffResultArea = JTextArea()
                diffResultArea.isEditable = false
                //Print diff
                val diff = calcDiff(originText, redactedText)
                var insertedPrev = false
                for (change in diff) {
                    if (change.type == ChangeType.INSERT) {
                        when {
                            insertedPrev -> {
                                diffResultArea.append(change.edit)
                                diffResultArea.append("\n")
                            }
                            change.index <= originText.size -> {
                                diffResultArea.append("Inserted before line ${change.index}:\n")
                                diffResultArea.append(change.edit)
                                diffResultArea.append("\n")
                            }
                            else -> {
                                diffResultArea.append("Inserted after the end of text:\n")
                                diffResultArea.append(change.edit)
                                diffResultArea.append("\n")
                            }
                        }
                        insertedPrev = true
                    } else {
                        diffResultArea.append("Deleted line ${change.index}: \n")
                        diffResultArea.append(change.edit)
                        diffResultArea.append("\n")
                        insertedPrev = false
                    }
                }
                contentPane = diffResultArea
                isVisible = true
            }
        }
        showStartFrame()
    }
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

//fun printlnInColor(output: String, color: Color) {
//    println(color.ansi + output + Color.RESET.ansi)
//}

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

fun main() {
    MainFrame()
}