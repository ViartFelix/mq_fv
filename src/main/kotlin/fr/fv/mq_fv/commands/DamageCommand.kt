package fr.fv.mq_fv.commands

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import fr.fv.mq_fv.protocolLib.FakeHurtAnimation
import fr.fv.mq_fv.utils.FloatingText
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.roundToInt

/**
 * Test command: applies a fixed amount of damage to a player through the
 * same custom damage pipeline as a real hit (armor reduction, death, etc.).
 * Forces an immediate respawn if the damage killed the player.
 */
class DamageCommand : BasicCommand {

    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        val sender = stack.sender

        val target: Player = args.getOrNull(0)?.let { Bukkit.getPlayer(it) } ?: run {
            sender.sendMessage("Usage: /dmg <player> <damage> [critical]")
            return
        }

        val damage = args.getOrNull(1)?.toDoubleOrNull() ?: run {
            sender.sendMessage("Usage: /dmg <player> <damage> [critical]")
            return
        }

        val isCritical = args.getOrNull(2)?.toBooleanStrictOrNull() ?: false

        val targetHandler = AllPlayersHandlerHolder.instance.getPlayerHandler(target) ?: run {
            sender.sendMessage("No PlayerHandler for '${target.name}'.")
            return
        }

        targetHandler.requestDamageToThisPlayer(
            DamageCalculationResult(damage = damage, isCritical = isCritical)
        )

        this.requestHurtAnimationToDamagee(target)
        this.displayDamageFloatingText(damage, isCritical, target.location)

        val origin = sender as? Player
        if (origin != null && origin.uniqueId != target.uniqueId) {
            this.applyKnockBackToDamagee(target, origin)
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
