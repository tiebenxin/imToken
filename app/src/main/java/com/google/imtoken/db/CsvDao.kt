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

    //更新
    @Update
    fun updateCsv(csv: CsvBean)

    //查询单条id
    @Query("SELECT * FROM db_csv WHERE hash=:id LIMIT 1")
    fun queryCsvByHash(id: String): CsvBean?


    @Query("SELECT * FROM db_csv")
    fun getAll(): List<CsvBean?>?


    //删除delete
    @Query("DELETE FROM db_csv WHERE hash=:hash")
    suspend fun deleteByHash(hash: String)

    //删除所有群数据
    @Query("DELETE FROM db_csv")
    fun clearAll()

}