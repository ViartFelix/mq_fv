package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.*
import fr.fv.mq_fv.interfaces.BuildablePacket
import java.util.*


/**
 * Creates a new player in the tab list
 */
class FakePlayerTabPacket(
    private val uuid: UUID = UUID.randomUUID(),
    private val name: String,
): BuildablePacket, AbstractPacket(PacketType.Play.Server.PLAYER_INFO) {

    lateinit var displayedName: WrappedChatComponent

    constructor(name: String, uuid: UUID = UUID.randomUUID()) : this(uuid, name)
    {
        this.displayedName = WrappedChatComponent.fromText(name)
    }

    constructor(displayName: WrappedChatComponent, name: String, uuid: UUID = UUID.randomUUID()) : this(uuid, name)
    {
        this.displayedName = displayName
    }

    override fun buildPacket()
    {
        packet.playerInfoActions.write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER,
            EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE, EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
            EnumWrappers.PlayerInfoAction.UPDATE_LISTED, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME))

        packet.playerInfoDataLists.write(1, listOf(
            PlayerInfoData(
                uuid,
                -1, //no network bar in the right side
                true,
                EnumWrappers.NativeGameMode.SURVIVAL,
                WrappedGameProfile(uuid, name),
                displayedName
            )
        ))
    }
}