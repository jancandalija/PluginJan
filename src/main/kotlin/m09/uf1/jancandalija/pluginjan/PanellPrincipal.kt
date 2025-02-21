package m09.uf1.jancandalija.pluginjan

import com.intellij.openapi.project.Project
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.io.*
import javax.swing.*


object PanellPrincipal {
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

        val espaiBlancPanel = getEspaiEnBlanc()


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

        // Agrupació inici sessió automàtic

        val credencialesPanel = JPanel()
        credencialesPanel.layout = BoxLayout(credencialesPanel, BoxLayout.Y_AXIS)
        credencialesPanel.isVisible = false

        val toggleCredenciales = JToggleButton("Mostrar credenciales")
        toggleCredenciales.addActionListener { e: ActionEvent? ->
            credencialesPanel.setVisible(toggleCredenciales.isSelected)
            toggleCredenciales.text =
                if (toggleCredenciales.isSelected) "Ocultar credenciales" else "Mostrar credenciales"
        }

        // APARTAT: Inici sessió automàtic

        val panellCredencialsEntorn = JPanel(FlowLayout(FlowLayout.LEFT))
        val panellCredencialsDomini = JPanel(FlowLayout(FlowLayout.LEFT))
        val panellCredencialsUsuari = JPanel(FlowLayout(FlowLayout.LEFT))
        val panellCredencialsPassword = JPanel(FlowLayout(FlowLayout.LEFT))

        val edtEntorn = JTextField("", 15)
        val edtDomini = JTextField("", 15)
        val edtUsuari = JTextField("", 15)
        val edtPassword = JPasswordField("", 18)

        panellCredencialsEntorn.add(JLabel("Entorn: "))
        panellCredencialsEntorn.add(edtEntorn)
        panellCredencialsDomini.add(JLabel("Domini: "))
        panellCredencialsDomini.add(edtDomini)
        panellCredencialsUsuari.add(JLabel("Usuari: "))
        panellCredencialsUsuari.add(edtUsuari)
        panellCredencialsPassword.add(JLabel("Password: "))
        panellCredencialsPassword.add(edtPassword)

        credencialesPanel.add(panellCredencialsEntorn);
        credencialesPanel.add(panellCredencialsDomini);
        credencialesPanel.add(panellCredencialsUsuari);
        credencialesPanel.add(panellCredencialsPassword);

        // Construir el panell amb cada component

        commandPanel.add(rutaExemplePanel)
        commandPanel.add(pathPanel)
        commandPanel.add(getEspaiEnBlanc())
        commandPanel.add(desinstalarAppPanel)
        commandPanel.add(instalarAppPanel)
        commandPanel.add(getEspaiEnBlanc())
        commandPanel.add(toggleCredenciales)
        commandPanel.add(credencialesPanel)

        return commandPanel
    }

    private fun execDesinstalarApp() {

        if (!adbPath.isNullOrEmpty()) {
            val command1 = "$adbPath shell am force-stop cat.escio.android"
            val command2 = "$adbPath shell su rm -rf /data/data/cat.escio.android/cache/*"
            val command3 = "$adbPath shell pm clear cat.escio.android"
            val command4 = "$adbPath shell pm uninstall cat.escio.android"

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
            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line) // Puedes imprimir la salida de cada comando si lo deseas
            }
            process.waitFor() // Esperar a que termine el proceso
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadAdbPath() {
        try {
            val file = File(CONFIG_FILE)
            if (file.exists()) {
                val reader = BufferedReader(FileReader(file))
                adbPath = reader.readLine()
                reader.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveAdbPath(path: String) {
        try {
            val file = File(CONFIG_FILE)
            val writer = FileWriter(file)
            writer.write(path)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getEspaiEnBlanc(): JPanel {
        val espaiBlancPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        espaiBlancPanel.add(JLabel(""))
        return espaiBlancPanel
    }

}