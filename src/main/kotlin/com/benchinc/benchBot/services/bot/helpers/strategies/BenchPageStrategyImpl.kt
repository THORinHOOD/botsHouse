package com.benchinc.benchBot.services.bot.helpers.strategies

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.default_pipeline.BackPageProcessor
import com.benchinc.benchBot.services.bot.processors.default_pipeline.ForwardPageProcessor
import com.db.benchLib.data.Bench
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service
import kotlin.math.min

@Service
class BenchPageStrategyImpl : BenchPageStrategy {

    override fun extractMessagePageNumber(message: String) : Int =
        message.substring(message.indexOf("(") + 1, message.indexOf("/")).toInt() - 1

    override fun buildPageWithBenches(session: Session, page: Int, callbackId: String?) : List<BaseRequest<*, *>> {
        session.currentBenches.let { benches ->
            val pagesCount = benches.size / 5 + (if (benches.size % 5 == 0) 0 else 1)
            val result = buildString {
                append("(${page + 1}/${pagesCount}) Найдено <b>${benches.size}</b> лавочек в радиусе " +
                        "<b>${session.radius}</b> метров\n\n")
                val benchesSubList = benches.subList(page * 5, min(page * 5 + 5, benches.size))
                for ((index, value) in benchesSubList.withIndex()) {
                    val realIndex = index + 1 + page * 5
                    append("<b>${realIndex}.</b> ${value.description()} \nПоказать на карте: /bench_${value.id}\n\n")
                }
            }
            val responses = mutableListOf<BaseRequest<*, *>>()
            callbackId?.let {
                responses.add(AnswerCallbackQuery(it))
            }
            responses.add(SendMessage(session.chatId, result)
                .parseMode(ParseMode.HTML)
                .replyMarkup(
                    InlineKeyboardMarkup(
                        InlineKeyboardButton("Назад").callbackData(BackPageProcessor.NAME),
                        InlineKeyboardButton("Дальше").callbackData(ForwardPageProcessor.NAME)
                    )
                ))
            return responses
        }
    }

    fun Bench.description() : String {
        val distance = 0
        var result = "Расстояние около $distance метров"
        result += orNone("спинка", properties["backrest"]) {
            when (it) {
                "yes" -> "да"
                "no" -> "нет"
                else -> "неизвестно"
            }
        }

        result += orNone("материал", properties["material"])
        result += orNone("поверхность", properties["surface"])
        result += orNone("цвет", properties["colour"])
        result += orNone("кол-во мест", properties["seats"])
        result += orNone("куда смотрит", properties["direction"])
        result += orNone("владелец", properties["operator"])
        result += orNone("надпись или посвящение", properties["inspiration"])
        return result
    }

    private fun orNone(caption: String, value: String?, converter: (String) -> String = { it }) : String {
        return if (value != null) {
            "\n$caption : ${converter(value)}"
        } else {
            ""
        }
    }
}