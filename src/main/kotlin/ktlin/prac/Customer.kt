package ktlin.prac

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Customer(val id: Int, val firstName: String, val lastName: String, val birthday: String)
