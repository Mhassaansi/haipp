package com.appsnado.haippNew.Adddevices

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.appsnado.haippNew.R
import com.appsnado.haippNew.databinding.AdddeviceDailog

class adddevicesdailog (
        var activityContext: Activity,
        var checkFrag: String,
) : DialogFragment(), View.OnClickListener {

    var binding: AdddeviceDailog? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.set_add_devices_dialog, container, false)
        setListener()
        setData()
        return binding!!.root
    }

    private fun setData() {
        //binding!!.tvAcceptt.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme)
        isCancelable = true
    }

    fun setListener() {
        // binding!!.tvReject.setOnClickListener(this)
        binding!!.tvAccept.setOnClickListener(this)
    }






//    private fun startCropImageActivity(imageUri: Uri) {
//        Objects.requireNonNull(activity)?.let {
//            CropImage.activity(imageUri)
//                .setCropShape(CropImageView.CropShape.RECTANGLE)
//                .setAspectRatio(1, 1)
//                .start(it)
//        }
//    }





    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvAccept -> {
                if(AdddevicesFragment.not_detail != null) {
                    AdddevicesFragment.not_detail!!.add(binding?.titletxt?.text.toString())
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