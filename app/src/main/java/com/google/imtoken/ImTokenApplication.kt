package com.google.imtoken

import android.app.Application
import com.google.imtoken.db.AppDataBase

/**
 * description ：
 * author : Liszt
 * date : 2021/5/13 14:36
 */

class ImTokenApplication() : Application(){


    override fun onCreate() {
        super.onCreate()
        AppConfig.context = applicationContext
        AppDataBase.initDB(applicationContext)
    }

}