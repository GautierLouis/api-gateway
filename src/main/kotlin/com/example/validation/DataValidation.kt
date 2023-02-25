package com.example.validation

enum class DataValidationValue {
    NOT_EMPTY_STRING,
    NOT_EMPTY_LIST,
    NOT_NULL,
    GREATER_THAN_ZERO
}


enum class Priority {
    HIGH, MEDIUM, LOW
}

enum class Type {
    BACKDROP,
    CREATOR,
    RUNTIME,
    FIRST_AIR_DATE,
    NUMBER_OF_EPISODE,
    NUMBER_OF_SEASON,
    OVERVIEW,
    POSTER
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class DataValidation(
    val type: Type,
    val value: DataValidationValue,
    val priority: Priority = Priority.MEDIUM
)