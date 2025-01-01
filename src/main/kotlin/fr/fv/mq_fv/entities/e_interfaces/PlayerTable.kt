package fr.fv.mq_fv.entities.e_interfaces

import org.ktorm.entity.Entity
import java.math.BigDecimal
import java.util.UUID

interface PlayerTable : Entity<PlayerTable> {
    val uuid: UUID
    val name: String
    val money: BigDecimal
}