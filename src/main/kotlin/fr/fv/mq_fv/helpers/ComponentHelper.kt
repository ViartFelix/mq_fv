package fr.fv.mq_fv.helpers

import fr.fv.mq_fv.exceptions.ComponentHelperException
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor

class ComponentHelper {

    fun buildTextMessage(components: ArrayList<Component>): Component
    {
       return this.handleBuildTextMessage(components, false)
    }

    fun buildTextMessageLn(components: ArrayList<Component>): Component
    {
        return this.handleBuildTextMessage(components, true)
    }

    /**
     * Handles the building of the Text Component
     */
    fun handleBuildTextMessage(components: ArrayList<Component>, appendNewLine: Boolean): TextComponent
    {
        if(components.isEmpty()) throw ComponentHelperException("The array of components to append is empty.")

        //init the final text
        var finalComponent = Component.text("")

        components.forEachIndexed { index, component ->
            finalComponent = finalComponent.append(component)

            if(appendNewLine && index != components.lastIndex)  {
                finalComponent = finalComponent.appendNewline() as TextComponent
            }
        }

        return finalComponent;
    }

    /**
     * Returns a text component with alternated colors
     */
    fun repeatingTextColors(character: Char, length: Int = 15, colors: Array<TextColor>): TextComponent
    {
        return this.handleRepeatingColor(character.toString(), length, colors)
    }

    /**
     * Returns a text component with alternated colors
     */
    fun repeatingTextColors(text: String, length: Int = 15, colors: Array<TextColor>): TextComponent
    {
        return this.handleRepeatingColor(text, length, colors)
    }

    /**
     * Handler for methods "repeatingTextColors"
     * @see ComponentHelper.repeatingTextColors
     */
    private fun handleRepeatingColor(text: String, length: Int, colors: Array<TextColor>): TextComponent
    {
        if(length <= 0) throw ComponentHelperException("Length of the repeating character is less than 1.")
        if(colors.isEmpty()) throw ComponentHelperException("The array of colors to repeat a string is empty.")

        //init the component (with an empty string)
        var finalComponent: TextComponent = Component.text("")

        for (i in 0 until length) {
            val currentColor = colors[i % colors.size]

            val currentComponent = Component
                .text(text)
                .color(currentColor)

            finalComponent = finalComponent.append(currentComponent)
        }

        return finalComponent;
    }


}