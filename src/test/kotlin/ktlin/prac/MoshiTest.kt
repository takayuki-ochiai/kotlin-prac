package ktlin.prac

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.EOFException
import java.lang.Exception
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import ktlin.adapter.moshi.ArrayListAdapter
import ktlin.adapter.moshi.BigDecimalAdapter
import ktlin.entity.model.Ad
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class MoshiTest : Spek({
    group("Moshi") {
        val ad by memoized {
            Ad(1,
                    1,
                    "title",
                    "http://example.com",
                    "http://example.com/img.gif",
                    "advertiser")
        }
        val adJson = """{"id":1,"adgroupId":1,"title":"title","landingPageUrl":"http://example.com","imageUrl":"http://example.com/img.gif","advertisingSubject":"advertiser"}"""

        val customer = Customer(1, "Takayuki", "Ochiai", "1990-12-13")

        val orderDetails = arrayListOf(
                OrderDetail(1, 1, 1, 1, BigDecimal(100)),
                OrderDetail(1, 1, 2, 2, BigDecimal(400))
        )
        val orderDetailsJson = """[{"id":1,"orderId":1,"goodsId":1,"quantity":1,"amount":"100"},{"id":1,"orderId":1,"goodsId":2,"quantity":2,"amount":"400"}]"""

        val order = Order(1, customer, BigDecimal(500), orderDetails)
        val orderJson = """{"id":1,"customer":{"id":1,"firstName":"Takayuki","lastName":"Ochiai","birthday":"1990-12-13"},"totalAmount":"500","orderDetailList":[{"id":1,"orderId":1,"goodsId":1,"quantity":1,"amount":"100"},{"id":1,"orderId":1,"goodsId":2,"quantity":2,"amount":"400"}]}"""

        describe("toJson") {
            context("シンプルなクラス") {
                it("データクラスをJSON文字列に変換する") {
                    val builder = Moshi.Builder().build()
                    assertEquals(adJson, builder.adapter(Ad::class.java).toJson(ad))
                }
            }

            context("BigDecimalがプロパティにあるクラス") {
                it("データクラスをJSON文字列に変換する") {
                    val builder = Moshi.Builder().add(BigDecimalAdapter).build()
                    val orderDetail = OrderDetail(1, 1, 1, 1, BigDecimal(100))
                    val orderDetailJson = """{"id":1,"orderId":1,"goodsId":1,"quantity":1,"amount":"100"}"""
                    val jsonStr = builder.adapter(OrderDetail::class.java).toJson(orderDetail)
                    assertEquals(orderDetailJson, jsonStr)
                }
            }

            context("ネストしたクラス") {
                it("データクラスをJSON文字列に変換する") {
                    val arrayListAdapter = ArrayListAdapter.create()
                    val builder = Moshi.Builder().add(BigDecimalAdapter).add(arrayListAdapter).build()
                    val jsonStr = builder.adapter(Order::class.java).toJson(order)
                    assertEquals(orderJson, jsonStr)
                }
            }

            context("リストデータ") {
                it("データクラスのリストをJSON文字列に変換する") {
                    val arrayListAdapter = ArrayListAdapter.create()
                    val builder = Moshi.Builder().add(BigDecimalAdapter).add(arrayListAdapter).build()
                    val typed = Types.newParameterizedType(ArrayList::class.java, OrderDetail::class.java)
                    val adapter = builder.adapter<ArrayList<OrderDetail>>(typed)
                    assertEquals(orderDetailsJson, adapter.toJson(orderDetails))
                }
            }
        }

        describe("fromJson") {
            context("普通のJSON文字列") {
                it("JSON文字列をデータクラスに変換する") {
                    val builder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val adFromJson: Ad = builder.adapter(Ad::class.java).fromJson(adJson) ?: throw Exception("adJsonのparseに失敗")
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
                    val builder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    assertFailsWith<EOFException> { builder.adapter(Ad::class.java).fromJson(jsonInvalidStr) }
                }
            }

            context("parseできる文字列だがフィールドが不足している") {
                it("フィールドが不足している場合、KotlinJsonAdapterFactoryを指定しているのでJsonDataExceptionが発生する") {
                    val jsonShort = """{"id":1,"adgroupId":1,"title":"title"}"""
                    val builder = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    assertFailsWith<JsonDataException> { builder.adapter(Ad::class.java).fromJson(jsonShort) }
                }
            }

            context("ネストした複雑な文字列") {
                it("ネストした複雑な文字列もparseできる") {
                    val builder = Moshi.Builder().add(KotlinJsonAdapterFactory()).add(BigDecimalAdapter).build()
                    val orderFromJson: Order = builder.adapter(Order::class.java).fromJson(orderJson) ?: throw Exception("orderJsonのparseに失敗")
                    assertEquals(order, orderFromJson)
                }
            }

            context("リスト型のJSON文字列") {
                it("リスト型のJSON文字列もparseできる") {
                    val arrayListAdapter = ArrayListAdapter.create()
                    val builder = Moshi.Builder().add(KotlinJsonAdapterFactory()).add(BigDecimalAdapter).add(arrayListAdapter).build()
                    val typed = Types.newParameterizedType(ArrayList::class.java, OrderDetail::class.java)
                    val adapter = builder.adapter<ArrayList<OrderDetail>>(typed)
                    val orderDetailsFromJson: ArrayList<OrderDetail> = adapter.fromJson(orderDetailsJson) ?: throw Exception("orderDetailsJsonのparseに失敗")
                    assertEquals(orderDetails, orderDetailsFromJson)
                }
            }
        }
    }
})
