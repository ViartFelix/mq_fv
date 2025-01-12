package fr.fv.mq_fv.commands

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Location
import org.bukkit.entity.Player

class BoomCommand: BasicCommand {

    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        val sender = stack.sender

        if( sender is Player ) run {

            val player: Player = sender as Player
            val location: Location = player.location

            val fakeExplosion = PacketContainer(PacketType.Play.Server.EXPLOSION)

            fakeExplosion.doubles
                .write(0, location.x)
                .write(1, location.y)
                .write(2, location.z)

            fakeExplosion.float.write(0, 3.0f)

            val affectedBlocks = mutableListOf<ByteArray>()
            // Add some example block offsets, you can modify this to suit your needs
            affectedBlocks.add(byteArrayOf(0, 0, 0)) // Example offset of (0,0,0)

            // Set the 'toBlow' field with the list of affected blocks
            fakeExplosion.getSpecificModifier(List::class.java).write(0, affectedBlocks)

            fakeExplosion.float
                .write(1, 0.0f) // knockbackX
                .write(2, 0.0f) // knockbackY
                .write(3, 0.0f) // knockbackZ

            val smallExplosionParticleId = 1 // Example ID for a particle (adjust as necessary)
            val largeExplosionParticleId = 1 // Same here, adjust as necessary

            fakeExplosion.modifier
                .write(4, smallExplosionParticleId) // Small Explosion Particle ID
                .write(5, largeExplosionParticleId) // Large Explosion Particle ID

            // Explosion sound (optional but needs to be non-null)
            //val sound: Holder<SoundEvent> = BuiltInReg.SOUND_EVENT.getHolderOrThrow() // Replace with an actual sound if needed
            val sound = null // Set the sound if desired
            fakeExplosion.modifier.write(6, sound)

            //fakeExplosion.modifier.fields.forEach { f -> println(f.field.toString()) }

            try {
                val manager = ProtocolLibrary.getProtocolManager()
                manager.sendServerPacket(player, fakeExplosion)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}