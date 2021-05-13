package com.google.imtoken.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * description ：
 * author : Liszt
 * date : 2021/5/13 14:05
 *
 *  hash,block,blocktime,from,label,to,label,value,symbol
 */

@Entity(tableName = "db_csv")
data class CsvBean(
    @PrimaryKey val hash: String,
    val block: String,
    val blocktime: String,
    val from: String,
    val label: String,
    val to: String,
    val label1: String,
    val value: String,
    val symbol: String


)