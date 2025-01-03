package fr.fv.mq_fv.events

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.utils.MessageFactory
import io.papermc.paper.text.PaperComponents
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class OnPlayerJoin(): Listener {

    lateinit var event: PlayerJoinEvent
        private set

    val playerRepository: PlayerRepository = PlayerRepository()

    val messageFactory: MessageFactory = MessageFactory()

    @EventHandler
    fun onPlayerJoinHandler(event: PlayerJoinEvent) {
        this.event = event

        val joinPlayer = this.event.player

        if( !playerRepository.isPlayerRegistered(joinPlayer) ) {
            playerRepository.registerPlayer(joinPlayer)
            this.broadcastWelcomeMessage()
        }
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