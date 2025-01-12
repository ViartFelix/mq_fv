package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.wrappers.BlockPosition
import fr.fv.mq_fv.interfaces.BuildablePacket
import fr.fv.mq_fv.interfaces.SendablePacket
import org.bukkit.entity.Player
import java.util.*

class FakeExposition (val player: Player): SendablePacket, BuildablePacket, AbstractPacket() {


    override fun buildPacket() {
        packet = manager.createPacket(PacketType.Play.Server.EXPLOSION)

        val location = player.location

        //player explosion
        packet.doubles
            .write(0, location.x)
            .write(1, location.y)
            .write(2, location.z)

        //strength
        packet.float.write(0, 5.0f)

        //affected blocs (no blocs)
        packet.byteArrays.write(0, byteArrayOf())
        packet.integers.write(1, 0)

        //motion (none)
        packet.float
            .write(1, 0.0f) //X
            .write(2, 0.0f) //Y
            .write(3, 0.0f) //Z
    }

    override fun sendPacket(player: Player) {
        manager.sendServerPacket(player, packet)
    }
}