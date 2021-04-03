package com.zzz.hotupdatehost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.zzz.library.TextUtil
import dalvik.system.BaseDexClassLoader
import java.io.File
import java.lang.Exception
import java.lang.reflect.Array
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv).text = TextUtil().getText()
    }
}