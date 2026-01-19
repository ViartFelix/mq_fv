package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import fr.fv.mq_fv.interfaces.BuildablePacket
import org.bukkit.entity.LivingEntity

/**
 * A fake hurt animation
 */
class FakeHurtAnimation(
    private val target: LivingEntity,
    private val yaw: Float,
): BuildablePacket, AbstractPacket(PacketType.Play.Server.HURT_ANIMATION) {
    override fun buildPacket() {
        println("qzdzdqqzqdzdqzdq")
        println(target)

        packet.integers.write(0, target.entityId)
        packet.float.write(0, yaw)
    }
}