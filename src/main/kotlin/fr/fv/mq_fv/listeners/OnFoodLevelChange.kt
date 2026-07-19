package fr.fv.mq_fv.listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

class OnFoodLevelChange() : Listener {

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if( event.entity is Player ) {
            event.isCancelled = true
            // reset
            event.entity.foodLevel = 20
        }
    }
}