package m09.uf1.jancandalija.pluginjan

import com.intellij.openapi.project.Project
import java.awt.FlowLayout

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


        // ESPAI EN BLANC

        val espaiBlancPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        espaiBlancPanel.add(JLabel(""))


        // APARTAT: Desinstal·lar APP

        val desinstalarAppPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val desinstalarPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val botoDesinstalarApp = JButton("INICIA")

        botoDesinstalarApp.addActionListener { execDesinstalarApp() }

        desinstalarPanel.add(botoDesinstalarApp)

        desinstalarAppPanel.add(desinstalarPanel)
        desinstalarAppPanel.add(JLabel("Desinstal·la APP (Neteja l'User Data i Cache)"))


        // APARTAT: Instal·lar APP

        val instalarAppPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val instalarPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val botoInstalarApp = JButton("INICIA")

        botoInstalarApp.addActionListener { execInstalarApp() }

        instalarPanel.add(botoInstalarApp)

        instalarAppPanel.add(instalarPanel)
        instalarAppPanel.add(JLabel("Instal·la APP (Google Play)"))



        // Construir el panell amb cada component

        commandPanel.add(rutaExemplePanel)
        commandPanel.add(pathPanel)
        commandPanel.add(espaiBlancPanel)
        commandPanel.add(desinstalarAppPanel)
        commandPanel.add(instalarAppPanel)

        return commandPanel
    }

    private fun execDesinstalarApp() {

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

    private fun execInstalarApp() {

        if (!adbPath.isNullOrEmpty()) {

            val thread = Thread {
                val command1 = "$adbPath shell am start -a android.intent.action.VIEW -d \"https://play.google.com/store\""
                val command3 = "$adbPath shell input tap 700 1700"
                val command5 = "$adbPath shell input tap 300 100"
                val command7 = "$adbPath shell input text \"escio\""
                val command9 = "$adbPath shell input tap 1000 1700"
                val command11 = "$adbPath shell input tap 950 1000"

                executeCommand(command1)
                Thread.sleep(4200)
                executeCommand(command3)
                Thread.sleep(500)
                executeCommand(command5)
                Thread.sleep(500)
                executeCommand(command7)
                Thread.sleep(500)
                executeCommand(command9)
                Thread.sleep(700)
                executeCommand(command11)
            }.start()
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