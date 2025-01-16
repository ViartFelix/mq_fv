package fr.fv.mq_fv.tabList

class TabListColumn(
    final val index: Int,
    final val maxCapacity: Int = 20
) {
    /** All players (or fake players) in that column */
    var allPlayers: HashMap<Int, SingleTabListPlayer> = HashMap(maxCapacity)
        private set

    /**
     * Gets a player (or fake player) at index
     */
    fun getAtIndex(index: Int): SingleTabListPlayer {
        return allPlayers[index]!!
    }

    /**
     * Edits a player at the index
     */
    fun setAtIndex(index: Int, player: SingleTabListPlayer) {
        allPlayers[index] = player
    }
}