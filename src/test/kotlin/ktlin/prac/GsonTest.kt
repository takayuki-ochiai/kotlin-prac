package ktlin.prac

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class GsonTest : Spek({
    group("GSON") {
        val ad by memoized {
            Ad(1,
                1,
                "title",
                "http://example.com",
                "http://example.com/img.gif",
                "advertiser")
        }
        val jsonStr = """{"id":1,"adgroupId":1,"title":"title","landingPageUrl":"http://example.com","imageUrl":"http://example.com/img.gif","advertisingSubject":"advertiser"}"""

        val customer = Customer(1, "Takayuki", "Ochiai", "1990-12-13")
        val orderDetails = arrayListOf<OrderDetail>(
            OrderDetail(1, 1, 1, 1, BigDecimal(100)),
            OrderDetail(1, 1, 2, 2, BigDecimal(400))
        )
        val order = Order(1, customer, BigDecimal(500), orderDetails)
        val orderJsonStr = """{"id":1,"customer":{"id":1,"firstName":"Takayuki","lastName":"Ochiai","birthday":"1990-12-13"},"totalAmount":500,"orderDetailList":[{"id":1,"orderId":1,"goodsId":1,"quantity":1,"amount":100},{"id":1,"orderId":1,"goodsId":2,"quantity":2,"amount":400}]}"""

        val gson = Gson()

        describe("toJson") {
            context("シンプルなクラス") {
                it("データクラスをJSON文字列に変換する") {
                    assertEquals(jsonStr, gson.toJson(ad))
                }
            }

            context("ネストしたクラス") {
                it("データクラスをJSON文字列に変換する") {
                    assertEquals(orderJsonStr, gson.toJson(order))
                }
            }
        }

        describe("fromJson") {
            it("JSON文字列をデータクラスに変換する") {
                val adFromJson = gson.fromJson(jsonStr, Ad::class.java)
                assertEquals(ad, adFromJson)
                assertEquals(ad.id, adFromJson.id)
                assertEquals(ad.adgroupId, adFromJson.adgroupId)
                assertEquals(ad.title, adFromJson.title)
                assertEquals(ad.landingPageUrl, adFromJson.landingPageUrl)
                assertEquals(ad.imageUrl, adFromJson.imageUrl)
                assertEquals(ad.advertisingSubject, adFromJson.advertisingSubject)
            }

            it("JSONとしてparseできない文字列だった場合失敗する") {
                val jsonInvalidStr = """{"id":1,"adgroupId":1,"title":"title","landingPageUrl":"""
                assertFailsWith<JsonSyntaxException> { gson.fromJson(jsonInvalidStr, Ad::class.java)}
            }

            it("parseできる文字列だった場合") {
                val jsonShort = """{"id":1,"adgroupId":1,"title":"title"}"""
                val adFromJson = gson.fromJson(jsonShort, Ad::class.java)
                assertEquals(1, adFromJson.id)
                assertEquals(1, adFromJson.adgroupId)
                assertEquals("title", adFromJson.title)
                // Nullを許容していないところにもNullで入ってしまう。。。
                assertNull(adFromJson.landingPageUrl)
                assertNull(adFromJson.imageUrl)
                assertNull(adFromJson.advertisingSubject)
            }

            it("ネストした複雑な文字列") {
                val orderFromJson = gson.fromJson<Order>(orderJsonStr, Order::class.java)
                assertEquals(order, orderFromJson)
            }
        }
    }
})