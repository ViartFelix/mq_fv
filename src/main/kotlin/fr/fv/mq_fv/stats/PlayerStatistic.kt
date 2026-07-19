package fr.fv.mq_fv.stats

import fr.fv.mq_fv.dto.DamageCalculationResult
import fr.fv.mq_fv.interfaces.ModifierApplicant
import net.kyori.adventure.text.format.TextColor
import kotlin.math.floor
import kotlin.random.Random

/**
 * Hold styles for the statistics of this plugin
 */
enum class PlayerStatistic(
    val symbol: String,
    val color: TextColor,
    val displayIndex: Int,
    val modifierIndex: Int,
    val modifierType: PlayerStatisticType,
    val decimalAmount: Int = 0,
) : ModifierApplicant {

    /**
     * Or max life
     */
    LIFE(
        symbol = "♥",
        color = TextColor.color(225, 50, 50),
        displayIndex = 1,
        modifierIndex = 999,
        modifierType = PlayerStatisticType.NONE,
        decimalAmount = 0,
    ),

    DEFENCE(
        symbol = "⛊",
        color = TextColor.color(114, 209, 110),
        displayIndex = 2,
        modifierIndex = 1,
        modifierType = PlayerStatisticType.DEFENCIVE,
        decimalAmount = 0,
    ) {
        override fun applyModifier(value: Double, calculation: DamageCalculationResult): DamageCalculationResult {
            if( 0 < calculation.damage ) {
                calculation.damage -= value
            }

            return calculation
        }
    },

    ATTACK(
        symbol = "✹",
        color = TextColor.color(242, 151, 82),
        displayIndex = 3,
        modifierIndex = 1,
        modifierType = PlayerStatisticType.OFFENSIVE,
        decimalAmount = 0,
    ) {
        override fun applyModifier(value: Double, calculation: DamageCalculationResult): DamageCalculationResult {
            calculation.damage += calculation.damage * ( 1 + (value / 100) )

            return calculation
        }
    },

    CRITICAL_CHANCE(
        symbol = "\uD83C\uDFAF",
        color = TextColor.color(88, 131, 239),
        displayIndex = 4,
        modifierIndex = 2,
        modifierType = PlayerStatisticType.OFFENSIVE,
        decimalAmount = 2
    ) {
        override fun applyModifier(value: Double, calculation: DamageCalculationResult): DamageCalculationResult {
            val isCriticalDamage = (Random.nextDouble(0.0, 100.0) <= value)
            calculation.isCritical = isCriticalDamage

            return calculation
        }

        override fun getStringRepresentation(amount: Double): String {
            val rawStringRepresentation = super.getStringRepresentation(amount)

            return "$rawStringRepresentation %"
        }
    },

    CRITICAL_DAMAGE(
        symbol = "\uD83D\uDC80",
        color = TextColor.color(88, 131, 239),
        displayIndex = 5,
        modifierIndex = 3,
        modifierType = PlayerStatisticType.OFFENSIVE,
        decimalAmount = 2
    ) {
        override fun applyModifier(value: Double, calculation: DamageCalculationResult): DamageCalculationResult {
            if(!calculation.isCritical) {
                return calculation
            }

            calculation.damage *= 1 + (value / 100)

            return calculation
        }

        override fun getStringRepresentation(amount: Double): String {
            val rawStringRepresentation = super.getStringRepresentation(amount)

            return "$rawStringRepresentation %"
        }
    },

    JUMPS(
        symbol = "\uD83E\uDEB6",
        color = TextColor.color(29, 229, 33),
        displayIndex = 6,
        modifierType = PlayerStatisticType.NONE,
        decimalAmount = 0,
        modifierIndex = 0,
    ) {
        override fun getStringRepresentation(amount: Double): String {
            return "%.0f".format(floor(amount))
        }
    };

    /**
     * Applies this modifier to the calculation.
     */
    override fun applyModifier(value: Double, calculation: DamageCalculationResult): DamageCalculationResult {
        return calculation
    }

    open fun getStringRepresentation(amount: Double): String
    {
        val formatString = "%.${this.decimalAmount}f"
        return String.format(formatString, amount)
    }
}