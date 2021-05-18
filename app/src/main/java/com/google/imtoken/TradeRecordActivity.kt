package com.google.imtoken

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.imtoken.db.AppDataBase
import com.liaoinstan.springview.container.DefaultFooter
import com.liaoinstan.springview.widget.SpringView
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX
import kotlinx.android.synthetic.main.activity_trade_record.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class TradeRecordActivity : AppCompatActivity() {
    private var mAdapter: TradeAdapter? = null

    val titleList = mutableListOf<String>("全部","转出","转入","失败")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.parseColor("#FBFCFE")

        UltimateBarX.with(this)
            .color(Color.parseColor("#FBFCFE"))
            .fitWindow(true)
            .light(true)
            .applyStatusBar()


        setContentView(R.layout.activity_trade_record)

        AppDataBase.initDB(applicationContext)

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = TradeAdapter(this)
        mAdapter!!.currentAccount = account
        recycler_view.adapter = mAdapter

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
}