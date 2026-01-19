package fr.fv.mq_fv.interfaces

import fr.fv.mq_fv.dto.DamageCalculationResult

/**
 * Applies a statistic modifier
 */
interface ModifierApplicant {

    /**
     * Applies this modifier to the damage calculation
     */
    fun applyModifier(value: Double, calculation: DamageCalculationResult): DamageCalculationResult
}