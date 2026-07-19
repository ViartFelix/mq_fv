package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.protocolLib.FakeHurtAnimation
import fr.fv.mq_fv.utils.FloatingText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Listener
import kotlin.math.roundToInt

/**
 * Shared behaviour for listeners that react to entity damage: the floating damage text,
 * the fake hurt animation packet and the knock back.
 */
abstract class AbstractDamageHandler : Listener {

    /**
     * Displays a floating text for the damage
     */
    protected fun displayDamageFloatingText(damage: Double, isCritical: Boolean, location: Location)
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
    protected fun applyKnockBackToDamagee(
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
    protected fun requestHurtAnimationToDamagee(target: LivingEntity) {
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
