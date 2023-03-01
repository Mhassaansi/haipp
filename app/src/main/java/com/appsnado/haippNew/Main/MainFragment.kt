package com.appsnado.haippNew.Main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.data.SharedPreferenceManager
import com.appsnado.haippNew.databinding.MainFragBinding


class MainFragment : BaseFragment<MainViewModel>() {
    //lateinit var navController: NavController
    var binding: MainFragBinding? = null
    var bottomview: Bottomview? = null
    var bottomlayout : Boolean? = false
    var mCallbacklayout: Bottomview? = null

    @JvmField
    var preferenceManager: SharedPreferenceManager? = null

    companion object {
        fun newInstance() = MainFragment()
        var notdetail : MainFragment? = null
    }
    private lateinit var mainviewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )


        notdetail = this
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         navController = Navigation.findNavController(binding!!.root)
          preferenceManager = SharedPreferenceManager(mainActivity!!)
         //NavigationUI.setupActionBarWithNavController(mainActivity!!,navController);

            navController!!.navigate(R.id.pre_login_fragment)

        if (preferenceManager!!.isLogin() == true) {
            if (preferenceManager!!.gettype() != "") {
                if (preferenceManager!!.gettype() != "parent") {
                    if (preferenceManager!!.getlockscreen() == true) {
                        //mainActivity!!.preferenceManager!!.setlockscreen(false)
                        navController!!.navigate(R.id.adddevicesFragment)
                    } else {
                        //mainActivity!!.preferenceManager!!.setlockscreen(true)
                        navController!!.navigate(R.id.homeFragment)
                        if (preferenceManager!!.isLogin() == true) {

                        } else {
                            navController!!.navigate(R.id.pre_login_fragment)
                        }

                    }
                } else {
                    if (preferenceManager!!.isLogin() == true) {
                        navController!!.navigate(R.id.homeFragment)
                    } else {
                        navController!!.navigate(R.id.pre_login_fragment)
                    }
                }
            }
        }







    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun createViewModel(): MainViewModel {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        mainviewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        return mainviewModel!!
    }

    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar!!.visibility = View.GONE
    }

}