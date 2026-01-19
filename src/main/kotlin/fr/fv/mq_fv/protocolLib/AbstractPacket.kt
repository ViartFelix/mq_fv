package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import fr.fv.mq_fv.interfaces.SendablePacket
import org.bukkit.entity.Player

abstract class AbstractPacket(val packetType: PacketType) : SendablePacket {

    /** Packet */
    val packet: PacketContainer = PacketContainer(packetType)

    override fun sendPacket(player: Player) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet)
    }
}