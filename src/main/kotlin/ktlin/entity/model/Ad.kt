package ktlin.entity.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ad(
    val id : Long,
    val adgroupId : Long,
    val title : String?,
    val landingPageUrl : String,
    val imageUrl : String,
    val advertisingSubject : String
    )