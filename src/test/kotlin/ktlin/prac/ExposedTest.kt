package ktlin.prac

import ktlin.adapter.dataaccess.AdDAO
import ktlin.adapter.dataaccess.AdgroupDAO
import ktlin.adapter.dataaccess.Adgroups
import ktlin.adapter.dataaccess.Ads
import ktlin.config.Config
import ktlin.config.Env
import org.assertj.core.api.SoftAssertions
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ExposedTest : Spek({
    beforeEachGroup {
        val config: Config = Config()
        val host = Database.connect(
            "jdbc:mysql://${config.env(Env.DB_HOST)}/${config.env(Env.DB_NAME)}",
            "com.mysql.jdbc.Driver",
            config.env(Env.DB_USER),
            config.env(Env.DB_PASSWORD)
        )
    }

    val softly = SoftAssertions()

    group("Adgroup DSL") {
        describe("selectAll") {
            it("count total rows") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    val allAdgroups = Adgroups.selectAll()
                }
            }

            it("fetch all columns") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    val allAdgroups = Adgroups.selectAll()
                    val adgroupIds = allAdgroups.map { it[Adgroups.id].value }
                    softly.assertThat(adgroupIds.size).isEqualTo(100)
                }
            }
        }

        describe("select") {
            it("select specified rows where name = 'hoge'") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.`name` = 'hoge'
                    val query = Adgroups.select { Adgroups.name eq "hoge" }
                    val adgroupIds = query.map { it[Adgroups.id].value }
                    softly.assertThat(adgroupIds.size).isEqualTo(0)
                }
            }

            it("select specified rows where id = 1") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.id = 1
                    val query = Adgroups.select { Adgroups.id eq 1.toLong() }
                    val adgroupIds = query.map { it[Adgroups.id].value }
                    softly.assertThat(adgroupIds.size).isEqualTo(1)
                }
            }

            it("allows you to select specific columns or/and expressions") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    // SELECT adgroups.id, adgroups.`name` FROM adgroups WHERE adgroups.id = 1
                    val adgroupIds =
                        Adgroups.slice(Adgroups.id, Adgroups.name)
                            .select { Adgroups.id eq 1.toLong() }
                            .map { it[Adgroups.id].value }

                    softly.assertThat(adgroupIds.size).isEqualTo(1)
                }
            }

            it("allows you to select specific columns and multi expressions") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    val condition1 = Adgroups.id greaterEq 50.toLong()
                    val condition2 = Adgroups.name eq "広告グループ10"
                    // SELECT adgroups.id, adgroups.`name` FROM adgroups WHERE adgroups.id >= 50 and name = '広告グループ10'
                    val andAdgroups =
                        Adgroups.slice(Adgroups.id, Adgroups.name)
                            .select { condition1 and condition2 }
                            .map { it[Adgroups.id].value }

                    softly.assertThat(andAdgroups.size).isEqualTo(0)

                    // SELECT adgroups.id, adgroups.`name` FROM adgroups WHERE adgroups.id >= 50 and name = '広告グループ10'
                    val orAdgroups =
                        Adgroups.slice(Adgroups.id, Adgroups.name)
                            .select { condition1 or condition2 }
                            .map { it[Adgroups.id].value }

                    softly.assertThat(orAdgroups.size).isEqualTo(52)

                    // can also write 'and condition'
                    // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.id >= 50 AND adgroups.`name` = '広告グループ10'
                    val andAdgroups2 =
                        Adgroups.selectAll()
                            .andWhere { condition1 }
                            .andWhere { condition2 }
                            .map { it[Adgroups.id].value }

                    softly.assertThat(andAdgroups2.size).`as`("can also write 'and condition'").isEqualTo(0)

                    // can also write 'or condition'
                    // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.id >= 50 OR adgroups.`name` = '広告グループ10'
                    val orAdgroups2 =
                        Adgroups.selectAll()
                            .andWhere { condition1 }
                            .orWhere { condition2 }
                            .map { it[Adgroups.id].value }

                    softly.assertThat(orAdgroups2.size).`as`("can also write 'or condition'").isEqualTo(52)
                }
            }
        }

        group("Adgroup DAO") {
            describe("selectAll") {
                it("count total rows") {
                    transaction {
                        addLogger(StdOutSqlLogger)
                        // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups
                        val count = AdgroupDAO.count()
                        softly.assertThat(count).isEqualTo(100)
                    }
                }
                it("fetch all columns") {
                    transaction {
                        addLogger(StdOutSqlLogger)
                        // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups
                        val allAdgroups = AdgroupDAO.all().map(AdgroupDAO::toAdgroup)
                        softly.assertThat(allAdgroups.size).isEqualTo(100)
                    }
                }
            }

            describe("select") {
                it("allows you to select specific columns or/and expressions") {
                    transaction {
                        addLogger(StdOutSqlLogger)
                        // SELECT adgroups.id, adgroups.`name` FROM adgroups WHERE adgroups.id = 1
                        val adgroups =
                            AdgroupDAO.find { Adgroups.id eq 1.toLong() }
                                .map(AdgroupDAO::toAdgroup)
                        softly.assertThat(adgroups.size).isEqualTo(1)

                        // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.id = 1
                        val adgroup = AdgroupDAO.findById(1.toLong())?.toAdgroup()
                        softly.assertThat(adgroup?.id).isEqualTo(1.toLong())
                    }
                }

                it("allows you to select with multi expressions") {
                    transaction {
                        addLogger(StdOutSqlLogger)
                        val condition1 = Adgroups.id greaterEq 50.toLong()
                        val condition2 = Adgroups.name eq "広告グループ10"
                        // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.id >= 50 AND adgroups.`name` = '広告グループ10'
                        var adgroups =
                            AdgroupDAO.find { condition1 and condition2 }
                                .map(AdgroupDAO::toAdgroup)
                        softly.assertThat(adgroups.size).isEqualTo(0)

                        // SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.id >= 50 OR adgroups.`name` = '広告グループ10'
                        adgroups = AdgroupDAO.find { condition1 or condition2 }
                            .map(AdgroupDAO::toAdgroup)
                        softly.assertThat(adgroups.size).isEqualTo(52)
                    }
                }
            }
        }
    }

    group("Ads DSL") {
        describe("join") {
            it("allows you to select with join") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    val adsQuery = Ads.innerJoin(Adgroups).slice(Ads.columns).selectAll().limit(10)
                    // DSLでjoinとかしたqueryを元にDAO生成する
                    val ads = AdDAO.wrapRows(adsQuery).map(AdDAO::toAd)

                    softly.assertThat(ads.size).isEqualTo(10)
                    softly.assertThat(ads.first().id).isEqualTo(1)
                }
            }

            it("allows you to select with join and expressions") {
                transaction {
                    addLogger(StdOutSqlLogger)
                    val adsQuery =
                        Ads.innerJoin(Adgroups).slice(Ads.columns)
                            .selectAll()
                            .andWhere { Adgroups.id greaterEq 90.toLong() }
                    // DSLでjoinとかしたqueryを元にDAO生成する
                    // with(AdDAO::adgroup)でAdgroupDAOをeager loadするようにしている
                    // SQL: SELECT ads.id, ads.adgroup_id, ads.title, ads.landing_page_url, ads.image_url, ads.advertising_subject FROM ads INNER JOIN adgroups ON adgroups.id = ads.adgroup_id WHERE adgroups.id >= 90
                    // SQL: SELECT adgroups.id, adgroups.category_id, adgroups.`name`, adgroups.click_price FROM adgroups WHERE adgroups.id IN (100, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99)
                    val ads = AdDAO.wrapRows(adsQuery).with(AdDAO::adgroup).map(AdDAO::toAd)
                    softly.assertThat(ads.size).isEqualTo(1100)
                }
            }
        }
    }
})