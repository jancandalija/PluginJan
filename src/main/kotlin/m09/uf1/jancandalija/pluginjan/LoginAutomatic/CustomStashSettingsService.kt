package m09.uf1.jancandalija.pluginjan.LoginAutomatic

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "ConfiguracioCustomStash",
    storages = [Storage("ConfiguracioCustomStash.xml")]
)
class CustomStashSettingsService : PersistentStateComponent<StashConfiguracio> {
    private var config = StashConfiguracio()

    override fun getState(): StashConfiguracio = config
    override fun loadState(state: StashConfiguracio) {
        config = state
    }

    companion object {
        fun getInstance(): CustomStashSettingsService = ServiceManager.getService(CustomStashSettingsService::class.java)
    }
}