package com.google.imtoken

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.imtoken.db.AppDataBase
import com.google.imtoken.db.CsvBean
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_trade.tv_account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    //    lateinit var linechart: LineChart
    val accountList: MutableList<String> = mutableListOf(
        "0xdbb7e5bf95d58c067f50f4c36fbebb4dfb837913",
        "0x056ce95d37ad99128c9b4922f0a3d01deea06344",
        "0x3e673742707229bca4d6b95363adf045c47a9e94"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_account.text = getLongAccount("0xdbb7E5Bf95D58c067F50F4c36FbEBB4Dfb837913")


//        linechart = findViewById<LineChart>(R.id.linechart)
        findViewById<ImageView>(R.id.scan).setOnClickListener {
//            showChart()
            startActivity(Intent(this@MainActivity, TradeRecordActivity2::class.java))
        }

        cl_wallet.setOnClickListener {
            for (s in accountList) {
                readExcel(s)
            }
        }
    }


    private fun readExcel(account: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
//                val inputStreamReader =
//                    InputStreamReader(assets.open("0xdbb7e5bf95d58c067f50f4c36fbebb4dfb837913.csv"))
//                val inputStreamReader =
//                    InputStreamReader(assets.open("0x056ce95d37ad99128c9b4922f0a3d01deea06344.csv"))
//                val inputStreamReader =
//                    InputStreamReader(assets.open("0x3e673742707229bca4d6b95363adf045c47a9e94.csv"))
                val inputStreamReader =
                    InputStreamReader(assets.open("${account}.csv"))
                val reader = BufferedReader(inputStreamReader)
                val firstline = reader.readLine() //读取第一行
                //hash,block,blocktime,from,label,to,label,value,symbol
                Log.d("first line -->", firstline)

                val list = mutableListOf<CsvBean>()

                val csvDao = AppDataBase.getDataBase()?.csvDao()
                reader.forEachLine {
                    Log.d(" line --> ", it)
                    // TODO: 2021/5/13  保存数据库
//                    save2DB(it, csvDao);
                    val split = it.split(",")
                    list.add(
                        CsvBean(
                            split[0],
                            split[1],
                            getTimestamp(split[2]),
                            split[3],
                            split[4],
                            split[5],
                            split[6],
                            split[7].toDouble(),
                            split[8]
                        )
                    )
                }

                csvDao!!.insertCsvList(list)
                //测试查询
//                val bean =
//                    csvDao?.queryCsvByHash("0x41bb9c0b6d561c15eb10cb9b601c1e7fca44e25489bc681305c924b228c5ff2c")
                val bean = csvDao?.queryCsvByHash2(account)
                Log.d("queryCsvByHash", bean.toString())
                val count = csvDao.getCount()
                Log.d("queryCsvByHash", "count=${count}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}
