package ktlin.prac

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Cloth(val characters: String, val price: Int)
