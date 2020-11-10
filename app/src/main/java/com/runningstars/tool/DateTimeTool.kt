package com.runningstars.tool

import java.text.ParseException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit

class DateTimeTool {
    companion object {

        fun counterTime(start : LocalDateTime, dateTime : LocalDateTime) : String {
            var day = 0
            var hh = 0
            var mm = 0
            var ss = 0
            try {
                val cDate = Date()
                val timeDiff = dateTime.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(
                    ZoneOffset.UTC)

                day = TimeUnit.SECONDS.toDays(timeDiff.toLong()).toInt()
                hh = (TimeUnit.SECONDS.toHours(timeDiff.toLong()) - TimeUnit.DAYS.toHours(day.toLong())).toInt()
                mm = (TimeUnit.SECONDS.toMinutes(timeDiff.toLong()) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.SECONDS.toHours(timeDiff.toLong()))).toInt()
                ss = (TimeUnit.SECONDS.toSeconds(timeDiff.toLong()) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.SECONDS.toMinutes(timeDiff.toLong()))).toInt()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return if (mm == 0) {
                " $ss sec"
            } else if (hh == 0) {
                "$mm min $ss sec"
            } else if (day == 0) {
                "$hh hour $mm min $ss sec"
            } else {
                "$day days $hh hour $mm min $ss sec"
            }
        }
    }
}