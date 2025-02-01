package fr.fv.mq_fv.commands

import fr.fv.mq_fv.protocolLib.FakePlayerTabPacket
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player

class TabCommand: BasicCommand {
    override fun execute(stack: CommandSourceStack, args: Array<out String>) {
        if( stack.sender is Player ) {
            val player = stack.sender as Player

            val packet = FakePlayerTabPacket("John")
            packet.buildPacket()
            packet.sendPacket(player)
        }
    }
}