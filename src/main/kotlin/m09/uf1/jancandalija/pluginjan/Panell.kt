package m09.uf1.jancandalija.pluginjan

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.FlowLayout
import javax.swing.JPanel


class Panell : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val contentPanel = JPanel(VerticalFlowLayout(FlowLayout.LEFT))
        contentPanel.add(DarkModePanel.addDarkModePanel(project))

        val scrollPanel = JBScrollPane(contentPanel)
        scrollPanel.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPanel.horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        val contentFactory = ContentFactory.getInstance()
        val windowContent = contentFactory.createContent(scrollPanel, "", false)
        toolWindow.contentManager.addContent(windowContent)
    }
}