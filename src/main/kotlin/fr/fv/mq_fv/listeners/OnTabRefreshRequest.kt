package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.events.TabRefreshRequestEvent
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

/**
 * Event representing when a request of a tab refresh is requested
 */
class OnTabRefreshRequest(): Listener {

    @EventHandler
    fun onTabRefreshRequest(event: TabRefreshRequestEvent) {
        val allHandlers = AllPlayersHandlerHolder.instance.allHandlers

        allHandlers.forEach { playerHandler ->
            playerHandler.updatePlayerTab()
        }
    }
}