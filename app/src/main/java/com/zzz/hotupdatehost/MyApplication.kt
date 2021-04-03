package com.zzz.hotupdatehost

import android.app.Application
import android.util.Log
import dalvik.system.BaseDexClassLoader
import java.io.File
import java.lang.Exception
import java.lang.reflect.Array
import java.lang.reflect.Field

/**
 *  author : chentao
 *  date : 2021/4/3
 *  email: chentao3@yy.com
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PluginManager.init(this)
    }
}
