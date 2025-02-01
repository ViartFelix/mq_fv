package fr.fv.mq_fv.utils

import fr.fv.mq_fv.helpers.ComponentHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import java.math.BigDecimal
import org.bukkit.entity.Player as mcPlayer

class ComponentFactory {

    /** Component helper */
    val componentHelper: ComponentHelper = ComponentHelper()

    /**
     * Return the component to display a welcome message to a new player
     */
    fun getNewPlayerMessage(name: String): TextComponent
    {
        val lineColors: Array<TextColor> = arrayOf(
            TextColor.color(105,112,244),
            TextColor.color(244,179,105)
        )

        //all components to append
        val allComponents: ArrayList<Component> = ArrayList()

        val line = componentHelper.repeatingTextColors("=", 25, lineColors)
        val playerNameLine = Component.text(" Welcome, $name ! ")

        allComponents.add(line)
        allComponents.add(playerNameLine)
        allComponents.add(line)

        return componentHelper.buildTextMessageLn(allComponents) as TextComponent
    }

    /**
     * Builds a component for a player's header tab
     */
    fun buildPlayerTabHeader(player: mcPlayer, serverName: String): Component
    {
        val separatorLength = 15

        //all lines of the header tab
        val headerTabLines: ArrayList<Component> = ArrayList()

        val leftRightSeparatorColors = arrayOf(TextColor.color(105,112,244), TextColor.color(244,179,105))
        val leftRightSeparator = componentHelper.repeatingTextColors('=', separatorLength, leftRightSeparatorColors)

        //server line
        val lineOne = Component.text(" ")
            .append(leftRightSeparator)
            .append(componentHelper.repeatingSpaces(2))
            .append(this.getTabServerNameComponent(serverName))
            .append(componentHelper.repeatingSpaces(2))
            .append(leftRightSeparator)
            .append(Component.text(" "))

        //player line
        val playerLine = Component.text("")
            .append(this.getTabPlayerNameComponent(player.name))
            .append(Component.text(" - "))
            .append(this.getTabPlayerPingComponent(player.ping))

        //assemble all lines !
        headerTabLines.add(Component.text(""))
        headerTabLines.add(lineOne)
        headerTabLines.add(Component.text(""))
        headerTabLines.add(playerLine)
        headerTabLines.add(Component.text(""))

        return componentHelper.buildTextMessageLn(headerTabLines)
    }

    /**
     * Returns the server name for the tab
     */
    fun getTabServerNameComponent(serverName: String): TextComponent
    {
        return Component
            .text("")
            .append(Component.text(serverName).decoration(TextDecoration.BOLD, true))
    }

    /**
     * Returns a text component for the player name in the tab
     */
    fun getTabPlayerNameComponent(playerName: String): TextComponent
    {
        return Component
            .text("")
            .append(Component.text(playerName).decoration(TextDecoration.ITALIC, true))
    }

    fun getTabPlayerPingComponent(ping: Int): TextComponent
    {
        val pingColor: TextColor = when {
            ping <= 50 -> TextColor.color(87, 224, 80)
            ping <= 150 -> TextColor.color(232, 190, 58)
            ping <= 300 -> TextColor.color(232, 136, 58)
            else -> TextColor.color(239, 52, 52)
        }

        return Component
            .text("")
            .append(
                Component.text(ping).color(pingColor)
            )
            .append(Component.text(" ms"))
    }

    /**
     * Builds a component for players' money in the tab
     */
    fun buildTabMoneyComponent(money: BigDecimal): TextComponent
    {
        return Component.text()
            //money icon
            .append(Component.text().content("$").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
            .append(Component.text(" "))

            //money
            .append(Component.text().content(
                money.toString()
            ).color(NamedTextColor.GOLD))
            .build()
    }

    /**
     * Returns the money title for the tab list
     */
    fun buildTabMoneyTitleComponent(moneyTitle: String): TextComponent
    {
        return Component.text()
            .append(Component.text().content(moneyTitle).color(NamedTextColor.GOLD))
            .build()
    }


}