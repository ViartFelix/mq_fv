package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityRegainHealthEvent

class OnRegainHealth() : Listener {

    @EventHandler
    fun onRegainHealth(event: EntityRegainHealthEvent) {
        if( event.entity is Player ) {
            event.isCancelled = true

            val playerHandler = AllPlayersHandlerHolder.instance.getPlayerHandler(event.entity as Player)!!
            playerHandler.updatePlayerHealthBar()
        }
    }
}