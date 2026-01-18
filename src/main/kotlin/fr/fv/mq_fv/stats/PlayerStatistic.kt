package fr.fv.mq_fv.stats

import net.kyori.adventure.text.format.TextColor

/**
 * Hold styles for the statistics of this plugin
 */
enum class PlayerStatistic(
    val symbol: String,
    val color: TextColor,
    val decimalAmount: Int = 0
) {
    LIFE("♥", TextColor.color(225, 50, 50), 0),
    DEFENCE("⛊", TextColor.color(114, 209, 110), 0),
    ATTACK("✹", TextColor.color(242, 151, 82), 0),
    CRITICAL_CHANCE("◈", TextColor.color(88, 131, 239), 2),
    CRITICAL_DAMAGE("◉", TextColor.color(88, 131, 239), 2)
}