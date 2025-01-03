package fr.fv.mq_fv.repositories

import fr.fv.mq_fv.utils.DatabaseWrapper
import org.ktorm.database.Database

abstract class Repository {
    protected val database: Database = DatabaseWrapper.instance.connection
}