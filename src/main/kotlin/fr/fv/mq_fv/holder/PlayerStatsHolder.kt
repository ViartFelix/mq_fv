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
        SinglePlayerStat(100.0f, PlayerStatistic.LIFE),
        SinglePlayerStat(5.0f, PlayerStatistic.DEFENCE),

        SinglePlayerStat(10.0f, PlayerStatistic.ATTACK),
        SinglePlayerStat(30.0f, PlayerStatistic.CRITICAL_CHANCE),
        SinglePlayerStat(75.0f, PlayerStatistic.CRITICAL_DAMAGE),
    )

    /**
     * Returns an ordered list for the modifiers application
     */
    fun getModifiersOrder(): List<SinglePlayerStat> = stats.sortedBy { it.stat.modifierIndex }

    /**
     * Returns an ordered list for the tab list
     */
    fun getTabListOrder(): List<SinglePlayerStat> = stats.sortedBy { it.stat.displayIndex }

    /**
     * Returns a map containing the stat and the string representation of said stat
     */
    fun toMapString(list: List<SinglePlayerStat>): Map<PlayerStatistic, String>
    {
        //println("to map string for the tab list")
        //println(list)
        val targetMap = HashMap<PlayerStatistic, String>()

        list.forEach {
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

        val modif = getModifiersOrder()

        //println("damage reduction for calculation")
        //println(modif)

        modif.forEach {
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

        val modif = getModifiersOrder()

        //println("attack modifiers")
        //println(modif)

        // apply modifiers
        modif.forEach {
            calculatedDamage = it.applyDamageModifier(calculatedDamage)
        }

        return calculatedDamage
    }

    fun getSafeTargetStat(stat: PlayerStatistic): SinglePlayerStat? = stats.find { stat == it.stat }

    fun getTargetStat(stat: PlayerStatistic): SinglePlayerStat =
        this.getSafeTargetStat(stat) ?:
            throw PlayerStatisticException("Player statistic '${stat.name}' is not found in the holder.")
}