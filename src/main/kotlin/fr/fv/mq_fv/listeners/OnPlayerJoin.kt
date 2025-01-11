package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import fr.fv.mq_fv.utils.ComponentFactory
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class OnPlayerJoin(): Listener {

    lateinit var event: PlayerJoinEvent
        private set

    private val playerRepository: PlayerRepository = PlayerRepository()

    private val messageFactory: ComponentFactory = ComponentFactory()

    private val allPlayersHandlerHolder: AllPlayersHandlerHolder = AllPlayersHandlerHolder.instance

    @EventHandler
    fun onPlayerJoinHandler(event: PlayerJoinEvent) {
        this.event = event

        val joinPlayer = this.event.player

        if( !playerRepository.isPlayerRegistered(joinPlayer) ) {
            playerRepository.registerPlayer(joinPlayer)
            this.broadcastWelcomeMessage()
        }

        //create a new playerHandler
        this.allPlayersHandlerHolder.createPlayerHandler(joinPlayer)
    }

    /**
     * Displays a welcome message in the chat
     */
    private fun broadcastWelcomeMessage()
    {
        //display the text after 2 seconds
        Bukkit.getScheduler().runTaskLater(Mq_fv.instance, Runnable {
            val welcomeMessage = messageFactory.getNewPlayerMessage(event.player.name)

            Bukkit.getOnlinePlayers().forEach() {
                player -> player.sendMessage(welcomeMessage)
            }
        }, 40L)
    }
}