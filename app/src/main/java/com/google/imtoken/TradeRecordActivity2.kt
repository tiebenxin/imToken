package com.google.imtoken

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.imtoken.db.AppDataBase
import com.google.imtoken.db.CsvBean
import com.liaoinstan.springview.container.DefaultFooter
import com.liaoinstan.springview.widget.SpringView
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX
import kotlinx.android.synthetic.main.activity_trade_detail.*
import kotlinx.android.synthetic.main.activity_trade_record2.*
import kotlinx.android.synthetic.main.activity_trade_record2.iv_back
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import java.text.SimpleDateFormat
import java.util.*

class TradeRecordActivity2 : AppCompatActivity() {
    private var mAdapter: TradeAdapter? = null

    val titleList = mutableListOf<String>("全部", "转出", "转入", "失败")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.statusBarColor = Color.parseColor("#FBFCFE")

        UltimateBarX.with(this)
//            .color(Color.parseColor("#FBFCFE"))
            .color(AppConfig.getColor(R.color.transparent)!!)
            .fitWindow(true)
            .light(true)
            .applyStatusBar()


        setContentView(R.layout.activity_trade_record2)

        iv_back.setOnClickListener {
            finish()
        }

        AppDataBase.initDB(applicationContext)

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = TradeAdapter(this)
        mAdapter!!.currentAccount = account
        recycler_view.adapter = mAdapter
        mAdapter!!.listener = object : TradeAdapter.IItemClickListener {
            override fun onItemClick(bean: CsvBean) {
                val intent = Intent(this@TradeRecordActivity2, TradeDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("data", bean)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        spring_view.setGive(SpringView.Give.BOTTOM)
        spring_view.footer = DefaultFooter(this)
        spring_view.setListener(object : SpringView.OnFreshListener {
            override fun onLoadmore() {
                loadMore()
            }

            override fun onRefresh() {
            }
        })


        val csvDao = AppDataBase.getDataBase()?.csvDao()
        val csvLists = csvDao?.getAll(account)
        mAdapter!!.bindData(csvLists!!, false)

        initMagicIndicator()

        showChart()
    }

    private fun loadMore() {
        if (mAdapter == null) {
            return
        }
        val bean = mAdapter!!.getLastBean() ?: return
        val csvLists = AppDataBase.getDataBase()?.csvDao()!!.getMore(account, bean!!.blocktime)
        mAdapter!!.bindData(csvLists!!, true)
        spring_view.onFinishFreshAndLoad()

    }

    private fun initMagicIndicator() {
        val magicIndicator = top_layout
        magicIndicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titleList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    SimplePagerTitleView(context)
                simplePagerTitleView.text = titleList[index]
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.normalColor = Color.parseColor("#687681")
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.setOnClickListener {

                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = 3f
                indicator.lineWidth = 80f
                indicator.roundRadius = 0f
                indicator.startInterpolator = AccelerateInterpolator()
//                indicator.colors = mutableListOf(Color.parseColor("#FFFFFF"))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
//        ViewPagerHelper.bind(magicIndicator, mViewBinding.viewPager)
    }


    private fun showChart() {
        val csvDao = AppDataBase.getDataBase()?.csvDao()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2020)
        calendar.set(Calendar.MONTH, 10 - 1)
        calendar.set(Calendar.DAY_OF_MONTH, 4)
        val calendarEnd = Calendar.getInstance()
        calendarEnd.set(Calendar.YEAR, 2020)
        calendarEnd.set(Calendar.MONTH, 10 - 1)
        calendarEnd.set(Calendar.DAY_OF_MONTH, 10)
        val csvLists =
            csvDao?.getAllInTime(account, calendar.timeInMillis, calendarEnd.timeInMillis)
        var entries = mutableListOf<Entry>()

        csvLists?.forEach {
            //2020-10-01 00:17:24
            val milliseconds = it?.blocktime
            entries.add(Entry(milliseconds.toFloat(), it?.value?.toFloat()!!))
            Log.d(" Entry", "milliseconds=${milliseconds},value=${it?.value?.toFloat()!!}")
        }

        val lineDataSet = LineDataSet(entries, "") // 添加数据
        val lineData = LineData(lineDataSet)

        //折线
        //设置折线的式样   这个是圆滑的曲线（有好几种自己试试）     默认是直线
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER;
        lineDataSet.color = AppConfig.getColor(R.color.color_out)!!  //折线的颜色
        lineDataSet.lineWidth = 1f;        //折线的粗细


        //对于右下角一串字母的操作
        linechart.description.isEnabled = false;                  //是否显示右下角描述
        linechart.description.text = "这是修改那串英文的方法";    //修改右下角字母的显示
        linechart.description.textSize = 20f;                    //字体大小
        linechart.description.textColor = AppConfig.getColor(R.color.color_out)!!             //字体颜色

        //图例
        val legend = linechart.legend;
        legend.isEnabled = true;    //是否显示图例

        //折线图背景
        linechart.setBackgroundColor(AppConfig.getColor(R.color.white)!!);   //背景颜色
        linechart.xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        linechart.axisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）

        //X轴
        val xAxis = linechart.xAxis;
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        xAxis.axisLineColor = AppConfig.getColor(R.color.color_income)!!    //X轴颜色
        xAxis.axisLineWidth = 1f;           //X轴粗细
        xAxis.position = XAxis.XAxisPosition.BOTTOM;        //X轴所在位置   默认为上面
//        xAxis.setAxisMaximum(5F);   //X轴最大数值
//        xAxis.setAxisMinimum(0f);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(10, false);

        xAxis.valueFormatter = object : ValueFormatter() {
            private val mFormat = SimpleDateFormat("MM-dd")
            override fun getFormattedValue(value: Float): String {
                val format = mFormat.format(Date(value.toLong()))
                Log.d("setValueFormatter", "$format")
                return format
            }
        }


        //Y轴
        val axisLeft = linechart.axisLeft;
        axisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        axisLeft.axisLineColor = AppConfig.getColor(R.color.color_income)!!;  //Y轴颜色
        axisLeft.axisLineWidth = 1f;           //Y轴粗细
//        AxisLeft.setAxisMaximum(15f);   //Y轴最大数值
//        AxisLeft.setAxisMinimum(0f);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
//        AxisLeft.setLabelCount(15,false);

        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        linechart.axisRight.isEnabled = false;


        linechart.data = lineData
//        linechart.invalidate() // 刷新

        //动画（如果使用了动画可以则省去更新数据的那一步）
//        linechart.animateY(3000); //折线在Y轴的动画  参数是动画执行时间 毫秒为单位
//        linechart.animateX(2000); //X轴动画
        linechart.animateXY(2000, 2000);//XY两轴混合动画
    }
}