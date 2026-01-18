package fr.fv.mq_fv.stats

import net.kyori.adventure.text.format.TextColor

/**
 * Hold styles for the statistics of this plugin
 */
enum class PlayerStatistic(
    val symbol: String,
    val color: TextColor,
    val displayIndex: Int,
    val modifierIndex: Int,
    val decimalAmount: Int = 0,
) {
    LIFE("♥", TextColor.color(225, 50, 50), 1, 999),
    DEFENCE("⛊", TextColor.color(114, 209, 110), 2, 1),
    ATTACK("✹", TextColor.color(242, 151, 82), 3, 1),
    CRITICAL_CHANCE("◈", TextColor.color(88, 131, 239), 4, 2, 2),
    CRITICAL_DAMAGE("◉", TextColor.color(88, 131, 239), 5, 3, 2)
}