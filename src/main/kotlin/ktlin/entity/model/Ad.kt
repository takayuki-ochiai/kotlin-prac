package ktlin.entity.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ad(
    var id: Long,
    var adgroupId: Long,
    var title: String?,
    var landingPageUrl: String,
    var imageUrl: String,
    var advertisingSubject: String,

    var adgroup: Adgroup? = null
)
