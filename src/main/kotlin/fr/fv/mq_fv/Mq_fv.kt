package fr.fv.mq_fv

import fr.fv.mq_fv.listeners.DmgEvent
import fr.fv.mq_fv.listeners.OnPlayerJoin
import fr.fv.mq_fv.listeners.OnTabRefreshRequest
import fr.fv.mq_fv.runnable.TabRefreshRunnable
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import fr.fv.mq_fv.utils.ConfigurationsHolder
import fr.fv.mq_fv.utils.DatabaseWrapper
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class Mq_fv : JavaPlugin() {

    companion object {
        lateinit var instance: Mq_fv
            private set
    }

    lateinit var scheduler: BukkitScheduler
        private set

    override fun onEnable() {
        instance = this

        this.loadClasses()
        this.loadConfigFiles()
        this.initDbConnection()
        this.registerEvents()
        this.registerRunners()
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
        server.pluginManager.registerEvents(OnTabRefreshRequest(), this)
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
     * Loads necessary classes and singletons.
     */
    private fun loadClasses()
    {
        this.scheduler = this.server.scheduler

        Class.forName("org.postgresql.Driver")

        AllPlayersHandlerHolder.instance
    }

    /**
     * Registers the runners of the plugin
     */
    private fun registerRunners()
    {
        //tab refresh
        TabRefreshRunnable().runTaskTimer(this, 20L, 60L)
    }
}
