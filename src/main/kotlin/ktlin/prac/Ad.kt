package ktlin.prac

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ad(
    val id : Int,
    val adgroupId : Int,
    val title : String,
    val landingPageUrl : String,
    val imageUrl : String,
    val advertisingSubject : String
    )