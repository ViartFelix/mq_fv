package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer

abstract class AbstractPacket {

    /** Packet */
    lateinit var packet: PacketContainer
        protected set

    /** ProtocolLib's manager */
    val manager = ProtocolLibrary.getProtocolManager()

}