package com.thorinhood.fileStorageBot.chatBotEngine.sessions.mongo

import com.thorinhood.fileStorageBot.chatBotEngine.Utils
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Profile("mongo")
@Configuration
@EnableMongoRepositories
class MongoDbConfig : Logging {

    init {
        Utils.initLog(logger, "mongo")
    }

}

@Profile("!mongo")
@Configuration
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration::class])
class NotMongoDbConfig