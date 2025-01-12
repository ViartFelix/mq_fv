package fr.fv.mq_fv.commands

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedChatComponent
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Location
import org.bukkit.entity.Player

class ChatCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {

        if( stack.sender is Player ) {
            val player = stack.sender as Player
            val manager: ProtocolManager = ProtocolLibrary.getProtocolManager()

            // Create a packet container of type "CHAT" (it is a packet that handles chat messages)
            val packet = manager.createPacket(PacketType.Play.Server.CHAT, true)

            // Set the message in the packet
            packet.chatComponents.write(0, WrappedChatComponent.fromText("hey !"))

            try {
                // Send the packet to the player
                manager.sendServerPacket(player, packet)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }
}