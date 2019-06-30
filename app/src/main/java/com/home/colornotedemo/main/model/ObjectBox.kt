package com.home.colornotedemo.main.model

import android.content.Context
import io.objectbox.BoxStore

/**
 * Singleton to keep BoxStore reference.
 */
object ObjectBox {

    lateinit var boxStore: BoxStore
        private set // 不對外公開set()

    fun init(context: Context) {
        boxStore = MyObjectBox.builder().androidContext(context.applicationContext).build()
    }
}