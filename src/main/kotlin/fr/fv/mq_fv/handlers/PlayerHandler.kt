package fr.fv.mq_fv.handlers

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.exceptions.DatabaseException
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.helpers.PotionEffectsHelper
import fr.fv.mq_fv.holder.PlayerStatsHolder
import fr.fv.mq_fv.tabList.TabListHandler
import fr.fv.mq_fv.utils.ComponentFactory
import fr.fv.mq_fv.utils.ConfigurationsHolder
import org.bukkit.entity.Player
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

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

    /** Holds the statistics of the current player */
    val playerStats: PlayerStatsHolder = PlayerStatsHolder()

    /** Holds the last hit (to prevent spam attack) */
    var lastHit: TimeSource.Monotonic.ValueTimeMark = TimeSource.Monotonic.markNow()
        private set

    /** Time between hits */
    val timeBetweenHits: Duration = 0.5.seconds

    init {
        //fetch the player
        val fetchedPlayer = playerRepository.getPlayer(this.mcPlayer)
            ?: throw DatabaseException("Player '${mcPlayer.name}' (uuid: ${mcPlayer.uniqueId}) trying to be fetched from the DB is not registered")

        this.playerEntity = fetchedPlayer

        this.applyPotionEffects()

        this.tabList.initTabList()
        this.tabList.updateRightInfoTab(this.playerEntity, this.playerStats)
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

        this.tabList.updateRightInfoTab(this.playerEntity, this.playerStats)
        this.tabList.sendAllPackets(this.mcPlayer)
    }

    /**
     * Calculates the damage reduction on this player from another player and applies said damage
     */
    fun requestDamageToThisPlayer(damage: DamageCalculationResult)
    {
        val finalDamageCalculation = playerStats.calculateDamageReduction(damage)

        val isPlayerDead = playerStats.removeHealth(finalDamageCalculation)

        if( isPlayerDead ) {
            mcPlayer.damage(0.0)
            mcPlayer.health = 0.0
        }
    }

    /**
     * Marks player as hit for now
     */
    fun markPlayerAsHit()
    {
        lastHit = TimeSource.Monotonic.markNow()
    }

    /**
     * Can this player be hit again ?
     */
    fun isPlayerHittable(): Boolean = lastHit.elapsedNow() >= timeBetweenHits
}