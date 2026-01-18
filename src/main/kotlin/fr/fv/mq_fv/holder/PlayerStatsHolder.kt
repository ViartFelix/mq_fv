package fr.fv.mq_fv.holder

import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.exceptions.PlayerStatisticException
import fr.fv.mq_fv.stats.PlayerStatistic

/**
 * Will hold player statistics
 */
class PlayerStatsHolder {

    /** Base statistics */
    val stats: List<SinglePlayerStat> = listOf(
        SinglePlayerStat(5.0f, PlayerStatistic.DEFENCE),
        SinglePlayerStat(100.0f, PlayerStatistic.LIFE),
        SinglePlayerStat(10.0f, PlayerStatistic.ATTACK),
        SinglePlayerStat(30.0f, PlayerStatistic.CRITICAL_CHANCE),
        SinglePlayerStat(75.0f, PlayerStatistic.CRITICAL_DAMAGE),
    )

    /**
     * Returns a map containing the stat and the string representation of said stat
     */
    fun toMapString(): Map<PlayerStatistic, String>
    {
        val targetMap = HashMap<PlayerStatistic, String>()

        this.stats
            .forEach {
                targetMap[it.stat] = it.amountToString()
            }

        return targetMap
    }

    /**
     * Calculates the damage
     */
    fun calculateDamage(): DamageCalculationResult {
        return this.getModifiersResult()
    }

    /**
     * Calculates the damage to receive to this player from another player
     */
    fun calculateDamageReduction(damageRequest: DamageCalculationResult): Double
    {
        var finalRequest = damageRequest.copy()

        this.stats
            .forEach {
                finalRequest = it.applyDefenceModifier(finalRequest)
            }

        return damageRequest.damage
    }

    /**
     * Applies the modifiers of statistics
     */
    private fun getModifiersResult(): DamageCalculationResult
    {
        var calculatedDamage = DamageCalculationResult(damage = 1.0, isCritical = false)

        // apply modifiers
        this.stats
            .forEach {
                calculatedDamage = it.applyDamageModifier(calculatedDamage)
            }

        return calculatedDamage
    }

    fun getSafeTargetStat(stat: PlayerStatistic): SinglePlayerStat?
    {
        return stats.find { stat == it.stat }
    }

    fun getTargetStat(stat: PlayerStatistic): SinglePlayerStat
    {
        return this.getSafeTargetStat(stat)
            ?: throw PlayerStatisticException("Player statistic '${stat.name}' is not found in the holder.")
    }
}