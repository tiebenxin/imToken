package com.google.imtoken

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.imtoken.db.AppDataBase
import com.google.imtoken.db.CsvBean
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX
import jxl.Workbook
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_trade.tv_account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
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
        UltimateBarX.with(this)
            .color(Color.parseColor("#FBFCFE"))
            .fitWindow(true)
            .light(true)
            .applyStatusBar()
        setContentView(R.layout.activity_main)
        tv_account.text = getLongAccount(account)

        cl_asset_eth.setOnClickListener {
            startActivity(Intent(this@MainActivity, TradeRecordActivity2::class.java))
        }


//        linechart = findViewById<LineChart>(R.id.linechart)
        findViewById<ImageView>(R.id.scan).setOnClickListener {
//            showChart()
            startActivity(Intent(this@MainActivity, TradeRecordActivity2::class.java))
        }

        cl_wallet.setOnClickListener {
            for (s in accountList) {
//                readExcel(s)
                readWorkExcel()
            }
        }
    }


    private fun readExcel(account: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStreamReader =
                    InputStreamReader(assets.open("${account}.csv"))
                val reader = BufferedReader(inputStreamReader)
                val firstline = reader.readLine() //读取第一行
                //hash,block,blocktime,from,label,to,label,value,symbol
                Log.d("first line -->", firstline)
                val list = mutableListOf<CsvBean>()
                reader.forEachLine {
                    Log.d(" line --> ", it)
                    // TODO: 2021/5/13  保存数据库
//                    save2DB(it, csvDao);
                    val split = it.split(",")
                    list.add(
                        CsvBean().apply {
                            hash = split[0]
                            block = split[1]
                            blocktime = getTimestamp(split[2])
                            from = split[3]
                            label = split[4]
                            to = split[5]
                            label1 = split[6]
                            value = split[7].toDouble()
                            symbol = split[8]
                        }
                    )
                }

                //读取第二张表，获取fee字段
                val inputStreamReader2 =
                    InputStreamReader(assets.open("${account}_fee.csv"))
                val reader2 = BufferedReader(inputStreamReader2)
                val firstline2 = reader2.readLine() //读取第一行
                //hash,block,blocktime,from,label,to,label,value,fee
                Log.d("first line2 -->", firstline2)

                val list2 = mutableListOf<CsvBean>()
                reader2.forEachLine {
                    Log.d(" line2 --> ", it)
                    val split = it.split(",")
                    list2.add(
                        CsvBean().apply {
                            hash = split[0]
                            block = split[1]
                            blocktime = getTimestamp(split[2])
                            from = split[3]
                            label = split[4]
                            to = split[5]
                            label1 = split[6]
                            value = split[7].toDouble()
                            fee = split[8].toDouble()
                        }
                    )
                }
                for (bean in list) {
                    val index = list2.indexOf(bean)
                    if (index >= 0) {
                        val bean2 = list2[index]
                        bean.fee = bean2.fee
                    }
                }
                val csvDao = AppDataBase.getDataBase()?.csvDao()
                AppDataBase.getDataBase()?.csvDao()!!.insertCsvList(list)
                //测试查询
//                val bean =
//                    csvDao?.queryCsvByHash("0x41bb9c0b6d561c15eb10cb9b601c1e7fca44e25489bc681305c924b228c5ff2c")
                val bean = csvDao?.queryCsvByHash2(account)
                Log.d("queryCsvByHash", bean.toString())
                val count = csvDao?.getCount()
                Log.d("queryCsvByHash", "count=${count}")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun readWorkExcel() {
        // jxl 只支持 win 03版本excel,其他版本的会报错，需要将excel
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val workbook = Workbook.getWorkbook(assets.open("work1.xls"))
                val sheet = workbook.getSheet(0)
                val sheetRows:Int = sheet.rows -1;
                val jsonArray = JSONArray();
                for (i in 0..sheetRows){
                    val obj = JSONObject()
                    obj.put("addr",sheet.getCell(0,i).contents)
                    jsonArray.put(obj)
                }
                Log.d("result -->", jsonArray.toString())
                workbook.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
