package fr.fv.mq_fv.handlers

import fr.fv.mq_fv.stats.PlayerStatistic
import fr.fv.mq_fv.utils.ComponentFactory
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

class PlayerScoreboardManager {

    companion object {
        const val HEALTH_DISPLAY_OBJECTIVE_NAME = "health-display"

        lateinit var mainScoreboard: Scoreboard
            private set

        lateinit var healthDisplayObjective: Objective
            private set

        val instance: PlayerScoreboardManager by lazy {
            PlayerScoreboardManager()
        }
    }

    fun initHealthObjective()
    {
        mainScoreboard = Bukkit.getScoreboardManager().mainScoreboard

        healthDisplayObjective = mainScoreboard.getObjective(HEALTH_DISPLAY_OBJECTIVE_NAME)
            ?: this.createHealthObjective(mainScoreboard)
    }

    /**
     * Creates the displayed health objective
     */
    private fun createHealthObjective(
        scoreboard: Scoreboard, objectiveName: String = HEALTH_DISPLAY_OBJECTIVE_NAME
    ): Objective {
        val objective = scoreboard.registerNewObjective(
            objectiveName,
            Criteria.DUMMY,
            ComponentFactory().buildStatSymbolComponent(PlayerStatistic.LIFE),
            RenderType.INTEGER
        )

        objective.displaySlot = DisplaySlot.BELOW_NAME

        return objective
    }

    /**
     * Sets the displayed health on the player
     */
    fun setHealthDisplayScoreForPlayer(player: Player, health: Double)
    {
        healthDisplayObjective.getScore(player).score = health.toInt()
    }

    /**
     * Adds this player to the health display objective
     */
    fun addPlayerToHealthDisplayScore(player: Player)
    {
        player.scoreboard = mainScoreboard
    }
}