package com.google.imtoken

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.imtoken.db.AppDataBase
import com.liaoinstan.springview.widget.SpringView
import kotlinx.android.synthetic.main.activity_trade_record.*

class TradeRecordActivity : AppCompatActivity() {
    var mAdapter: TradeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_trade_record)

        AppDataBase.initDB(applicationContext)

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = TradeAdapter(this)
        mAdapter!!.currentAccount = "0xdbb7E5Bf95D58c067F50F4c36FbEBB4Dfb837913"
        recycler_view.adapter = mAdapter

        spring_view.setGive(SpringView.Give.BOTTOM)
        spring_view.setListener(object : SpringView.OnFreshListener {
            override fun onLoadmore() {
            }

            override fun onRefresh() {
            }
        })


        val csvDao = AppDataBase.getDataBase()?.csvDao()
        val csvLists = csvDao?.getAll()
        mAdapter!!.bindData(csvLists!!, false)
    }

    private fun loadMore() {

    }
}