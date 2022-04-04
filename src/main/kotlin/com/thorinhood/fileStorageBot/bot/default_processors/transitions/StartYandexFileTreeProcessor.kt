package com.thorinhood.fileStorageBot.bot.default_processors.transitions

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.baseProcessors.YandexBaseEntitiesProcessor
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor

@Processor
class StartYandexFileTreeProcessor(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
"startFileTree",
    ProcSpaces.DEFAULT
) {

    override fun processInner(session: Session, update: Update): ProcessResult = getEntities(session)

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Список файлов"
    }
}