package fr.fv.mq_fv

import fr.fv.mq_fv.utils.FloatingText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.roundToInt

class DmgEvent(): Listener {

    @EventHandler
    fun onDamageEvent(event: EntityDamageByEntityEvent) {
        val finalIcon: Char = this.getPrependIcon(event.isCritical)
        val finalTextColor: NamedTextColor = this.getTextColor(event.isCritical)
        val finalBackgroundColor: Color = this.getBackgroundColor(event.isCritical)

        val finalString = "$finalIcon ${event.finalDamage.roundToInt()}"
        val textComponent: Component = Component.text(finalString).color(finalTextColor)

        var floatingTextComponent: FloatingText? = FloatingText(
            event.entity.location,
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

    private fun getPrependIcon(isCritical: Boolean): Char = if(isCritical) '✦' else '⚔'

    private fun getTextColor(isCritical: Boolean): NamedTextColor = if(isCritical) NamedTextColor.RED else NamedTextColor.BLACK

    private fun getBackgroundColor(isCritical: Boolean): Color = if (isCritical) Color.fromARGB(50, 255, 0, 0) else Color.fromARGB(50,255,255,255)
}