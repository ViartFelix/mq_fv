package fr.fv.mq_fv

import org.bukkit.Bukkit
import org.bukkit.entity.Display
import org.bukkit.entity.TextDisplay
import org.bukkit.plugin.java.JavaPlugin

class Mq_fv : JavaPlugin() {

    companion object {
        lateinit var instance: Mq_fv
            private set
    }

    override fun onEnable() {
        instance = this
        server.pluginManager.registerEvents(DmgEvent(), this)
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
