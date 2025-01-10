package fr.fv.mq_fv.runnable

import fr.fv.mq_fv.events.TabRefreshRequestEvent
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

/**
 * Runnable of the tab refresh runnable
 */
class TabRefreshRunnable: BukkitRunnable() {

    override fun run() {
        //if any player is on the server then call the event

        if( !Bukkit.getOnlinePlayers().isEmpty() ) {
            val tabRefreshRequestEvent = TabRefreshRequestEvent()
            Bukkit.getPluginManager().callEvent(tabRefreshRequestEvent)
        }
    }
}