package com.home.colornotedemo.utility

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast

private var toast: Toast? = null

@SuppressLint("ShowToast")
fun Context.toast(text: CharSequence) {
    toast ?: let {
        toast = Toast.makeText(this, null, Toast.LENGTH_SHORT)
    }
    toast?.apply {
        setText(text)
        show()
    }
}

/**
 * @param resId 字符串資源
 */
fun Context.toast(@StringRes resId:Int){
    toast(getString(resId))
}

@SuppressLint("ShowToast")
fun <T: Fragment> T.toast(text: CharSequence){
    context?.toast(text)
}

/**
 * @param resId 字符串資源
 */
fun <T:Fragment> T.toast(@StringRes resId: Int){
    toast(getString(resId))
}