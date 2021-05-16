package com.google.imtoken.db

import androidx.room.*


/**
 * description ：
 * author : Liszt
 * date : 2021/5/13 14:14
 */

@Dao
interface CsvDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCsv(csv: CsvBean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCsvList(list: MutableList<CsvBean>)

    //更新
    @Update
    fun updateCsv(csv: CsvBean)

    //查询单条id
    @Query("SELECT * FROM db_csv WHERE hash=:id LIMIT 1")
    fun queryCsvByHash(id: String): CsvBean?

    //查询单条id
    @Query("SELECT * FROM db_csv WHERE from_account=:account LIMIT 1")
    fun queryCsvByHash2(account: String): CsvBean?

    //    @Query("SELECT * FROM db_csv WHERE from_account=:account OR to_account=:account")
    @Query("SELECT COUNT(hash) FROM db_csv")
    fun getCount(): Int
//    fun getCount(account: String): Int

    @Query("SELECT * FROM db_csv WHERE from_account=:account OR to_account=:account ORDER BY blocktime ASC LIMIT 40")
    fun getAll(account: String): MutableList<CsvBean>?

    @Query("SELECT * FROM db_csv WHERE (from_account=:account OR to_account=:account) AND blocktime >:time ORDER BY blocktime ASC LIMIT 40")
    fun getMore(account: String, time: Long): MutableList<CsvBean>?


    //删除delete
    @Query("DELETE FROM db_csv WHERE hash=:hash")
    suspend fun deleteByHash(hash: String)

    //删除所有群数据
    @Query("DELETE FROM db_csv")
    fun clearAll()

}