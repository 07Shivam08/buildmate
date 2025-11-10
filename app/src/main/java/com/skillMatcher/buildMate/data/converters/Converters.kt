package com.skillMatcher.buildMate.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

//    @TypeConverter
//    fun fromList(list: List<String>): String {
//        return Gson().toJson(list)
//    }
//
//    @TypeConverter
//    fun toList(json: String): List<String> {
//        val type = object : TypeToken<List<String>>() {}.type
//        return Gson().fromJson(json, type)
//    }

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<String>?): String = gson.toJson(list ?: emptyList<String>())

    @TypeConverter
    fun toList(json: String?): List<String> =
        gson.fromJson(json ?: "[]", object : TypeToken<List<String>>() {}.type)
}
