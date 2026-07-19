package fr.fv.mq_fv.runnable

import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.scheduler.BukkitRunnable

class PlayerSeeableInfosRunnable : BukkitRunnable() {
    override fun run() {
        AllPlayersHandlerHolder.instance.allHandlers.forEach {
            it.updatePlayerDisplayedInfos()
        }
    }
}