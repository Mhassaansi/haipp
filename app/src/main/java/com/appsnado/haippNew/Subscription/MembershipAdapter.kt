package com.appsnado.haippNew.Subscription

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.appsnado.haippNew.R
import java.util.ArrayList

class MembershipAdapter(private val mContext: Context, val feedsModels: ArrayList<Int>) : PagerAdapter() {
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val modelObject = feedsModels[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.item_membership, collection, false) as ViewGroup
        collection.addView(layout)
        return layout

    }
    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)

    }
    override fun getCount(): Int {
        return feedsModels.size
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    @SuppressLint("ResourceType")
    override fun getPageTitle(position: Int): CharSequence {
        val customPagerEnum = feedsModels[position]
        return mContext.getString(1)
    }
}