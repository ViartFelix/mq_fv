package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class DmgEvent(): AbstractDamageHandler() {

    @EventHandler
    fun onDamageEvent(event: EntityDamageByEntityEvent) {
        event.isCancelled = true

        when( event.damager ) {
            is Player -> this.handleWhenDamagerIsPlayer(event)
            is Projectile -> this.handleWhenDamagerIsProjectile(event)
            is LivingEntity -> this.handleWhenDamagerIsLivingEntity(event)
            else -> {}
        }
    }

    /**
     * Handle the damage when the origin of the damage is a player
     */
    private fun handleWhenDamagerIsPlayer(event: EntityDamageByEntityEvent) {
        val targetDamager = event.damager as Player

        this.applyPlayerDamageToTarget(targetDamager, event.entity, targetDamager.location)
    }

    /**
     * Handles the damage event when the damager is a living entity (a mob, **not a player**)
     */
    private fun handleWhenDamagerIsLivingEntity(event: EntityDamageByEntityEvent)
    {
        val targetDamager = event.damager as LivingEntity

        this.applyLivingEntityDamageToTarget(
            event.entity, event.damage, event.isCritical, targetDamager.location
        )
    }

    /**
     * Handles the damage event when the damager is a projectile (arrow, trident, etc.).
     * The actual origin of the damage is the projectile's shooter.
     */
    private fun handleWhenDamagerIsProjectile(event: EntityDamageByEntityEvent)
    {
        val projectile = event.damager as Projectile

        // knock back from the projectile's impact point, not the (possibly far away) shooter
        when( val shooter = projectile.shooter ) {
            is Player -> this.applyPlayerDamageToTarget(shooter, event.entity, projectile.location)
            is LivingEntity -> this.applyLivingEntityDamageToTarget(
                event.entity, event.damage, event.isCritical, projectile.location
            )
            else -> {}
        }
    }

    /**
     * Applies the damage from a player (whether by melee or by projectile) to the target entity
     */
    private fun applyPlayerDamageToTarget(attacker: Player, target: Entity, knockBackOrigin: Location)
    {
        val attackerHandler = AllPlayersHandlerHolder.instance.getPlayerHandler(attacker)!!

        // player -> player
        if( target is Player ) {
            val targetPlayerHandler = AllPlayersHandlerHolder.instance.getPlayerHandler(target)!!

            if( !targetPlayerHandler.isPlayerHittable() ) {
                return
            }

            val damageCalculationResult = attackerHandler.playerStats.calculateDamageToInflict()

            targetPlayerHandler.requestDamageToThisPlayer(damageCalculationResult)
            this.requestHurtAnimationToDamagee(target)
            this.applyKnockBackToDamagee(target, knockBackOrigin)
            targetPlayerHandler.markPlayerAsHit()
            this.displayDamageFloatingText(damageCalculationResult.damage, damageCalculationResult.isCritical, target.location)
            targetPlayerHandler.updatePlayerDisplayedInfos()
        }
        // player -> mob
        else if( target is LivingEntity ) {
            val damageCalculationResult = attackerHandler.playerStats.calculateDamageToInflict()

            if( 0 >= target.health - damageCalculationResult.damage ) {
                target.health = 0.0
            } else {
                target.health -= damageCalculationResult.damage
                this.requestHurtAnimationToDamagee(target)
                this.applyKnockBackToDamagee(target, knockBackOrigin)
            }

            this.displayDamageFloatingText(damageCalculationResult.damage, damageCalculationResult.isCritical, target.location)
        }
    }

    /**
     * Applies the damage from a living entity, ie. a mob, (whether by melee or by projectile) to the target entity
     */
    private fun applyLivingEntityDamageToTarget(
        target: Entity, damage: Double, isCritical: Boolean, knockBackOrigin: Location
    ) {
        // mob -> player
        if( target is Player ) {
            val targetPlayerHandler = AllPlayersHandlerHolder.instance.getPlayerHandler(target)!!

            if( !targetPlayerHandler.isPlayerHittable() ) {
                return
            }

            val calculationRequest = DamageCalculationResult(damage = damage, isCritical = isCritical)

            targetPlayerHandler.requestDamageToThisPlayer(calculationRequest)
            this.displayDamageFloatingText(calculationRequest.damage, calculationRequest.isCritical, target.location)
            this.applyKnockBackToDamagee(target, knockBackOrigin)
            this.requestHurtAnimationToDamagee(target)
            targetPlayerHandler.markPlayerAsHit()
            targetPlayerHandler.updatePlayerDisplayedInfos()
        }
        // mob -> mob
        else if( target is LivingEntity ) {
            this.displayDamageFloatingText(damage, isCritical, target.location)

            if( 0 >= target.health - damage ) {
                target.health = 0.0
            } else {
                target.health -= damage
                this.applyKnockBackToDamagee(target, knockBackOrigin)
                this.requestHurtAnimationToDamagee(target)
            }
        }
    }
}
