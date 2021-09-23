import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.io.File
import javax.swing.*
import javax.swing.text.StyleConstants

class MainFrame: JFrame("Difference calculator") {
    /** Создаёт начальное окно и выводит содержимое на экран */
    private fun showStartFrame() {
        val contents = JPanel()
        contents.layout = BoxLayout(contents, BoxLayout.PAGE_AXIS)
        contents.add(chooseOriginFileButton)
        contents.add(originPathField)
        contents.add(chooseRedactedFileButton)
        contents.add(redactedPathField)
        contents.add(calculateButton)
        contentPane = contents
        setSize(500, 200)
        isVisible = true
    }
    /** Создаёт окно результата и выводит содержимое на экран */
    private fun showResultFrame() {
        val contents = JPanel()
        contents.layout = BorderLayout()
        contents.add(backToMainButton, BorderLayout.PAGE_END)
        contents.add(scrollPane, BorderLayout.CENTER)
        contentPane = contents
        setSize(1000, 500)
        isVisible = true
    }
    /** Выводит сообщение об ошибке с заданным текстом */
    private fun showErrorDialog(message : String) {
        JOptionPane.showMessageDialog( this, message, "Error", JOptionPane.ERROR_MESSAGE)
    }

    /** Выводит с цветовой индикацией diff двух заданных файлов в окно результата */
    private fun printDiff(originText : Text, redactedText : Text) {
        val diff = calcDiff(originText, redactedText)
        var insertedPrev = false
        for (change in diff) {
            if (change.type == ChangeType.INSERT) {
                when {
                    insertedPrev -> showResultInColor("${change.edit}\n", Color.GREEN)
                    change.index <= originText.size -> {
                        showResultInColor("Inserted before line ${change.index}:\n")
                        showResultInColor("${change.edit}\n", Color.GREEN)
                    }
                    else -> {
                        showResultInColor("Inserted after the end of text:\n")
                        showResultInColor("${change.edit}\n", Color.GREEN)
                    }
                }
                insertedPrev = true
            } else {
                showResultInColor("Deleted line ${change.index}:\n")
                showResultInColor("${change.edit}\n", Color.RED)
                insertedPrev = false
            }
        }
    }

    /** Печатает заданную фразу с заданным текстом в окно результата */
    private fun showResultInColor(message: String, color: Color = Color.BLACK) {
        val style = diffResultPane.getStyle("default")
        StyleConstants.setForeground(style, color)
        val doc = diffResultPane.styledDocument
        doc.insertString(doc.length, message, style)
        StyleConstants.setForeground(style, Color.BLACK)
    }

    // Текстовые поля
    private val originPathField = JTextField(45)
    private val redactedPathField = JTextField(45)
    private val diffResultPane = JTextPane()

    // Кнопки
    private val calculateButton = JButton("Calculate!")
    private val backToMainButton = JButton("Back")
    private val chooseOriginFileButton = JButton("Click to choose origin file")
    private val chooseRedactedFileButton = JButton("Click to choose redacted file")

    //Окна выбора файла
    private val originFileChooser = JFileChooser()
    private val redactedFileChooser = JFileChooser()

    //Скроллер
    private val scrollPane = JScrollPane(diffResultPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        // Настройка текстовых полей
        originPathField.isEditable = false
        redactedPathField.isEditable = false
        diffResultPane.isEditable = false
        diffResultPane.addStyle("default", null)

        //Настройка окон выбора файла
        originFileChooser.currentDirectory = File("src/test")
        redactedFileChooser.currentDirectory = File("src/test")

        // Настройка шрифта
        originPathField.font = Font("Dialog", Font.PLAIN, 15)
        redactedPathField.font = Font("Dialog", Font.PLAIN, 15)
        calculateButton.font = Font("Dialog", Font.PLAIN, 20)
        chooseOriginFileButton.font = Font("Dialog", Font.PLAIN, 18)
        chooseRedactedFileButton.font = Font("Dialog", Font.PLAIN, 18)

        //Настройка расположения
        originPathField.alignmentX = CENTER_ALIGNMENT
        redactedPathField.alignmentX = CENTER_ALIGNMENT
        calculateButton.alignmentX = CENTER_ALIGNMENT
        chooseOriginFileButton.alignmentX = CENTER_ALIGNMENT
        chooseRedactedFileButton.alignmentX = CENTER_ALIGNMENT

        // Слушатели для кнопок
        calculateButton.addActionListener {
            val originText = originFileChooser.selectedFile
            val redactedText = redactedFileChooser.selectedFile
            if (originText == null)
            {
                if (redactedText == null)
                    showErrorDialog("Origin and redacted files weren't chose. Please, try again.")
                else
                    showErrorDialog("Origin text wasn't chose. Please, try again.")
            }
            else if (redactedText == null) {
                showErrorDialog("Redacted text wasn't chose. Please, try again.")
            }
            else {
                printDiff(getTextFormFile(originText), getTextFormFile(redactedText))
                showResultFrame()
            }
        }

        backToMainButton.addActionListener {
            diffResultPane.text = ""
            showStartFrame()
        }

        chooseOriginFileButton.addActionListener {
            originFileChooser.showOpenDialog(this)
            if (originFileChooser.selectedFile == null)
                originPathField.text = "Error! Please, choose file again"
            else
                originPathField.text = originFileChooser.selectedFile.path
        }

        chooseRedactedFileButton.addActionListener {
            redactedFileChooser.showOpenDialog(this)
            if (redactedFileChooser.selectedFile == null)
                redactedPathField.text = "Error! Please, choose file again"
            else
                redactedPathField.text = redactedFileChooser.selectedFile.path
        }
        //3..2..1..Запуск!
        showStartFrame()
    }
}