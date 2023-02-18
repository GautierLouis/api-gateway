package com.example.utils

suspend fun <I, O> Result<I>.next(call: suspend (I) -> Result<O>): Result<O> {
    val unknownError = Throwable("Fail to parse next result")
    return try {
        if (isSuccess) {
            call(getOrThrow())
        } else {
            Result.failure(exceptionOrNull() ?: unknownError)
        }
    } catch (e: Exception) {
        Result.failure(unknownError)
    }
}