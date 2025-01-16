package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import fr.fv.mq_fv.interfaces.BuildablePacket
import fr.fv.mq_fv.interfaces.SendablePacket
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player


class FakeEntity(val entityId: Int, val player: Player, val entity: EntityType): SendablePacket, BuildablePacket, AbstractPacket() {


    override fun buildPacket() {
        val testPacket = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY)

        testPacket.modifier.writeDefaults()
        val spawnPacketModifier = testPacket.modifier

        testPacket.entityTypeModifier.write(0, entity)
        spawnPacketModifier.write(0, entityId)
        spawnPacketModifier.write(3, player.location.x)
        spawnPacketModifier.write(4, player.location.y)
        spawnPacketModifier.write(5, player.location.z)

        packet = testPacket
    }

    override fun sendPacket(player: Player) {
        manager.sendServerPacket(player, packet)
    }
}