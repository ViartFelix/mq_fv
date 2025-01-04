package fr.fv.mq_fv

import fr.fv.mq_fv.exceptions.DatabaseException
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.utils.PotionEffectsHelper
import org.bukkit.entity.Player

/**
 * Class to handle the player's actions and other things
 */
class PlayerHandler (
    mcPlayer: Player
) {
    /** Minecraft's Player instance */
    var mcPlayer: Player = mcPlayer
        private set

    /** Player entity of the DB */
    var playerEntity: PlayerTable
        private set

    /** Player DB repository */
    var playerRepository: PlayerRepository = PlayerRepository()
        private set

    /** Potion effect factory */
    val potionEffectsHelper: PotionEffectsHelper = PotionEffectsHelper()

    init {
        //fetch the player
        val fetchedPlayer = playerRepository.getPlayer(this.mcPlayer)
            ?: throw DatabaseException("Player '${mcPlayer.name}' (uuid: ${mcPlayer.uniqueId}) trying to be fetched from the DB is not registered")

        this.playerEntity = fetchedPlayer

        this.applyPotionEffects()
    }

    /**
     * Displays the necessary potion effects to the player once logged in
     */
    private fun applyPotionEffects()
    {
        val player = this.mcPlayer

        player.addPotionEffect(potionEffectsHelper.getInfiniteNightVision())
    }
}