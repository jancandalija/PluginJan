package m09.uf1.jancandalija.pluginjan

import com.intellij.icons.AllIcons
import com.intellij.icons.AllIcons.Icons
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.base.util.onTextChange
import org.w3c.dom.css.RGBColor
import java.awt.Color
import java.awt.FlowLayout
import java.io.*
import java.util.Properties
import javax.swing.*


object PanellPrincipal {
    private const val CONFIG_FILE_ADB = "config.txt"  // Archivo donde se guardará la ruta de adb
    private const val CONFIG_FILE_ACTIVA_LOGIN_AUTO = "configLoginAuto.txt"  // Archivo donde se guardará la ruta de adb
    private var examplePath: String = "C:\\Users\\Jan\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb"
    val colorBlau = Color(100,100,255)
    val colorGreen = Color.GREEN

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
        val saveButton = JButton() // DESA
        saveButton.icon = AllIcons.Actions.MenuSaveall
        saveButton.addActionListener {
            adbPath = adbPathTextField.text  // Guardar la ruta en la variable
            saveAdbPath(adbPath)  // Guardar la ruta en el archivo
            JOptionPane.showMessageDialog(null, "(OK) Ruta desada")
        }
        pathPanel.add(saveButton)


        // APARTAT: Desinstal·lar APP

        val desinstalarAppPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val desinstalarPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val botoDesinstalarApp = JButton("INICIA")
        botoDesinstalarApp.icon = AllIcons.Debugger.ThreadRunning
        botoDesinstalarApp.foreground = colorGreen

        botoDesinstalarApp.addActionListener { execDesinstalarApp() }

        desinstalarPanel.add(botoDesinstalarApp)

        desinstalarAppPanel.add(desinstalarPanel)
        desinstalarAppPanel.add(JLabel("Desinstal·la APP (Neteja l'User Data i Cache)"))


        // APARTAT: Instal·lar APP

        val instalarAppPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val instalarPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val botoInstalarApp = JButton("INICIA")
        botoInstalarApp.icon = AllIcons.Debugger.ThreadRunning
        botoInstalarApp.foreground = colorGreen

        botoInstalarApp.addActionListener { execInstalarApp() }

        instalarPanel.add(botoInstalarApp)

        instalarAppPanel.add(instalarPanel)
        instalarAppPanel.add(JLabel("Instal·la APP (Google Play)"))

        // Agrupació inici sessió automàtic

        val isToggleOn = loadToggleLoginAutoState()

        // Botó ON OFF Login Window
        val credencialesPanel = JPanel()
        credencialesPanel.layout = BoxLayout(credencialesPanel, BoxLayout.Y_AXIS)
        credencialesPanel.isVisible = isToggleOn

        val grupBotoActivarLoginAutomatic = JPanel(FlowLayout(FlowLayout.LEFT))
        val toggleLoginPanel = JPanel(FlowLayout(FlowLayout.LEFT))

        // Boto INICIA

        val grupBotoIniciaSaveLoginAuto = JPanel(FlowLayout(FlowLayout.LEFT))

        val iniciaLoginAutomatic = JButton("INICIA")
        iniciaLoginAutomatic.foreground = colorGreen
        iniciaLoginAutomatic.icon = AllIcons.Debugger.ThreadRunning
        iniciaLoginAutomatic.addActionListener {

        }

        // Boto DESA

        val saveLoginAutomatic = JButton() //DESA
        saveLoginAutomatic.icon = AllIcons.Actions.MenuSaveall
        saveLoginAutomatic.addActionListener {

        }

        grupBotoIniciaSaveLoginAuto.add(iniciaLoginAutomatic)
        grupBotoIniciaSaveLoginAuto.add(saveLoginAutomatic)

        // Labels Credencials

        val toggleCredenciales = JToggleButton(if (isToggleOn) "ON" else "OFF")
        toggleCredenciales.isSelected = isToggleOn
        toggleCredenciales.foreground = if (isToggleOn) colorBlau else Color.RED
        toggleCredenciales.addActionListener {
            val selected = toggleCredenciales.isSelected
            credencialesPanel.isVisible = selected
            toggleCredenciales.text = if (selected) "ON" else "OFF"
            toggleCredenciales.foreground = if (selected) colorBlau else Color.RED

            saveToggleLoginAutoState(selected)
        }
        toggleLoginPanel.add(toggleCredenciales)

        grupBotoActivarLoginAutomatic.add(toggleLoginPanel)
        grupBotoActivarLoginAutomatic.add(JLabel("Login Automàtic "))

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

        credencialesPanel.add(panellCredencialsEntorn)
        credencialesPanel.add(panellCredencialsDomini)
        credencialesPanel.add(panellCredencialsUsuari)
        credencialesPanel.add(panellCredencialsPassword)
        credencialesPanel.add(grupBotoIniciaSaveLoginAuto)

        // Construir el panell amb cada component

        commandPanel.add(rutaExemplePanel)
        commandPanel.add(pathPanel)
        commandPanel.add(getEspaiEnBlanc())
        commandPanel.add(JSeparator(SwingConstants.HORIZONTAL));
        commandPanel.add(desinstalarAppPanel)
        commandPanel.add(JSeparator(SwingConstants.HORIZONTAL));
        commandPanel.add(instalarAppPanel)
        commandPanel.add(JSeparator(SwingConstants.HORIZONTAL));
        commandPanel.add(grupBotoActivarLoginAutomatic)
        commandPanel.add(credencialesPanel)
        commandPanel.add(JSeparator(SwingConstants.HORIZONTAL));

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
            val file = File(CONFIG_FILE_ADB)
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
            val file = File(CONFIG_FILE_ADB)
            val writer = FileWriter(file)
            writer.write(path)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveToggleLoginAutoState(isOn: Boolean) {
        try {
            val properties = Properties()
            properties.setProperty("toggleState", isOn.toString())

            FileWriter(CONFIG_FILE_ACTIVA_LOGIN_AUTO).use { writer ->
                properties.store(writer, null)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadToggleLoginAutoState(): Boolean {
        val properties = Properties()
        try {
            File(CONFIG_FILE_ACTIVA_LOGIN_AUTO).takeIf { it.exists() }?.let { file ->
                FileReader(file).use { reader ->
                    properties.load(reader)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return properties.getProperty("toggleState", "false").toBoolean()
    }

    private fun getEspaiEnBlanc(): JPanel {
        val espaiBlancPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        espaiBlancPanel.add(JLabel(""))
        return espaiBlancPanel
    }

}