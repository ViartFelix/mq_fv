package fr.fv.mq_fv

import fr.fv.mq_fv.utils.ConfigurationsHolder
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Mq_fv : JavaPlugin() {

    companion object {
        lateinit var instance: Mq_fv
            private set
    }

    override fun onEnable() {
        instance = this

        this.registerEvents()

        //get initial yaml config files
        this.loadConfigFiles()
    }

    override fun onDisable() {
    }

    /**
     * Registers the necessary events
     */
    private fun registerEvents()
    {
        server.pluginManager.registerEvents(DmgEvent(), this)
    }

    /**
     * Loads the configuration of the .yaml and .yml files
     */
    private fun loadConfigFiles()
    {
        val configMap = hashMapOf(
            "floating-text" to "floating-text-defaults.yaml"
        )

        ConfigurationsHolder.instance.loadFromMap(configMap)
    }
}
