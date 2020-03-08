package ktlin.prac

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import ktlin.entity.model.Ad
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class GsonTest : Spek({
    group("GSON") {
        val ad by memoized {
            Ad(
                1,
                1,
                "title",
                "http://example.com",
                "http://example.com/img.gif",
                "advertiser"
            )
        }
        val jsonStr = """{"id":1,"adgroupId":1,"title":"title","landingPageUrl":"http://example.com","imageUrl":"http://example.com/img.gif","advertisingSubject":"advertiser"}"""

        val customer = Customer(1, "Takayuki", "Ochiai", "1990-12-13")
        val orderDetails = arrayListOf<OrderDetail>(
            OrderDetail(1, 1, 1, 1, BigDecimal(100)),
            OrderDetail(1, 1, 2, 2, BigDecimal(400))
        )
        val orderDetailsJsonStr = """[{"id":1,"orderId":1,"goodsId":1,"quantity":1,"amount":100},{"id":1,"orderId":1,"goodsId":2,"quantity":2,"amount":400}]"""
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

            context("リストデータ") {
                it("データクラスのリストをJSON文字列に変換する") {
                    assertEquals(orderDetailsJsonStr, gson.toJson(orderDetails))
                }
            }
        }

        describe("fromJson") {
            context("普通のJSON文字列") {
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
            }

            context("JSONとしてparseできない文字列") {
                it("JSONとしてparseできない文字列だった場合失敗する") {
                    val jsonInvalidStr = """{"id":1,"adgroupId":1,"title":"title","landingPageUrl":"""
                    assertFailsWith<JsonSyntaxException> { gson.fromJson(jsonInvalidStr, Ad::class.java) }
                }
            }

            context("parseできる文字列だがフィールドが不足している") {
                it("フィールドが不足している場合、Non Nullableな属性にもnullが入る") {
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
            }

            context("ネストした複雑な文字列") {
                it("ネストした複雑な文字列もparseできる") {
                    val orderFromJson = gson.fromJson<Order>(orderJsonStr, Order::class.java)
                    assertEquals(order, orderFromJson)
                }
            }

            context("リスト型のJSON文字列") {
                it("リスト型のJSON文字列もparseできる") {
                    // コンパイルが通りませーん！
                    // List::class.java は書けても List<OrderDetail>::class.javaは通らない
                    // assertEquals(orderDetails, gson.fromJson(orderDetailsJsonStr, ArrayList<OrderDetail>::class.java))

                    // コンパイルは通るけどOrderDetailをジェネリクスに指定していないから所々Intを期待しているところが少数になる
                    assertNotEquals(orderDetails, gson.fromJson(orderDetailsJsonStr, ArrayList::class.java))

                    // TypeToken<ArrayList<OrderDetail>>を継承した無名クラスのインスタンスを使ってデシリアライズするとちゃんと変換できる
                    val typeToken = object : TypeToken<ArrayList<OrderDetail>>() {}
                    assertEquals(orderDetails, gson.fromJson(orderDetailsJsonStr, typeToken.type))
                }
            }
        }
    }
})
