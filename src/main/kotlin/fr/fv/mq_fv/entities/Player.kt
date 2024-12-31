package fr.fv.mq_fv.entities

import org.ktorm.schema.Table
import org.ktorm.schema.decimal
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar


/**
 * Player DB entity (Ktorm)
 */
object Player : Table<Nothing>("player") {
    val uuid = uuid("uuid")
    val name = varchar("name")
    val money = decimal("money")
}