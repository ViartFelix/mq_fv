package fr.fv.mq_fv.handlers

import fr.fv.mq_fv.exceptions.DatabaseException
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.helpers.PotionEffectsHelper
import fr.fv.mq_fv.utils.ComponentFactory
import fr.fv.mq_fv.utils.ConfigurationsHolder
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

    val componentFactory: ComponentFactory = ComponentFactory()

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

    /**
     * Updates the player tab for that player
     */
    fun updatePlayerTab()
    {
        val configServerName = ConfigurationsHolder.instance
            .getConfig("tab-list")
            .getString("options.server_name")!!

        mcPlayer.sendPlayerListHeader(
            componentFactory.buildPlayerTabHeader(this.mcPlayer, configServerName)
        )

        //addFakePlayer(mcPlayer, "balls")
    }

    /**
     * Adds a fake player to the scoreboard
     */
    fun addFakePlayer(player: Player, fakeName: String) {

    }
}