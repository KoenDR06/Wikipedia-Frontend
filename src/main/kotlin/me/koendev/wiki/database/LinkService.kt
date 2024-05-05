package me.koendev.wiki.database

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

data class Link(val from: Int, val to: Int)

class LinkItem(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<LinkItem>(LinkService.Links)

    var fromID by LinkService.Links.fromID
    var toID by LinkService.Links.toID
}

class LinkService(datasource: HikariDataSource) {
    object Links : IntIdTable() {
        val fromID = integer("from_id")
        val toID = integer("to_id")
    }

    init {
        Database.connect(datasource)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Links)
        }
    }

    fun read(from: Int, to: Int): Int? {
        var id: Int? = null
        transaction {
            id = Links.select {
                (Links.fromID eq from) and (Links.toID eq to)
            }.map {
                it[Links.id]
            }.singleOrNull()?.value
        }
        return id
    }

    fun readChildren(from: Int): List<Int> {
        var children: List<Int> = listOf()
        transaction {
            children = Links.select {
                Links.fromID eq from
            }.map {
                it[Links.toID]
            }
        }
        return children
    }

    fun readParents(to: Int): List<Int> {
        var children: List<Int> = listOf()
        transaction {
            children = Links.select {
                Links.toID eq to
            }.map {
                it[Links.fromID]
            }
        }
        return children
    }
}