package fr.fv.mq_fv.helpers

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PotionEffectsHelper {

    /** Returns a potion effect for a permanent night vision for the player */
    fun getInfiniteNightVision() = PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 1)
}