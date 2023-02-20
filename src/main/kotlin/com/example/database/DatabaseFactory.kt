package com.example.database

import com.example.database.entity.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection


suspend fun <T> query(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }


suspend fun <T> querySafe(block: suspend () -> T?): Result<T> =
    try {
        newSuspendedTransaction(Dispatchers.IO) {
            val result = block()
            result?.let { Result.success(it) } ?: Result.failure(Exception("Unable to perform query"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

fun <T : Table, M> T.insertSingle(serializer: (ResultRow) -> M, body: T.(InsertStatement<Number>) -> Unit): M? =
    InsertStatement<Number>(this)
        .apply {
            body(this)
            execute(TransactionManager.current())
        }.resultedValues
        ?.firstOrNull()
        ?.let(serializer)


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
            SchemaUtils.create(
                TokenEntity,
                ShowEntity,
                ShowExternalIdsEntity,
                SeasonsEntity,
                EpisodesEntity,
                ShowSeasonReference,
                ShowSeasonEpisodeReference
            )
        }
    }
}