package com.appsnado.haippNew.Role

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.appsnado.haippNew.Applocakpacakges.activities.lock.GestureSelfUnlockActivity
import com.appsnado.haippNew.Applocakpacakges.base.AppConstants
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.DecidedBind
import com.appsnado.haippNew.databinding.RoleBind
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class DecidedFragment : BaseFragment<RoleViewModel>(),View.OnClickListener {
    lateinit var navController: NavController
    private lateinit var roleviewModel: RoleViewModel

    companion object {
        fun newInstance() = DecidedFragment()
    }

    var binding: DecidedBind? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.decided_fragment,
            container,
            false)



        binding?.btnparent?.setOnClickListener(this)
        binding?.btnkid?.setOnClickListener(this)

        YoYo.with(Techniques.FlipInY)
            .duration(500)
            .repeat(0)
            .playOn(binding!!.btnparent);

        YoYo.with(Techniques.FlipInY)
            .duration(500)
            .repeat(0)
            .playOn(binding!!.btnkid);


        //   getDeviceToken()
        //   calltoken()


        return binding?.root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnkid -> {

                val intent =
                    Intent(mainActivity, GestureSelfUnlockActivity::class.java)
                intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME)
                intent.putExtra(
                    AppConstants.LOCK_FROM,
                    AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY
                )
                startActivity(intent)
                mainActivity?.finish()
                mainActivity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                mainActivity!!.preferenceManager!!.settype("kid")
                mainActivity!!.setView()
                navController!!.navigate(R.id.adddevicesFragment)
                mainActivity!!.preferenceManager!!.setlockscreen(true)


            }
            R.id.btnparent -> {



                // callresponces()
            }




        }
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }
    override fun createViewModel(): RoleViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        roleviewModel = ViewModelProviders.of(this, factory).get(RoleViewModel::class.java)
        return roleviewModel!!
    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility =View.GONE
    }

}