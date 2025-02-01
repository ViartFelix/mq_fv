package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.*
import fr.fv.mq_fv.interfaces.BuildablePacket
import fr.fv.mq_fv.interfaces.SendablePacket
import org.bukkit.entity.Player
import java.util.*


/**
 * Creates a new player in the tab list
 */
class FakePlayerTabPacket(
    private val uuid: UUID = UUID.randomUUID(),
    private val name: String,
): SendablePacket, BuildablePacket, AbstractPacket() {

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
        //WIP: This code adds a player with the same name as the player in the tab list

        val testPacket = PacketContainer(PacketType.Play.Server.PLAYER_INFO)
        testPacket.playerInfoActions.write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER,
            EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE, EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
            EnumWrappers.PlayerInfoAction.UPDATE_LISTED, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME))

        val fakeProfile = WrappedGameProfile(uuid, name)

        testPacket.playerInfoDataLists.write(1, listOf(
            PlayerInfoData(
                uuid,
                -1, //no network bar in the right side
                true,
                EnumWrappers.NativeGameMode.SURVIVAL,
                fakeProfile,
                displayedName
            )
        ))

        packet = testPacket
    }

    override fun sendPacket(player: Player) {
        manager.sendServerPacket(player, packet)
    }
}