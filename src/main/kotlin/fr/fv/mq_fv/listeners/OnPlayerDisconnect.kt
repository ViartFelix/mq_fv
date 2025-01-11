package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Removes the player handler when the player disconnects.
 */
class OnPlayerDisconnect: Listener {

    private val allPlayerHandler: AllPlayersHandlerHolder = AllPlayersHandlerHolder.instance

    @EventHandler
    fun onPlayerDisconnects(event: PlayerQuitEvent) {
        val mcPlayer: Player = event.player

        if(allPlayerHandler.hasPlayerHandler(mcPlayer)) {
            allPlayerHandler.removePlayerHandler(mcPlayer)
        }
    }
}