package ktlin.prac

import java.math.BigDecimal

data class Order(val id : Int, val customer : Customer, val totalAmount : BigDecimal, val orderDetailList: ArrayList<OrderDetail>)
