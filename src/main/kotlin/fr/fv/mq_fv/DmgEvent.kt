package fr.fv.mq_fv

import fr.fv.mq_fv.utils.FloatingText
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class DmgEvent: Listener {

    @EventHandler
    public fun onDamageEvent(event: EntityDamageEvent) {
        println("Something damaged !")

        val floatingText: FloatingText = FloatingText(event.damage.toString(), event.entity.location)
        floatingText.display()
    }
}