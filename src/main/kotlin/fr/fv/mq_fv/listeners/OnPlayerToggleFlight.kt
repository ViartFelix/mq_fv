package fr.fv.mq_fv.listeners

import fr.fv.mq_fv.handlers.AllPlayersHandlerHolder
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import java.util.UUID

class OnPlayerToggleFlight : Listener {

    companion object {
        // below this horizontal distance per tick, the player is considered not to be pressing any movement key
        private const val MOVEMENT_THRESHOLD = 0.02
        private const val LOOK_BOOST = 0.4
    }

    /**
     * Squared horizontal distance moved on the last recorded tick, per player.
     * player.velocity does not reflect WASD movement (it is only set by knockback,
     * explosions, projectiles, etc.), so we track real movement via move deltas instead.
     */
    private val lastHorizontalMoveSquared = HashMap<UUID, Double>()

    @EventHandler
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        val player = event.player

        if( player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR ) {
            return
        }

        event.isCancelled = true
        player.isFlying = false

        if( player.isOnGround ) {
            return
        }

        val playerStats = AllPlayersHandlerHolder.instance.getPlayerHandler(player)!!.playerStats

        if( 0 >= playerStats.jumpAmount ) {
            return
        }

        playerStats.useJump()
        this.applyDoubleJumpBoost(player)
    }

    /**
     * Resets the jump charges once the player is back on the ground
     */
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val to = event.to

        val dx = to.x - event.from.x
        val dz = to.z - event.from.z
        lastHorizontalMoveSquared[player.uniqueId] = dx * dx + dz * dz

        if( player.isOnGround || player.isInLava || player.isInWater ) {
            AllPlayersHandlerHolder.instance.getPlayerHandler(player)?.playerStats?.resetJumpCount()
        }
    }

    private fun applyDoubleJumpBoost(player: Player) {
        val velocity = player.velocity.clone()
        val horizontalMoveSquared = lastHorizontalMoveSquared[player.uniqueId] ?: 0.0

        if( horizontalMoveSquared > MOVEMENT_THRESHOLD * MOVEMENT_THRESHOLD ) {
            val look = player.location.direction.clone()
            look.y = 0.0

            if( 0.0 < look.lengthSquared() ) {
                look.normalize()
            }

            velocity.x += look.x * LOOK_BOOST
            velocity.z += look.z * LOOK_BOOST
        }

        velocity.y = 0.8

        player.velocity = velocity
    }
}
