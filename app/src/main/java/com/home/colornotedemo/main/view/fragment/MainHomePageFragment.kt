package com.home.colornotedemo.main.view.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.home.colornotedemo.R
import com.home.colornotedemo.databinding.FragmentMainHomePageBinding
import com.home.colornotedemo.main.model.NoteData
import com.home.colornotedemo.main.model.NoteData_
import com.home.colornotedemo.main.model.ObjectBox
import com.home.colornotedemo.main.view.MainActivity
import com.home.colornotedemo.main.view.adapter.NotesAdapter
import io.objectbox.android.AndroidScheduler
import io.objectbox.kotlin.query
import io.objectbox.reactive.DataSubscription

/**
 * 首頁
 */
class MainHomePageFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance(activity: MainActivity): MainHomePageFragment {
            val fragment = MainHomePageFragment()
            fragment.activity = activity
            return fragment
        }
    }

    lateinit var activity: MainActivity
    lateinit var binding: FragmentMainHomePageBinding
    private lateinit var subscription: DataSubscription

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_home_page, container, false)
        setActivityCurrentShowFragmentLabel()
        initializeClickListener()
        initializeListView()
        return binding.root
    }

    /** 告訴Activity, 目前顯示的Fragment是哪一個 */
    private fun setActivityCurrentShowFragmentLabel() {
        activity.currentShowFragment = MainActivity.MAIN_HOME_PAGE_FRAGMENT
    }

    private fun initializeClickListener() {
        binding.addNoteView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.addNoteView -> activity.showFragment(MainActivity.MAIN_ADD_NOTE_FRAGMENT)
        }
    }

    private fun initializeListView() {
        val notesAdapter = NotesAdapter()
        binding.listView.apply { adapter = notesAdapter }
        val notesBox = ObjectBox.boxStore.boxFor(NoteData::class.java)
        val notesQuery = notesBox.query { order(NoteData_.lastModifiedTimestamp) }
        subscription = notesQuery.subscribe()
            .on(AndroidScheduler.mainThread()) // 觀察者在Android的主線程上被調用
            // 一旦查詢結果有變化，它們被傳遞給觀察者
            .observer { notes ->
                notes.reverse() // 反轉List, 按照修改的時間排序, 由近到遠
                notesAdapter.setNotes(notes)
            }
    }

    override fun onDestroy() {
        subscription.cancel()
        super.onDestroy()
    }

    /**
     * 判斷此Fragment目前屬於顯示還是隱藏
     * 第二次顯示之後, 每次都會觸發此方法; 第一次顯示, 只會觸發onCreateView
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) setActivityCurrentShowFragmentLabel()
    }
}