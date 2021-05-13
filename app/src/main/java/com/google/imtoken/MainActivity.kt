package com.google.imtoken

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.imtoken.db.AppDataBase
import com.google.imtoken.db.CsvBean
import com.google.imtoken.db.CsvDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readExcel()
    }


    private fun readExcel() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStreamReader =
                    InputStreamReader(assets.open("0xdbb7e5bf95d58c067f50f4c36fbebb4dfb837913.csv"))
                val reader = BufferedReader(inputStreamReader)
                val firstline = reader.readLine() //读取第一行
                //hash,block,blocktime,from,label,to,label,value,symbol
                Log.d("first line -->", firstline)

                val csvDao = AppDataBase.getDataBase()?.csvDao();
                reader.forEachLine {
                    Log.d(" line --> ", it)
                    // TODO: 2021/5/13  保存数据库
                    save2DB(it, csvDao);
                }

                //测试查询
                val queryCsvByHash =
                    csvDao?.queryCsvByHash("0x41bb9c0b6d561c15eb10cb9b601c1e7fca44e25489bc681305c924b228c5ff2c")
                Log.d("queryCsvByHash", queryCsvByHash.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun save2DB(s: String, csvDao: CsvDao?) {
        val split = s.split(",")
        Log.d("save2DB size", "${split.size}")
        val csvBean = CsvBean(
            split[0],
            split[1],
            split[2],
            split[3],
            split[4],
            split[5],
            split[6],
            split[7],
            split[8]
        )
        Log.d("save2DB", csvBean.toString())
        csvDao?.insertCsv(csvBean)
    }


}
