package ktlin.prac

import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class Order(val id: Int, val customer: Customer, val totalAmount: BigDecimal, val orderDetailList: List<OrderDetail>)
