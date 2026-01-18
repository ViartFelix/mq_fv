package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
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
        }
    }

    /**
     * Handle the damage when the origin of the damage is a player
     */
    private fun handleWhenDamagerIsPlayer(event: EntityDamageByEntityEvent) {
        val targetDamager = event.damager as Player

        val playerHandlerInstance = AllPlayersHandlerHolder.instance
        val targetPlayerHandler = playerHandlerInstance.getPlayerHandler(targetDamager)!!

        val damageCalculationResult = targetPlayerHandler.playerStats.calculateDamage()

        // if damagee is another player
        if( EntityType.PLAYER == event.entity.type ) {
            // the target player
            val targetDamageePlayerHandler = playerHandlerInstance.getPlayerHandler(event.entity as Player)!!
            targetDamageePlayerHandler.requestDamageToThisPlayer(damageCalculationResult)
        } else if( event.entity is LivingEntity ) {
            val targetEntity = event.entity as LivingEntity

            if( 0 >= targetEntity.health - damageCalculationResult.damage ) {
                targetEntity.health = 0.0
            } else {
                //apply the damage to the target
                targetEntity.health -= damageCalculationResult.damage
            }
        }

        this.displayDamageFloatingText(
            damageCalculationResult.damage,
            damageCalculationResult.isCritical,
            event.entity.location
        )
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