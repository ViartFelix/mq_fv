package fr.fv.mq_fv.protocolLib

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer

abstract class AbstractPacket {

    /** Packet */
    lateinit var packet: PacketContainer
        protected set

    /** ProtolLib helper */
    var protocolLibHelper: ProtocolManager = ProtocolLibrary.getProtocolManager()

    /** ProtocolLib's manager */
    val manager = ProtocolLibrary.getProtocolManager()

}