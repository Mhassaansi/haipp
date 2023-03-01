package com.appsnado.haippNew.Subscription

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Features
import com.appsnado.haippNew.databinding.Membershipbind
import java.util.ArrayList

class SubscriptionFragment : BaseFragment<SubscriptionViewModel>() {
    var binding: Membershipbind? = null
    var membershipAdapter: MembershipAdapter? = null
    var feedsModels: ArrayList<Int> = ArrayList<Int>()
    lateinit var viewpager: ViewPager

    companion object {
        fun newInstance() = SubscriptionFragment()
    }

    private lateinit var subscripviewModel: SubscriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.subscription_fragment,
            container,
            false
        )
        feedsModels.add(R.drawable.tickyes)
        feedsModels.add(R.drawable.tickyes)
        feedsModels.add(R.drawable.tickyes)


        binding?.viewpager?.setClipChildren(false)
        binding?.viewpager?.setOffscreenPageLimit(3)
        binding?.viewpager?.setPageTransformer(false, CarouselMembership(mainActivity))
        membershipAdapter = MembershipAdapter(mainActivity!!, feedsModels)
        binding?.viewpager?.setAdapter(membershipAdapter)
        binding?.viewpager?.setCurrentItem(1)

        return binding!!.root;

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SubscriptionViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun createViewModel(): SubscriptionViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        subscripviewModel = ViewModelProviders.of(this, factory).get(SubscriptionViewModel::class.java)
        return subscripviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.showBackButton(baseActivity!!,"Subscription")

    }

}