package com.google.imtoken.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * description ：
 * author : Liszt
 * date : 2021/5/13 14:05
 *
 *  hash,block,blocktime,from,label,to,label,value,symbol
 */

@Entity(tableName = "db_csv")
class CsvBean : Serializable {
    @PrimaryKey
    var hash: String = ""
    var block: String = ""
    var blocktime: Long = 0L

    @ColumnInfo(name = "from_account")
    var from: String = ""
    var label: String = ""

    @ColumnInfo(name = "to_account")
    var to: String = ""
    var label1: String = ""
    var value: Double = 0.0
    var symbol: String = ""
    var fee: Double = 0.0

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other is CsvBean) {
            if ((other as CsvBean).hash == hash) {
                return true
            }
        }
        return false
    }
}
