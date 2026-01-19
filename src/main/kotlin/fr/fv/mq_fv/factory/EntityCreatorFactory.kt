package fr.fv.mq_fv.factory

import fr.fv.mq_fv.utils.ComponentFactory
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

/**
 * Creates entities and spawn them
 */
class EntityCreatorFactory {

    fun spawnTextDisplayForPlayerHp(
        player: Player, currentHp: Double, location: Location? = null, backgroundColor: Color? = null
    ): TextDisplay {
        val finalTextLocation = location ?: player.location
        val finalBackgroundColor = backgroundColor ?: Color.fromARGB(50,255,255,255)

        val componentFactory = ComponentFactory()

        val textComponent = componentFactory.buildPlayerCurrentHpComponent(currentHp)

        return player.world.spawn(finalTextLocation, TextDisplay::class.java) { entity ->
            entity.text(textComponent)
            entity.billboard = Display.Billboard.VERTICAL
            entity.backgroundColor = finalBackgroundColor
            entity.isSeeThrough = true
            entity.alignment = TextDisplay.TextAlignment.CENTER
        }
    }
}