package com.google.imtoken

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat

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

fun formatTime(time: String): String {
    val pattern = "MM/dd/yyyy HH:mm:ss"
    val format = SimpleDateFormat(pattern)
    return format.format(time)
}

fun getTimestamp(time: String): Long {
    val pattern = "yyyy-mm-dd HH:mm:ss"
    val format = SimpleDateFormat(pattern)
    return format.parse(time).time
}

fun getColor(context: Context, @ColorRes rid: Int):Int {
   return ContextCompat.getColor(context, rid)
}