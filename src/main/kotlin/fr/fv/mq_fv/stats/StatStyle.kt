package fr.fv.mq_fv.stats

import net.kyori.adventure.text.format.TextColor

/**
 * Hold styles for the statistics of this plugin
 */
enum class StatStyle(val symbol: String, val color: TextColor) {
    LIFE("♥", TextColor.color(225, 50, 50)),
    DEFENCE("⛊", TextColor.color(114, 209, 110))
}