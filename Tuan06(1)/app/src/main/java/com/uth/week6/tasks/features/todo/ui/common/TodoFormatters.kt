package com.uth.week6.tasks.features.todo.ui.common

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

fun vietnameseDayName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Thứ Hai"
        DayOfWeek.TUESDAY -> "Thứ Ba"
        DayOfWeek.WEDNESDAY -> "Thứ Tư"
        DayOfWeek.THURSDAY -> "Thứ Năm"
        DayOfWeek.FRIDAY -> "Thứ Sáu"
        DayOfWeek.SATURDAY -> "Thứ Bảy"
        DayOfWeek.SUNDAY -> "Chủ Nhật"
    }
}

fun formatTime(time: LocalTime): String {
    return "%02d:%02d".format(time.hour, time.minute)
}

fun formatHeaderDate(date: LocalDate): String {
    return "${vietnameseDayName(date.dayOfWeek)}, ${date.dayOfMonth} tháng ${date.monthValue}"
}

fun formatShortDate(date: LocalDate): String {
    return "${vietnameseDayName(date.dayOfWeek)}, %02d/%02d".format(date.dayOfMonth, date.monthValue)
}

fun formatDayLabel(date: LocalDate, today: LocalDate): String {
    return when {
        date == today -> "Hôm nay"
        date == today.plusDays(1) -> "Ngày mai"
        date == today.minusDays(1) -> "Hôm qua"
        date.isAfter(today) && date.isBefore(today.plusDays(7)) -> vietnameseDayName(date.dayOfWeek)
        else -> "%02d/%02d".format(date.dayOfMonth, date.monthValue)
    }
}

fun formatDueLabel(date: LocalDate, time: LocalTime, today: LocalDate): String {
    return "${formatDayLabel(date, today)}, ${formatTime(time)}"
}

fun LocalDate.toUtcMillis(): Long {
    return atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

fun utcMillisToLocalDate(millis: Long): LocalDate {
    return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
}
