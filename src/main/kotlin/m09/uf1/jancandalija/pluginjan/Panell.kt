package m09.uf1.jancandalija.pluginjan

import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitLineHandler
import git4idea.repo.GitRepositoryManager
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project


class Panell : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel()
        val button = JButton("Aplicar Stash")

        button.addActionListener {
            // Aquí disparas el menú desplegable o acción que quieras
            showStashMenu(project, button)
        }

        panel.add(button, BorderLayout.CENTER)
        val content = toolWindow.contentManager.factory.createContent(panel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun showStashMenu(project: Project, invoker: JButton) {
        val stashOptions = listOf("stash@{0} - UI Mock", "stash@{1} - Backend Pruebas") // TODO: Son dades d'exemple

        val popupStep = object : BaseListPopupStep<String>("Selecciona un Stash", stashOptions) {
            override fun onChosen(selectedValue: String?, finalChoice: Boolean): PopupStep<*>? {
                if (selectedValue != null) {
                    applyStash(project, selectedValue)
                }
                return PopupStep.FINAL_CHOICE
            }
        }

        val popup: ListPopup = JBPopupFactory.getInstance().createListPopup(popupStep)
        popup.showUnderneathOf(invoker)
    }

    private fun applyStash(project: Project, stashRef: String) {
        val git = Git.getInstance()
        val repoManager = GitRepositoryManager.getInstance(project)
        val repo = repoManager.repositories.firstOrNull()

        if (repo == null) {
            println("No se encontró repositorio Git en el proyecto")
            return
        }

        // Ejecutar "git stash apply <stashRef>"
        val handler = GitLineHandler(project, repo.root, GitCommand.STASH)
        handler.addParameters("apply", stashRef)

        val result = git.runCommand(handler)
        if (!result.success()) {
            println("Error aplicando stash: ${result.errorOutputAsJoinedString}")
            return
        }

        val changeListManager = ChangeListManager.getInstance(project)

        // Crear o buscar changelist temporal
        val changelistName = "_TEMP_${stashRef.replace(Regex("[^a-zA-Z0-9_]"), "_")}"
        var changelist: LocalChangeList? = changeListManager.findChangeList(changelistName)
        if (changelist == null) {
            changelist = changeListManager.addChangeList(changelistName, "Cambios temporales para $stashRef")
        }

        // Mover todos los cambios actuales (de la changelist por defecto) a la changelist temporal
        val defaultList = changeListManager.defaultChangeList
        val changes = defaultList.changes.toTypedArray()

        if (changes.isNotEmpty()) {
            changeListManager.moveChangesTo(changelist, *changes)
        }
    }

//    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
//
//        val contentPanel = JPanel(VerticalFlowLayout(FlowLayout.LEFT))
//        contentPanel.add(PanellGenerarClasseBBDDJava.crearInterfaz())
//
//        val scrollPanel = JBScrollPane(contentPanel)
//        scrollPanel.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
//        scrollPanel.horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
//
//        val contentFactory = ContentFactory.getInstance()
//        val windowContent = contentFactory.createContent(scrollPanel, "", false)
//        toolWindow.contentManager.addContent(windowContent)
//    }

//    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
//
//        val contentPanel = JPanel(VerticalFlowLayout(FlowLayout.LEFT))
//        contentPanel.add(PanellGenerarClasseBBDDJava.crearInterfaz())
//
//        val scrollPanel = JBScrollPane(contentPanel)
//        scrollPanel.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
//        scrollPanel.horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
//
//        val contentFactory = ContentFactory.getInstance()
//        val windowContent = contentFactory.createContent(scrollPanel, "", false)
//        toolWindow.contentManager.addContent(windowContent)
//    }
}