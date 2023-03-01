package com.appsnado.haippNew.chidverification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.appsnado.haippNew.Activities.MainActivity.Companion.navController
import com.appsnado.haippNew.Main.Datamanager
import com.appsnado.haippNew.Main.MainViewModelFactory
import com.appsnado.haippNew.R
import com.appsnado.haippNew.base.basefragment.BaseFragment
import com.appsnado.haippNew.custom_views.TitleBar
import com.appsnado.haippNew.databinding.ChildparentBinding
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.poovam.pinedittextfield.PinField
import org.jetbrains.annotations.NotNull

class ChildParentFragment : BaseFragment<ChildParentViewModel>() {
    var binding: ChildparentBinding? = null
    companion object {
        fun newInstance() = ChildParentFragment()
    }

    private lateinit var childviewModel: ChildParentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.appsnado.haippNew.R.layout.child_parent_fragment,
            container,
            false
        )

        YoYo.with(Techniques.FadeInLeft)
                .duration(900)
                .repeat(0)
                .playOn(binding!!.etPinText);





        binding?.etPinText?.onTextCompleteListener = (object : PinField.OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                navController.popBackStack(R.id.childParentFragment, true);
                navController!!.navigate(R.id.homeFragment)
                binding!!.etPinText.text = null




                return true // Return false to keep the keyboard open else return true to close the keyboard
            }
        })


        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun createViewModel(): ChildParentViewModel? {
        val factory = Datamanager.getInstance()?.let { MainViewModelFactory(it, activity) }
        childviewModel = ViewModelProviders.of(this, factory).get(ChildParentViewModel::class.java)
        return childviewModel!!

    }


    override fun setTitleBar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.txtTitleName?.setText("Connect to Parent")
        titleBar?.btnLeft?.visibility =View.VISIBLE
        titleBar?.setBtnLeft(R.drawable.backbutton, View.OnClickListener {
            navController!!.popBackStack()
        });

    }

}