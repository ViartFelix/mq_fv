package fr.fv.mq_fv

import org.bukkit.plugin.java.JavaPlugin

class Mq_fv : JavaPlugin() {

    override fun onEnable() {
        server.pluginManager.registerEvents(DmgEvent(), this)
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
