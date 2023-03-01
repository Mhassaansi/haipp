//package com.appsnado.haippNew.custom_views
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.ImageView
//import android.widget.RelativeLayout
//import butterknife.BindView
//import com.appsnado.haipp.R
//
//
//class BottomBar : RelativeLayout {
//    @kotlin.jvm.JvmField
//    @BindView(R.id.imguser)
//    var imgu: ImageView? = null
//
//    @kotlin.jvm.JvmField
//    @BindView(R.id.imgtrainer)
//    var imgt: ImageView? = null
//
//    @kotlin.jvm.JvmField
//    @BindView(R.id.imgSetting)
//    var imgSetting: ImageView? = null
//    internal var context: Context? = null
//
//    constructor(context: Context) : super(context) {
//        this.context = context
//        initLayout(context)
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        initLayout(context)
//        attrs?.let { initAttrs(context, it) }
//    }
//
//    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
//        initLayout(context)
//        attrs?.let { initAttrs(context, it) }
//    }
//
//    private fun initAttrs(context: Context, attrs: AttributeSet) {}
//    private fun initLayout(context: Context) {
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        if (inflater != null) {
//            inflater.inflate(R.layout.bottom_bar, this)
//            ButterKnife.bind(this)
//        }
//    }
//
//
//
//    fun setBtnuser(drawable: Int, listener: OnClickListener?) {
//
//
//        imgu!!.setImageResource(drawable)
//        imgu!!.setOnClickListener(listener)
//        imgu!!.visibility = View.VISIBLE
//    }
//
//    fun setbtnsocial(drawable: Int) {
//        imgt!!.setImageResource(drawable)
//    }
//    fun setbtnclassic() {
//
//    }
//    fun setBtnuserDrawable(drawable: Int) {
//        imgu!!.setImageResource(drawable)
//        imgu!!.visibility = View.VISIBLE
//    }
//
//    fun setBtnclassicDrawable(drawable: Int) {
//        imgt!!.setImageResource(drawable)
//        imgt!!.visibility = View.VISIBLE
//    }
//
//
//    fun setBtnclassic(drawable: Int, listener: OnClickListener?) {
//        imgt!!.setImageResource(drawable)
//        imgt!!.setOnClickListener(listener)
//        imgt!!.visibility = View.VISIBLE
//    }
//    fun hideBottomBar() {
//        this.visibility = View.GONE
//    }
//
//    fun showBottomBar() {
//        this.visibility = View.VISIBLE
//    }
//
//    //    @Override
//    //    public boolean onTouchEvent(final MotionEvent event) {
//    //        if(event.getAction() == MotionEvent.ACTION_UP){
//    //            return performClick();
//    //        }
//    //        return true;
//    //    }
//    fun setListener(onClickListener: OnClickListener?) {
//        setOnClickListener(onClickListener)
//    } //    @Override
//    //    public void onClick(View view) {
//    //        switch (view.getId()){
//    //            case R.id.imgHome:
//    //                UIHelper.showToastLong("Home Clicked");
//    //                break;
//    //            case R.id.imgTryout:
//    //                UIHelper.showToastLong("Tryout Clicked");
//    //                break;
//    //            case R.id.imgSetting:
//    //                UIHelper.showToastLong("Setting Clicked");
//    //                break;
//    //        }
//    //    }
//}
//
//
