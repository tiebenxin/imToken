package com.google.imtoken

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jxl.Workbook
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        readExcel()
    }


    private fun readExcel() {
        try {
            val book =
                Workbook.getWorkbook(assets.open("0xdbb7e5bf95d58c067f50f4c36fbebb4dfb837913.csv"))
            val sheet = book.getSheet(0)
            val sheetNum = book.numberOfSheets
            val sheetRows = sheet.rows
            val sheetColunms = sheet.columns
            println("sheetNum=${sheetNum}")
            println("sheetRows=${sheetRows}")
            println("sheetColunms=${sheetColunms}")

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}
