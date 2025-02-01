package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.PlayerInfoData
import fr.fv.mq_fv.interfaces.BuildablePacket
import fr.fv.mq_fv.interfaces.SendablePacket
import org.bukkit.entity.Player
import java.util.*

class FakePlayerCollectionPacket(
    private val allPlayers: List<PlayerInfoData>
): BuildablePacket, SendablePacket, AbstractPacket() {

    override fun buildPacket() {
        packet = PacketContainer(PacketType.Play.Server.PLAYER_INFO)
        packet.playerInfoActions.write(0, EnumSet.of(
            EnumWrappers.PlayerInfoAction.ADD_PLAYER,
            EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE, EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
            EnumWrappers.PlayerInfoAction.UPDATE_LISTED, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME))

        packet.playerInfoDataLists.write(1, allPlayers)
    }

    override fun sendPacket(player: Player) {
        manager.sendServerPacket(player, packet)
    }
}