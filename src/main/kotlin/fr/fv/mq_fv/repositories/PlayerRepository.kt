package fr.fv.mq_fv.repositories

import fr.fv.mq_fv.entities.PlayerEntity
import fr.fv.mq_fv.interfaces.entities.PlayerTable
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.any
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import java.math.BigDecimal
import org.bukkit.entity.Player as McPlayer

class PlayerRepository : Repository() {

    /**
     * Checks if the player is registered in the DB
     */
    fun isPlayerRegistered(player: McPlayer): Boolean
    {
        return database.sequenceOf(PlayerEntity).any {
            it.uuid eq player.uniqueId
        }
    }

    /**
     * Registers a new player to the DB
     */
    fun registerPlayer(player: McPlayer)
    {
        database.sequenceOf(PlayerEntity).add(
            PlayerTable {
                uuid = player.uniqueId
                name = player.name
                money = BigDecimal.ZERO
            }
        )
    }

    /**
     * Returns the data of the player from the DB
     */
    fun getPlayer(player: McPlayer): PlayerTable?
    {
        return database
            .sequenceOf(PlayerEntity)
            .find { db -> player.uniqueId eq db.uuid }
    }


}