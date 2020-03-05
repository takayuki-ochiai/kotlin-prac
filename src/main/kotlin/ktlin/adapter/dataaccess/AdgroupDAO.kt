package ktlin.adapter.dataaccess

import ktlin.entity.model.Adgroup
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object Adgroups : LongIdTable(name = "adgroups") {
    val categoryId: Column<Long> = long("category_id")
    val name: Column<String> = varchar("name", 255)
    val clickPrice: Column<Long?> = long("click_price").nullable()
}

class AdgroupDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AdgroupDAO>(Adgroups)
    var categoryId by Adgroups.categoryId
    var name by Adgroups.name
    var clickPrice by Adgroups.clickPrice

    fun toAdgroup(): Adgroup = Adgroup(
        id = id.value,
        categoryId = categoryId,
        name = name,
        clickPrice = clickPrice
    )
}
