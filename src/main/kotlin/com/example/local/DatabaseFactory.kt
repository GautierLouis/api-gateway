package com.example.local

import com.example.local.entity.ShowEntity
import com.example.local.entity.ShowIdEntity
import com.example.local.entity.TokenEntity
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.sql.Connection


suspend fun <T> query(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

object DatabaseFactory {

    fun initInFile() {

        val url = "jdbc:sqlite:/Users/louisgautier/Desktop/test/data.db"
        init(url)
    }

    fun initInMemory() {
        val url = "jdbc:sqlite:file:test?mode=memory&cache=shared"
        init(url)
    }

    private fun init(url: String) {
        val config = DatabaseConfig {
            sqlLogger = Slf4jSqlDebugLogger
        }

        val database = Database.connect(url, "org.sqlite.JDBC", databaseConfig = config)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction(database) {
            SchemaUtils.create(TokenEntity, ShowEntity, ShowIdEntity)
        }
    }
}