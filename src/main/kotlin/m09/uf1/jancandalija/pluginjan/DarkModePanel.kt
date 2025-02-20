package m09.uf1.jancandalija.pluginjan

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.components.JBScrollPane
import javax.swing.BoxLayout
import java.awt.FlowLayout
import javax.swing.JPanel
import javax.swing.JLabel

import com.intellij.ui.components.JBLabel
import com.intellij.openapi.ui.Messages
import javax.swing.JButton
import com.android.sdklib.devices.Device
import com.android.tools.idea.sdk.AndroidSdks

import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.android.ddmlib.AdbCommandRejectedException
import com.android.ddmlib.TimeoutException
import org.jetbrains.android.sdk.AndroidSdkUtils


object DarkModePanel {
    fun addDarkModePanel(project: Project): JPanel {
        val darkModePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        darkModePanel.add(JLabel(" Uninstall "))

        val onButton = JButton("Start")
        onButton.addActionListener { onButtonClick(project, true) }
        darkModePanel.add(onButton)

        return darkModePanel
    }

    private fun onButtonClick(project: Project, show: Boolean) {
        val devices = AndroidSdkUtils.getDebugBridge(project)?.devices

        if (!devices.isNullOrEmpty()) {
            devices.forEach { device ->
                try {
                    if (show) {
                        try {
                            val command1 = "adb shell am force-stop cat.escio.android"
                            val command2 = "C:\\Users\\Jan\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb shell su rm -rf /data/data/cat.escio.android/cache/*"
                            val command3 = "C:\\Users\\Jan\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb shell pm clear cat.escio.android"
                            val command4 = "C:\\Users\\Jan\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb shell pm uninstall cat.escio.android"
                            device.executeShellCommand(command1, null, 0)
                            device.executeShellCommand(command2, null, 0)
                            device.executeShellCommand(command3, null, 0)
                            device.executeShellCommand(command4, null, 0)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}