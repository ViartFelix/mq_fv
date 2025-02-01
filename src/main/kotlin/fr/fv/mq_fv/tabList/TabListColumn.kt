package fr.fv.mq_fv.tabList

import com.comphenix.protocol.wrappers.WrappedChatComponent

class TabListColumn(
    private val index: Int,
    private val maxCapacity: Int = 20
) {
    /** All players (or fake players) in that column */
    var allPlayers: LinkedHashMap<Int, SingleTabListPlayer> = LinkedHashMap(maxCapacity)
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

    /**
     * Initiates this column and all fake players
     */
    fun initColumn()
    {
        for (i in 0..<this.maxCapacity) {
            val baseIndex = index * maxCapacity

            this.allPlayers[i] = SingleTabListPlayer(
                WrappedChatComponent.fromText(" "),
                baseIndex + i
            )
        }
    }
}