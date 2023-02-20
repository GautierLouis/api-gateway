package com.example.database

import com.example.database.entity.TokenEntity
import com.example.model.Token
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class TokenRepository : TokenDao {
    private fun resultRowToToken(row: ResultRow) = Token(
        name = row[TokenEntity.name],
        value = row[TokenEntity.value],
    )

    override suspend fun loadToken(name: String): Token? = query {
        TokenEntity.select(TokenEntity.name eq name)
            .map(::resultRowToToken)
            .singleOrNull()
    }


    override suspend fun saveToken(token: Token) = query {
        TokenEntity.deleteWhere { TokenEntity.name eq token.name }
        TokenEntity.insert {
            it[name] = token.name
            it[value] = token.value
        }.isIgnore
    }
}