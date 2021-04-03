package com.zzz.hotupdatehost

import android.content.Context
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
object PluginManager {

    val TAG = "PluginManager"
    lateinit var context: Context
    fun init(context: Context) {
        this.context = context
        insertDex()
    }

    //插入dex
    private fun insertDex() {

        val pathListField =
            getField(BaseDexClassLoader::class.java, "pathList"); // DexPathList类的Field
        val pathListObj = pathListField?.get(this::class.java.classLoader)
        //获得classLoader下的pathList的Element[]类实例
        val dexElementsField = getField(pathListObj?.javaClass, "dexElements"); //Element[]类的Field
        var dexElementsObj = dexElementsField?.get(pathListObj)
        //获得制作dex文件的函数
        val makeDexElements = pathListObj?.javaClass?.getDeclaredMethod(
            "makePathElements",
            List::class.java,
            File::class.java,
            List::class.java
        )
        makeDexElements?.isAccessible = true

        val pluginDir = context.getExternalFilesDir("plugin")
        if (pluginDir == null || !pluginDir.exists()) {
            Log.i(TAG, "plugin dir don't exist")
            return
        }
        val soFiles = pluginDir.listFiles();
        if (soFiles == null) {
            Log.i(TAG, "so files don't exist")
            return
        }
        //添加所有so文件
        val combined = Array.newInstance(
            Class.forName("dalvik.system.DexPathList\$Element"),
            soFiles.size + 1
        )
        for (i in 0 until soFiles.size) {
            //利用apk创建dex文件
            val files1 = listOf<File>(soFiles.get(i))
            var newElementsObj1 = makeDexElements?.invoke(pathListObj, files1, null, null)
            //创造新的Element[]类实例并进行pathList下的dexElements属性替换
            System.arraycopy(newElementsObj1!!, 0, combined, i, 1)
        }
        System.arraycopy(dexElementsObj!!, 0, combined, soFiles.size, 1)
        dexElementsField?.set(pathListObj, combined)
    }

    private fun getField(clazz: Class<*>?, name: String): Field? {
        var field: Field? = null
        try {
            field = clazz?.getDeclaredField(name)
            field?.isAccessible = true
        } catch (e: Exception) {
            Log.i("getField", e.toString())
        }
        return field
    }
}