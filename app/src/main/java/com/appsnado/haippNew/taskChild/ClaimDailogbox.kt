package com.appsnado.haippNew.taskChild

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.R
import com.appsnado.haippNew.databinding.CongDialogs

class ClaimDailogbox(
        var activityContext: Activity,
        var checkFrag: String,
        var tsId: String?,
        var taskReward: String?,
        var con: String?,
) : DialogFragment(), View.OnClickListener {

    var binding: CongDialogs? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
                DataBindingUtil.inflate(inflater, R.layout.dialog_congrats, container, false)
        setListener()
        setData()
        return binding!!.root
    }

    private fun setData() {
        //You have got 50 points and accumulated points are 1,500
        binding!!.text.text= "You have got " +taskReward+ " points"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }

    fun setListener() {
       // binding!!.tvReject.setOnClickListener(this)
        binding!!.tvClose.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvClose -> {

                if(ClaimFragment.not_detail != null){
                    ClaimFragment.not_detail!!.calldata(tsId!!,taskReward!!,con!!)
                }

                dismiss()
            }
            R.id.tvReject -> {
                dismiss()
            }

//            R.id.tvTermCondition -> {
//                dismiss()
//
//                    true,
//                    true
//                )
//            }
        }
    }


}