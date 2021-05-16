package com.google.imtoken

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.imtoken.db.CsvBean

class TradeAdapter : RecyclerView.Adapter<TradeAdapter.MyViewHolder> {
    private var mList: MutableList<CsvBean> = mutableListOf()
    private var mContext: Context? = null
    var currentAccount = ""

    constructor(context: Context) : super() {
        mContext = context
    }

    fun bindData(list: MutableList<CsvBean>, isRefresh: Boolean) {
        if (!isRefresh) {
            mList.clear()
        }
        mList.addAll(list)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(mContext!!).inflate(R.layout.item_trade, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    inner class MyViewHolder : RecyclerView.ViewHolder {
        private var ivIcon: ImageView? = null
        private var tvAccount: TextView? = null
        private var tvTime: TextView? = null
        private var tvMoney: TextView? = null
        private var tvFail: TextView? = null

        constructor(itemView: View) : super(itemView) {
            initView()
        }

        private fun initView() {
            ivIcon = itemView.findViewById(R.id.iv_icon)
            tvAccount = itemView.findViewById(R.id.tv_account)
            tvTime = itemView.findViewById(R.id.tv_time)
            tvMoney = itemView.findViewById(R.id.tv_money)
            tvFail = itemView.findViewById(R.id.tv_fail)
        }

        fun bindData(bean: CsvBean) {
            var isInCome = -1
            var otherAccount = ""
            if (currentAccount == bean.from) {
                otherAccount = bean.to
                isInCome = 0
                tvMoney!!.text = "-${bean.value}"
                tvMoney!!.setTextColor(getColor(mContext!!,R.color.color_out))

            } else {
                otherAccount = bean.from
                isInCome = 1
                tvMoney!!.text = "+${bean.value}"
                tvMoney!!.setTextColor(getColor(mContext!!,R.color.color_income))
            }
            tvAccount!!.text = getShortAccount(otherAccount)
            if (isInCome == 1) {
                ivIcon!!.setImageResource(R.mipmap.ic_txreceive)
            } else if (isInCome == 0) {
                ivIcon!!.setImageResource(R.mipmap.ic_txsend)
            } else if (isInCome == -1) {
                ivIcon!!.setImageResource(R.mipmap.ic_txfail)
            }

        }
    }

}