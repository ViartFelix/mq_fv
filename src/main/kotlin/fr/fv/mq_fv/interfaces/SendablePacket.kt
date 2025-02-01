package fr.fv.mq_fv.interfaces

import org.bukkit.entity.Player

interface SendablePacket {

    /**
     * Sends this packet to the specified player
     */
    fun sendPacket(player: Player)
}