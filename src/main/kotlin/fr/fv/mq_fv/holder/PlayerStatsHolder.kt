package fr.fv.mq_fv.holder

import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.exceptions.PlayerStatisticException
import fr.fv.mq_fv.stats.PlayerStatistic
import fr.fv.mq_fv.stats.PlayerStatisticType

/**
 * Will hold player statistics
 */
class PlayerStatsHolder {

    /** Base statistics */
    private val stats: List<SinglePlayerStat> = listOf(
        SinglePlayerStat(100.0f, PlayerStatistic.LIFE),
        SinglePlayerStat(5.0f, PlayerStatistic.DEFENCE),

        SinglePlayerStat(10.0f, PlayerStatistic.ATTACK),
        SinglePlayerStat(30.0f, PlayerStatistic.CRITICAL_CHANCE),
        SinglePlayerStat(75.0f, PlayerStatistic.CRITICAL_DAMAGE),
    )

    var currentHp: Double = 100.0
        private set

    /**
     * Returns the modifiers with this type and sorts the results by their modifiers index
     */
    fun getModifiersByType(type: PlayerStatisticType): List<SinglePlayerStat> =
        stats
            .filter { type === it.stat.modifierType }
            .sortedBy { it.stat.modifierIndex }

    /**
     * Returns an ordered list for the tab list
     */
    fun getTabListOrder(): List<SinglePlayerStat> =
        stats
            .sortedBy { it.stat.displayIndex }

    /**
     * Returns a map containing the stat and the string representation of said stat
     */
    fun toMapString(list: List<SinglePlayerStat>): Map<PlayerStatistic, String>
    {
        val targetMap = HashMap<PlayerStatistic, String>()

        list.forEach {
            targetMap[it.stat] = it.stat.getStringRepresentation(it.amount.toDouble())
        }

        return targetMap
    }

    /**
     * Returns a pair containing the stat and the string representation of said stat
     */
    fun toPairString(list: List<SinglePlayerStat>): List<Pair<PlayerStatistic, String>> =
        list.map {
            Pair(it.stat, it.stat.getStringRepresentation(it.amount.toDouble()))
        }

    /**
     * Calculates the damage to receive to this player from another player
     */
    fun calculateDamageReduction(damageRequest: DamageCalculationResult): Double
    {
        var finalRequest = damageRequest.copy()

        getModifiersByType(PlayerStatisticType.DEFENCIVE)
            .forEach {
                finalRequest = it.stat.applyModifier(it.amount.toDouble(), finalRequest)
            }

        return damageRequest.damage
    }

    /**
     * Calculate the damage to give
     */
    fun calculateDamageToInflict(): DamageCalculationResult
    {
        var calculatedDamage = DamageCalculationResult(damage = 330.0, isCritical = false)

        getModifiersByType(PlayerStatisticType.OFFENSIVE)
            .forEach {
                calculatedDamage = it.stat.applyModifier(it.amount.toDouble(), calculatedDamage)
            }

        return calculatedDamage
    }

    fun getSafeTargetStat(stat: PlayerStatistic): SinglePlayerStat? = stats.find { stat == it.stat }

    fun getTargetStat(stat: PlayerStatistic): SinglePlayerStat =
        this.getSafeTargetStat(stat) ?:
            throw PlayerStatisticException("Player statistic '${stat.name}' is not found in the holder.")

    /**
     * Adds health to this player
     */
    fun addHealth(health: Double)
    {
        val maxHpStat = getTargetStat(PlayerStatistic.LIFE)

        if( 0 >= health ) {
            return
        }

        if( maxHpStat.amount <= currentHp + health ) {
            currentHp = maxHpStat.amount.toDouble()
        } else {
            currentHp += currentHp + health
        }
    }

    /**
     * Removes health from the player.
     * Returns true if the player is dead from this HP removal
     */
    fun removeHealth(health: Double): Boolean
    {
        if( 0 < health ) {
            if( 0 >= currentHp - health ) {
                currentHp = 0.0
            } else {
                currentHp -= health
            }
        }

        return 0 >= currentHp
    }
}