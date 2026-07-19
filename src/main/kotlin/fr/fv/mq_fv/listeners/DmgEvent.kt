package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import fr.fv.mq_fv.protocolLib.FakeHurtAnimation
import fr.fv.mq_fv.utils.FloatingText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.roundToInt

class DmgEvent(): Listener {

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

    /**
     * Displays a floating text for the damage
     */
    private fun displayDamageFloatingText(damage: Double, isCritical: Boolean, location: Location)
    {
        val finalIcon: Char = this.getPrependIcon(isCritical)
        val finalTextColor: NamedTextColor = this.getTextColor(isCritical)
        val finalBackgroundColor: Color = this.getBackgroundColor(isCritical)

        val finalString = "$finalIcon ${damage.roundToInt()}"
        val textComponent: Component = Component.text(finalString).color(finalTextColor)

        var floatingTextComponent: FloatingText? = FloatingText(
            location,
            textComponent,
            finalBackgroundColor,
            1f,
        ).display()

        //clear the text after
        Bukkit.getScheduler().runTaskLater(Mq_fv.instance, Runnable {
            floatingTextComponent?.destroy()
            floatingTextComponent = null
        }, 30L)
    }

    /**
     * Applies a knock back to the damagee target, away from the given origin location
     */
    private fun applyKnockBackToDamagee(
        target: LivingEntity, originLocation: Location, strength: Double = 0.4, yBoost: Double = 0.75
    ) {
        val direction = target.location.toVector()
            .subtract(originLocation.toVector())
            .normalize()

        direction.y = yBoost

        target.velocity = direction.multiply(strength)
    }

    /**
     * Sends a packet to play the animation where an entity is hurt (turns red).
     * Only sent to players who actually have the entity loaded client-side.
     */
    private fun requestHurtAnimationToDamagee(target: LivingEntity) {
        val animation = FakeHurtAnimation(target, 0.0f)
        animation.buildPacket()

        target.trackedBy
            .forEach {
                animation.sendPacket(it)
            }
    }

    private fun getPrependIcon(isCritical: Boolean): Char = if(isCritical)
        '✦'
        else '⚔'

    private fun getTextColor(isCritical: Boolean): NamedTextColor = if(isCritical)
        NamedTextColor.RED
        else NamedTextColor.BLACK

    private fun getBackgroundColor(isCritical: Boolean): Color = if (isCritical)
        Color.fromARGB(50, 255, 0, 0)
        else Color.fromARGB(50,255,255,255)
}