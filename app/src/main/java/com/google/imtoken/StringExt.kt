package com.google.imtoken

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

const val account = "0xdbb7e5bf95d58c067f50f4c36fbebb4dfb837913"
//const val account="0x056ce95d37ad99128c9b4922f0a3d01deea06344"
//const val account="0x3e673742707229bca4d6b95363adf045c47a9e94"

fun getLongAccount(account: String): String {
    // 10 22 10
    return account.replaceRange(10, 32, "...")
}

fun getShortAccount(account: String): String {
    //5 32 5
    return account.replaceRange(5, 37, "...")
}

fun formatMoney(money: Double): String {
    val pattern = "###,###.###"
    val format = DecimalFormat(pattern)
    return format.format(money)
}

fun formatTime(time: Long): String {
    val date = Date()
    date.time = time
    val pattern = "MM/dd/yyyy HH:mm:ss"
    val format = SimpleDateFormat(pattern)
    return format.format(date)
}

fun getTimestamp(time: String): Long {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val format = SimpleDateFormat(pattern)
    return format.parse(time).time
}

fun getColor(context: Context, @ColorRes rid: Int): Int {
    return ContextCompat.getColor(context, rid)
}

fun getInt():Int{
    return (1..9).random()
}

fun getInt0():Int{
    return (0..9).random()
}

fun getGasPrice():Int{
    0.0054
    //0.00129253108199423
    //17.00,20.00,23.00,27,28.00,36.00,,50,70
    return (17..70).random()
}

fun getFee(sour:Double):Double{
    //0.0054
    //0.0012 9253108199423
    val s0 = sour.toString()
    val result = when (s0.length) {
        5 -> {
            "${s0}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt()}"
        }
        4 -> {
            "${s0}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt()}"
        }
        3 -> {
            "${s0}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt0()}${getInt()}"
        }
        else->{
            s0
        }
    }
//    val pattern = "#.###"
//    val format = DecimalFormat(pattern)
    return result.toDouble()
}