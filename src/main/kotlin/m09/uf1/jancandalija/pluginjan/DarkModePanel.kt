package m09.uf1.jancandalija.pluginjan

import com.intellij.openapi.project.Project
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.Graphics2D

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import javax.swing.*


object DarkModePanel {
    private const val CONFIG_FILE = "config.txt"  // Archivo donde se guardará la ruta de adb
    private var examplePath: String = "C:\\Users\\Jan\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb"


    // La ruta por defecto si no existe la configuración
    private var adbPath: String = ""

    init {
        // Intentar leer la ruta guardada en el archivo cuando se inicie el plugin
        loadAdbPath()
    }

    fun addPanellPrincipal(project: Project): JPanel {
        val commandPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        commandPanel.layout = BoxLayout(commandPanel, BoxLayout.Y_AXIS)  // Organizar en una columna


        val rutaExemplePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        rutaExemplePanel.add(JLabel("Ruta ADB (Exemple): " + examplePath))
        commandPanel.add(rutaExemplePanel)


        // Panel para organizar la etiqueta y el campo de texto en horizontal

        val pathPanel = JPanel(FlowLayout(FlowLayout.LEFT))

        // Campo de texto para ingresar la ruta a adb
        pathPanel.add(JLabel("Ruta ADB:"))
        val adbPathTextField = JTextField(adbPath.ifEmpty { "Ruta no especificada" }, 15)
        pathPanel.add(adbPathTextField)

        // Botón para guardar la ruta
        val saveButton = JButton("Desa")
        saveButton.addActionListener {
            adbPath = adbPathTextField.text  // Guardar la ruta en la variable
            saveAdbPath(adbPath)  // Guardar la ruta en el archivo
            JOptionPane.showMessageDialog(null, "(OK) Ruta desada")
        }
        pathPanel.add(saveButton)

        // Añadir el panel con la ruta de ADB al panel principal
        commandPanel.add(pathPanel)

        // ESPAI EN BLANC
        val espaiBlancPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        espaiBlancPanel.add(JLabel(""))
        commandPanel.add(espaiBlancPanel)

        val desinstalarAppPanel = JPanel(FlowLayout(FlowLayout.LEFT))

        // Botón para ejecutar los comandos
        val onButton = JButton("INICIA")
        onButton.addActionListener {
            onButtonClick()
        }
        onButton.background = JBColor.BLUE
        onButton.isOpaque = true

        val executeButtonPanel = JPanel(FlowLayout(FlowLayout.LEFT))  // Alineado a la izquierda
        executeButtonPanel.add(onButton)
        desinstalarAppPanel.add(executeButtonPanel)

        // Título del panel

        desinstalarAppPanel.add(JLabel("Desinstal·la APP (Neteja l'User Data i Cache)"))
        commandPanel.add(desinstalarAppPanel)



        return commandPanel
    }

    private fun onButtonClick() {

        if (!adbPath.isNullOrEmpty()) {
            val command1 = "$adbPath shell am force-stop cat.escio.android"
            val command2 = "$adbPath shell su rm -rf /data/data/cat.escio.android/cache/*"
            val command3 = "$adbPath shell pm clear cat.escio.android"
            val command4 = "$adbPath shell pm uninstall cat.escio.android"

            // Ejecutamos los comandos secuenciales
            executeCommand(command1)
            executeCommand(command2)
            executeCommand(command3)
            executeCommand(command4)
        }
    }

    private fun executeCommand(command: String) {
        try {
            // Ejecutar el comando en el sistema
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line) // Puedes imprimir la salida de cada comando si lo deseas
            }
            process.waitFor() // Esperar a que termine el proceso
        } catch (e: Exception) {
            e.printStackTrace()  // Imprimir error si algo falla
        }
    }

    private fun loadAdbPath() {
        // Intentar leer la ruta de adb desde el archivo
        try {
            val file = File(CONFIG_FILE)
            if (file.exists()) {
                val reader = BufferedReader(FileReader(file))
                adbPath = reader.readLine()  // Leer la primera línea que contiene la ruta
                reader.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveAdbPath(path: String) {
        // Guardar la ruta de adb en un archivo
        try {
            val file = File(CONFIG_FILE)
            val writer = FileWriter(file)
            writer.write(path)  // Guardamos la ruta en el archivo
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}