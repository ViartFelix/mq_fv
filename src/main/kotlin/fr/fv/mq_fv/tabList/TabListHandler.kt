package fr.fv.mq_fv.tabList

import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import fr.fv.mq_fv.protocolLib.FakePlayerCollectionPacket
import fr.fv.mq_fv.utils.ComponentFactory
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.bukkit.entity.Player

class TabListHandler {
    var maxColumnsNumber: Int = 4
        private set

    var allColumns: LinkedHashMap<Int, TabListColumn> = LinkedHashMap(maxColumnsNumber)
        private set

    /**
     * Gets a column from the tab list
     */
    fun getColumn(columnNumber: Int): TabListColumn
    {
        return allColumns[columnNumber]!!
    }

    /**
     * Replace the contents of the column
     */
    fun replaceColumn(columnNumber: Int, tabListColumn: TabListColumn)
    {
        allColumns[columnNumber] = tabListColumn
    }

    /**
     * Initiates all columns and fake players to send
     */
    fun initTabList()
    {
        for( i in 0..<maxColumnsNumber ) {
            val newColumn = TabListColumn(i, 20)
            newColumn.initColumn()

            this.allColumns[i] = newColumn
        }

        //at this point we have a blank tab list. Now we're able to fill in the tab with whatever we want !
    }

    /**
     * Sends all tab packets to a certain player.
     */
    fun sendAllPackets(player: Player): TabListHandler {

        //sort all entries by index
        val allSinglePlayerTab = this.getReorderedTabList()

        //create packets
        val allPlayerInfoDataList: MutableList<PlayerInfoData> = mutableListOf()

        //add all playerInfoDataLists
        allSinglePlayerTab.forEach { it ->
            allPlayerInfoDataList.addAll(it.buildPacket().playerInfoDataLists.read(1))
        }

        //build the final packet
        val finalPacket = FakePlayerCollectionPacket(allPlayerInfoDataList)
        finalPacket.buildPacket()
        finalPacket.sendPacket(player)

        return this
    }

    fun updateRightInfoTab(player: PlayerTable) {
        val infoColumn = this.getColumn(3)

        //the "money title"
        val moneyTitleComponent = ComponentFactory().buildTabMoneyTitleComponent("Money")
        val moneyTitleComponentJson = JSONComponentSerializer.json().serialize(moneyTitleComponent)
        val monetTitleDisplay = infoColumn.getAtIndex(0)

        //the true money of the player
        val moneyComponent = ComponentFactory().buildTabMoneyComponent(player.money)
        val moneyComponentJson = JSONComponentSerializer.json().serialize(moneyComponent)
        val moneyDisplay = infoColumn.getAtIndex(1)

        moneyDisplay.displayName = WrappedChatComponent.fromJson(moneyComponentJson)
        monetTitleDisplay.displayName = WrappedChatComponent.fromJson(moneyTitleComponentJson)
    }

    /**
     * Handles the reordering the tab list.
     * Returns an ordered list of SingleTabListPlayer
     */
    private fun getReorderedTabList(): List<SingleTabListPlayer>
    {
        val list: MutableList<SingleTabListPlayer> = mutableListOf()

        //sorted columns from their index
        val allColumns: List<TabListColumn> = this.allColumns
            .entries
            .map { it.value }

        allColumns.forEach { column ->
            val allPlayersColumn: List<SingleTabListPlayer> = column.allPlayers
                .entries
                .map { it.value }

            list.addAll(allPlayersColumn)
        }

        //mutable -> immutable
        return list.toList()
    }
}