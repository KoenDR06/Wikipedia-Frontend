package me.koendev.wiki.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import me.koendev.wiki.database.ArticleService
import me.koendev.wiki.database.LinkService
import me.koendev.wiki.dotEnv
import org.jetbrains.exposed.sql.Database

lateinit var database: Database
lateinit var articleService: ArticleService
lateinit var linkService: LinkService


fun Application.configureDatabases() {
    val config = HikariConfig()
    config.jdbcUrl = dotEnv["DB_URL"]
    config.username = dotEnv["DB_USER"]
    config.password = dotEnv["DB_PASSWORD"]
    config.driverClassName = "org.mariadb.jdbc.Driver"
    config.maximumPoolSize = 10
    config.connectionTimeout = 30000
    config.idleTimeout = 600000
    val datasource = HikariDataSource(config)

    articleService = ArticleService(datasource)
    linkService = LinkService(datasource)
}
