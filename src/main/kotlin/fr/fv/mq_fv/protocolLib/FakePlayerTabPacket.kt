package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.*
import fr.fv.mq_fv.interfaces.BuildablePacket
import fr.fv.mq_fv.interfaces.SendablePacket
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.security.PublicKey
import java.time.Instant
import java.util.*


/**
 * Creates a new player in the tab list
 */
class FakePlayerTabPacket( val targetPlayer: Player, val fakeName: String ): SendablePacket, BuildablePacket, AbstractPacket() {

    /** 147 = Player */
    //private val entityId: Int = 147

    override fun buildPacket()
    {
        packet = manager.createPacket(PacketType.Play.Server.PLAYER_INFO)

        packet.playerInfoActions.write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER))

        val uuid: UUID = UUID.randomUUID()
        val commonName = "TestPlayer"

        /*
        packet.playerInfoDataLists.write(0, Collections.singletonList(
            PlayerInfoData(
                uuid,
                50,
                true,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedGameProfile.fromPlayer(targetPlayer),
                WrappedChatComponent.fromText(commonName)
            )
        ))

         */

        // Create a fake player profile
        val fakeUUID = UUID.randomUUID()
        val fakeProfile = WrappedGameProfile(fakeUUID, fakeName)

        // Create the PlayerInfoData object for the fake player
        val playerInfoData = PlayerInfoData(
            fakeProfile,
            100, // Ping (latency)
            EnumWrappers.NativeGameMode.CREATIVE, // Game mode
            WrappedChatComponent.fromText(fakeName) // Display name in tab list
        )

        // Add the fake player to the packet
        packet.playerInfoDataLists.write(0, listOf(playerInfoData))

        // Debug: Log the packet information
        println("Packet built for fake player: $fakeName ($fakeUUID)")
    }

    override fun sendPacket(player: Player) {
        // Debug: Print packet details
        println("Sending packet to player: ${player.name}")
        println("Packet details: $packet")

        manager.sendServerPacket(player, packet)
    }
}