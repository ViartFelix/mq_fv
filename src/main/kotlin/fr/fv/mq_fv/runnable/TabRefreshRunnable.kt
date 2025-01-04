package fr.fv.mq_fv.runnable

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.events.TabRefreshRequestEvent
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

/**
 * Runnable of the tab refresh runnable
 */
class TabRefreshRunnable: BukkitRunnable() {

    override fun run() {
        //if any player is on the server

        if( !Bukkit.getOnlinePlayers().isEmpty() ) {
            val tabRefreshRequestEvent = TabRefreshRequestEvent()
            Bukkit.getPluginManager().callEvent(tabRefreshRequestEvent)
        }
    }
}