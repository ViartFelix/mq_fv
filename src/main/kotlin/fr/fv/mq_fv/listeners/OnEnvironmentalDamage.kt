package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.damage.DamageScaling
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause

/**
 * Handles player damage whose cause isn't another entity (fall, fire, lava, drowning,
 * starvation, void, etc.), so it's still routed through the custom stats system
 * instead of applying straight to vanilla health.
 */
class OnEnvironmentalDamage(): AbstractDamageHandler() {

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        // damage caused by another entity/projectile is already handled by DmgEvent
        if( event is EntityDamageByEntityEvent ) {
            return
        }

        val target = event.entity as? Player ?: return

        event.isCancelled = true

        val targetPlayerHandler = AllPlayersHandlerHolder.instance.getPlayerHandler(target)!!

        val damageCalculationResult = DamageCalculationResult(damage = event.damage, isCritical = false)

        targetPlayerHandler.requestDamageToThisPlayer(damageCalculationResult)
        this.displayDamageFloatingText(damageCalculationResult.damage, damageCalculationResult.isCritical, target.location)

        if( event.entity is LivingEntity ) {
            this.requestHurtAnimationToDamagee(event.entity as LivingEntity)
        }
    }
}
