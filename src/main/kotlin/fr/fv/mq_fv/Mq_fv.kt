package fr.fv.mq_fv

import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import fr.fv.mq_fv.interfaces.EventsRegisterer
import fr.fv.mq_fv.listeners.DmgEvent
import fr.fv.mq_fv.listeners.OnPlayerDisconnect
import fr.fv.mq_fv.listeners.OnPlayerJoin
import fr.fv.mq_fv.listeners.OnTabRefreshRequest
import fr.fv.mq_fv.runnable.TabRefreshRunnable
import fr.fv.mq_fv.utils.ConfigurationsHolder
import fr.fv.mq_fv.utils.DatabaseWrapper
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler


class Mq_fv : JavaPlugin(), EventsRegisterer {

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

    override fun registerEvents()
    {
        server.pluginManager.registerEvents(DmgEvent(), this)
        server.pluginManager.registerEvents(OnPlayerJoin(), this)
        server.pluginManager.registerEvents(OnTabRefreshRequest(), this)
        server.pluginManager.registerEvents(OnPlayerDisconnect(), this)

        //ProtocolLibHelper.instance.registerEvents()
    }

    /**
     * Loads the configuration of the .yaml and .yml files
     */
    private fun loadConfigFiles()
    {
        val configMap = hashMapOf(
            "floating-text" to "floating-text-defaults.yaml",
            "database" to "database.yaml",
            "tab-list" to "tab-list.yaml",
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
        val tabListConfig = ConfigurationsHolder.instance
            .getConfig("tab-list")
            .getConfigurationSection("options")!!

        val configRefreshInterval: Float = tabListConfig
            .getString("refresh")!!
            .toFloat() * 20

        val configDelay: Float = tabListConfig
            .getString("initial_delay")!!
            .toFloat() * 20

        //tab refresh
        TabRefreshRunnable().runTaskTimer(this, configDelay.toLong(), configRefreshInterval.toLong())
    }
}
