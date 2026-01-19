package fr.fv.mq_fv.holder

import fr.fv.mq_fv.factory.EntityCreatorFactory
import fr.fv.mq_fv.utils.ComponentFactory
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

/**
 * Holds various nametags on top of the player
 */
class PlayerNametagHolder {

    /** The HP floating text */
    lateinit var hpTextDisplay: TextDisplay
        private set

    fun initNametagsForPlayer(player: Player)
    {
        hpTextDisplay = EntityCreatorFactory().spawnTextDisplayForPlayerHp(
            player,
            currentHp = 100.0,
            location = calculateLocationForHpText(player)
        )
    }

    fun updateNametags(player: Player, currentHp: Double)
    {
        hpTextDisplay.teleport(calculateLocationForHpText(player))
        hpTextDisplay.text(ComponentFactory().buildPlayerCurrentHpComponent(currentHp))
    }

    private fun calculateLocationForHpText(player: Player): Location = player.location.add(0.0, 2.0, 0.0)
}