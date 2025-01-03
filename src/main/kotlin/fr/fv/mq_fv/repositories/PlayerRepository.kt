package fr.fv.mq_fv.repositories

import fr.fv.mq_fv.entities.Players
import fr.fv.mq_fv.entities.Players.uuid
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.any
import org.ktorm.entity.sequenceOf
import java.math.BigDecimal
import org.bukkit.entity.Player as McPlayer

class PlayerRepository : Repository() {

    /**
     * Checks if the player is registered in the DB
     */
    fun isPlayerRegistered(player: McPlayer): Boolean
    {
        return database.sequenceOf(Players).any {
            it.uuid eq player.uniqueId
        }
    }

    /**
     * Registers a new player to the DB
     */
    fun registerPlayer(player: McPlayer)
    {
        database.sequenceOf(Players).add(
            PlayerTable {
                uuid = player.uniqueId
                name = player.name
                money = BigDecimal.ZERO
            }
        )
    }

    /**
     * Tran
     */
    fun mcPlayerAsEntity()
    {

    }
}