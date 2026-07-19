package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

/**
 * Handles the death of the player
 */
class OnPlayerRespawn() : Listener {

    companion object {
        val playerHandler : AllPlayersHandlerHolder = AllPlayersHandlerHolder.instance
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerRespawnEvent) {
        // Funny timing bug within MC.
        Bukkit.getScheduler().runTask(Mq_fv.instance, Runnable {
            playerHandler.getPlayerHandler(event.player)!!.handleWhenPlayerDie()
        })
    }
}