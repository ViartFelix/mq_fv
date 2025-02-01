package fr.fv.mq_fv.tabList

import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedChatComponent
import fr.fv.mq_fv.protocolLib.FakePlayerTabPacket
import org.bukkit.entity.Player
import java.util.*

class SingleTabListPlayer(
    var displayName: WrappedChatComponent,
    val index: Int,
) {
    /** UUid of the player in the tab list */
    val uuid: UUID = UUID.randomUUID()

    /**
     * The player name in the packet (not display name)
     * (this value will be used to sort the fake names).
     */
    //add padding start so that MC clients will sort the right way the player names in the tab
    val playerNamePadding: String = this.index
        .toString()
        .padStart(3, '0')

    /**
     * Builds and sends a new packet to the player
     */
    fun sendPacketToPlayer(player: Player)
    {
        val tabPlayerPacket = FakePlayerTabPacket(displayName, playerNamePadding, uuid)
        tabPlayerPacket.buildPacket()
        tabPlayerPacket.sendPacket(player)
    }

    /**
     * Builds and returns the packet
     */
    fun buildPacket(): PacketContainer
    {
        val tabPlayerPacket = FakePlayerTabPacket(displayName, playerNamePadding, uuid)
        tabPlayerPacket.buildPacket()

        return tabPlayerPacket.packet
    }
}