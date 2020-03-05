package ktlin.entity.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Adgroup(
    var id: Long,
    var categoryId: Long,
    var name: String,
    var clickPrice: Long?
)