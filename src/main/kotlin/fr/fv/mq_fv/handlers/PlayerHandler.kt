package fr.fv.mq_fv.handlers

import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.exceptions.DatabaseException
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.helpers.PotionEffectsHelper
import fr.fv.mq_fv.holder.PlayerStatsHolder
import fr.fv.mq_fv.stats.PlayerStatistic
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
    val mcPlayer: Player
) {
    /** Holds the last hit (to prevent spam attack) */
    private var lastHit: TimeSource.Monotonic.ValueTimeMark = TimeSource.Monotonic.markNow()

    /** Holds the statistics of the current player */
    val playerStats: PlayerStatsHolder = PlayerStatsHolder()

    companion object {
        /** Player entity of the DB */
        lateinit var playerEntity: PlayerTable
            private set

        /** Player DB repository */
        val playerRepository: PlayerRepository = PlayerRepository()

        /** Potion effect factory */
        val potionEffectsHelper: PotionEffectsHelper = PotionEffectsHelper()

        /** Tab list manager for that player */
        val tabList: TabListHandler = TabListHandler()

        /** Time between hits */
        val timeBetweenHits: Duration = 0.5.seconds

        val scoreboardManager: PlayerScoreboardManager = PlayerScoreboardManager.instance
    }

    init {
        PlayerScoreboardManager.instance.initHealthObjective()

        //fetch the player
        val fetchedPlayer = playerRepository.getPlayer(mcPlayer)
            ?: throw DatabaseException("Player '${mcPlayer.name}' (uuid: ${mcPlayer.uniqueId}) trying to be fetched from the DB is not registered")

        playerEntity = fetchedPlayer

        this.applyPotionEffects()

        tabList.initTabList()
        tabList.updateRightInfoTab(playerEntity, playerStats)
        tabList.sendAllPackets(mcPlayer)

        scoreboardManager.addPlayerToHealthDisplayScore(mcPlayer)
        scoreboardManager.setHealthDisplayScoreForPlayer(mcPlayer, playerStats.currentHp)

        /*
        tabList.initTabList()

        scoreboardManager.addPlayerToHealthDisplayScore(mcPlayer)

        updatePlayerDisplayedInfos()
         */
    }

    /**
     * Displays the necessary potion effects to the player once logged in
     */
    private fun applyPotionEffects()
    {
        mcPlayer.addPotionEffect(potionEffectsHelper.getInfiniteNightVision())
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
            ComponentFactory().buildPlayerTabHeader(mcPlayer, configServerName)
        )

        tabList.updateRightInfoTab(playerEntity, playerStats)
        tabList.sendAllPackets(mcPlayer)
    }

    /**
     * Updates the player's health bar
     */
    fun updatePlayerHealthBar() {
        val percent = (this.playerStats.currentHp / this.playerStats.getTargetStat(PlayerStatistic.LIFE).amount).toFloat()
        mcPlayer.health = (mcPlayer.healthScale * percent)
    }

    /**
     * Calculates the damage reduction on this player from another player and applies said damage
     */
    fun requestDamageToThisPlayer(damage: DamageCalculationResult)
    {
        val finalDamageCalculation = playerStats.calculateDamageReduction(damage)

        val isPlayerDead = playerStats.removeHealth(finalDamageCalculation)

        //updateDisplayedHealth(playerStats.currentHp)

        if( isPlayerDead ) {
            mcPlayer.damage(0.0)
            mcPlayer.health = 0.0
        } else {
            updatePlayerHealthBar()
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

    /**
     * Handles when the player disconnects
     */
    fun onPlayerDisconnect()
    {
        //
    }

    /**
     * Updates the displayed health on this player
     */
    private fun updateDisplayedHealth(health: Double)
    {
        scoreboardManager.setHealthDisplayScoreForPlayer(mcPlayer, health)
    }

    /**
     * Handles when the player dies
     */
    fun handleWhenPlayerDie()
    {
        markPlayerAsHit()

        // set health to max
        playerStats.addHealth(playerStats.getTargetStat(PlayerStatistic.LIFE).amount.toDouble())

        // set hearts to max
        mcPlayer.health = mcPlayer.healthScale

        updatePlayerDisplayedInfos()
    }

    fun handleWhenPlayerRespawn()
    {
        markPlayerAsHit()
        updatePlayerDisplayedInfos()
    }

    /**
     * Updates what the player can see in the world (nametags, tablist, etc.).
     */
    private fun updatePlayerDisplayedInfos()
    {
        applyPotionEffects()
        updatePlayerTab()

        tabList.updateRightInfoTab(playerEntity, playerStats)
        tabList.sendAllPackets(mcPlayer)

        updateDisplayedHealth(playerStats.currentHp)
    }
}