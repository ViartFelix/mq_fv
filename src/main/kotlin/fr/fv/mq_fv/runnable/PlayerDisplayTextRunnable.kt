package fr.fv.mq_fv.runnable

import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.scheduler.BukkitRunnable

/**
 * Runnable for the update of the nametags of the players
 */
class PlayerDisplayTextRunnable : BukkitRunnable() {
    override fun run() {
        val allPlayerHandlers = AllPlayersHandlerHolder.instance.allHandlers

        allPlayerHandlers.forEach {
            it.nametagHolder.updateNametags(it.playerStats.currentHp)
        }
    }
}