package fr.fv.mq_fv.holder

import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.stats.PlayerStatistic
import kotlin.random.Random

/**
 * A single player statistic
 */
class SinglePlayerStat(
    var amount: Float, val stat: PlayerStatistic
) {
    /**
     * Returns a string representation of the amount for the held statistic
     */
    fun amountToString(): String {
        val formatString = "%.${stat.decimalAmount}f"
        return String.format(formatString, amount)
    }

    /**
     * Applies the modifier of damage to a dealt damage
     */
    fun applyDamageModifier(calculation: DamageCalculationResult): DamageCalculationResult
    {
        return when(stat) {
            PlayerStatistic.ATTACK -> this.handleWhenTypeIsAttack(calculation)
            PlayerStatistic.CRITICAL_CHANCE -> this.handleWhenTypeIsCriticalChance(calculation)
            PlayerStatistic.CRITICAL_DAMAGE -> this.handleWhenTypeIsCriticalDamage(calculation)
            else -> calculation // prevent IDE error
        }
    }

    /**
     * Applies the modifiers for defence
     */
    fun applyDefenceModifier(request: DamageCalculationResult): DamageCalculationResult
    {
        return when(stat) {
            PlayerStatistic.DEFENCE -> this.handleWhenTypeIsDefence(request)
            else -> request
        }
    }

    /**
     * Handle adding the modifier of attack to the damage dealt
     */
    private fun handleWhenTypeIsAttack(calculation: DamageCalculationResult): DamageCalculationResult
    {
        val baseDamage = calculation.damage;
        val attackToDecimal = amount.toDouble()

        return DamageCalculationResult(
            damage = baseDamage * (1 + (attackToDecimal / 100)),
            isCritical = calculation.isCritical
        )
    }

    /**
     * Handle adding the modifier of critical chance and critical damage to the damage dealt
     */
    private fun handleWhenTypeIsCriticalChance(calculation: DamageCalculationResult): DamageCalculationResult
    {
        val isCriticalDamage = (Random.nextDouble(0.0, 100.0) <= amount.toDouble())
        calculation.isCritical = isCriticalDamage

        return calculation;

    }

    private fun handleWhenTypeIsCriticalDamage(calculation: DamageCalculationResult): DamageCalculationResult
    {
        if(!calculation.isCritical) {
            return calculation
        }

        val baseDamage = calculation.damage;
        val attackToDecimal = amount.toDouble()

        calculation.damage = baseDamage * (1 + (attackToDecimal / 100))

        return calculation
    }

    /**
     * Handles the damage reduction calculation
     */
    private fun handleWhenTypeIsDefence(request: DamageCalculationResult): DamageCalculationResult
    {
        val statAsDouble = amount.toDouble()

        if( 0 >= statAsDouble ) {
            return request
        }

        request.damage -= statAsDouble

        return request
    }
}