package m09.uf1.jancandalija.pluginjan.LoginAutomatic

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.changes.ChangeListManager
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitLineHandler
import git4idea.repo.GitRepositoryManager

class CustomStashGroupAction : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project = e?.project ?: return emptyArray()
        val entries = CustomStashSettingsService.getInstance().state.data

        return entries.map { entry ->
            object : AnAction(entry.nom) {
                override fun actionPerformed(e: AnActionEvent) {
                    applyStashToChangeList(project, entry.nom, entry.refStash)
                }
            }
        }.toTypedArray()
    }

    fun applyStashToChangeList(project: Project, changelistName: String, stashRef: String) {
        val repository = GitRepositoryManager.getInstance(project).repositories.firstOrNull() ?: return

        val handler = GitLineHandler(project, repository.root, GitCommand.STASH)
        handler.addParameters("apply", stashRef)

        val result = Git.getInstance().runCommand(handler)

        if (result.success()) {
            val changeListManager = ChangeListManager.getInstance(project)
            val newList = changeListManager.addChangeList("⚠️ $changelistName", "Aplicado: $stashRef")

            ApplicationManager.getApplication().invokeLater {
                ApplicationManager.getApplication().runWriteAction {
                    val allChanges = changeListManager.allChanges
                    changeListManager.moveChangesTo(newList, *allChanges.toTypedArray())
                }
            }

            Messages.showInfoMessage(project, "Stash $stashRef aplicado correctamente", "Éxito")
        } else {
            Messages.showErrorDialog(project, "Error aplicando $stashRef:\n${result.errorOutputAsJoinedString}", "Error")
        }
    }
}