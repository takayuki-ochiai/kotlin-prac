package ktlin.adapter.dataaccess

import ktlin.entity.model.Ad
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

// Exposedではテーブル定義を object にて行う
private object Ads : LongIdTable(name = "ads") {
    val adgroupId: Column<Long> = long("adgroup_id")
    val title: Column<String?> = varchar("title", 255).nullable()
    val landingPageUrl: Column<String> = varchar("landing_page_url", 1024)
    val imageUrl: Column<String> = varchar("image_url", 1024)
    val advertisingSubject: Column<String> = varchar("advertising_subject", 255)
}

class AdDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AdDAO>(Ads)

    var adgroupId by Ads.adgroupId
    var title by Ads.title
    var landingPageUrl by Ads.landingPageUrl
    var imageUrl by Ads.imageUrl
    var advertisingSubject by Ads.advertisingSubject

    fun toAd() : Ad = Ad(
        id = id.value,
        adgroupId = adgroupId,
        title = title,
        landingPageUrl = landingPageUrl,
        imageUrl = imageUrl,
        advertisingSubject = advertisingSubject
    )
}


