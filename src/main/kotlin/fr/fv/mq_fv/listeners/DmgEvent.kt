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
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.roundToInt

class DmgEvent(): Listener {

    @EventHandler
    fun onDamageEvent(event: EntityDamageByEntityEvent) {
        event.isCancelled = true

        if( EntityType.PLAYER === event.damager.type ) {
            this.handleWhenDamagerIsPlayer(event)
        } else if( event.damager is LivingEntity ) {
            this.handleWhenDamagerIsLivingEntity(event)
        }
    }

    /**
     * Handle the damage when the origin of the damage is a player
     */
    private fun handleWhenDamagerIsPlayer(event: EntityDamageByEntityEvent) {
        val targetDamager = event.damager as Player

        val playerHandlerInstance = AllPlayersHandlerHolder.instance
        val targetPlayerHandler = playerHandlerInstance.getPlayerHandler(targetDamager)!!

        val damageCalculationResult = targetPlayerHandler.playerStats.calculateDamageToInflict()

        // player -> player
        if( EntityType.PLAYER == event.entity.type ) {
            // the target player
            val targetPlayerDamagee = event.entity as Player
            val targetDamageePlayerHandler = playerHandlerInstance.getPlayerHandler(targetPlayerDamagee)!!

            if( !targetDamageePlayerHandler.isPlayerHittable() ) {
                return
            }

            targetDamageePlayerHandler.requestDamageToThisPlayer(damageCalculationResult)
            this.requestHurtAnimationToDamagee(targetPlayerDamagee)
            this.applyKnockBackToDamagee(targetPlayerDamagee, targetDamager)
            targetDamageePlayerHandler.markPlayerAsHit()
        }
        // player -> mob
        else if( event.entity is LivingEntity ) {
            val targetEntity = event.entity as LivingEntity

            if( 0 >= targetEntity.health - damageCalculationResult.damage ) {
                targetEntity.health = 0.0
            } else {
                //apply the damage to the target
                targetEntity.health -= damageCalculationResult.damage
                this.requestHurtAnimationToDamagee(targetEntity)
                this.applyKnockBackToDamagee(targetEntity, targetDamager)
            }
        }

        this.displayDamageFloatingText(
            damageCalculationResult.damage,
            damageCalculationResult.isCritical,
            event.entity.location
        )
    }

    /**
     * Handles the damage event when the damager is a living entity (a mob, **not a player**)
     */
    private fun handleWhenDamagerIsLivingEntity(event: EntityDamageByEntityEvent)
    {
        val targetDamager = event.damager as LivingEntity
        val damagee = event.entity

        // mob -> player
        if( damagee is Player ) {
            // the target player
            val targetPlayerDamagee = event.entity as Player
            val targetDamageePlayerHandler = AllPlayersHandlerHolder.instance.getPlayerHandler(targetPlayerDamagee)!!

            if( !targetDamageePlayerHandler.isPlayerHittable() ) {
                return
            }

            val calculationRequest = DamageCalculationResult(
                damage = event.damage,
                isCritical = event.isCritical
            )

            targetDamageePlayerHandler.requestDamageToThisPlayer(calculationRequest)
            this.displayDamageFloatingText(calculationRequest.damage, calculationRequest.isCritical, damagee.location)
            this.applyKnockBackToDamagee(targetPlayerDamagee, targetDamager)
            this.requestHurtAnimationToDamagee(targetPlayerDamagee)
            targetDamageePlayerHandler.markPlayerAsHit()
        }
        // mob -> mob
        else if( damagee is LivingEntity ) {
            this.displayDamageFloatingText(event.damage, event.isCritical, damagee.location)
            val finalTargetHealth = damagee.health - event.damage

            if( 0 >= finalTargetHealth ) {
                damagee.health = 0.0
            } else {
                damagee.health -= event.damage
                this.applyKnockBackToDamagee(damagee, targetDamager)
                this.requestHurtAnimationToDamagee(damagee)
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
     * Applies a knock back to the damagee target
     */
    private fun applyKnockBackToDamagee(
        target: LivingEntity, origin: LivingEntity, strength: Double = 0.4, yBoost: Double = 0.75
    ) {
        val direction = target.location.toVector()
            .subtract(origin.location.toVector())
            .normalize()

        direction.y = yBoost

        target.velocity = direction.multiply(strength)
    }

    /**
     * Sends a packet to play the animation where an entity is hurt (turns red)
     */
    private fun requestHurtAnimationToDamagee(target: LivingEntity) {
        val animation = FakeHurtAnimation(target, 0.0f)
        animation.buildPacket()

        Bukkit.getOnlinePlayers()
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