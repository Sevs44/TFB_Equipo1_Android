package com.saulhervas.easychat.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter {
    companion object {
        fun formatDay(date: String?): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val dateFormatted: Date = inputFormat.parse(date.toString()) as Date

            val dayOfWeekFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val dayOfWeek: String = dayOfWeekFormat.format(dateFormatted)

            val dayOfMonthFormat = SimpleDateFormat("dd", Locale.getDefault())
            val dayOfMonth: String = dayOfMonthFormat.format(dateFormatted)

            return "$dayOfWeek. $dayOfMonth"
        }

        fun formatHour(date: String?): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())

            val dateFormat: Date = inputFormat.parse(date.toString()) as Date

            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            return outputFormat.format(dateFormat)
        }
    }
}
