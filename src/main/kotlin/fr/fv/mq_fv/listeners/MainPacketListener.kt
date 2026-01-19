package fr.fv.mq_fv.listeners

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketAdapter

/**
 * The packet listener manager / instanciator.
 * Singleton
 */
class MainPacketListener {

    private val manager: ProtocolManager = ProtocolLibrary.getProtocolManager()

    private var areListenersReady: Boolean = false

    //singleton
    companion object {
        val instance : MainPacketListener by lazy {
            MainPacketListener()
        }
    }

    private val allListeners: List<PacketAdapter> = listOf()

    fun registerAllPacketListeners()
    {
        if( !areListenersReady ) {
            allListeners.forEach {
                manager.addPacketListener(it)
            }

            areListenersReady = true
        }
    }
}