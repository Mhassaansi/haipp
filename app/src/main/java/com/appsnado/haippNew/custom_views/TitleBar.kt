package com.appsnado.haippNew.custom_views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.appsnado.haippNew.R
import com.appsnado.haippNew.baseactivity.BaseActivity
import com.appsnado.haippNew.data.SharedPreferenceManager
import de.hdodenhof.circleimageview.CircleImageView


class TitleBar : LinearLayout {
    internal var btnLeft: ImageButton? = null
    internal var btnRight: ImageButton? = null
    internal var headerLayout: LinearLayout? = null
    internal var txtTitleName: TextView? = null
    var circleImageView: CircleImageView? = null
    var txtRightText: TextView? = null
    var preferenceManager: SharedPreferenceManager? = null
    internal var context: Context? = null

    //profile_image
    constructor(context: Context) : super(context) {
        this.context = context
        initLayout(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initLayout(context)
        attrs?.let { initAttrs(context, it) }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initLayout(context)
        attrs?.let { initAttrs(context, it) }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet) {}
    private fun initLayout(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater?.inflate(R.layout.view_titlebar, this)
        bindViews()
    }

    private fun bindViews() {
        headerLayout = findViewById<View>(R.id.header_layout) as LinearLayout
        btnLeft = findViewById<View>(R.id.btnLeft) as ImageButton
        btnRight = findViewById<View>(R.id.btnRight) as ImageButton
        circleImageView = findViewById<View>(R.id.btnRightprofile) as CircleImageView
        txtTitleName = findViewById<View>(R.id.txtTitleName) as TextView
        txtRightText = findViewById<View>(R.id.txtRightText) as TextView
        preferenceManager = SharedPreferenceManager(context)
    }

    fun resetTitleBar() {
        this.visibility = View.VISIBLE
        headerLayout!!.visibility = View.VISIBLE
        btnLeft!!.visibility = View.GONE
        btnRight!!.visibility = View.GONE
        txtTitleName!!.visibility = View.GONE
        txtRightText!!.visibility = View.GONE
        circleImageView!!.visibility = View.GONE
        // headerLayout!!.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTransparent))
    }

    fun showHeaderText(value: String?) {
        txtTitleName!!.visibility = View.VISIBLE
        txtTitleName!!.text = value
    }

    fun showRightText(value: String?) {
        txtRightText!!.visibility = View.VISIBLE
        txtRightText!!.text = value
    }

    fun setSolidColor() {
        //getHeaderLayout()!!.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.profile_solid))
    }

    fun showHeaderTextTransparent(value: String?) {
        setBackgroundColor(Color.TRANSPARENT)
        txtTitleName!!.visibility = View.VISIBLE
        txtTitleName!!.text = value
        getHeaderLayout()!!.visibility = View.VISIBLE
    }

    fun hideTitleBar() {
        getHeaderLayout()!!.visibility = View.GONE
    }

    fun setBtnLeft(drawable: Int, listener: OnClickListener?) {
        btnLeft!!.setImageResource(drawable)
        btnLeft!!.setOnClickListener(listener)
        btnLeft!!.visibility = View.VISIBLE
    }


    fun setBtnRight(drawable: Int, listener: OnClickListener?) {
        btnRight!!.setBackgroundResource(drawable)
        btnRight!!.setOnClickListener(listener)
        btnRight!!.visibility = View.VISIBLE
    }

    fun setBtnRightDrawable(drawable: Int) {
        btnRight!!.setBackgroundResource(drawable)
        btnRight!!.visibility = View.VISIBLE
    }

    fun setBtnLeftDrawable(drawable: Int) {
        btnLeft!!.setImageResource(drawable)
        btnLeft!!.visibility = View.VISIBLE
    }

    fun getBtnLeft(): ImageButton? {
        return btnLeft
    }

//    fun setprofileimage(drawable: String?) {
//        Glide.with(circleImageView!!).load(drawable).placeholder(R.drawable.dummypic).into(circleImageView!!)
//        circleImageView!!.visibility = View.VISIBLE
//    }
//
//    fun setprofileimageset(drawable: Int?) {
//        Glide.with(circleImageView!!).load(drawable).placeholder(R.drawable.dummypic).into(circleImageView!!)
//        circleImageView!!.visibility = View.VISIBLE
//    }

    fun getprofileimage(): CircleImageView? {
        return circleImageView
    }

    fun gettxtTitleName(): TextView? {
        return txtTitleName
    }

    fun getBtnRight(): ImageButton? {
        return btnRight
    }

    fun getTxtTitleName(): TextView? {
        return txtTitleName
    }

    fun setHeaderLayout(headerLayout: LinearLayout?) {
        this.headerLayout = headerLayout
    }

    fun setHeaderLayoytBackgroundColor(color: Int) {
        getHeaderLayout()!!.setBackgroundColor(color)
        getHeaderLayout()!!.visibility = View.VISIBLE
    }

    fun setHeaderLayoutBackgroundDrawable(drawable: Int) {
        getHeaderLayout()!!.setBackgroundResource(drawable)
        getHeaderLayout()!!.visibility = View.VISIBLE
    }

    fun getHeaderLayout(): LinearLayout? {
        return headerLayout
    }

    fun showBackButton(baseActivity: BaseActivity, s: String) {
        resetTitleBar()
        btnLeft!!.visibility = View.VISIBLE
        txtTitleName!!.visibility  = View.VISIBLE
        txtTitleName!!.text = s
        btnLeft!!.setOnClickListener { baseActivity?.onBackPressed() }
    }

    fun text( s: String) {
        resetTitleBar()
        txtTitleName!!.visibility  = View.VISIBLE
        txtTitleName!!.text = s
    }
}