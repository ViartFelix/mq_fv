package fr.fv.mq_fv.interfaces.entities

import org.ktorm.entity.Entity
import java.math.BigDecimal
import java.util.UUID

interface PlayerTable : Entity<PlayerTable> {
    var uuid: UUID
    var name: String
    var money: BigDecimal

    companion object : Entity.Factory<PlayerTable>()
}