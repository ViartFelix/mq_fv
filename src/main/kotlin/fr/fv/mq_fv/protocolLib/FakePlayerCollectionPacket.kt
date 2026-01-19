package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.PlayerInfoData
import fr.fv.mq_fv.interfaces.BuildablePacket
import java.util.*

class FakePlayerCollectionPacket(
    private val allPlayers: List<PlayerInfoData>
): BuildablePacket, AbstractPacket(PacketType.Play.Server.PLAYER_INFO) {
    override fun buildPacket() {
        packet.playerInfoActions.write(0, EnumSet.of(
            EnumWrappers.PlayerInfoAction.ADD_PLAYER,
            EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE, EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
            EnumWrappers.PlayerInfoAction.UPDATE_LISTED, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME))

        packet.playerInfoDataLists.write(1, allPlayers)
    }
}