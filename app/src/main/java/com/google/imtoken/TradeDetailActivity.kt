package com.google.imtoken

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.imtoken.db.CsvBean
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX
import kotlinx.android.synthetic.main.activity_trade_detail.*


/**
 *@Author:Liszt
 *@Date:2021/6/3 17:34
 *@Description:
 */
class TradeDetailActivity : AppCompatActivity() {
    private var bean: CsvBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UltimateBarX.with(this)
            .color(Color.parseColor("#FBFCFE"))
            .fitWindow(true)
            .light(true)
            .applyStatusBar()
        setContentView(R.layout.activity_trade_detail)

        iv_back.setOnClickListener {
            finish()
        }

        bean = intent.getSerializableExtra("data") as CsvBean
        if (bean != null) {
            if (bean!!.from == account) {
                tv_amount.text = "-${formatMoney(bean!!.value)} USDT"
            } else {
                tv_amount.text = "+${formatMoney(bean!!.value)} USDT"
            }
            tv_fee.text = "${bean!!.fee} ETH"
            tv_receive_address.text = "${bean!!.to}"
            tv_send_address.text = "${bean!!.from}"
            tv_hash.text = "${bean!!.hash}"

        }
    }
}