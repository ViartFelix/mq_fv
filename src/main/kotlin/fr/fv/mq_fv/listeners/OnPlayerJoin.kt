package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.Mq_fv
import fr.fv.mq_fv.repositories.PlayerRepository
import fr.fv.mq_fv.utils.MessageFactory
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class OnPlayerJoin(): Listener {

    lateinit var event: PlayerJoinEvent
        private set

    private val playerRepository: PlayerRepository = PlayerRepository()

    private val messageFactory: MessageFactory = MessageFactory()

    @EventHandler
    fun onPlayerJoinHandler(event: PlayerJoinEvent) {
        this.event = event

        val joinPlayer = this.event.player

        this.applyPotionEffects()

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

    /**
     * Displays the necessary potion effects to the player once logged in
     */
    private fun applyPotionEffects()
    {
        val player = this.event.player

        val infiniteNightVisionEffect = PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 1)

        player.addPotionEffect(infiniteNightVisionEffect)
    }

}