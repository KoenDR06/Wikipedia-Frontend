package me.koendev.wiki.database

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

data class Article(val title: String)

class ArticleItem(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ArticleItem>(ArticleService.Articles)

    var title by ArticleService.Articles.title
}

class ArticleService(datasource: HikariDataSource) {
    object Articles : IntIdTable() {
        val title = varchar("title", length = 256)
    }

    init {
        Database.connect(datasource)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Articles)
        }
    }

    fun read(title: String): Int? {
        var id: Int? = null
        transaction {
            id = Articles.select {
                Articles.title eq title
            }.map {
                it[Articles.id]
            }.singleOrNull()?.value
        }
        return id
    }

    fun read(id: Int): String? {
        var title: String? = null
        transaction {
            title = Articles.select {
                Articles.id eq id
            }.map {
                it[Articles.title]
            }.singleOrNull()
        }
        return title
    }
}