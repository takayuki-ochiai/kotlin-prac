package ktlin.prac

import java.math.BigDecimal

data class OrderDetail(val id : Int, val orderId : Int, val goodsId : Int, val quantity : Int, val amount : BigDecimal)