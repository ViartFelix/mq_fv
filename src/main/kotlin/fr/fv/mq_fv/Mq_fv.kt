package fr.fv.mq_fv

import fr.fv.mq_fv.events.DmgEvent
import fr.fv.mq_fv.events.OnPlayerJoin
import fr.fv.mq_fv.utils.ConfigurationsHolder
import fr.fv.mq_fv.utils.DatabaseWrapper
import org.bukkit.plugin.java.JavaPlugin

class Mq_fv : JavaPlugin() {

    companion object {
        lateinit var instance: Mq_fv
            private set
    }

    override fun onEnable() {
        instance = this

        this.loadClasses()
        this.loadConfigFiles()
        this.initDbConnection()

        this.registerEvents()
    }

    override fun onDisable() {
    }

    /**
     * Registers the necessary events
     */
    private fun registerEvents()
    {
        server.pluginManager.registerEvents(DmgEvent(), this)
        server.pluginManager.registerEvents(OnPlayerJoin(), this)
    }

    /**
     * Loads the configuration of the .yaml and .yml files
     */
    private fun loadConfigFiles()
    {
        val configMap = hashMapOf(
            "floating-text" to "floating-text-defaults.yaml",
            "database" to "database.yaml"
        )

        ConfigurationsHolder.instance.loadFromMap(configMap)
    }

    /**
     * Initiates the Database connection (creates the singleton)
     */
    private fun initDbConnection()
    {
        DatabaseWrapper.instance.testConnection()
    }

    /**
     * Loads classes that doesn't load.
     * I don't know why postgres driver won't load
     */
    private fun loadClasses()
    {
        Class.forName("org.postgresql.Driver")
    }
}
