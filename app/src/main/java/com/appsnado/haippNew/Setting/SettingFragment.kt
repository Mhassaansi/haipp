package com.appsnado.haippNew.Setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.Setting


class SettingFragment : BaseFragment<SettingViewModel>(), View.OnClickListener {

    var binding: Setting? = null
    var SettingArrayList: ArrayList<String>? = null
    var settingadapter: Settingadapter? = null

    companion object {
        fun newInstance() = SettingFragment()
    }

    private lateinit var viewModelsetting: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,R.layout.setting_fragment,
            container,
            false
        )
//        binding!!.kiddevices.setOnClickListener(this)
//        binding!!.changeemail.setOnClickListener(this)
//        binding!!.changepassword.setOnClickListener(this)
//        binding!!.privacy.setOnClickListener(this)
//        binding!!.termandcond.setOnClickListener(this)
//        binding!!.faqs.setOnClickListener(this)
//        binding!!.logout.setOnClickListener(this)
//        binding!!.subscription.setOnClickListener(this)
        binding?.linearLayoutCompat2!!.visibility =View.GONE
        setData()
       // startTopToBottomAnimation();

   //     val scrollView =
//            ScrollView(mainActivity!!.getApplicationContext())
//        scrollView.smoothScrollTo(7, 6)
//        scrollView.smoothScrollBy(10, 20)
//
//        assertEquals(17, scrollView.scrollX)
//        assertEquals(26, scrollView.scrollY)

        ///binding!!.scroll.setSmoothScrollingEnabled(false);

        //ObjectAnimator.ofInt(binding!!.scroll, "scrollY",  binding!!.scroll).setDuration(1000).start();


        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startTopToBottomAnimation();

    }

    private fun startTopToBottomAnimation() {
        val animation =
            AnimationUtils.loadAnimation(mainActivity, com.appsnado.haippNew.R.anim.slide_out_to_top)
        binding?.linearLayoutCompat2!!.startAnimation(animation)
        //binding?.linearLayoutCompat2!!.visibility = View.GONE

    }

    fun setData() {

        var email =mainActivity!!.preferenceManager!!.getUser()!!.userEmail
        binding?.email?.text = email

        SettingArrayList = ArrayList()

        SettingArrayList!!.add("Kids Devices")
 //         SettingArrayList!!.add("Subscription")
//          SettingArrayList!!.add("Change Email")
        SettingArrayList!!.add("Change Password")
        SettingArrayList!!.add("Term & Condition")
        SettingArrayList!!.add("Privacy Policy")
        SettingArrayList!!.add("FAQS")
        SettingArrayList!!.add("Logout")


//        binding!!.recycler.layoutManager = LinearLayoutManager(
//                mainActivity,
//                LinearLayoutManager.HORIZONTAL,
//                false
//        )


        binding!!.recycler?.layoutManager = GridLayoutManager(context, 2)

        settingadapter =
                Settingadapter(mainActivity!!, SettingArrayList)
        binding!!.recycler.adapter = settingadapter
    }

    override fun createViewModel(): SettingViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        viewModelsetting = ViewModelProviders.of(this, factory).get(SettingViewModel::class.java)
        return viewModelsetting!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {

       titleBar?.visibility =View.VISIBLE
        titleBar?.text("Settings")
    }

    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.kiddevices -> navController!!.navigate(R.id.kiddevicesFragment)
//            R.id.changeemail -> navController!!.navigate(R.id.changeemailFragment)
//            R.id.changepassword -> navController!!.navigate(R.id.changepasswordFragment)
//            R.id.privacy -> navController!!.navigate(R.id.privacyFragment)
//            R.id.termandcond -> navController!!.navigate(R.id.termsandconditionFragment)
//            R.id.faqs -> navController!!.navigate(R.id.faqFragment)
//            R.id.subscription -> navController!!.navigate(R.id.subscriptionFragment)
//            R.id.logout ->if(MainActivity.not_detail != null){
//                MainActivity!!.not_detail?.logoutdailog()
//            }
//        }
    }
}