package fr.fv.mq_fv.handlers

import org.bukkit.entity.Player

/**
 * Class to hold and manage all player handlers.
 * Singleton
 */
class AllPlayersHandlerHolder
private constructor() {

    /** All handlers  */
    var allHandlers: ArrayList<PlayerHandler> = ArrayList()
        private set

    //singleton
    companion object {
        val instance : AllPlayersHandlerHolder by lazy {
            AllPlayersHandlerHolder()
        }
    }

    /**
     * Returns the handler for the player
     */
    fun getPlayerHandler(mcPlayer: Player): PlayerHandler?
    {
        return this.allHandlers.find {
            playerHandler -> playerHandler.mcPlayer.uniqueId == mcPlayer.uniqueId
        }
    }

    /**
     * Creates a new player handler if it not exists
     */
    fun createPlayerHandler(mcPlayer: Player)
    {
        if( !this.hasPlayerHandler(mcPlayer) ) {
            val newPlayerHandler = PlayerHandler(mcPlayer)
            allHandlers.add(newPlayerHandler)
        }
    }

    /**
     * Removes a player handler from the list
     */
    fun removePlayerHandler(mcPlayer: Player)
    {
        if( this.hasPlayerHandler(mcPlayer) ) {
            val foundPlayerHandler = this.getPlayerHandler(mcPlayer)
            this.allHandlers.remove(foundPlayerHandler)
        }
    }

    /**
     * Returns true if the PlayerHandler for that player is already registered
     */
    fun hasPlayerHandler(mcPlayer: Player): Boolean
    {
        val filterResult = this.allHandlers.find {
            playerHandler -> playerHandler.mcPlayer.uniqueId == mcPlayer.uniqueId
        }

        return filterResult is PlayerHandler
    }

}

