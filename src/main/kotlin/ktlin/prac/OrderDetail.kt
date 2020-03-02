package ktlin.prac

import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class OrderDetail(val id : Int, val orderId : Int, val goodsId : Int, val quantity : Int, val amount : BigDecimal)
