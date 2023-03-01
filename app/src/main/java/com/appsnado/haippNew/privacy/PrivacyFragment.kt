package com.appsnado.haippNew.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar

class PrivacyFragment : BaseFragment<PrivacyViewModel>() {

    companion object {
        fun newInstance() = PrivacyFragment()
    }

    private lateinit var contentviewModel: PrivacyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.privacy_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun createViewModel(): PrivacyViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        contentviewModel = ViewModelProviders.of(this, factory).get(PrivacyViewModel::class.java)
        return contentviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.text = "Privacy Policy"
        titleBar?.btnLeft?.visibility = View.VISIBLE
        titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
            navController!!.popBackStack()
        });
    }

}