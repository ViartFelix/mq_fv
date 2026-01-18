package fr.fv.mq_fv.dto


data class DamageCalculationResult(
    var damage: Double,
    var isCritical: Boolean,
) {
    override fun toString(): String {
        return "DamageCalculationResult(damage=$damage, isCritical=$isCritical)"
    }
}
