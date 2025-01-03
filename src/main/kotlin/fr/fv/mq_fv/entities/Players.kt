package fr.fv.mq_fv.entities

import fr.fv.mq_fv.interfaces.entities.PlayerTable
import org.ktorm.schema.Table
import org.ktorm.schema.decimal
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar


/**
 * Player DB entity (Ktorm)
 */
object Players : Table<PlayerTable>("player") {
    var uuid = uuid("uuid").bindTo { it.uuid }
    var name = varchar("name").bindTo { it.name }
    var money = decimal("money").bindTo { it.money }

    override fun toString(): String {
        return "Player(uuid=$uuid, name=$name, money=$money)"
    }
}
