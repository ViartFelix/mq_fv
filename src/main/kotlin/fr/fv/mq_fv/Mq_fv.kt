package fr.fv.mq_fv

import fr.fv.mq_fv.commands.BoomCommand
import fr.fv.mq_fv.commands.ChatCommand
import fr.fv.mq_fv.commands.TabCommand
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import fr.fv.mq_fv.interfaces.EventsRegisterer
import fr.fv.mq_fv.listeners.DmgEvent
import fr.fv.mq_fv.listeners.OnPlayerDisconnect
import fr.fv.mq_fv.listeners.OnPlayerJoin
import fr.fv.mq_fv.listeners.OnTabRefreshRequest
import fr.fv.mq_fv.runnable.TabRefreshRunnable
import fr.fv.mq_fv.utils.ConfigurationsHolder
import fr.fv.mq_fv.utils.DatabaseWrapper
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import kotlin.random.Random

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
        this.registerCommands()
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
     * Registers the necessary custom commands to the server
     */
    private fun registerCommands()
    {
        val lifecycleEventManager: LifecycleEventManager<Plugin> = this.getLifecycleManager()

        lifecycleEventManager.registerEventHandler<ReloadableRegistrarEvent<Commands?>>(LifecycleEvents.COMMANDS) { event ->
            val commands = event.registrar()

            if (commands != null) {
                commands.register("boom", "AND KABOLOOIE", BoomCommand())
                commands.register("chat", "among us", ChatCommand())
                commands.register("tab", "Adds a fake player to the tab", TabCommand())
            }
        }
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
