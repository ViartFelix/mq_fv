package fr.fv.mq_fv.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Custom event to represent a request to refresh the tab of every player on the server
 */
class TabRefreshRequestEvent: Event() {

    companion object {
        private val HANDLER_LIST: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLER_LIST
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLER_LIST;
    }
}