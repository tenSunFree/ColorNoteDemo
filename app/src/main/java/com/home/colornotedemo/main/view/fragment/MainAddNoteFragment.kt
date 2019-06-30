package com.home.colornotedemo.main.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.home.colornotedemo.R
import com.home.colornotedemo.databinding.FragmentMainAddNoteBinding
import com.home.colornotedemo.main.model.NoteData
import com.home.colornotedemo.main.model.ObjectBox
import com.home.colornotedemo.main.view.MainActivity
import com.home.colornotedemo.utility.toast
import kotlinx.android.synthetic.main.fragment_main_add_note.*
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

/**
 * 添加便條
 */
@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class MainAddNoteFragment : Fragment(), View.OnTouchListener {

    companion object {
        fun newInstance(activity: MainActivity): MainAddNoteFragment {
            val fragment = MainAddNoteFragment()
            fragment.activity = activity
            return fragment
        }
    }

    lateinit var activity: MainActivity
    lateinit var binding: FragmentMainAddNoteBinding
    private var enableEditModeCurrentTimeMillis: Long = 0 // 第一次點擊畫面的時間
    private val enableEditModeTimeInterval: Long = 300 // 在0.3秒內再次點擊, 則開啟編輯模式
    private var lastContentLength: Int = 0 // 提供判斷, 是否要保存資料

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_add_note, container, false
        )
        setActivityCurrentShowFragmentLabel()
        initializeListener()
        enableEditMode(true)
        return binding.root
    }

    /** 告訴Activity, 目前顯示的Fragment是哪一個 */
    private fun setActivityCurrentShowFragmentLabel() {
        activity.currentShowFragment = MainActivity.MAIN_ADD_NOTE_FRAGMENT
    }

    private fun initializeListener() {
        binding.view.setOnTouchListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        when (motionEvent?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (enableEditModeCurrentTimeMillis + enableEditModeTimeInterval >
                    System.currentTimeMillis()
                ) enableEditMode(true)
                enableEditModeCurrentTimeMillis = System.currentTimeMillis()
            }
        }
        return false // 返回false, 表示會再被其他事件監聽器調用, 提供給ScrollView滾動畫面
    }

    fun isFocusable(): Boolean {
        return editText.isFocusable
    }

    /**
     * isEnable為true: 可編輯模式
     * isEnable為false: 不可編輯模式
     */
    fun enableEditMode(isEnable: Boolean) {
        if (isEnable) {
            binding.editText.post {
                binding.stateTextView.text = "編輯中"
                binding.timeTextView.text = getTime()
                lastContentLength = binding.editText.length()
                binding.editText.isFocusableInTouchMode = true
                binding.editText.isFocusable = true
                binding.editText.requestFocus()
                binding.view.visibility = View.INVISIBLE
                showKeyboard()
            }
        } else {
            if (lastContentLength != binding.editText.length()) {
                toast("已保存")
                binding.timeTextView.text = getTime()
            }
            binding.stateTextView.text = "0 秒前"
            binding.editText.isFocusable = false
            binding.editText.isFocusableInTouchMode = false
            binding.view.visibility = View.VISIBLE
        }
    }

    /**
     * 彈出軟鍵盤
     */
    private fun showKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.showSoftInput(editText, 0)
    }

    fun saveData() {
        if (binding.editText.length() == 0) return
        val lastModifiedTime = getTime()
        val lastModifiedMonthDay = getMonthDay()
        val lastModifiedTimestamp = org.threeten.bp.Instant.now().toEpochMilli()
        val content = binding.editText.text.toString()
        val notesBox = ObjectBox.boxStore.boxFor(
            NoteData::class.java
        )
        val note = NoteData(
            lastModifiedTime = lastModifiedTime, lastModifiedMonthDay = lastModifiedMonthDay,
            lastModifiedTimestamp = lastModifiedTimestamp, content = content
        )
        notesBox.put(note)
    }

    private fun getTime(): String? {
        val timePattern = "yyyy/MM/dd ahh:mm"
        val timeFormatter =
            DateTimeFormatter.ofPattern(timePattern)
                .withLocale(Locale.TAIWAN)
                .withZone(ZoneId.systemDefault())
        return timeFormatter.format(Instant.now())
    }

    private fun getMonthDay(): String? {
        val monthDayPattern = "M月d日"
        val monthDayFormatter =
            DateTimeFormatter.ofPattern(monthDayPattern)
                .withLocale(Locale.TAIWAN)
                .withZone(ZoneId.systemDefault())
        return monthDayFormatter.format(Instant.now())
    }

    /**
     * 判斷此Fragment目前屬於顯示還是隱藏
     * 第二次顯示之後, 每次都會觸發此方法; 第一次顯示, 只會觸發onCreateView
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            setActivityCurrentShowFragmentLabel()
            binding.editText.text.clear()
            enableEditMode(true)
        }
    }
}