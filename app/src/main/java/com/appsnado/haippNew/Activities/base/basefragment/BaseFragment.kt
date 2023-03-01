package com.appsnado.haippNew.base.basefragment

import android.R
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.appnado.haipp.baseviewmodel.BaseViewModel
import com.appsnado.haippNew.Activities.MainActivity
import com.appsnado.haippNew.base.BaseApplication
import com.appsnado.haippNew.baseactivity.BaseActivity
import com.appsnado.haippNew.custom_views.TitleBar

abstract class BaseFragment <V : BaseViewModel<*>?> : Fragment(){
    private var mLayoutRes = -1
        private set
    protected var imm: InputMethodManager? = null
    protected var baseActivity:BaseActivity?  = null
   /// protected var mUnBinder: Unbinder? = null
    private var progressDialog: ProgressDialog? = null
    protected open var viewModel: V? = null




    protected abstract fun createViewModel(): V?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModel()
        try {
            imm = BaseApplication.getApplication()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        }catch (e: Exception){
            e.stackTrace
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mLayoutRes == -1) {
            throw RuntimeException("You have to call setContentView!")
        }
        return inflater.inflate(mLayoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // mUnBinder = ButterKnife.bind(this, view)
        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage("Please wait")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        baseActivity = activity as BaseActivity
       // setListeners()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
       // mUnBinder!!.unbind()
        super.onDestroyView()
        hideKeyBoard()
    }

    protected fun hideKeyBoard() {
        val view = baseActivity!!.currentFocus
        if (view != null) {
            if(imm != null)
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    protected fun showKeyBoard(view: View?) {
        if (view != null) {
            imm!!.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    abstract fun setTitleBar(titleBar: TitleBar?)
  //  abstract fun setBottomBar(spaceNavigationView: BottomBar?)
    override fun onResume() {
        super.onResume()
        //Log.i(ContentValues.TAG, "onResume: " + mainActivity!!.titleBar)
              setTitleBar(mainActivity!!.titleBar)
        //setBottomBar(mainActivity!!.bottomBar)
    }

    protected val mainActivity: MainActivity?
        protected get() = activity as MainActivity?





//    fun checkInternetConnection(): Boolean {
//        if (!NetworkHelper.isNetworkAvailable(Objects.requireNonNull(context))) {
//            DialogFactory.createSingleButtonDialog(activity, { dialog, which ->
//                startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
//                dialog.dismiss()
//            }, "Internet Connection", "Please provide internet connection or mobile data to access")!!.show()
//            return false
//        }
//        return true
//    }

    fun getScreenWidth(ratio: Double): Int {
        return (resources.displayMetrics.widthPixels * ratio).toInt()
    }

    protected fun displayProgressBar() {
        progressDialog!!.show()
    }

    protected fun hideProgressBar() {
        progressDialog!!.dismiss()
    }


}

private fun Nothing.dismiss() {
    TODO("Not yet implemented")
}
