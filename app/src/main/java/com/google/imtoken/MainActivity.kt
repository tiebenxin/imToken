package com.google.imtoken

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.imtoken.db.AppDataBase
import com.google.imtoken.db.CsvBean
import com.google.imtoken.db.CsvDao
import kotlinx.android.synthetic.main.item_trade.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var linechart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_account.text = getLongAccount("0xdbb7E5Bf95D58c067F50F4c36FbEBB4Dfb837913")

        readExcel()

        linechart = findViewById<LineChart>(R.id.linechart)
        findViewById<ImageView>(R.id.scan).setOnClickListener {
            showChart()
        }
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
            getTimestamp(split[2]),
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


    private fun showChart() {
        val csvDao = AppDataBase.getDataBase()?.csvDao()
        val csvLists = csvDao?.getAll()
        var entries = mutableListOf<Entry>()

        csvLists?.forEach {
            //2020-10-01 00:17:24
            val milliseconds = it?.blocktime
            entries.add(Entry(milliseconds.toFloat(), it?.value?.toFloat()!!))
            Log.d(" Entry","milliseconds=${milliseconds},value=${it?.value?.toFloat()!!}")
        }

        val lineDataSet = LineDataSet(entries, "") // 添加数据
        val lineData = LineData(lineDataSet)

        //折线
        //设置折线的式样   这个是圆滑的曲线（有好几种自己试试）     默认是直线
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setColor(Color.GREEN);  //折线的颜色
        lineDataSet.setLineWidth(2F);        //折线的粗细


        //对于右下角一串字母的操作
        linechart.getDescription().setEnabled(false);                  //是否显示右下角描述
        linechart.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        linechart.getDescription().setTextSize(20f);                    //字体大小
        linechart.getDescription().setTextColor(Color.RED);             //字体颜色

        //图例
        val legend = linechart.legend;
        legend.setEnabled(true);    //是否显示图例

        //折线图背景
        linechart.setBackgroundColor(0x30000000);   //背景颜色
        linechart.getXAxis().setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        linechart.getAxisLeft().setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）

        //X轴
        val xAxis = linechart.xAxis;
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        xAxis.setAxisLineColor(Color.RED);   //X轴颜色
        xAxis.setAxisLineWidth(2f);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
//        xAxis.setAxisMaximum(5F);   //X轴最大数值
//        xAxis.setAxisMinimum(0f);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(10, false);

        xAxis.setValueFormatter(object : ValueFormatter(){
            private val mFormat = SimpleDateFormat("MM-dd")
            override fun getFormattedValue(value: Float): String {
                val format = mFormat.format(Date(value.toLong()))
                Log.d("setValueFormatter","$format")
                return format
            }
        })


        //Y轴
        val AxisLeft = linechart.axisLeft;
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        AxisLeft.setAxisLineColor(Color.BLUE);  //Y轴颜色
        AxisLeft.setAxisLineWidth(2f);           //Y轴粗细
//        AxisLeft.setAxisMaximum(15f);   //Y轴最大数值
//        AxisLeft.setAxisMinimum(0f);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
//        AxisLeft.setLabelCount(15,false);

        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        linechart.getAxisRight().setEnabled(false);


        linechart.setData(lineData)
//        linechart.invalidate() // 刷新

        //动画（如果使用了动画可以则省去更新数据的那一步）
//        linechart.animateY(3000); //折线在Y轴的动画  参数是动画执行时间 毫秒为单位
//        linechart.animateX(2000); //X轴动画
        linechart.animateXY(2000,2000);//XY两轴混合动画
    }

}
