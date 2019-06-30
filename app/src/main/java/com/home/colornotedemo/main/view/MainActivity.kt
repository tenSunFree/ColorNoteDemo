package com.home.colornotedemo.main.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.home.colornotedemo.R
import com.home.colornotedemo.databinding.ActivityMainBinding
import com.home.colornotedemo.main.view.fragment.MainAddNoteFragment
import com.home.colornotedemo.main.view.fragment.MainHomePageFragment
import com.home.colornotedemo.utility.toast

class MainActivity : AppCompatActivity() {

    companion object {
        const val MAIN_HOME_PAGE_FRAGMENT: String = "MainHomePageFragment"
        const val MAIN_ADD_NOTE_FRAGMENT: String = "MainAddNoteFragment"
    }

    private var binding: ActivityMainBinding? = null // 關於類型後面加一個問號, 表示該變量是Nullable, 不加表示該變量不可為null
    private var mainHomePageFragment: MainHomePageFragment? = null
    private var mainAddNoteFragment: MainAddNoteFragment? = null
    var currentShowFragment: String? = null // 提供判斷目前activity是顯示哪一個fragment
    private var firstCloseAppCurrentTimeMillis: Long = 0 // 第一次點擊返回鍵, 關閉app的時間
    private val closeAppTimeInterval: Long = 2000 // 在2秒內再次點擊返回鍵, 則關閉app

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        showFragment(MAIN_HOME_PAGE_FRAGMENT) // 預設是載入首頁
    }

    /**
     * 顯示Fragment
     * 多個Fragment切換時, 避免重複創建Fragment
     */
    fun showFragment(fragmentName: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction() // 開啟事務
        hideFragment(fragmentTransaction) // 先隱藏所有的Fragment
        when (fragmentName) {
            MAIN_HOME_PAGE_FRAGMENT -> // 首頁
                if (mainHomePageFragment == null) { // 如果Fragment為空, 就新建一個實例
                    mainHomePageFragment = MainHomePageFragment.newInstance(this)
                    fragmentTransaction.add(R.id.containerFrameLayout, mainHomePageFragment!!) // 執行事務, 添加Fragment
                } else { // 如果不為空, 就將它從棧中顯示出來
                    fragmentTransaction.show(mainHomePageFragment!!)
                }
            MAIN_ADD_NOTE_FRAGMENT -> // 添加便條
                if (mainAddNoteFragment == null) { // 如果Fragment為空, 就新建一個實例
                    mainAddNoteFragment = MainAddNoteFragment.newInstance(this)
                    fragmentTransaction.add(R.id.containerFrameLayout, mainAddNoteFragment!!) // 執行事務, 添加Fragment
                } else { // 如果不為空, 就將它從棧中顯示出來
                    fragmentTransaction.show(mainAddNoteFragment!!)
                }
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    /**
     * 隱藏Fragment
     */
    private fun hideFragment(fragmentTransaction: FragmentTransaction) {
        if (mainHomePageFragment != null) { // 如果不為空, 就先隱藏起來
            fragmentTransaction.hide(mainHomePageFragment!!)
        }
        if (mainAddNoteFragment != null) { // 如果不為空, 就先隱藏起來
            fragmentTransaction.hide(mainAddNoteFragment!!)
        }
    }

    /**
     * 根據currentFragment判斷目前顯示的是哪一個Fragment, 並執行對應的行為
     */
    override fun onBackPressed() {
        when (currentShowFragment) {
            MAIN_HOME_PAGE_FRAGMENT -> { // 2秒內點擊2次返回鍵, 才會退出app
                if (firstCloseAppCurrentTimeMillis + closeAppTimeInterval > System.currentTimeMillis()) {
                    finish()
                } else {
                    val leave = "再點一次離開"
                    toast(leave)
                }
                firstCloseAppCurrentTimeMillis = System.currentTimeMillis()
            }
            MAIN_ADD_NOTE_FRAGMENT -> {
                // 如果還沒結束編輯模式, 會先結束編輯模式
                // 如果已經結束編輯模式, 會儲存資料並跳轉回首頁
                if (mainAddNoteFragment?.isFocusable()!!) {
                    mainAddNoteFragment?.enableEditMode(false)
                } else{
                    mainAddNoteFragment?.saveData()
                    showFragment(MAIN_HOME_PAGE_FRAGMENT)
                }
            }
        }
    }
}
