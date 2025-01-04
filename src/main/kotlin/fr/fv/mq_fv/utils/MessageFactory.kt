package fr.fv.mq_fv.utils

import fr.fv.mq_fv.helpers.ComponentHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor

class MessageFactory {

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
        val allComponents: ArrayList<Component> = ArrayList();

        val line = componentHelper.repeatingTextColors("=", 25, lineColors)
        val playerNameLine = Component.text(" Welcome, $name ! ")

        allComponents.add(line)
        allComponents.add(playerNameLine)
        allComponents.add(line)

        return componentHelper.buildTextMessageLn(allComponents) as TextComponent
    }


}