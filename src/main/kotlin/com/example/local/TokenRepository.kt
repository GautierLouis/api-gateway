package com.example.local

import com.example.model.Token

interface TokenDao {
    suspend fun loadToken(name: String): Token?
    suspend fun saveToken(token: Token): Boolean
}