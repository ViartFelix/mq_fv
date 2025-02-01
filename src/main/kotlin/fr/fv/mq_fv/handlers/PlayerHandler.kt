package fr.fv.mq_fv.handlers

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import fr.fv.mq_fv.exceptions.DatabaseException
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.helpers.PotionEffectsHelper
import fr.fv.mq_fv.tabList.TabListHandler
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

    /** Component factory */
    val componentFactory: ComponentFactory = ComponentFactory()

    /** ProtocolLib manager */
    val manager: ProtocolManager = ProtocolLibrary.getProtocolManager()

    /** Tab list manager for that player */
    val tabList: TabListHandler = TabListHandler()

    init {
        //fetch the player
        val fetchedPlayer = playerRepository.getPlayer(this.mcPlayer)
            ?: throw DatabaseException("Player '${mcPlayer.name}' (uuid: ${mcPlayer.uniqueId}) trying to be fetched from the DB is not registered")

        this.playerEntity = fetchedPlayer

        this.applyPotionEffects()

        this.tabList.initTabList()
        this.tabList.updateRightInfoTab(this.playerEntity)
        this.tabList.sendAllPackets(this.mcPlayer)
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

        //header
        mcPlayer.sendPlayerListHeader(
            componentFactory.buildPlayerTabHeader(this.mcPlayer, configServerName)
        )

        this.tabList.updateRightInfoTab(this.playerEntity)
        this.tabList.sendAllPackets(this.mcPlayer)
    }
}