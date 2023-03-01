package com.appsnado.haippNew.Webfilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Features
import com.appsnado.haippNew.databinding.Webfilter

class WebfilterFragment :  BaseFragment<WebfilterViewModel>() {
    var binding: Webfilter? = null
    var WebfilterAdapter: WebfilterAdapter? = null
    var featuresArrayList: ArrayList<String>? = null

    companion object {
        fun newInstance() = WebfilterFragment()
    }

    private lateinit var viewModelfeature: WebfilterViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                com.appsnado.haippNew.R.layout.webfilter_fragment,
                container,
                false
        )
       // startTopToBottomAnimation()
        setData()

        return binding!!.root;

    }

    private fun startTopToBottomAnimation() {
        //val animation = AnimationUtils.loadAnimation(mainActivity, com.appsnado.haipp.R.anim.slide_in_from_top)
        //binding?.linearLayoutCompat2!!.startAnimation(animation)

    }

    fun setData() {

        featuresArrayList = ArrayList()

        featuresArrayList!!.add("Activity Report")
        featuresArrayList!!.add("App Blocker")
        featuresArrayList!!.add("Browsing History")
        featuresArrayList!!.add("Geo Fences")
        featuresArrayList!!.add("Location History")
        featuresArrayList!!.add("Smart Schdule")
        featuresArrayList!!.add("Task")
        featuresArrayList!!.add("Web Filters")

        binding!!.rvWebfilter.layoutManager = LinearLayoutManager(
                mainActivity,
                LinearLayoutManager.VERTICAL,
                false
        )
        WebfilterAdapter = WebfilterAdapter(mainActivity!!, featuresArrayList)
        binding!!.rvWebfilter.adapter = WebfilterAdapter
    }

    override fun createViewModel(): WebfilterViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelfeature = ViewModelProviders.of(this, factory).get(WebfilterViewModel::class.java)
        return viewModelfeature
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!, "Web Filters")
    }

}