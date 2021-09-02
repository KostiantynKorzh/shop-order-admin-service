package study.me.db

import org.ktorm.database.Database


class DatabaseConfig {

    fun initDb(): Database {
        val host = System.getenv("MYSQL_HOST")
        return Database.connect(
            "jdbc:mysql://$host:3306/orders_db?allowPublicKeyRetrieval=true&useSSL=false&?zeroDateTimeBehavior=convertToNull",
            user = "root", password = System.getenv("MYSQL_ROOT_PASSWORD")
        )
    }

}