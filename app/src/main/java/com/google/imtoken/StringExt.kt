package com.google.imtoken

import java.text.DecimalFormat
import java.text.SimpleDateFormat

fun getLongAccount(account: String): String {
    // 10 22 10
    return account.replaceRange(10, 32, ".")
}

fun getShortAccount(account: String): String {
    //5 32 5
    return account.replaceRange(5, 37, ".")
}

fun formatMoney(money: Double): String {
    val pattern = "###,###.###"
    val format = DecimalFormat(pattern)
    return format.format(money)
}

fun formatTime(time: String): String {
    val pattern = "MM/dd/yyyy HH:mm:ss"
    val format = SimpleDateFormat(pattern)
    return format.format(time)
}

fun getTimestamp(time: String): Long {
    val pattern = "MM/dd/yyyy HH:mm:ss"
    val format = SimpleDateFormat(pattern)
    return format.parse(time).time
}