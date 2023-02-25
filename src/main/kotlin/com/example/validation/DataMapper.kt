package com.example.validation

import com.example.remote.tmdb.model.TMDBShow
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlin.reflect.jvm.kotlinProperty

class DataMapper() {

    fun checkDataValidity(input: TMDBShow): Result<TMDBShow> {
        val requiredField = input::class.java.declaredFields
            .filter { it.isAnnotationPresent(DataValidation::class.java) }

        val nonValidField = requiredField.mapNotNull { field ->
            val anno = field.getAnnotation(DataValidation::class.java)!!
            val fieldValue: Any? = field.kotlinProperty!!.getter.call(input)
            val isValid = when (anno.value) {
                DataValidationValue.NOT_NULL -> {
                    fieldValue != null
                }

                DataValidationValue.NOT_EMPTY_STRING -> {
                    (fieldValue as? String)?.isNotBlank() == true
                }

                DataValidationValue.NOT_EMPTY_LIST -> {
                    (fieldValue as? List<*>)?.isNotEmpty() == true
                }

                DataValidationValue.GREATER_THAN_ZERO -> {
                    ((fieldValue as? Number)?.toInt() ?: 0) > 0
                }
            }

            if (!isValid) {
                anno.type to anno.priority
            } else null
        }

        val hasHighPriority = nonValidField.any { it.second == Priority.HIGH }

        return Result.success(input)// TODO

    }

    private fun mapDataToModel(input: TMDBShow) {
        // TODO Check Integrity

        val nextAired = input.nextEpisodeToAir?.airDate?.atTime(LocalTime.fromSecondOfDay(0))
//        Show(
//            input.name,
//            input.firstAirDate,
//            input.lastAirDate,
//            nextAired ?: TODO(),
//            input.posterPath ?: TODO(),
//            input.originCountry.first(),
//            input.originalLanguage,
//            ShowStatus.valueOf(input.status),
//            input.episodeRunTime.average(),
//            input.overview,
//            input.genres.map { Genre(it.id.toString(), it.name) },
//            emptyList(), // TODO()
//            input.seasons.map {
//                Season(
//                    it.id,
//                    input.id,
//                    it.seasonNumber,
//                    it.posterPath ?: TODO(),
//
//                    )
//            },
//            listOf(ShowID(input.id.toString(), ShowSourceName.TMDB)),
//
//
//        )

    }
}