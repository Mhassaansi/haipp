package com.appsnado.haippNew.Changeemail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.MainActivity
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.ChangeemailBinding

class ChangeemailFragment: BaseFragment<ChangeemailViewModel>(),View.OnClickListener {
    var binding: ChangeemailBinding? = null
    //    private val login: TransitionButton? = null
    lateinit var navController: NavController

    companion object {
        fun newInstance() = ChangeemailFragment()
    }

    private lateinit var changeemailviewModel: ChangeemailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,com.appsnado.haippNew.R.layout.changeemail_fragment, container, false)

//        binding!!.btnLogin.setOnClickListener(this)
//        binding!!.tvSignUp.setOnClickListener(this)
//        binding!!.btnLeft.setOnClickListener(this)



        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin -> navController!!.navigate(R.id.homeFragment)
            R.id.tvSignUp -> navController!!.navigate(R.id.signupFragment)
            R.id.btnLeft -> navController.popBackStack()
        }
    }

    override fun createViewModel(): ChangeemailViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        changeemailviewModel = ViewModelProviders.of(this, factory).get(ChangeemailViewModel::class.java)
        return changeemailviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.text = "Change Email"
        titleBar?.btnLeft?.visibility = View.VISIBLE
        titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
            navController!!.popBackStack()
        });
    }


}