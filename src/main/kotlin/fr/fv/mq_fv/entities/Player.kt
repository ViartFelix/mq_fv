package fr.fv.mq_fv.entities

import fr.fv.mq_fv.entities.e_interfaces.PlayerTable
import org.ktorm.schema.Table
import org.ktorm.schema.decimal
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar


/**
 * Player DB entity (Ktorm)
 */
object Player : Table<PlayerTable>("player") {
    val uuid = uuid("uuid").bindTo { it.uuid }
    val name = varchar("name").bindTo { it.name }
    val money = decimal("money").bindTo { it.money }

    override fun toString(): String {
        return "Player(uuid=$uuid, name=$name, money=$money)"
    }
}
