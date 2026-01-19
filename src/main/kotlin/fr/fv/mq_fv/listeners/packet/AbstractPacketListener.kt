package fr.fv.mq_fv.listeners.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import fr.fv.mq_fv.Mq_fv

/**
 * A packet listener
 */
abstract class AbstractPacketListener(
    packetType: PacketType,
    priority: ListenerPriority = ListenerPriority.NORMAL,
) : PacketAdapter(Mq_fv.instance, priority, packetType) {
    abstract override fun onPacketReceiving(event: PacketEvent)
    abstract override fun onPacketSending(event: PacketEvent?)
}