package com.appsnado.haippNew.Kiddevices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Listworkbind

class ListworkFragment :  BaseFragment<ListworkViewModel>() {
    var binding: Listworkbind? = null
    var kidAdapter: ListworkAdapter? = null
    var kidsArrayList: ArrayList<String>? = null

    companion object {
        fun newInstance() = ListworkFragment()
    }

    private lateinit var viewModelkids: ListworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.listwork_fragment,
            container,
            false
        )
       // startTopToBottomAnimation()
        setData()

        return binding!!.root;

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    private fun startTopToBottomAnimation() {
//        val animation = AnimationUtils.loadAnimation(mainActivity, com.appsnado.haipp.R.anim.slide_in_from_top)
//        binding?.linearLayoutCompat2!!.startAnimation(animation)

    }

    fun setData() {

        kidsArrayList = ArrayList()

        kidsArrayList!!.add("Bodmas")
        kidsArrayList!!.add("Multiplication")
        kidsArrayList!!.add("Linear Equation")
        kidsArrayList!!.add("Percentage")
        kidsArrayList!!.add("Number Pattern")

        binding!!.rvFeatures.setLayoutManager(
            LinearLayoutManager(
                mainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        kidAdapter =
            ListworkAdapter(mainActivity!!, kidsArrayList)
        binding!!.rvFeatures.adapter = kidAdapter
    }

    override fun createViewModel(): ListworkViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelkids = ViewModelProviders.of(this, factory).get(ListworkViewModel::class.java)
        return viewModelkids!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.text = "List of Worksheet"
        titleBar?.btnLeft?.visibility = View.VISIBLE
        titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
            navController!!.popBackStack()
        });

    }
}