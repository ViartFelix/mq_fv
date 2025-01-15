package fr.fv.mq_fv.tabList

import com.comphenix.protocol.wrappers.WrappedChatComponent
import org.bukkit.entity.Player
import java.util.*

class SingleTabListPlayer(
    val displayName: WrappedChatComponent
) {
    final val uuid: UUID = UUID.randomUUID()

    /**
     * Builds and sends a new packet to the player
     */
    fun sendPacketToPlayer(player: Player)
    {

    }
}